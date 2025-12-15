package pro.kensait.berrybooks.dao;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pro.kensait.berrybooks.entity.OrderDetail;
import pro.kensait.berrybooks.entity.OrderDetailPK;

/**
 * OrderDetailDao - Data Access Object for OrderDetail entity
 * 
 * Provides CRUD operations for order details with composite key support.
 */
@ApplicationScoped
public class OrderDetailDao {
    
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * Find order detail by composite primary key
     * 
     * @param orderTranId the order transaction ID
     * @param orderDetailId the order detail ID
     * @return the order detail, or null if not found
     */
    public OrderDetail findById(Integer orderTranId, Integer orderDetailId) {
        OrderDetailPK pk = new OrderDetailPK(orderTranId, orderDetailId);
        return em.find(OrderDetail.class, pk);
    }
    
    /**
     * Find order detail by composite primary key object
     * 
     * @param pk the composite primary key
     * @return the order detail, or null if not found
     */
    public OrderDetail findById(OrderDetailPK pk) {
        return em.find(OrderDetail.class, pk);
    }
    
    /**
     * Find all order details for a specific order transaction
     * 
     * @param orderTranId the order transaction ID
     * @return list of order details for the order
     */
    public List<OrderDetail> findByOrderTranId(Integer orderTranId) {
        TypedQuery<OrderDetail> query = em.createQuery(
            "SELECT od FROM OrderDetail od " +
            "WHERE od.orderTranId = :orderTranId " +
            "ORDER BY od.orderDetailId", 
            OrderDetail.class);
        query.setParameter("orderTranId", orderTranId);
        return query.getResultList();
    }
    
    /**
     * Find all order details for a specific book
     * 
     * @param bookId the book ID
     * @return list of order details containing the book
     */
    public List<OrderDetail> findByBookId(Integer bookId) {
        TypedQuery<OrderDetail> query = em.createQuery(
            "SELECT od FROM OrderDetail od " +
            "WHERE od.book.bookId = :bookId " +
            "ORDER BY od.orderTranId, od.orderDetailId", 
            OrderDetail.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }
    
    /**
     * Find all order details
     * 
     * @return list of all order details
     */
    public List<OrderDetail> findAll() {
        TypedQuery<OrderDetail> query = em.createQuery(
            "SELECT od FROM OrderDetail od " +
            "ORDER BY od.orderTranId, od.orderDetailId", 
            OrderDetail.class);
        return query.getResultList();
    }
    
    /**
     * Persist a new order detail
     * 
     * @param orderDetail the order detail to persist
     */
    public void persist(OrderDetail orderDetail) {
        em.persist(orderDetail);
    }
    
    /**
     * Update an existing order detail
     * 
     * @param orderDetail the order detail to update
     * @return the updated order detail
     */
    public OrderDetail update(OrderDetail orderDetail) {
        return em.merge(orderDetail);
    }
    
    /**
     * Remove an order detail
     * 
     * @param orderDetail the order detail to remove
     */
    public void remove(OrderDetail orderDetail) {
        if (!em.contains(orderDetail)) {
            orderDetail = em.merge(orderDetail);
        }
        em.remove(orderDetail);
    }
}
