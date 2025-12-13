package pro.kensait.berrybooks.service.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * OrderHistoryTO - Transfer Object for order history display
 * 
 * Used for displaying order history in a simplified format.
 */
public class OrderHistoryTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer orderTranId;
    private LocalDate orderDate;
    private String deliveryAddress;
    private Integer settlementType;
    private Integer totalPrice;
    private Integer deliveryPrice;
    
    // Constructors
    
    public OrderHistoryTO() {
    }
    
    public OrderHistoryTO(Integer orderTranId, LocalDate orderDate, String deliveryAddress,
                         Integer settlementType, Integer totalPrice, Integer deliveryPrice) {
        this.orderTranId = orderTranId;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
        this.settlementType = settlementType;
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
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
    
    // Convenience methods
    
    public BigDecimal getTotalPriceAsBigDecimal() {
        return totalPrice != null ? BigDecimal.valueOf(totalPrice) : BigDecimal.ZERO;
    }
    
    public BigDecimal getDeliveryPriceAsBigDecimal() {
        return deliveryPrice != null ? BigDecimal.valueOf(deliveryPrice) : BigDecimal.ZERO;
    }
    
    public BigDecimal getGrandTotal() {
        return getTotalPriceAsBigDecimal().add(getDeliveryPriceAsBigDecimal());
    }
    
    @Override
    public String toString() {
        return "OrderHistoryTO [orderTranId=" + orderTranId + ", orderDate=" + orderDate 
                + ", deliveryAddress=" + deliveryAddress + ", settlementType=" + settlementType 
                + ", totalPrice=" + totalPrice + ", deliveryPrice=" + deliveryPrice + "]";
    }
}
