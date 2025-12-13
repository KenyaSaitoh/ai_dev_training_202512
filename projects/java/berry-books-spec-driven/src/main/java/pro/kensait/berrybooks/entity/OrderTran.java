package pro.kensait.berrybooks.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * OrderTran Entity (注文取引)
 * 
 * Represents an order transaction with customer and order details.
 */
@Entity
@Table(name = "ORDER_TRAN")
public class OrderTran implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_TRAN_ID")
    private Integer orderTranId;
    
    @Column(name = "ORDER_DATE", nullable = false)
    private LocalDate orderDate;
    
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;
    
    @Column(name = "TOTAL_PRICE", nullable = false)
    private Integer totalPrice;
    
    @Column(name = "DELIVERY_PRICE", nullable = false)
    private Integer deliveryPrice;
    
    @Column(name = "DELIVERY_ADDRESS", nullable = false, length = 30)
    private String deliveryAddress;
    
    @Column(name = "SETTLEMENT_TYPE", nullable = false)
    private Integer settlementType;
    
    @OneToMany(mappedBy = "orderTran", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    
    // Constructors
    
    public OrderTran() {
    }
    
    public OrderTran(Integer orderTranId, LocalDate orderDate, Customer customer,
                    Integer totalPrice, Integer deliveryPrice, String deliveryAddress,
                    Integer settlementType) {
        this.orderTranId = orderTranId;
        this.orderDate = orderDate;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
        this.deliveryAddress = deliveryAddress;
        this.settlementType = settlementType;
    }
    
    // Getters and Setters
    
    public Integer getOrderTranId() {
        return orderTranId;
    }
    
    public void setOrderTranId(Integer orderTranId) {
        this.orderTranId = orderTranId;
    }
    
    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Integer getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }
    
    public void setDeliveryPrice(Integer deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    public Integer getSettlementType() {
        return settlementType;
    }
    
    public void setSettlementType(Integer settlementType) {
        this.settlementType = settlementType;
    }
    
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }
    
    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    // Convenience methods
    
    /**
     * Add order detail to this order transaction
     * 
     * @param orderDetail the order detail to add
     */
    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrderTran(this);
    }
    
    /**
     * Remove order detail from this order transaction
     * 
     * @param orderDetail the order detail to remove
     */
    public void removeOrderDetail(OrderDetail orderDetail) {
        orderDetails.remove(orderDetail);
        orderDetail.setOrderTran(null);
    }
    
    /**
     * Get total price as BigDecimal for calculations
     */
    public BigDecimal getTotalPriceAsBigDecimal() {
        return totalPrice != null ? BigDecimal.valueOf(totalPrice) : BigDecimal.ZERO;
    }
    
    /**
     * Get delivery price as BigDecimal for calculations
     */
    public BigDecimal getDeliveryPriceAsBigDecimal() {
        return deliveryPrice != null ? BigDecimal.valueOf(deliveryPrice) : BigDecimal.ZERO;
    }
    
    /**
     * Calculate grand total (total price + delivery price)
     */
    public BigDecimal getGrandTotal() {
        return getTotalPriceAsBigDecimal().add(getDeliveryPriceAsBigDecimal());
    }
    
    // hashCode, equals, toString
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderTranId == null) ? 0 : orderTranId.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderTran other = (OrderTran) obj;
        if (orderTranId == null) {
            if (other.orderTranId != null)
                return false;
        } else if (!orderTranId.equals(other.orderTranId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "OrderTran [orderTranId=" + orderTranId + ", orderDate=" + orderDate 
                + ", customerId=" + (customer != null ? customer.getCustomerId() : null)
                + ", totalPrice=" + totalPrice + ", deliveryPrice=" + deliveryPrice 
                + ", deliveryAddress=" + deliveryAddress + ", settlementType=" + settlementType + "]";
    }
}
