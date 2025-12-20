package pro.kensait.berrybooks.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;

/**
 * 書籍マスタのエンティティクラス
 * 
 * <p>書籍の基本情報を表すエンティティです。</p>
 * <p>STOCKテーブルを@SecondaryTableとして扱い、在庫数を直接アクセス可能にしています。</p>
 */
@Entity
@Table(name = "BOOK")
@SecondaryTable(name = "STOCK",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "BOOK_ID"))
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
     * カテゴリID
     */
    @Column(name = "CATEGORY_ID", nullable = false, insertable = false, updatable = false)
    private Integer categoryId;
    
    /**
     * 出版社ID
     */
    @Column(name = "PUBLISHER_ID", nullable = false, insertable = false, updatable = false)
    private Integer publisherId;
    
    /**
     * 価格
     */
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;
    
    /**
     * 在庫数（STOCKテーブルから直接マッピング）
     * 画面表示用に@SecondaryTableを使用してアクセス
     */
    @Column(table = "STOCK", name = "QUANTITY")
    private Integer quantity;
    
    /**
     * カテゴリ（多対一のリレーション）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;
    
    /**
     * 出版社（多対一のリレーション）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PUBLISHER_ID", nullable = false)
    private Publisher publisher;
    
    /**
     * 在庫情報（一対一のリレーション）
     * 画面表示で常に必要なのでEAGERフェッチを使用
     */
    @OneToOne(mappedBy = "book", fetch = FetchType.EAGER)
    private Stock stock;
    
    /**
     * デフォルトコンストラクタ
     */
    public Book() {
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
     * 書籍名を取得します
     * 
     * @return 書籍名
     */
    public String getBookName() {
        return bookName;
    }
    
    /**
     * 書籍名を設定します
     * 
     * @param bookName 書籍名
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    
    /**
     * 著者名を取得します
     * 
     * @return 著者名
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * 著者名を設定します
     * 
     * @param author 著者名
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    
    /**
     * カテゴリIDを取得します
     * 
     * @return カテゴリID
     */
    public Integer getCategoryId() {
        return categoryId;
    }
    
    /**
     * カテゴリIDを設定します
     * 
     * @param categoryId カテゴリID
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * 出版社IDを取得します
     * 
     * @return 出版社ID
     */
    public Integer getPublisherId() {
        return publisherId;
    }
    
    /**
     * 出版社IDを設定します
     * 
     * @param publisherId 出版社ID
     */
    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }
    
    /**
     * 価格を取得します
     * 
     * @return 価格
     */
    public BigDecimal getPrice() {
        return price;
    }
    
    /**
     * 価格を設定します
     * 
     * @param price 価格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    /**
     * カテゴリを取得します
     * 
     * @return カテゴリ
     */
    public Category getCategory() {
        return category;
    }
    
    /**
     * カテゴリを設定します
     * 
     * @param category カテゴリ
     */
    public void setCategory(Category category) {
        this.category = category;
    }
    
    /**
     * 出版社を取得します
     * 
     * @return 出版社
     */
    public Publisher getPublisher() {
        return publisher;
    }
    
    /**
     * 出版社を設定します
     * 
     * @param publisher 出版社
     */
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
    
    /**
     * 在庫情報を取得します
     * 
     * @return 在庫情報
     */
    public Stock getStock() {
        return stock;
    }
    
    /**
     * 在庫情報を設定します
     * 
     * @param stock 在庫情報
     */
    public void setStock(Stock stock) {
        this.stock = stock;
    }
    
    /**
     * カバー画像のファイル名を取得します
     * 
     * <p>書籍名の空白をアンダースコアに置き換えて「.jpg」を付加したファイル名を返します。</p>
     * 
     * @return カバー画像のファイル名（例: "Java_SEディープダイブ.jpg"）
     */
    public String getImageFileName() {
        if (bookName == null) {
            return "no-image.jpg";
        }
        return bookName.replace(" ", "_") + ".jpg";
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
     * 在庫があるかどうかを判定します
     * 
     * @return 在庫がある場合はtrue、ない場合はfalse
     */
    public boolean isInStock() {
        return quantity != null && quantity > 0;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.bookId == null && other.bookId != null) 
                || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Book[bookId=" + bookId + ", bookName=" + bookName + ", author=" + author + "]";
    }
}

