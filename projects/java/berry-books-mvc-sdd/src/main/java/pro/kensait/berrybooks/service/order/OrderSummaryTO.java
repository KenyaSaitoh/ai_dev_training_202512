package pro.kensait.berrybooks.service.order;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * OrderSummaryTO - Transfer Object for order summary display
 * 
 * Used for displaying order summary information (e.g., in cart or order confirmation).
 */
public class OrderSummaryTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer totalPrice;
    private Integer deliveryPrice;
    private Integer itemCount;
    
    // Constructors
    
    public OrderSummaryTO() {
    }
    
    public OrderSummaryTO(Integer totalPrice, Integer deliveryPrice, Integer itemCount) {
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
        this.itemCount = itemCount;
    }
    
    // Getters and Setters
    
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
    
    public Integer getItemCount() {
        return itemCount;
    }
    
    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
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
    
    public Integer getGrandTotalAsInteger() {
        return getGrandTotal().intValue();
    }
    
    @Override
    public String toString() {
        return "OrderSummaryTO [totalPrice=" + totalPrice + ", deliveryPrice=" + deliveryPrice 
                + ", itemCount=" + itemCount + ", grandTotal=" + getGrandTotal() + "]";
    }
}
