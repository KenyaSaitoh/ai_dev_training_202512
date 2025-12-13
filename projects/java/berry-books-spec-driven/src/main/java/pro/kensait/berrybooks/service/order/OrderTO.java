package pro.kensait.berrybooks.service.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderTO - Transfer Object for order data
 * 
 * Used to transfer order information between layers.
 */
public class OrderTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer customerId;
    private LocalDate orderDate;
    private Integer totalPrice;
    private Integer deliveryPrice;
    private String deliveryAddress;
    private Integer settlementType;
    private List<OrderItemTO> orderItems = new ArrayList<>();
    
    // Constructors
    
    public OrderTO() {
    }
    
    // Getters and Setters
    
    public Integer getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
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
    
    public List<OrderItemTO> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItemTO> orderItems) {
        this.orderItems = orderItems;
    }
    
    public void addOrderItem(OrderItemTO orderItem) {
        this.orderItems.add(orderItem);
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
        return "OrderTO [customerId=" + customerId + ", orderDate=" + orderDate 
                + ", totalPrice=" + totalPrice + ", deliveryPrice=" + deliveryPrice 
                + ", deliveryAddress=" + deliveryAddress + ", settlementType=" + settlementType 
                + ", orderItems=" + orderItems.size() + " items]";
    }
    
    /**
     * OrderItemTO - Transfer Object for order item data
     */
    public static class OrderItemTO implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private Integer bookId;
        private Integer price;
        private Integer count;
        private Long version;  // For optimistic locking
        
        public OrderItemTO() {
        }
        
        public OrderItemTO(Integer bookId, Integer price, Integer count, Long version) {
            this.bookId = bookId;
            this.price = price;
            this.count = count;
            this.version = version;
        }
        
        public Integer getBookId() {
            return bookId;
        }
        
        public void setBookId(Integer bookId) {
            this.bookId = bookId;
        }
        
        public Integer getPrice() {
            return price;
        }
        
        public void setPrice(Integer price) {
            this.price = price;
        }
        
        public Integer getCount() {
            return count;
        }
        
        public void setCount(Integer count) {
            this.count = count;
        }
        
        public Long getVersion() {
            return version;
        }
        
        public void setVersion(Long version) {
            this.version = version;
        }
        
        public BigDecimal getSubtotal() {
            if (price == null || count == null) {
                return BigDecimal.ZERO;
            }
            return BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(count));
        }
        
        @Override
        public String toString() {
            return "OrderItemTO [bookId=" + bookId + ", price=" + price 
                    + ", count=" + count + ", version=" + version + "]";
        }
    }
}
