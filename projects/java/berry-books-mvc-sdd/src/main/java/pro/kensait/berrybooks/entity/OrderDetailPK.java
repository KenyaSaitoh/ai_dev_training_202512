package pro.kensait.berrybooks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * 注文明細の複合主キークラス
 * 
 * <p>注文明細エンティティの複合主キーを表します。</p>
 */
@Embeddable
public class OrderDetailPK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 注文取引ID
     */
    @Column(name = "ORDER_TRAN_ID", nullable = false)
    private Integer orderTranId;
    
    /**
     * 注文明細ID（注文内連番）
     */
    @Column(name = "ORDER_DETAIL_ID", nullable = false)
    private Integer orderDetailId;
    
    /**
     * デフォルトコンストラクタ
     */
    public OrderDetailPK() {
    }
    
    /**
     * コンストラクタ
     * 
     * @param orderTranId 注文取引ID
     * @param orderDetailId 注文明細ID
     */
    public OrderDetailPK(Integer orderTranId, Integer orderDetailId) {
        this.orderTranId = orderTranId;
        this.orderDetailId = orderDetailId;
    }
    
    /**
     * 注文取引IDを取得します
     * 
     * @return 注文取引ID
     */
    public Integer getOrderTranId() {
        return orderTranId;
    }
    
    /**
     * 注文取引IDを設定します
     * 
     * @param orderTranId 注文取引ID
     */
    public void setOrderTranId(Integer orderTranId) {
        this.orderTranId = orderTranId;
    }
    
    /**
     * 注文明細IDを取得します
     * 
     * @return 注文明細ID
     */
    public Integer getOrderDetailId() {
        return orderDetailId;
    }
    
    /**
     * 注文明細IDを設定します
     * 
     * @param orderDetailId 注文明細ID
     */
    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderTranId != null ? orderTranId.hashCode() : 0);
        hash += (orderDetailId != null ? orderDetailId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrderDetailPK)) {
            return false;
        }
        OrderDetailPK other = (OrderDetailPK) object;
        if ((this.orderTranId == null && other.orderTranId != null) 
                || (this.orderTranId != null && !this.orderTranId.equals(other.orderTranId))) {
            return false;
        }
        if ((this.orderDetailId == null && other.orderDetailId != null) 
                || (this.orderDetailId != null && !this.orderDetailId.equals(other.orderDetailId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "OrderDetailPK[orderTranId=" + orderTranId + ", orderDetailId=" + orderDetailId + "]";
    }
}

