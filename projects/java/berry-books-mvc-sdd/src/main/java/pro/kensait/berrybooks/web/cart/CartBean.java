package pro.kensait.berrybooks.web.cart;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.common.MessageUtil;
import pro.kensait.berrybooks.dao.StockDao;
import pro.kensait.berrybooks.entity.Book;
import pro.kensait.berrybooks.entity.Stock;
import pro.kensait.berrybooks.service.book.BookService;
import pro.kensait.berrybooks.service.delivery.DeliveryFeeService;
import pro.kensait.berrybooks.web.customer.CustomerBean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * カート画面のコントローラー
 * 
 * <p>カート画面のビューとビジネスロジックの橋渡しを行います。</p>
 */
@Named
@SessionScoped
public class CartBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(CartBean.class);
    
    /**
     * 書籍サービス
     */
    @Inject
    private BookService bookService;
    
    /**
     * 在庫DAO
     */
    @Inject
    private StockDao stockDao;
    
    /**
     * カートセッション
     */
    @Inject
    private CartSession cartSession;
    
    /**
     * 顧客Bean
     */
    @Inject
    private CustomerBean customerBean;
    
    /**
     * 配送料金サービス
     */
    @Inject
    private DeliveryFeeService deliveryFeeService;
    
    /**
     * グローバルエラーメッセージ
     */
    private String globalErrorMessage;
    
    /**
     * デフォルトコンストラクタ
     */
    public CartBean() {
    }
    
    /**
     * 書籍をカートに追加します
     * 
     * @param bookId 書籍ID
     * @param count 数量
     * @return カート画面へのナビゲーション
     */
    public String addBook(Integer bookId, Integer count) {
        logger.info("[ CartBean#addBook ] bookId={}, count={}", bookId, count);
        
        Book book = bookService.findBookById(bookId);
        
        // 楽観的ロック用：Stockエンティティから現在のVERSION値を取得
        Stock stock = stockDao.findByBookId(bookId);
        logger.info("[ CartBean#addBook ] Stock version={}", stock.getVersion());
        
        // 選択された書籍がカートに存在している場合は、注文数と金額を加算する
        boolean isExists = false;
        for (CartItem cartItem : cartSession.getCartItems()) {
            if (bookId.equals(cartItem.getBookId())) {
                cartItem.setCount(cartItem.getCount() + count);
                cartItem.setPrice(cartItem.getPrice().add(book.getPrice().multiply(BigDecimal.valueOf(count))));
                // VERSION値は最初にカートに入れた時点のものを保持（更新しない）
                isExists = true;
                break;
            }
        }
        
        // 選択された書籍がカートに存在していない場合は、新しいCartItemを生成しカートに追加する
        if (!isExists) {
            CartItem cartItem = new CartItem(
                    book.getBookId(),
                    book.getBookName(),
                    book.getPublisher().getPublisherName(),
                    book.getPrice().multiply(BigDecimal.valueOf(count)),
                    count,
                    stock.getVersion());
            cartSession.getCartItems().add(cartItem);
        }
        
        // 合計金額を加算する
        BigDecimal totalPrice = cartSession.getTotalPrice();
        cartSession.setTotalPrice(totalPrice.add(book.getPrice().multiply(BigDecimal.valueOf(count))));
        
        return "cartView?faces-redirect=true";
    }
    
    /**
     * 選択した書籍をカートから削除します
     * 
     * @return null（同じページに留まる）
     */
    public String removeSelectedBooks() {
        logger.info("[ CartBean#removeSelectedBooks ]");
        
        // 選択された書籍を削除し、合計金額を再計算
        cartSession.getCartItems().removeIf(item -> {
            if (item.isRemove()) {
                BigDecimal totalPrice = cartSession.getTotalPrice();
                cartSession.setTotalPrice(totalPrice.subtract(item.getPrice()));
                return true;
            }
            return false;
        });
        
        return null;
    }
    
    /**
     * カート全体をクリアします
     * 
     * @return カートクリア確認画面へのナビゲーションルール
     */
    public String clearCart() {
        logger.info("[ CartBean#clearCart ]");
        cartSession.getCartItems().clear();
        cartSession.setTotalPrice(BigDecimal.ZERO);
        cartSession.setDeliveryPrice(BigDecimal.ZERO);
        return "cartClear?faces-redirect=true";
    }
    
    /**
     * 注文画面に進みます
     * 
     * <p>カートが空の場合はエラーメッセージを表示し、注文画面に遷移しません（BIZ-005）。</p>
     * <p>配送先住所、配送料金、決済方法のデフォルト値を設定します。</p>
     * 
     * @return 注文画面へのナビゲーションルール、またはnull（エラー時）
     */
    public String proceedToOrder() {
        logger.info("[ CartBean#proceedToOrder ]");
        
        if (cartSession.getCartItems().isEmpty()) {
            return null;
        }
        
        // デフォルトの配送先住所として、顧客の住所を設定する
        if (customerBean.getCustomer() != null) {
            cartSession.setDeliveryAddress(customerBean.getCustomer().getAddress());
        }
        
        // 配送料金を計算する
        // ※通常800円、沖縄県は1700円、5000円以上は送料無料
        BigDecimal deliveryPrice = deliveryFeeService.calculateDeliveryFee(
                cartSession.getDeliveryAddress(), 
                cartSession.getTotalPrice());
        cartSession.setDeliveryPrice(deliveryPrice);
        
        // デフォルトの決済方法はクレジットカード（2）
        cartSession.setSettlementType(2);
        
        return "bookOrder?faces-redirect=true";
    }
    
    /**
     * カートを参照します
     * 
     * @return カート画面へのナビゲーション
     */
    public String viewCart() {
        logger.info("[ CartBean#viewCart ]");
        
        // カートに商品が一つも入っていなかった場合は、エラーメッセージを設定
        if (cartSession.getCartItems().isEmpty()) {
            logger.info("[ CartBean#viewCart ] カートに商品なしエラー");
            globalErrorMessage = MessageUtil.getMessage("error.cart.empty");
        }
        
        return "cartView?faces-redirect=true";
    }
    
    /**
     * グローバルエラーメッセージを取得します
     * 
     * @return グローバルエラーメッセージ
     */
    public String getGlobalErrorMessage() {
        return globalErrorMessage;
    }
    
    /**
     * グローバルエラーメッセージを設定します
     * 
     * @param globalErrorMessage グローバルエラーメッセージ
     */
    public void setGlobalErrorMessage(String globalErrorMessage) {
        this.globalErrorMessage = globalErrorMessage;
    }
    
    /**
     * 合計金額を取得します（CartSessionへの委譲）
     * 
     * @return 合計金額
     */
    public BigDecimal getTotalPrice() {
        return cartSession.getTotalPrice();
    }
    
    /**
     * 配送料金を取得します（CartSessionへの委譲）
     * 
     * @return 配送料金
     */
    public BigDecimal getDeliveryPrice() {
        return cartSession.getDeliveryPrice();
    }
    
    /**
     * カートが空かどうかを確認します（CartSessionへの委譲）
     * 
     * @return カートが空の場合はtrue
     */
    public boolean isCartEmpty() {
        return cartSession.getCartItems().isEmpty();
    }
}



