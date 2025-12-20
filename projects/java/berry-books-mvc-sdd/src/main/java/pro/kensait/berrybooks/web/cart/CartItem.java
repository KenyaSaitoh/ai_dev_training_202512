package pro.kensait.berrybooks.web.cart;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * カート内の書籍情報を保持するDTOクラス
 * 
 * <p>ショッピングカート内の1つの商品アイテムを表します。</p>
 */
public class CartItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 書籍ID
     */
    private Integer bookId;
    
    /**
     * 書籍名
     */
    private String bookName;
    
    /**
     * 出版社名
     */
    private String publisherName;
    
    /**
     * 単価
     */
    private BigDecimal price;
    
    /**
     * 数量
     */
    private Integer count;
    
    /**
     * 在庫バージョン番号（楽観的ロック用）
     */
    private Long version;
    
    /**
     * 削除フラグ
     */
    private boolean remove;
    
    /**
     * デフォルトコンストラクタ
     */
    public CartItem() {
    }
    
    /**
     * すべてのフィールドを初期化するコンストラクタ
     * 
     * @param bookId 書籍ID
     * @param bookName 書籍名
     * @param publisherName 出版社名
     * @param price 単価
     * @param count 数量
     * @param version 在庫バージョン番号
     */
    public CartItem(Integer bookId, String bookName, String publisherName, 
                    BigDecimal price, Integer count, Long version) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.publisherName = publisherName;
        this.price = price;
        this.count = count;
        this.version = version;
        this.remove = false;
    }
    
    /**
     * 小計を計算します（単価 × 数量）
     * 
     * @return 小計金額
     */
    public BigDecimal getSubTotal() {
        if (price == null || count == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(count));
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
     * 出版社名を取得します
     * 
     * @return 出版社名
     */
    public String getPublisherName() {
        return publisherName;
    }
    
    /**
     * 出版社名を設定します
     * 
     * @param publisherName 出版社名
     */
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
    
    /**
     * 単価を取得します
     * 
     * @return 単価
     */
    public BigDecimal getPrice() {
        return price;
    }
    
    /**
     * 単価を設定します
     * 
     * @param price 単価
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    /**
     * 数量を取得します
     * 
     * @return 数量
     */
    public Integer getCount() {
        return count;
    }
    
    /**
     * 数量を設定します
     * 
     * @param count 数量
     */
    public void setCount(Integer count) {
        this.count = count;
    }
    
    /**
     * 在庫バージョン番号を取得します
     * 
     * @return 在庫バージョン番号
     */
    public Long getVersion() {
        return version;
    }
    
    /**
     * 在庫バージョン番号を設定します
     * 
     * @param version 在庫バージョン番号
     */
    public void setVersion(Long version) {
        this.version = version;
    }
    
    /**
     * 削除フラグを取得します
     * 
     * @return 削除フラグ
     */
    public boolean isRemove() {
        return remove;
    }
    
    /**
     * 削除フラグを設定します
     * 
     * @param remove 削除フラグ
     */
    public void setRemove(boolean remove) {
        this.remove = remove;
    }
    
    @Override
    public String toString() {
        return "CartItem[bookId=" + bookId + ", bookName=" + bookName + 
               ", count=" + count + ", price=" + price + "]";
    }
}



