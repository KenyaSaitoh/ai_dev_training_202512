package pro.kensait.berrybooks.web.order;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.common.MessageUtil;
import pro.kensait.berrybooks.entity.OrderTran;
import pro.kensait.berrybooks.service.delivery.DeliveryFeeService;
import pro.kensait.berrybooks.service.order.OrderService;
import pro.kensait.berrybooks.service.order.OrderTO;
import pro.kensait.berrybooks.service.order.OutOfStockException;
import pro.kensait.berrybooks.web.cart.CartSession;
import pro.kensait.berrybooks.web.customer.CustomerBean;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 注文処理のコントローラーBean
 * 
 * <p>注文入力、配送料金計算、注文確定の処理を行います。</p>
 */
@Named
@ViewScoped
public class OrderBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(OrderBean.class);
    
    @Inject
    private OrderService orderService;
    
    @Inject
    private DeliveryFeeService deliveryFeeService;
    
    @Inject
    private CartSession cartSession;
    
    @Inject
    private CustomerBean customerBean;
    
    /**
     * 配送先住所
     */
    private String deliveryAddress;
    
    /**
     * 決済方法コード
     */
    private Integer settlementCode;
    
    /**
     * 注文トランザクション（注文完了後にセット）
     */
    private OrderTran orderTran;
    
    /**
     * 初期化処理
     * 
     * <p>カートが空の場合はエラーメッセージを表示します（BIZ-005）。</p>
     */
    @PostConstruct
    public void init() {
        logger.info("[ OrderBean#init ]");
        
        if (cartSession.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                MessageUtil.getMessage("BIZ-005"),
                ""
            );
            context.addMessage(null, message);
        }
        
        // デフォルトの決済方法はクレジットカード（2）
        this.settlementCode = 2;
    }
    
    /**
     * 配送料金を計算します
     * 
     * <p>商品合計金額と配送先住所を基に配送料金を算出し、
     * CartSessionに設定します。</p>
     * 
     * @return 同じページ（null）
     */
    public String calculateDeliveryFee() {
        logger.info("[ OrderBean#calculateDeliveryFee ] deliveryAddress={}", deliveryAddress);
        
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                MessageUtil.getMessage("VAL-003"),
                ""
            );
            context.addMessage(null, message);
            return null;
        }
        
        // 配送料金を計算
        BigDecimal deliveryFee = deliveryFeeService.calculateDeliveryFee(
            cartSession.getTotalPrice(), 
            deliveryAddress
        );
        
        // CartSessionに配送料金と配送先住所を設定
        cartSession.setDeliveryPrice(deliveryFee);
        cartSession.setDeliveryAddress(deliveryAddress);
        
        logger.info("[ OrderBean#calculateDeliveryFee ] deliveryPrice={}", deliveryFee);
        
        return null;
    }
    
    /**
     * 注文を確定します
     * 
     * <p>OrderServiceを呼び出して注文処理を実行します。
     * 成功時はカートをクリアして注文完了画面に遷移します。</p>
     * 
     * @return 遷移先画面（成功: orderSuccess、失敗: orderError）
     */
    public String placeOrder() {
        logger.info("[ OrderBean#placeOrder ]");
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        // カート空チェック
        if (cartSession.isEmpty()) {
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                MessageUtil.getMessage("BIZ-005"),
                ""
            );
            context.addMessage(null, message);
            return "orderError";
        }
        
        // 配送先住所チェック
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                MessageUtil.getMessage("VAL-003"),
                ""
            );
            context.addMessage(null, message);
            return null;
        }
        
        // 配送料金が未計算の場合は計算する
        if (cartSession.getDeliveryPrice() == null 
                || cartSession.getDeliveryPrice().compareTo(BigDecimal.ZERO) < 0) {
            calculateDeliveryFee();
        }
        
        try {
            // OrderTOを作成
            OrderTO orderTO = new OrderTO();
            orderTO.setCustomerId(customerBean.getCustomer().getCustomerId());
            orderTO.setDeliveryAddress(deliveryAddress);
            orderTO.setDeliveryPrice(cartSession.getDeliveryPrice());
            orderTO.setSettlementCode(settlementCode);
            orderTO.setCartItems(cartSession.getCartItems());
            
            // 注文処理を実行
            orderTran = orderService.orderBooks(orderTO);
            
            // 成功時: カートをクリア
            cartSession.clear();
            
            // 成功メッセージ
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                MessageUtil.getMessage("INFO-005"),
                ""
            );
            context.addMessage(null, message);
            
            logger.info("[ OrderBean#placeOrder ] Order completed: orderTranId={}", 
                       orderTran.getOrderTranId());
            
            // 注文完了画面に遷移
            return "orderSuccess?faces-redirect=true";
            
        } catch (OutOfStockException e) {
            // 在庫不足エラー
            logger.warn("[ OrderBean#placeOrder ] OutOfStockException: bookId={}, bookName={}", 
                       e.getBookId(), e.getBookName());
            
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                MessageUtil.getMessage("BIZ-003") + ": " + e.getBookName(),
                ""
            );
            context.addMessage(null, message);
            
            return "orderError?faces-redirect=true";
            
        } catch (OptimisticLockException e) {
            // 楽観的ロック競合エラー
            logger.warn("[ OrderBean#placeOrder ] OptimisticLockException", e);
            
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                MessageUtil.getMessage("BIZ-004"),
                ""
            );
            context.addMessage(null, message);
            
            return "orderError?faces-redirect=true";
            
        } catch (Exception e) {
            // その他のエラー
            logger.error("[ OrderBean#placeOrder ] Unexpected error", e);
            
            FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                MessageUtil.getMessage("BIZ-008"),
                ""
            );
            context.addMessage(null, message);
            
            return "orderError?faces-redirect=true";
        }
    }
    
    /**
     * 配送先住所を取得します
     * 
     * @return 配送先住所
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    /**
     * 配送先住所を設定します
     * 
     * @param deliveryAddress 配送先住所
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    /**
     * 決済方法コードを取得します
     * 
     * @return 決済方法コード
     */
    public Integer getSettlementCode() {
        return settlementCode;
    }
    
    /**
     * 決済方法コードを設定します
     * 
     * @param settlementCode 決済方法コード
     */
    public void setSettlementCode(Integer settlementCode) {
        this.settlementCode = settlementCode;
    }
    
    /**
     * 注文トランザクションを取得します
     * 
     * @return 注文トランザクション
     */
    public OrderTran getOrderTran() {
        return orderTran;
    }
    
    /**
     * 注文トランザクションを設定します
     * 
     * @param orderTran 注文トランザクション
     */
    public void setOrderTran(OrderTran orderTran) {
        this.orderTran = orderTran;
    }
    
    /**
     * 総合計金額を取得します（商品合計 + 配送料）
     * 
     * @return 総合計金額
     */
    public BigDecimal getGrandTotal() {
        BigDecimal total = cartSession.getTotalPrice();
        BigDecimal delivery = cartSession.getDeliveryPrice();
        
        if (total == null) {
            total = BigDecimal.ZERO;
        }
        if (delivery == null) {
            delivery = BigDecimal.ZERO;
        }
        
        return total.add(delivery);
    }
}

