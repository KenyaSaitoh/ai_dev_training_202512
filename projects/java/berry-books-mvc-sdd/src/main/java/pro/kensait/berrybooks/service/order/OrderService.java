package pro.kensait.berrybooks.service.order;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.dao.BookDao;
import pro.kensait.berrybooks.dao.OrderDetailDao;
import pro.kensait.berrybooks.dao.OrderTranDao;
import pro.kensait.berrybooks.dao.StockDao;
import pro.kensait.berrybooks.entity.Book;
import pro.kensait.berrybooks.entity.Customer;
import pro.kensait.berrybooks.entity.OrderDetail;
import pro.kensait.berrybooks.entity.OrderDetailPK;
import pro.kensait.berrybooks.entity.OrderTran;
import pro.kensait.berrybooks.entity.Stock;
import pro.kensait.berrybooks.web.cart.CartItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 注文処理のビジネスロジッククラス
 * 
 * <p>注文の作成、在庫管理、注文履歴の取得などを行います。</p>
 */
@ApplicationScoped
public class OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    @Inject
    private StockDao stockDao;
    
    @Inject
    private OrderTranDao orderTranDao;
    
    @Inject
    private OrderDetailDao orderDetailDao;
    
    @Inject
    private BookDao bookDao;
    
    /**
     * 注文を作成します（トランザクション処理）
     * 
     * <p>このメソッドは以下の処理を単一トランザクションで実行します：</p>
     * <ol>
     *   <li>在庫チェック（BR-022）</li>
     *   <li>楽観的ロックでの在庫更新（BR-024）</li>
     *   <li>在庫数の減算（BR-023）</li>
     *   <li>注文トランザクションの作成</li>
     *   <li>注文明細の作成</li>
     * </ol>
     * 
     * @param orderTO 注文情報
     * @return 作成された注文トランザクション
     * @throws OutOfStockException 在庫不足の場合
     * @throws OptimisticLockException 楽観的ロック競合の場合
     */
    @Transactional
    public OrderTran orderBooks(OrderTO orderTO) {
        logger.info("[ OrderService#orderBooks ] customerId={}, deliveryAddress={}", 
                    orderTO.getCustomerId(), orderTO.getDeliveryAddress());
        
        // 1. カートアイテムごとに在庫チェック（BR-022）
        for (CartItem cartItem : orderTO.getCartItems()) {
            Stock stock = stockDao.findByBookId(cartItem.getBookId());
            
            if (stock == null) {
                logger.warn("Stock not found for bookId={}", cartItem.getBookId());
                throw new OutOfStockException(cartItem.getBookId(), cartItem.getBookName());
            }
            
            // 2. 在庫数 < 注文数の場合: OutOfStockExceptionをスロー
            if (stock.getQuantity() < cartItem.getCount()) {
                logger.warn("Out of stock: bookId={}, bookName={}, stock={}, ordered={}", 
                           cartItem.getBookId(), cartItem.getBookName(), 
                           stock.getQuantity(), cartItem.getCount());
                throw new OutOfStockException(cartItem.getBookId(), cartItem.getBookName());
            }
            
            // 3. カートアイテムに保存したVERSION値で在庫を更新（BR-024）
            // バージョンチェックはJPAが自動的に行う（WHERE VERSION = ?）
            stock.setVersion(cartItem.getVersion());
            
            // 4. 在庫数を減算（BR-023）
            stock.setQuantity(stock.getQuantity() - cartItem.getCount());
            
            // 在庫を更新（楽観的ロック付き）
            // VERSION不一致の場合、OptimisticLockExceptionが自動スロー
            stockDao.update(stock);
            
            logger.debug("Stock updated: bookId={}, newQuantity={}, version={}", 
                        cartItem.getBookId(), stock.getQuantity(), stock.getVersion());
        }
        
        // 5. OrderTranエンティティを作成・登録
        OrderTran orderTran = new OrderTran();
        orderTran.setOrderDate(LocalDateTime.now());
        orderTran.setDeliveryAddress(orderTO.getDeliveryAddress());
        orderTran.setDeliveryPrice(orderTO.getDeliveryPrice());
        orderTran.setSettlementType(orderTO.getSettlementCode());
        
        logger.debug("OrderTran created: settlementType={}, deliveryAddress={}", 
                    orderTO.getSettlementCode(), orderTO.getDeliveryAddress());
        
        // 合計金額を計算
        BigDecimal totalPrice = orderTO.getCartItems().stream()
            .map(CartItem::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderTran.setTotalPrice(totalPrice);
        
        // 顧客との関連を設定
        Customer customer = new Customer();
        customer.setCustomerId(orderTO.getCustomerId());
        orderTran.setCustomer(customer);
        
        orderTranDao.persist(orderTran);
        
        logger.debug("OrderTran created: orderTranId={}, totalPrice={}", 
                    orderTran.getOrderTranId(), orderTran.getTotalPrice());
        
        // 6. OrderDetailエンティティを作成・登録（カートアイテムごと）
        int detailId = 1;
        for (CartItem cartItem : orderTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            
            // 複合主キーを設定
            OrderDetailPK pk = new OrderDetailPK();
            pk.setOrderTranId(orderTran.getOrderTranId());
            pk.setOrderDetailId(detailId++);
            orderDetail.setId(pk);
            
            // OrderTranとの関連を設定（@MapsIdにより複合主キーのorderTranIdも自動設定）
            orderDetail.setOrderTran(orderTran);
            
            // Bookエンティティを取得して設定（bookIdフィールドはinsertable=falseのため）
            Book book = bookDao.findById(cartItem.getBookId());
            orderDetail.setBook(book);
            
            orderDetail.setPrice(cartItem.getPrice());
            orderDetail.setCount(cartItem.getCount());
            
            orderDetailDao.persist(orderDetail);
            
            logger.debug("OrderDetail created: orderTranId={}, orderDetailId={}, bookId={}", 
                        pk.getOrderTranId(), pk.getOrderDetailId(), book.getBookId());
        }
        
        // 7. トランザクションコミット（@Transactionalにより自動）
        logger.info("[ OrderService#orderBooks ] Order completed: orderTranId={}, totalPrice={}", 
                   orderTran.getOrderTranId(), orderTran.getTotalPrice());
        
        return orderTran;
    }
    
    /**
     * 注文履歴を取得します
     * 
     * <p>注文トランザクションと注文明細を結合した注文履歴を取得します。</p>
     * <p>各注文明細ごとに1レコード生成されます。</p>
     * 
     * @param customerId 顧客ID
     * @return 注文履歴のリスト（注文明細単位）
     */
    public List<OrderHistoryTO> getOrderHistory(Integer customerId) {
        logger.info("[ OrderService#getOrderHistory ] customerId={}", customerId);
        
        // 顧客IDで注文トランザクションを取得（JOIN FETCHで明細も同時取得）
        List<OrderTran> orderTrans = orderTranDao.findByCustomerIdWithDetails(customerId);
        List<OrderHistoryTO> historyList = new ArrayList<>();
        
        // 各注文トランザクションの各明細をOrderHistoryTOに変換
        for (OrderTran orderTran : orderTrans) {
            for (pro.kensait.berrybooks.entity.OrderDetail detail : orderTran.getOrderDetails()) {
                OrderHistoryTO historyTO = new OrderHistoryTO(
                    orderTran.getOrderDate().toLocalDate(),  // LocalDateTimeをLocalDateに変換
                    orderTran.getOrderTranId(),
                    detail.getId().getOrderDetailId(),  // 複合キーからorderDetailIdを取得
                    detail.getBook().getBookName(),
                    detail.getBook().getPublisher().getPublisherName(),
                    detail.getPrice(),
                    detail.getCount()
                );
                historyList.add(historyTO);
            }
        }
        
        logger.debug("Order history found: count={}", historyList.size());
        return historyList;
    }
    
    /**
     * 注文詳細を取得します（JOIN FETCHでN+1問題を回避）
     * 
     * @param orderTranId 注文ID
     * @return 注文サマリー
     */
    public OrderSummaryTO getOrderDetail(Integer orderTranId) {
        logger.info("[ OrderService#getOrderDetail ] orderTranId={}", orderTranId);
        
        // JOIN FETCHで注文トランザクションと明細を同時に取得（N+1問題回避）
        OrderTran orderTran = orderTranDao.findByIdWithDetails(orderTranId);
        if (orderTran == null) {
            logger.warn("OrderTran not found: orderTranId={}", orderTranId);
            return null;
        }
        
        List<OrderDetail> orderDetails = orderTran.getOrderDetails();
        
        OrderSummaryTO summaryTO = new OrderSummaryTO(orderTran, orderDetails);
        
        logger.debug("OrderSummary created: orderTranId={}, detailCount={}", 
                    orderTranId, orderDetails.size());
        
        return summaryTO;
    }
    
    /**
     * 注文トランザクションを明細と共に取得します
     * 
     * @param orderTranId 注文ID
     * @return 注文トランザクション（明細含む）
     */
    public OrderTran getOrderTranWithDetails(Integer orderTranId) {
        logger.info("[ OrderService#getOrderTranWithDetails ] orderTranId={}", orderTranId);
        
        // JOIN FETCHで注文トランザクションと明細を同時に取得
        OrderTran orderTran = orderTranDao.findByIdWithDetails(orderTranId);
        if (orderTran == null) {
            logger.warn("OrderTran not found: orderTranId={}", orderTranId);
            throw new RuntimeException("注文が見つかりません: orderTranId=" + orderTranId);
        }
        
        logger.debug("OrderTran loaded with details: orderTranId={}, detailCount={}", 
                    orderTranId, orderTran.getOrderDetails().size());
        
        return orderTran;
    }
}

