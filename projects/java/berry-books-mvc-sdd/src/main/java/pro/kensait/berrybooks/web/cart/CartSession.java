package pro.kensait.berrybooks.web.cart;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * セッションスコープでカート状態を管理するBean
 * 
 * <p>ショッピングカート内のアイテムと合計金額を管理します。</p>
 */
@Named
@SessionScoped
public class CartSession implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(CartSession.class);
    
    /**
     * カートアイテムのリスト
     */
    private List<CartItem> cartItems = new ArrayList<>();
    
    /**
     * 合計金額
     */
    private BigDecimal totalPrice = BigDecimal.ZERO;
    
    /**
     * 配送料金
     */
    private BigDecimal deliveryPrice = BigDecimal.ZERO;
    
    /**
     * 配送先住所
     */
    private String deliveryAddress;
    
    /**
     * 決済方法（1:銀行振込, 2:クレジットカード, 3:着払い）
     */
    private Integer settlementType;
    
    /**
     * デフォルトコンストラクタ
     */
    public CartSession() {
    }
    
    /**
     * カートをクリアします
     * 
     * <p>すべてのカートアイテムを削除し、合計金額をゼロにリセットします。</p>
     */
    public void clear() {
        logger.info("[ CartSession#clear ]");
        cartItems.clear();
        totalPrice = BigDecimal.ZERO;
        deliveryPrice = BigDecimal.ZERO;
        deliveryAddress = null;
        settlementType = null;
    }
    
    /**
     * カートにアイテムを追加します
     * 
     * <p>同じ書籍IDが既に存在する場合は数量を加算します（BR-011）。
     * 存在しない場合は新しいアイテムとして追加します。
     * 追加後、合計金額を自動計算します。</p>
     * 
     * @param item 追加するカートアイテム
     */
    public void addItem(CartItem item) {
        if (item == null) {
            logger.warn("[ CartSession#addItem ] item is null");
            return;
        }
        
        logger.info("[ CartSession#addItem ] bookId={}, count={}", item.getBookId(), item.getCount());
        
        // 同じ書籍IDが既に存在するか確認
        CartItem existingItem = findItemByBookId(item.getBookId());
        
        if (existingItem != null) {
            // 既存のアイテムが存在する場合、数量を加算（BR-011）
            int newCount = existingItem.getCount() + item.getCount();
            existingItem.setCount(newCount);
            // バージョン番号を最新のもので上書き
            existingItem.setVersion(item.getVersion());
            logger.info("[ CartSession#addItem ] Updated existing item, new count={}", newCount);
        } else {
            // 新しいアイテムとして追加
            cartItems.add(item);
            logger.info("[ CartSession#addItem ] Added new item");
        }
        
        // 合計金額を再計算（BR-013）
        calculateTotalPrice();
    }
    
    /**
     * カートからアイテムを削除します
     * 
     * @param bookId 削除する書籍ID
     */
    public void removeItem(Integer bookId) {
        if (bookId == null) {
            logger.warn("[ CartSession#removeItem ] bookId is null");
            return;
        }
        
        logger.info("[ CartSession#removeItem ] bookId={}", bookId);
        
        cartItems.removeIf(item -> item.getBookId().equals(bookId));
        
        // 合計金額を再計算
        calculateTotalPrice();
    }
    
    /**
     * 合計金額を計算します（BR-013）
     * 
     * <p>カート内のすべてのアイテムの小計を合算します。</p>
     */
    public void calculateTotalPrice() {
        totalPrice = cartItems.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        logger.info("[ CartSession#calculateTotalPrice ] totalPrice={}", totalPrice);
    }
    
    /**
     * 指定された書籍IDのカートアイテムを検索します
     * 
     * @param bookId 書籍ID
     * @return 見つかったカートアイテム、存在しない場合はnull
     */
    private CartItem findItemByBookId(Integer bookId) {
        return cartItems.stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * カートが空かどうかを判定します
     * 
     * @return カートが空の場合はtrue、それ以外はfalse
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
    
    /**
     * カートアイテムのリストを取得します
     * 
     * @return カートアイテムのリスト
     */
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    
    /**
     * カートアイテムのリストを設定します
     * 
     * @param cartItems カートアイテムのリスト
     */
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    
    /**
     * 合計金額を取得します
     * 
     * @return 合計金額
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    /**
     * 合計金額を設定します
     * 
     * @param totalPrice 合計金額
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    /**
     * 配送料金を取得します
     * 
     * @return 配送料金
     */
    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }
    
    /**
     * 配送料金を設定します
     * 
     * @param deliveryPrice 配送料金
     */
    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
    
    /**
     * 配送先住所を取得します
     * 
     * @return 配送先住所
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    /**
     * 配送先住所を設定します
     * 
     * @param deliveryAddress 配送先住所
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    /**
     * 決済方法を取得します
     * 
     * @return 決済方法
     */
    public Integer getSettlementType() {
        return settlementType;
    }
    
    /**
     * 決済方法を設定します
     * 
     * @param settlementType 決済方法
     */
    public void setSettlementType(Integer settlementType) {
        this.settlementType = settlementType;
    }
}



