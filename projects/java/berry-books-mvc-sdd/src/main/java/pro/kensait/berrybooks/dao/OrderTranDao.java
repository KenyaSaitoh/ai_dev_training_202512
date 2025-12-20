package pro.kensait.berrybooks.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pro.kensait.berrybooks.entity.OrderTran;
import java.util.List;

/**
 * 注文トランザクションのデータアクセスクラス
 * 
 * <p>注文トランザクションエンティティに対するCRUD操作を提供します。</p>
 */
@ApplicationScoped
public class OrderTranDao {
    
    /**
     * エンティティマネージャ
     */
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * 注文トランザクションを登録します
     * 
     * @param orderTran 注文トランザクション
     */
    public void persist(OrderTran orderTran) {
        em.persist(orderTran);
    }
    
    /**
     * 顧客IDで注文履歴を取得します（注文日降順）
     * 
     * @param customerId 顧客ID
     * @return 注文履歴のリスト
     */
    public List<OrderTran> findByCustomerId(Integer customerId) {
        TypedQuery<OrderTran> query = em.createQuery(
            "SELECT o FROM OrderTran o WHERE o.customerId = :customerId ORDER BY o.orderDate DESC, o.orderTranId DESC",
            OrderTran.class
        );
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    /**
     * 注文IDで注文トランザクションを取得します
     * 
     * @param orderTranId 注文ID
     * @return 注文トランザクション（存在しない場合はnull）
     */
    public OrderTran findById(Integer orderTranId) {
        return em.find(OrderTran.class, orderTranId);
    }
    
    /**
     * 注文IDで注文トランザクションと明細をJOIN FETCHで取得します
     * 
     * <p>N+1問題を回避するため、注文明細を同時に取得します。</p>
     * 
     * @param orderTranId 注文ID
     * @return 注文トランザクション（存在しない場合はnull）
     */
    public OrderTran findByIdWithDetails(Integer orderTranId) {
        TypedQuery<OrderTran> query = em.createQuery(
            "SELECT o FROM OrderTran o LEFT JOIN FETCH o.orderDetails WHERE o.orderTranId = :orderTranId",
            OrderTran.class
        );
        query.setParameter("orderTranId", orderTranId);
        List<OrderTran> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}

