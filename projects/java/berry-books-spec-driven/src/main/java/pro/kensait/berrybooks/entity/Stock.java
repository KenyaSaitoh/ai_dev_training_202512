package pro.kensait.berrybooks.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * Stock Entity (在庫)
 * 
 * Represents book stock with optimistic locking support.
 * The @Version annotation enables automatic optimistic lock handling.
 */
@Entity
@Table(name = "STOCK")
public class Stock implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "BOOK_ID")
    private Integer bookId;
    
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;
    
    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;
    
    // Constructors
    
    public Stock() {
    }
    
    public Stock(Integer bookId, Integer quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }
    
    public Stock(Integer bookId, Integer quantity, Long version) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.version = version;
    }
    
    // Getters and Setters
    
    public Integer getBookId() {
        return bookId;
    }
    
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    // Business methods
    
    /**
     * Check if stock is available for the requested quantity
     * 
     * @param requestedQuantity the quantity to check
     * @return true if stock is sufficient, false otherwise
     */
    public boolean isAvailable(int requestedQuantity) {
        return quantity != null && quantity >= requestedQuantity;
    }
    
    /**
     * Decrease stock quantity
     * 
     * @param count the quantity to decrease
     * @throws IllegalArgumentException if count is negative or exceeds available quantity
     */
    public void decreaseQuantity(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative: " + count);
        }
        if (quantity == null || quantity < count) {
            throw new IllegalArgumentException(
                "Insufficient stock. Available: " + quantity + ", Requested: " + count);
        }
        this.quantity -= count;
    }
    
    /**
     * Increase stock quantity
     * 
     * @param count the quantity to increase
     * @throws IllegalArgumentException if count is negative
     */
    public void increaseQuantity(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative: " + count);
        }
        if (this.quantity == null) {
            this.quantity = count;
        } else {
            this.quantity += count;
        }
    }
    
    // hashCode, equals, toString
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
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
        Stock other = (Stock) obj;
        if (bookId == null) {
            if (other.bookId != null)
                return false;
        } else if (!bookId.equals(other.bookId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Stock [bookId=" + bookId + ", quantity=" + quantity + ", version=" + version + "]";
    }
}
