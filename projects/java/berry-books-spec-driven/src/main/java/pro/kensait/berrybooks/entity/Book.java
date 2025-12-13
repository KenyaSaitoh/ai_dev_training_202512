package pro.kensait.berrybooks.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;

/**
 * Book Entity (書籍)
 * 
 * Represents a book in the system with category and publisher relationships.
 * Uses @SecondaryTable to include stock quantity from STOCK table.
 */
@Entity
@Table(name = "BOOK")
@SecondaryTable(name = "STOCK", pkJoinColumns = @jakarta.persistence.PrimaryKeyJoinColumn(name = "BOOK_ID"))
public class Book implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    private Integer bookId;
    
    @Column(name = "BOOK_NAME", nullable = false, length = 80)
    private String bookName;
    
    @Column(name = "AUTHOR", nullable = false, length = 40)
    private String author;
    
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "PUBLISHER_ID", nullable = false)
    private Publisher publisher;
    
    @Column(name = "PRICE", nullable = false)
    private Integer price;
    
    // Quantity from STOCK table (using @SecondaryTable)
    @Column(name = "QUANTITY", table = "STOCK")
    private Integer quantity;
    
    // Constructors
    
    public Book() {
    }
    
    public Book(Integer bookId, String bookName, String author, Category category, 
                Publisher publisher, Integer price, Integer quantity) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.price = price;
        this.quantity = quantity;
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
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Publisher getPublisher() {
        return publisher;
    }
    
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
    
    public Integer getPrice() {
        return price;
    }
    
    public void setPrice(Integer price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    // Convenience methods
    
    /**
     * Get price as BigDecimal for calculations
     */
    public BigDecimal getPriceAsBigDecimal() {
        return price != null ? BigDecimal.valueOf(price) : BigDecimal.ZERO;
    }
    
    /**
     * Check if book is in stock
     */
    public boolean isInStock() {
        return quantity != null && quantity > 0;
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
        Book other = (Book) obj;
        if (bookId == null) {
            if (other.bookId != null)
                return false;
        } else if (!bookId.equals(other.bookId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Book [bookId=" + bookId + ", bookName=" + bookName + ", author=" + author 
                + ", categoryId=" + (category != null ? category.getCategoryId() : null)
                + ", publisherId=" + (publisher != null ? publisher.getPublisherId() : null)
                + ", price=" + price + ", quantity=" + quantity + "]";
    }
}
