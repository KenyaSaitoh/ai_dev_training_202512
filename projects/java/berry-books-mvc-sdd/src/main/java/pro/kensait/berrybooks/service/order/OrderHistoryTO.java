package pro.kensait.berrybooks.service.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 注文履歴情報をレイヤー間で転送するTOクラス
 * 
 * <p>注文履歴一覧表示用の転送オブジェクトです。</p>
 */
public class OrderHistoryTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 注文トランザクションID
     */
    private Integer orderTranId;
    
    /**
     * 注文日
     */
    private LocalDate orderDate;
    
    /**
     * 合計金額
     */
    private BigDecimal totalPrice;
    
    /**
     * 配送料金
     */
    private BigDecimal deliveryPrice;
    
    /**
     * 決済方法名
     */
    private String settlementName;
    
    /**
     * デフォルトコンストラクタ
     */
    public OrderHistoryTO() {
    }
    
    /**
     * 注文トランザクションIDを取得します
     * 
     * @return 注文トランザクションID
     */
    public Integer getOrderTranId() {
        return orderTranId;
    }
    
    /**
     * 注文トランザクションIDを設定します
     * 
     * @param orderTranId 注文トランザクションID
     */
    public void setOrderTranId(Integer orderTranId) {
        this.orderTranId = orderTranId;
    }
    
    /**
     * 注文日を取得します
     * 
     * @return 注文日
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    /**
     * 注文日を設定します
     * 
     * @param orderDate 注文日
     */
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
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
     * 決済方法名を取得します
     * 
     * @return 決済方法名
     */
    public String getSettlementName() {
        return settlementName;
    }
    
    /**
     * 決済方法名を設定します
     * 
     * @param settlementName 決済方法名
     */
    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }
}

