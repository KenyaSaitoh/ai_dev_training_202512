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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * 書籍エンティティ
 * 
 * テーブル: BOOK
 * 目的: 書籍の基本情報を管理
 */
@Entity
@Table(name = "BOOK")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 書籍ID（主キー、自動採番）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    private Integer bookId;

    /**
     * 書籍名
     */
    @Column(name = "BOOK_NAME", length = 80, nullable = false)
    private String bookName;

    /**
     * 著者名
     */
    @Column(name = "AUTHOR", length = 40, nullable = false)
    private String author;

    /**
     * 価格（円）
     */
    @Column(name = "PRICE", nullable = false)
    private Integer price;

    /**
     * カテゴリ（多対1リレーションシップ）
     */
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    /**
     * 出版社（多対1リレーションシップ）
     */
    @ManyToOne
    @JoinColumn(name = "PUBLISHER_ID", nullable = false)
    private Publisher publisher;

    /**
     * 在庫（1対1リレーションシップ）
     */
    @OneToOne(mappedBy = "book")
    private Stock stock;

    // ========================================
    // Constructors
    // ========================================

    public Book() {
    }

    public Book(Integer bookId, String bookName, String author, Integer price) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
    }

    // ========================================
    // Business Methods
    // ========================================

    /**
     * カバー画像ファイル名を生成
     * 
     * BR-005: カバー画像ファイル名は書籍名 + ".jpg" で生成
     * BR-007: 画像ファイルが存在しない場合は no-image.jpg を表示
     * 
     * @return 画像ファイル名（書籍名.jpg）
     */
    public String getImageFileName() {
        if (bookName == null || bookName.isEmpty()) {
            return "no-image.jpg";
        }
        return bookName + ".jpg";
    }

    /**
     * 在庫数を取得（便利メソッド）
     * 
     * @return 在庫数（在庫情報がない場合は0）
     */
    public Integer getQuantity() {
        return (stock != null) ? stock.getQuantity() : 0;
    }

    // ========================================
    // Getters and Setters
    // ========================================

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    // ========================================
    // toString, equals, hashCode
    // ========================================

    @Override
    public String toString() {
        return "Book [bookId=" + bookId + ", bookName=" + bookName + ", author=" + author + ", price=" + price + "]";
    }

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
}

