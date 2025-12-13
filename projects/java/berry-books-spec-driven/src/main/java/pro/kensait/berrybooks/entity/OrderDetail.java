package pro.kensait.berrybooks.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * OrderDetail Entity (注文明細)
 * 
 * Represents an order detail line item with composite primary key.
 * Uses @IdClass to define the composite key (ORDER_TRAN_ID, ORDER_DETAIL_ID).
 */
@Entity
@Table(name = "ORDER_DETAIL")
@IdClass(OrderDetailPK.class)
public class OrderDetail implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ORDER_TRAN_ID")
    private Integer orderTranId;
    
    @Id
    @Column(name = "ORDER_DETAIL_ID")
    private Integer orderDetailId;
    
    @ManyToOne
    @JoinColumn(name = "ORDER_TRAN_ID", insertable = false, updatable = false)
    private OrderTran orderTran;
    
    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;
    
    @Column(name = "PRICE", nullable = false)
    private Integer price;
    
    @Column(name = "COUNT", nullable = false)
    private Integer count;
    
    // Constructors
    
    public OrderDetail() {
    }
    
    public OrderDetail(Integer orderTranId, Integer orderDetailId, Book book, 
                      Integer price, Integer count) {
        this.orderTranId = orderTranId;
        this.orderDetailId = orderDetailId;
        this.book = book;
        this.price = price;
        this.count = count;
    }
    
    // Getters and Setters
    
    public Integer getOrderTranId() {
        return orderTranId;
    }
    
    public void setOrderTranId(Integer orderTranId) {
        this.orderTranId = orderTranId;
    }
    
    public Integer getOrderDetailId() {
        return orderDetailId;
    }
    
    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }
    
    public OrderTran getOrderTran() {
        return orderTran;
    }
    
    public void setOrderTran(OrderTran orderTran) {
        this.orderTran = orderTran;
        if (orderTran != null) {
            this.orderTranId = orderTran.getOrderTranId();
        }
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
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
    
    // Convenience methods
    
    /**
     * Get price as BigDecimal for calculations
     */
    public BigDecimal getPriceAsBigDecimal() {
        return price != null ? BigDecimal.valueOf(price) : BigDecimal.ZERO;
    }
    
    /**
     * Calculate subtotal (price * count)
     */
    public BigDecimal getSubtotal() {
        if (price == null || count == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(count));
    }
    
    /**
     * Get subtotal as Integer
     */
    public Integer getSubtotalAsInteger() {
        return getSubtotal().intValue();
    }
    
    // hashCode, equals, toString
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderTranId == null) ? 0 : orderTranId.hashCode());
        result = prime * result + ((orderDetailId == null) ? 0 : orderDetailId.hashCode());
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
        OrderDetail other = (OrderDetail) obj;
        if (orderTranId == null) {
            if (other.orderTranId != null)
                return false;
        } else if (!orderTranId.equals(other.orderTranId))
            return false;
        if (orderDetailId == null) {
            if (other.orderDetailId != null)
                return false;
        } else if (!orderDetailId.equals(other.orderDetailId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "OrderDetail [orderTranId=" + orderTranId + ", orderDetailId=" + orderDetailId 
                + ", bookId=" + (book != null ? book.getBookId() : null)
                + ", price=" + price + ", count=" + count + "]";
    }
}
