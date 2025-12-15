package pro.kensait.berrybooks.service.order;

import java.util.List;
import pro.kensait.berrybooks.entity.OrderDetail;
import pro.kensait.berrybooks.entity.OrderTran;

/**
 * OrderServiceIF - Interface for Order Service
 * 
 * Defines the contract for order-related business operations.
 */
public interface OrderServiceIF {
    
    /**
     * Get order history for a customer (Variation 1: Basic query)
     * 
     * @param customerId the customer ID
     * @return list of order history transfer objects
     */
    List<OrderHistoryTO> getOrderHistory(Integer customerId);
    
    /**
     * Get order history for a customer (Variation 2: DTO projection)
     * 
     * @param customerId the customer ID
     * @return list of order history transfer objects
     */
    List<OrderHistoryTO> getOrderHistory2(Integer customerId);
    
    /**
     * Get order history for a customer (Variation 3: JOIN FETCH)
     * 
     * @param customerId the customer ID
     * @return list of order history transfer objects
     */
    List<OrderHistoryTO> getOrderHistory3(Integer customerId);
    
    /**
     * Get order transaction by ID
     * 
     * @param orderTranId the order transaction ID
     * @return the order transaction, or null if not found
     */
    OrderTran getOrderTran(Integer orderTranId);
    
    /**
     * Get order transaction with details by ID
     * 
     * @param orderTranId the order transaction ID
     * @return the order transaction with details, or null if not found
     */
    OrderTran getOrderTranWithDetails(Integer orderTranId);
    
    /**
     * Get order detail by composite key
     * 
     * @param orderTranId the order transaction ID
     * @param orderDetailId the order detail ID
     * @return the order detail, or null if not found
     */
    OrderDetail getOrderDetail(Integer orderTranId, Integer orderDetailId);
    
    /**
     * Get all order details for an order transaction
     * 
     * @param orderTranId the order transaction ID
     * @return list of order details
     */
    List<OrderDetail> getOrderDetails(Integer orderTranId);
    
    /**
     * Place an order (main business logic)
     * 
     * This method performs the following operations in a transaction:
     * 1. Validates stock availability for all items
     * 2. Updates stock quantities with optimistic locking
     * 3. Creates OrderTran record
     * 4. Creates OrderDetail records
     * 
     * @param orderTO the order transfer object containing order information
     * @return the created order transaction ID
     * @throws OutOfStockException if any item is out of stock
     * @throws jakarta.persistence.OptimisticLockException if version conflict occurs
     */
    Integer orderBooks(OrderTO orderTO) throws OutOfStockException;
}
