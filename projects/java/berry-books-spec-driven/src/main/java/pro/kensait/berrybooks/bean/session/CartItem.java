package pro.kensait.berrybooks.bean.session;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CartItem - Data Transfer Object for cart items
 * 
 * Represents a single item in the shopping cart.
 */
public class CartItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer bookId;
    private String bookName;
    private String publisherName;
    private Integer price;
    private Integer count;
    private Long version;  // For optimistic locking
    private boolean remove;  // Flag for removal
    
    // Constructors
    
    public CartItem() {
    }
    
    public CartItem(Integer bookId, String bookName, String publisherName, 
                   Integer price, Integer count, Long version) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.publisherName = publisherName;
        this.price = price;
        this.count = count;
        this.version = version;
        this.remove = false;
    }
    
    // Getters and Setters
    
    public Integer getBookId() {
        return bookId;
    }
    
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    
    public String getBookName() {
        return bookName;
    }
    
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    
    public String getPublisherName() {
        return publisherName;
    }
    
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
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
    
    public boolean isRemove() {
        return remove;
    }
    
    public void setRemove(boolean remove) {
        this.remove = remove;
    }
    
    // Convenience methods
    
    /**
     * Calculate subtotal for this cart item
     * 
     * @return subtotal (price * count)
     */
    public Integer getSubtotal() {
        if (price == null || count == null) {
            return 0;
        }
        return price * count;
    }
    
    /**
     * Get subtotal as BigDecimal
     * 
     * @return subtotal as BigDecimal
     */
    public BigDecimal getSubtotalAsBigDecimal() {
        return BigDecimal.valueOf(getSubtotal());
    }
    
    @Override
    public String toString() {
        return "CartItem [bookId=" + bookId + ", bookName=" + bookName 
                + ", publisherName=" + publisherName + ", price=" + price 
                + ", count=" + count + ", version=" + version 
                + ", remove=" + remove + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem other = (CartItem) obj;
        return bookId != null && bookId.equals(other.bookId);
    }
    
    @Override
    public int hashCode() {
        return bookId != null ? bookId.hashCode() : 0;
    }
}
