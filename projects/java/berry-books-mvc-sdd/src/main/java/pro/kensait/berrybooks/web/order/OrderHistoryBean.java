package pro.kensait.berrybooks.web.order;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pro.kensait.berrybooks.service.order.OrderHistoryTO;
import pro.kensait.berrybooks.service.order.OrderService;
import pro.kensait.berrybooks.service.order.OrderSummaryTO;
import pro.kensait.berrybooks.web.customer.CustomerBean;

/**
 * 注文履歴参照のコントローラーBean
 * 
 * <p>注文履歴一覧表示と注文詳細表示の処理を行います。</p>
 */
@Named
@ViewScoped
public class OrderHistoryBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(OrderHistoryBean.class);
    
    @Inject
    private OrderService orderService;
    
    @Inject
    private CustomerBean customerBean;
    
    /**
     * 注文履歴リスト
     */
    private List<OrderHistoryTO> orderHistoryList;
    
    /**
     * 注文サマリー（注文詳細表示用）
     */
    private OrderSummaryTO orderSummary;
    
    /**
     * 注文ID（注文詳細画面用のパラメータ）
     */
    private Integer orderTranId;
    
    /**
     * 初期化処理
     * 
     * <p>注文履歴一覧画面の初期化を行います。</p>
     * <p>注文詳細画面の場合は、viewActionから明示的にloadOrderDetail()が呼ばれます。</p>
     */
    @PostConstruct
    public void init() {
        logger.info("[ OrderHistoryBean#init ] orderTranId={}", orderTranId);
        
        // 注文履歴一覧画面の場合のみ、ここで注文履歴を読み込む
        // 注文詳細画面の場合は、viewActionからloadOrderDetail()が呼ばれる
        if (orderTranId == null) {
            loadOrderHistory();
        }
    }
    
    /**
     * 注文詳細を読み込みます
     * 
     * <p>viewActionから呼ばれ、orderTranIdパラメータから注文詳細を取得します。</p>
     */
    public void loadOrderDetail() {
        logger.info("[ OrderHistoryBean#loadOrderDetail ] orderTranId={}", orderTranId);
        
        if (orderTranId != null) {
            orderSummary = orderService.getOrderDetail(orderTranId);
            
            if (orderSummary == null) {
                logger.warn("OrderSummary not found: orderTranId={}", orderTranId);
            } else {
                logger.info("OrderSummary loaded: orderTranId={}, detailCount={}, settlementType={}", 
                            orderTranId, 
                            orderSummary.getOrderDetails() != null ? orderSummary.getOrderDetails().size() : 0,
                            orderSummary.getOrderTran() != null ? orderSummary.getOrderTran().getSettlementType() : "NULL");
            }
        } else {
            logger.warn("orderTranId is null in loadOrderDetail");
        }
    }
    
    /**
     * 注文履歴を読み込みます
     * 
     * <p>ログイン中の顧客の注文履歴を取得します。</p>
     */
    public void loadOrderHistory() {
        logger.info("[ OrderHistoryBean#loadOrderHistory ]");
        
        if (customerBean.getCustomer() != null) {
            Integer customerId = customerBean.getCustomer().getCustomerId();
            orderHistoryList = orderService.getOrderHistory(customerId);
            
            logger.debug("Order history loaded: count={}", 
                        orderHistoryList != null ? orderHistoryList.size() : 0);
        } else {
            logger.warn("Customer not logged in");
        }
    }
    
    /**
     * 注文詳細を取得します
     * 
     * <p>指定された注文IDの詳細情報を取得し、注文詳細画面に遷移します。</p>
     * 
     * @param orderTranId 注文ID
     * @return 遷移先画面（orderDetail）
     */
    public String getOrderDetail(Integer orderTranId) {
        logger.info("[ OrderHistoryBean#getOrderDetail ] orderTranId={}", orderTranId);
        
        // 注文詳細画面にorderTranIdをパラメータとして渡す
        return "orderDetail?faces-redirect=true&orderTranId=" + orderTranId;
    }
    
    /**
     * 注文履歴リストを取得します
     * 
     * @return 注文履歴リスト
     */
    public List<OrderHistoryTO> getOrderHistoryList() {
        return orderHistoryList;
    }
    
    /**
     * 注文履歴リストを設定します
     * 
     * @param orderHistoryList 注文履歴リスト
     */
    public void setOrderHistoryList(List<OrderHistoryTO> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
    }
    
    /**
     * 注文サマリーを取得します
     * 
     * @return 注文サマリー
     */
    public OrderSummaryTO getOrderSummary() {
        return orderSummary;
    }
    
    /**
     * 注文サマリーを設定します
     * 
     * @param orderSummary 注文サマリー
     */
    public void setOrderSummary(OrderSummaryTO orderSummary) {
        this.orderSummary = orderSummary;
    }
    
    /**
     * 注文IDを取得します
     * 
     * @return 注文ID
     */
    public Integer getOrderTranId() {
        return orderTranId;
    }
    
    /**
     * 注文IDを設定します
     * 
     * @param orderTranId 注文ID
     */
    public void setOrderTranId(Integer orderTranId) {
        this.orderTranId = orderTranId;
    }
}



