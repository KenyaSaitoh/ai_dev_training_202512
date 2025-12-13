package pro.kensait.berrybooks.bean;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.bean.session.CartItem;
import pro.kensait.berrybooks.bean.session.CartSession;
import pro.kensait.berrybooks.bean.session.CustomerBean;
import pro.kensait.berrybooks.entity.OrderTran;
import pro.kensait.berrybooks.service.order.OrderHistoryTO;
import pro.kensait.berrybooks.service.order.OrderService;
import pro.kensait.berrybooks.service.order.OrderTO;
import pro.kensait.berrybooks.service.order.OutOfStockException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderBean - View-scoped bean for order processing
 * 
 * Manages order placement and order history display.
 */
@Named
@ViewScoped
public class OrderBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(OrderBean.class);
    
    @Inject
    private OrderService orderService;
    
    @Inject
    private CartSession cartSession;
    
    @Inject
    private CustomerBean customerBean;
    
    private OrderTran orderTran;
    private List<OrderHistoryTO> orderList = new ArrayList<>();
    private Integer settlementType = 1; // Default: Bank Transfer
    
    // Constructors
    
    public OrderBean() {
    }
    
    // Getters and Setters
    
    public OrderTran getOrderTran() {
        return orderTran;
    }
    
    public void setOrderTran(OrderTran orderTran) {
        this.orderTran = orderTran;
    }
    
    public List<OrderHistoryTO> getOrderList() {
        return orderList;
    }
    
    public void setOrderList(List<OrderHistoryTO> orderList) {
        this.orderList = orderList;
    }
    
    public Integer getSettlementType() {
        return settlementType;
    }
    
    public void setSettlementType(Integer settlementType) {
        this.settlementType = settlementType;
    }
    
    // Business methods
    
    /**
     * Place order
     * 
     * @return navigation outcome
     */
    public String placeOrder() {
        logger.info("[ OrderBean#placeOrder ] customerId={}, settlementType={}", 
                customerBean.getCustomerId(), settlementType);
        
        try {
            // Validate cart
            if (cartSession.isEmpty()) {
                logger.warn("[ OrderBean#placeOrder ] Cart is empty");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "エラー", "カートが空です。"));
                return null;
            }
            
            // Validate customer
            if (!customerBean.isLoggedIn()) {
                logger.warn("[ OrderBean#placeOrder ] User not logged in");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "エラー", "ログインしてください。"));
                return "index?faces-redirect=true";
            }
            
            // Create OrderTO from cart session
            OrderTO orderTO = new OrderTO();
            orderTO.setCustomerId(customerBean.getCustomerId());
            orderTO.setDeliveryAddress(cartSession.getDeliveryAddress());
            orderTO.setSettlementType(settlementType);
            orderTO.setDeliveryPrice(cartSession.getDeliveryPrice().intValue());
            
            // Add order details from cart items
            for (CartItem cartItem : cartSession.getCartItems()) {
                orderTO.addOrderDetail(
                        cartItem.getBookId(),
                        cartItem.getPrice(),
                        cartItem.getCount(),
                        cartItem.getVersion()
                );
            }
            
            // Place order
            orderTran = orderService.orderBooks(orderTO);
            
            logger.info("[ OrderBean#placeOrder ] Order placed successfully: orderTranId={}", 
                    orderTran.getOrderTranId());
            
            // Clear cart after successful order
            cartSession.clear();
            
            return "orderSuccess?faces-redirect=true";
            
        } catch (OutOfStockException e) {
            logger.warn("[ OrderBean#placeOrder ] Out of stock: bookId={}, bookName={}", 
                    e.getBookId(), e.getBookName());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "在庫不足", 
                            String.format("「%s」の在庫が不足しています。", e.getBookName())));
            return "orderError?faces-redirect=true";
            
        } catch (OptimisticLockException e) {
            logger.warn("[ OrderBean#placeOrder ] Optimistic lock exception - concurrent update detected");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "注文エラー", 
                            "他のユーザーが同時に購入したため、注文を完了できませんでした。カートを確認してください。"));
            return "orderError?faces-redirect=true";
            
        } catch (Exception e) {
            logger.error("[ OrderBean#placeOrder ] Order placement failed", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "注文エラー", "注文処理中にエラーが発生しました。"));
            return "orderError?faces-redirect=true";
        }
    }
    
    /**
     * Load order history (variation 1 - using entity relationships)
     * 
     * @return navigation outcome
     */
    public String loadOrderHistory() {
        logger.info("[ OrderBean#loadOrderHistory ] customerId={}", customerBean.getCustomerId());
        
        try {
            if (!customerBean.isLoggedIn()) {
                logger.warn("[ OrderBean#loadOrderHistory ] User not logged in");
                return "index?faces-redirect=true";
            }
            
            orderList = orderService.getOrderHistory(customerBean.getCustomerId());
            
            logger.info("[ OrderBean#loadOrderHistory ] Loaded {} orders", orderList.size());
            
            return null; // Stay on current page
            
        } catch (Exception e) {
            logger.error("[ OrderBean#loadOrderHistory ] Failed to load order history", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "エラー", "注文履歴の読み込みに失敗しました。"));
            return null;
        }
    }
    
    /**
     * Load order history (variation 2 - using DTO projection)
     * 
     * @return navigation outcome
     */
    public String loadOrderHistory2() {
        logger.info("[ OrderBean#loadOrderHistory2 ] customerId={}", customerBean.getCustomerId());
        
        try {
            if (!customerBean.isLoggedIn()) {
                logger.warn("[ OrderBean#loadOrderHistory2 ] User not logged in");
                return "index?faces-redirect=true";
            }
            
            orderList = orderService.getOrderHistory2(customerBean.getCustomerId());
            
            logger.info("[ OrderBean#loadOrderHistory2 ] Loaded {} orders (DTO projection)", 
                    orderList.size());
            
            return null; // Stay on current page
            
        } catch (Exception e) {
            logger.error("[ OrderBean#loadOrderHistory2 ] Failed to load order history", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "エラー", "注文履歴の読み込みに失敗しました。"));
            return null;
        }
    }
    
    /**
     * Load order history (variation 3 - using JOIN FETCH)
     * 
     * @return navigation outcome
     */
    public String loadOrderHistory3() {
        logger.info("[ OrderBean#loadOrderHistory3 ] customerId={}", customerBean.getCustomerId());
        
        try {
            if (!customerBean.isLoggedIn()) {
                logger.warn("[ OrderBean#loadOrderHistory3 ] User not logged in");
                return "index?faces-redirect=true";
            }
            
            orderList = orderService.getOrderHistory3(customerBean.getCustomerId());
            
            logger.info("[ OrderBean#loadOrderHistory3 ] Loaded {} orders (JOIN FETCH)", 
                    orderList.size());
            
            return null; // Stay on current page
            
        } catch (Exception e) {
            logger.error("[ OrderBean#loadOrderHistory3 ] Failed to load order history", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "エラー", "注文履歴の読み込みに失敗しました。"));
            return null;
        }
    }
    
    /**
     * Get order transaction by ID
     * 
     * @param orderTranId the order transaction ID
     * @return navigation outcome
     */
    public String getOrderTran(Integer orderTranId) {
        logger.info("[ OrderBean#getOrderTran ] orderTranId={}", orderTranId);
        
        try {
            orderTran = orderService.getOrderTran(orderTranId);
            
            if (orderTran == null) {
                logger.warn("[ OrderBean#getOrderTran ] Order not found: orderTranId={}", orderTranId);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "エラー", "注文が見つかりません。"));
                return null;
            }
            
            logger.info("[ OrderBean#getOrderTran ] Order loaded: orderTranId={}", orderTranId);
            
            return null; // Stay on current page
            
        } catch (Exception e) {
            logger.error("[ OrderBean#getOrderTran ] Failed to load order", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "エラー", "注文の読み込みに失敗しました。"));
            return null;
        }
    }
    
    /**
     * Get order transaction with details by ID
     * 
     * @param orderTranId the order transaction ID
     * @return navigation outcome
     */
    public String getOrderTranWithDetails(Integer orderTranId) {
        logger.info("[ OrderBean#getOrderTranWithDetails ] orderTranId={}", orderTranId);
        
        try {
            orderTran = orderService.getOrderTranWithDetails(orderTranId);
            
            if (orderTran == null) {
                logger.warn("[ OrderBean#getOrderTranWithDetails ] Order not found: orderTranId={}", 
                        orderTranId);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "エラー", "注文が見つかりません。"));
                return null;
            }
            
            logger.info("[ OrderBean#getOrderTranWithDetails ] Order with details loaded: orderTranId={}", 
                    orderTranId);
            
            return "orderDetail?faces-redirect=true";
            
        } catch (Exception e) {
            logger.error("[ OrderBean#getOrderTranWithDetails ] Failed to load order details", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "エラー", "注文詳細の読み込みに失敗しました。"));
            return null;
        }
    }
    
    /**
     * Navigate back to order history
     * 
     * @return navigation outcome
     */
    public String backToOrderHistory() {
        logger.info("[ OrderBean#backToOrderHistory ]");
        return "orderHistory?faces-redirect=true";
    }
    
    /**
     * Navigate to book search
     * 
     * @return navigation outcome
     */
    public String continueShopping() {
        logger.info("[ OrderBean#continueShopping ]");
        return "bookSearch?faces-redirect=true";
    }
}
