package pro.kensait.berrybooks.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pro.kensait.berrybooks.entity.OrderDetail;
import java.util.List;

/**
 * 注文明細のデータアクセスクラス
 * 
 * <p>注文明細エンティティに対するCRUD操作を提供します。</p>
 */
@ApplicationScoped
public class OrderDetailDao {
    
    /**
     * エンティティマネージャ
     */
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * 注文明細を登録します
     * 
     * @param orderDetail 注文明細
     */
    public void persist(OrderDetail orderDetail) {
        em.persist(orderDetail);
    }
    
    /**
     * 注文IDで注文明細を取得します
     * 
     * @param orderTranId 注文ID
     * @return 注文明細のリスト
     */
    public List<OrderDetail> findByOrderTranId(Integer orderTranId) {
        TypedQuery<OrderDetail> query = em.createQuery(
            "SELECT od FROM OrderDetail od WHERE od.id.orderTranId = :orderTranId",
            OrderDetail.class
        );
        query.setParameter("orderTranId", orderTranId);
        return query.getResultList();
    }
}

