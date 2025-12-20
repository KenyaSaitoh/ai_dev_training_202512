package pro.kensait.berrybooks.service.order;

import pro.kensait.berrybooks.web.cart.CartItem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 注文情報をレイヤー間で転送するTOクラス
 * 
 * <p>注文処理に必要な情報をまとめた転送オブジェクトです。</p>
 */
public class OrderTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 顧客ID
     */
    private Integer customerId;
    
    /**
     * 配送先住所
     */
    private String deliveryAddress;
    
    /**
     * 配送料金
     */
    private BigDecimal deliveryPrice;
    
    /**
     * 決済方法コード
     */
    private Integer settlementCode;
    
    /**
     * カートアイテムのリスト
     */
    private List<CartItem> cartItems;
    
    /**
     * デフォルトコンストラクタ
     */
    public OrderTO() {
    }
    
    /**
     * 顧客IDを取得します
     * 
     * @return 顧客ID
     */
    public Integer getCustomerId() {
        return customerId;
    }
    
    /**
     * 顧客IDを設定します
     * 
     * @param customerId 顧客ID
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
     * 決済方法コードを取得します
     * 
     * @return 決済方法コード
     */
    public Integer getSettlementCode() {
        return settlementCode;
    }
    
    /**
     * 決済方法コードを設定します
     * 
     * @param settlementCode 決済方法コード
     */
    public void setSettlementCode(Integer settlementCode) {
        this.settlementCode = settlementCode;
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
}

