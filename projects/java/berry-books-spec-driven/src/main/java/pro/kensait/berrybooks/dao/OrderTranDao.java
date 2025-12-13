package pro.kensait.berrybooks.dao;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pro.kensait.berrybooks.entity.OrderTran;

/**
 * OrderTranDao - Data Access Object for OrderTran entity
 * 
 * Provides CRUD operations and order history queries.
 */
@ApplicationScoped
public class OrderTranDao {
    
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * Find order transaction by ID
     * 
     * @param orderTranId the order transaction ID
     * @return the order transaction, or null if not found
     */
    public OrderTran findById(Integer orderTranId) {
        return em.find(OrderTran.class, orderTranId);
    }
    
    /**
     * Find order transaction by ID with details (using JOIN FETCH)
     * 
     * This method eagerly loads order details to avoid N+1 problem.
     * 
     * @param orderTranId the order transaction ID
     * @return the order transaction with details, or null if not found
     */
    public OrderTran findByIdWithDetails(Integer orderTranId) {
        TypedQuery<OrderTran> query = em.createQuery(
            "SELECT o FROM OrderTran o " +
            "LEFT JOIN FETCH o.orderDetails " +
            "WHERE o.orderTranId = :orderTranId", 
            OrderTran.class);
        query.setParameter("orderTranId", orderTranId);
        List<OrderTran> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    /**
     * Find order transactions by customer ID
     * 
     * @param customerId the customer ID
     * @return list of order transactions for the customer
     */
    public List<OrderTran> findByCustomerId(Integer customerId) {
        TypedQuery<OrderTran> query = em.createQuery(
            "SELECT o FROM OrderTran o " +
            "WHERE o.customer.customerId = :customerId " +
            "ORDER BY o.orderDate DESC, o.orderTranId DESC", 
            OrderTran.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    /**
     * Find order transactions by customer ID with details (using JOIN FETCH)
     * 
     * This method eagerly loads order details to avoid N+1 problem.
     * 
     * @param customerId the customer ID
     * @return list of order transactions with details for the customer
     */
    public List<OrderTran> findByCustomerIdWithDetails(Integer customerId) {
        TypedQuery<OrderTran> query = em.createQuery(
            "SELECT DISTINCT o FROM OrderTran o " +
            "LEFT JOIN FETCH o.orderDetails " +
            "WHERE o.customer.customerId = :customerId " +
            "ORDER BY o.orderDate DESC, o.orderTranId DESC", 
            OrderTran.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    /**
     * Find all order transactions
     * 
     * @return list of all order transactions
     */
    public List<OrderTran> findAll() {
        TypedQuery<OrderTran> query = em.createQuery(
            "SELECT o FROM OrderTran o ORDER BY o.orderDate DESC, o.orderTranId DESC", 
            OrderTran.class);
        return query.getResultList();
    }
    
    /**
     * Persist a new order transaction
     * 
     * @param orderTran the order transaction to persist
     */
    public void persist(OrderTran orderTran) {
        em.persist(orderTran);
    }
    
    /**
     * Update an existing order transaction
     * 
     * @param orderTran the order transaction to update
     * @return the updated order transaction
     */
    public OrderTran update(OrderTran orderTran) {
        return em.merge(orderTran);
    }
    
    /**
     * Remove an order transaction
     * 
     * @param orderTran the order transaction to remove
     */
    public void remove(OrderTran orderTran) {
        if (!em.contains(orderTran)) {
            orderTran = em.merge(orderTran);
        }
        em.remove(orderTran);
    }
}
