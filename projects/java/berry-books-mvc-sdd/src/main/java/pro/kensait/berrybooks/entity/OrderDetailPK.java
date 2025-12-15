package pro.kensait.berrybooks.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * OrderDetailPK - Composite Primary Key for OrderDetail
 * 
 * Represents the composite primary key (ORDER_TRAN_ID, ORDER_DETAIL_ID)
 * for the ORDER_DETAIL table.
 */
public class OrderDetailPK implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer orderTranId;
    private Integer orderDetailId;
    
    // Constructors
    
    public OrderDetailPK() {
    }
    
    public OrderDetailPK(Integer orderTranId, Integer orderDetailId) {
        this.orderTranId = orderTranId;
        this.orderDetailId = orderDetailId;
    }
    
    // Getters and Setters
    
    public Integer getOrderTranId() {
        return orderTranId;
    }
    
    public void setOrderTranId(Integer orderTranId) {
        this.orderTranId = orderTranId;
    }
    
    public Integer getOrderDetailId() {
        return orderDetailId;
    }
    
    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }
    
    // hashCode and equals (required for composite key)
    
    @Override
    public int hashCode() {
        return Objects.hash(orderTranId, orderDetailId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderDetailPK other = (OrderDetailPK) obj;
        return Objects.equals(orderTranId, other.orderTranId) 
                && Objects.equals(orderDetailId, other.orderDetailId);
    }
    
    @Override
    public String toString() {
        return "OrderDetailPK [orderTranId=" + orderTranId + ", orderDetailId=" + orderDetailId + "]";
    }
}
