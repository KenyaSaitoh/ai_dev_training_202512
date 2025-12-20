package pro.kensait.berrybooks.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 注文トランザクションのエンティティクラス
 * 
 * <p>注文の基本情報を表すエンティティです。</p>
 */
@Entity
@Table(name = "ORDER_TRAN")
public class OrderTran implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 注文取引ID（主キー、自動採番）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_TRAN_ID")
    private Integer orderTranId;
    
    /**
     * 注文日
     */
    @Column(name = "ORDER_DATE", nullable = false)
    private LocalDate orderDate;
    
    /**
     * 顧客ID
     */
    @Column(name = "CUSTOMER_ID", nullable = false, insertable = false, updatable = false)
    private Integer customerId;
    
    /**
     * 注文金額合計
     */
    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;
    
    /**
     * 配送料金
     */
    @Column(name = "DELIVERY_PRICE", nullable = false)
    private BigDecimal deliveryPrice;
    
    /**
     * 配送先住所
     */
    @Column(name = "DELIVERY_ADDRESS", length = 30, nullable = false)
    private String deliveryAddress;
    
    /**
     * 決済方法（1:銀行振込, 2:クレジットカード, 3:着払い）
     */
    @Column(name = "SETTLEMENT_TYPE", nullable = false)
    private Integer settlementType;
    
    /**
     * 顧客（多対一のリレーション）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;
    
    /**
     * 注文明細リスト（一対多のリレーション）
     */
    @OneToMany(mappedBy = "orderTran", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;
    
    /**
     * デフォルトコンストラクタ
     */
    public OrderTran() {
    }
    
    /**
     * 注文取引IDを取得します
     * 
     * @return 注文取引ID
     */
    public Integer getOrderTranId() {
        return orderTranId;
    }
    
    /**
     * 注文取引IDを設定します
     * 
     * @param orderTranId 注文取引ID
     */
    public void setOrderTranId(Integer orderTranId) {
        this.orderTranId = orderTranId;
    }
    
    /**
     * 注文日を取得します
     * 
     * @return 注文日
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    /**
     * 注文日を設定します
     * 
     * @param orderDate 注文日
     */
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    
    /**
     * 顧客IDを取得します
     * 
     * @return 顧客ID
     */
    public Integer getCustomerId() {
        return customerId;
    }
    
    /**
     * 顧客IDを設定します
     * 
     * @param customerId 顧客ID
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    /**
     * 注文金額合計を取得します
     * 
     * @return 注文金額合計
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    /**
     * 注文金額合計を設定します
     * 
     * @param totalPrice 注文金額合計
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    /**
     * 配送料金を取得します
     * 
     * @return 配送料金
     */
    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }
    
    /**
     * 配送料金を設定します
     * 
     * @param deliveryPrice 配送料金
     */
    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
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
     * 決済方法を取得します
     * 
     * @return 決済方法
     */
    public Integer getSettlementType() {
        return settlementType;
    }
    
    /**
     * 決済方法を設定します
     * 
     * @param settlementType 決済方法
     */
    public void setSettlementType(Integer settlementType) {
        this.settlementType = settlementType;
    }
    
    /**
     * 顧客を取得します
     * 
     * @return 顧客
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * 顧客を設定します
     * 
     * @param customer 顧客
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    /**
     * 注文明細リストを取得します
     * 
     * @return 注文明細リスト
     */
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }
    
    /**
     * 注文明細リストを設定します
     * 
     * @param orderDetails 注文明細リスト
     */
    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderTranId != null ? orderTranId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrderTran)) {
            return false;
        }
        OrderTran other = (OrderTran) object;
        if ((this.orderTranId == null && other.orderTranId != null) 
                || (this.orderTranId != null && !this.orderTranId.equals(other.orderTranId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "OrderTran[orderTranId=" + orderTranId + ", orderDate=" + orderDate + ", totalPrice=" + totalPrice + "]";
    }
}

