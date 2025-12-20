package pro.kensait.berrybooks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 注文明細のエンティティクラス
 * 
 * <p>注文の詳細（購入書籍）を表すエンティティです。</p>
 */
@Entity
@Table(name = "ORDER_DETAIL")
public class OrderDetail implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 複合主キー
     */
    @EmbeddedId
    private OrderDetailPK id;
    
    /**
     * 書籍ID
     */
    @Column(name = "BOOK_ID", nullable = false, insertable = false, updatable = false)
    private Integer bookId;
    
    /**
     * 価格（注文時点の書籍価格）
     */
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;
    
    /**
     * 注文数
     */
    @Column(name = "COUNT", nullable = false)
    private Integer count;
    
    /**
     * 注文トランザクション（多対一のリレーション）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_TRAN_ID", insertable = false, updatable = false)
    private OrderTran orderTran;
    
    /**
     * 書籍（多対一のリレーション）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;
    
    /**
     * デフォルトコンストラクタ
     */
    public OrderDetail() {
    }
    
    /**
     * 複合主キーを取得します
     * 
     * @return 複合主キー
     */
    public OrderDetailPK getId() {
        return id;
    }
    
    /**
     * 複合主キーを設定します
     * 
     * @param id 複合主キー
     */
    public void setId(OrderDetailPK id) {
        this.id = id;
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
     * 注文数を取得します
     * 
     * @return 注文数
     */
    public Integer getCount() {
        return count;
    }
    
    /**
     * 注文数を設定します
     * 
     * @param count 注文数
     */
    public void setCount(Integer count) {
        this.count = count;
    }
    
    /**
     * 注文トランザクションを取得します
     * 
     * @return 注文トランザクション
     */
    public OrderTran getOrderTran() {
        return orderTran;
    }
    
    /**
     * 注文トランザクションを設定します
     * 
     * @param orderTran 注文トランザクション
     */
    public void setOrderTran(OrderTran orderTran) {
        this.orderTran = orderTran;
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
    
    /**
     * 小計を計算します（価格 × 数量）
     * 
     * @return 小計
     */
    public BigDecimal getSubTotal() {
        if (price == null || count == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(count));
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrderDetail)) {
            return false;
        }
        OrderDetail other = (OrderDetail) object;
        if ((this.id == null && other.id != null) 
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "OrderDetail[id=" + id + ", price=" + price + ", count=" + count + "]";
    }
}

