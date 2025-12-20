package pro.kensait.berrybooks.service.order;

import pro.kensait.berrybooks.entity.OrderDetail;
import pro.kensait.berrybooks.entity.OrderTran;
import java.io.Serializable;
import java.util.List;

/**
 * 注文サマリー情報をレイヤー間で転送するTOクラス
 * 
 * <p>注文詳細表示用の転送オブジェクトです。</p>
 */
public class OrderSummaryTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 注文トランザクション
     */
    private OrderTran orderTran;
    
    /**
     * 注文明細のリスト
     */
    private List<OrderDetail> orderDetails;
    
    /**
     * デフォルトコンストラクタ
     */
    public OrderSummaryTO() {
    }
    
    /**
     * すべてのフィールドを初期化するコンストラクタ
     * 
     * @param orderTran 注文トランザクション
     * @param orderDetails 注文明細のリスト
     */
    public OrderSummaryTO(OrderTran orderTran, List<OrderDetail> orderDetails) {
        this.orderTran = orderTran;
        this.orderDetails = orderDetails;
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
     * 注文明細のリストを取得します
     * 
     * @return 注文明細のリスト
     */
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }
    
    /**
     * 注文明細のリストを設定します
     * 
     * @param orderDetails 注文明細のリスト
     */
    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}

