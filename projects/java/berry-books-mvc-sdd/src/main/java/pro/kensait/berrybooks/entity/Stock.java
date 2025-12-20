package pro.kensait.berrybooks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;

/**
 * 在庫情報のエンティティクラス
 * 
 * <p>書籍の在庫数を管理するエンティティです。楽観的ロックに対応しています。</p>
 */
@Entity
@Table(name = "STOCK")
public class Stock implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 書籍ID（主キー、外部キー）
     */
    @Id
    @Column(name = "BOOK_ID")
    private Integer bookId;
    
    /**
     * 在庫数
     */
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;
    
    /**
     * バージョン番号（楽観的ロック用）
     */
    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;
    
    /**
     * 書籍（一対一のリレーション）
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", insertable = false, updatable = false)
    private Book book;
    
    /**
     * デフォルトコンストラクタ
     */
    public Stock() {
    }
    
    /**
     * 書籍IDを取得します
     * 
     * @return 書籍ID
     */
    public Integer getBookId() {
        return bookId;
    }
    
    /**
     * 書籍IDを設定します
     * 
     * @param bookId 書籍ID
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    
    /**
     * 在庫数を取得します
     * 
     * @return 在庫数
     */
    public Integer getQuantity() {
        return quantity;
    }
    
    /**
     * 在庫数を設定します
     * 
     * @param quantity 在庫数
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    /**
     * バージョン番号を取得します
     * 
     * @return バージョン番号
     */
    public Long getVersion() {
        return version;
    }
    
    /**
     * バージョン番号を設定します
     * 
     * @param version バージョン番号
     */
    public void setVersion(Long version) {
        this.version = version;
    }
    
    /**
     * 書籍を取得します
     * 
     * @return 書籍
     */
    public Book getBook() {
        return book;
    }
    
    /**
     * 書籍を設定します
     * 
     * @param book 書籍
     */
    public void setBook(Book book) {
        this.book = book;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.bookId == null && other.bookId != null) 
                || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Stock[bookId=" + bookId + ", quantity=" + quantity + ", version=" + version + "]";
    }
}

