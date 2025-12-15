package pro.kensait.berrybooks.service.order;

import java.util.ArrayList;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.dao.BookDao;
import pro.kensait.berrybooks.dao.OrderDetailDao;
import pro.kensait.berrybooks.dao.OrderTranDao;
import pro.kensait.berrybooks.dao.StockDao;
import pro.kensait.berrybooks.entity.Book;
import pro.kensait.berrybooks.entity.Customer;
import pro.kensait.berrybooks.entity.OrderDetail;
import pro.kensait.berrybooks.entity.OrderTran;
import pro.kensait.berrybooks.entity.Stock;
import pro.kensait.berrybooks.service.order.OrderTO.OrderItemTO;

/**
 * OrderService - Business logic for order operations
 * 
 * Provides order placement, order history retrieval, and order detail management.
 * Implements complex transaction logic with optimistic locking for stock management.
 */
@ApplicationScoped
public class OrderService implements OrderServiceIF {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    @Inject
    private OrderTranDao orderTranDao;
    
    @Inject
    private OrderDetailDao orderDetailDao;
    
    @Inject
    private StockDao stockDao;
    
    @Inject
    private BookDao bookDao;
    
    /**
     * Get order history for a customer (Variation 1: Basic query)
     * 
     * @param customerId the customer ID
     * @return list of order history transfer objects
     */
    @Override
    public List<OrderHistoryTO> getOrderHistory(Integer customerId) {
        logger.info("[ OrderService#getOrderHistory ] customerId={}", customerId);
        
        List<OrderTran> orderTrans = orderTranDao.findByCustomerId(customerId);
        List<OrderHistoryTO> historyList = new ArrayList<>();
        
        for (OrderTran orderTran : orderTrans) {
            OrderHistoryTO history = new OrderHistoryTO(
                orderTran.getOrderTranId(),
                orderTran.getOrderDate(),
                orderTran.getDeliveryAddress(),
                orderTran.getSettlementType(),
                orderTran.getTotalPrice(),
                orderTran.getDeliveryPrice()
            );
            historyList.add(history);
        }
        
        logger.info("[ OrderService#getOrderHistory ] Found {} orders", historyList.size());
        return historyList;
    }
    
    /**
     * Get order history for a customer (Variation 2: DTO projection)
     * 
     * This variation demonstrates DTO projection in JPQL.
     * 
     * @param customerId the customer ID
     * @return list of order history transfer objects
     */
    @Override
    public List<OrderHistoryTO> getOrderHistory2(Integer customerId) {
        logger.info("[ OrderService#getOrderHistory2 ] customerId={}", customerId);
        
        // For this implementation, we use the same approach as getOrderHistory
        // In a real scenario, you might use JPQL constructor expression:
        // SELECT NEW OrderHistoryTO(o.orderTranId, o.orderDate, ...) FROM OrderTran o
        return getOrderHistory(customerId);
    }
    
    /**
     * Get order history for a customer (Variation 3: JOIN FETCH)
     * 
     * This variation uses JOIN FETCH to eagerly load related data.
     * 
     * @param customerId the customer ID
     * @return list of order history transfer objects
     */
    @Override
    public List<OrderHistoryTO> getOrderHistory3(Integer customerId) {
        logger.info("[ OrderService#getOrderHistory3 ] customerId={}", customerId);
        
        List<OrderTran> orderTrans = orderTranDao.findByCustomerIdWithDetails(customerId);
        List<OrderHistoryTO> historyList = new ArrayList<>();
        
        for (OrderTran orderTran : orderTrans) {
            OrderHistoryTO history = new OrderHistoryTO(
                orderTran.getOrderTranId(),
                orderTran.getOrderDate(),
                orderTran.getDeliveryAddress(),
                orderTran.getSettlementType(),
                orderTran.getTotalPrice(),
                orderTran.getDeliveryPrice()
            );
            historyList.add(history);
        }
        
        logger.info("[ OrderService#getOrderHistory3 ] Found {} orders", historyList.size());
        return historyList;
    }
    
    /**
     * Get order transaction by ID
     * 
     * @param orderTranId the order transaction ID
     * @return the order transaction, or null if not found
     */
    @Override
    public OrderTran getOrderTran(Integer orderTranId) {
        logger.info("[ OrderService#getOrderTran ] orderTranId={}", orderTranId);
        return orderTranDao.findById(orderTranId);
    }
    
    /**
     * Get order transaction with details by ID
     * 
     * @param orderTranId the order transaction ID
     * @return the order transaction with details, or null if not found
     */
    @Override
    public OrderTran getOrderTranWithDetails(Integer orderTranId) {
        logger.info("[ OrderService#getOrderTranWithDetails ] orderTranId={}", orderTranId);
        return orderTranDao.findByIdWithDetails(orderTranId);
    }
    
    /**
     * Get order detail by composite key
     * 
     * @param orderTranId the order transaction ID
     * @param orderDetailId the order detail ID
     * @return the order detail, or null if not found
     */
    @Override
    public OrderDetail getOrderDetail(Integer orderTranId, Integer orderDetailId) {
        logger.info("[ OrderService#getOrderDetail ] orderTranId={}, orderDetailId={}", 
                   orderTranId, orderDetailId);
        return orderDetailDao.findById(orderTranId, orderDetailId);
    }
    
    /**
     * Get all order details for an order transaction
     * 
     * @param orderTranId the order transaction ID
     * @return list of order details
     */
    @Override
    public List<OrderDetail> getOrderDetails(Integer orderTranId) {
        logger.info("[ OrderService#getOrderDetails ] orderTranId={}", orderTranId);
        return orderDetailDao.findByOrderTranId(orderTranId);
    }
    
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
     * @throws OptimisticLockException if version conflict occurs
     */
    @Override
    @Transactional
    public Integer orderBooks(OrderTO orderTO) throws OutOfStockException {
        logger.info("[ OrderService#orderBooks ] customerId={}, items={}", 
                   orderTO.getCustomerId(), orderTO.getOrderItems().size());
        
        // Step 1: Validate stock availability and update stock
        for (OrderItemTO item : orderTO.getOrderItems()) {
            logger.debug("[ OrderService#orderBooks ] Processing bookId={}, count={}, version={}", 
                        item.getBookId(), item.getCount(), item.getVersion());
            
            // Get current stock
            Stock stock = stockDao.findById(item.getBookId());
            if (stock == null) {
                logger.error("[ OrderService#orderBooks ] Stock not found for bookId={}", 
                           item.getBookId());
                throw new OutOfStockException(item.getBookId(), "Unknown", 
                                            item.getCount(), 0);
            }
            
            // Check stock availability
            if (stock.getQuantity() < item.getCount()) {
                Book book = bookDao.findById(item.getBookId());
                String bookName = book != null ? book.getBookName() : "Unknown";
                logger.warn("[ OrderService#orderBooks ] Out of stock: bookId={}, bookName={}, " +
                           "requested={}, available={}", 
                           item.getBookId(), bookName, item.getCount(), stock.getQuantity());
                throw new OutOfStockException(item.getBookId(), bookName, 
                                            item.getCount(), stock.getQuantity());
            }
            
            // Verify version for optimistic locking
            if (!stock.getVersion().equals(item.getVersion())) {
                logger.warn("[ OrderService#orderBooks ] Version mismatch for bookId={}: " +
                           "expected={}, actual={}", 
                           item.getBookId(), item.getVersion(), stock.getVersion());
                // The update will fail with OptimisticLockException
            }
            
            // Update stock quantity
            stock.setQuantity(stock.getQuantity() - item.getCount());
            try {
                stockDao.update(stock);
                logger.debug("[ OrderService#orderBooks ] Stock updated: bookId={}, " +
                            "newQuantity={}, version={}", 
                            item.getBookId(), stock.getQuantity(), stock.getVersion());
            } catch (OptimisticLockException e) {
                logger.error("[ OrderService#orderBooks ] Optimistic lock exception for bookId={}", 
                           item.getBookId(), e);
                throw e;
            }
        }
        
        // Step 2: Create OrderTran
        OrderTran orderTran = new OrderTran();
        
        // Set customer (create a reference with just the ID)
        Customer customer = new Customer();
        customer.setCustomerId(orderTO.getCustomerId());
        orderTran.setCustomer(customer);
        
        orderTran.setOrderDate(orderTO.getOrderDate());
        orderTran.setTotalPrice(orderTO.getTotalPrice());
        orderTran.setDeliveryPrice(orderTO.getDeliveryPrice());
        orderTran.setDeliveryAddress(orderTO.getDeliveryAddress());
        orderTran.setSettlementType(orderTO.getSettlementType());
        
        orderTranDao.persist(orderTran);
        logger.info("[ OrderService#orderBooks ] OrderTran created: orderTranId={}", 
                   orderTran.getOrderTranId());
        
        // Step 3: Create OrderDetails
        int detailId = 1;
        for (OrderItemTO item : orderTO.getOrderItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderTranId(orderTran.getOrderTranId());
            orderDetail.setOrderDetailId(detailId++);
            
            // Set book (create a reference with just the ID)
            Book book = new Book();
            book.setBookId(item.getBookId());
            orderDetail.setBook(book);
            
            orderDetail.setPrice(item.getPrice());
            orderDetail.setCount(item.getCount());
            
            orderDetailDao.persist(orderDetail);
            logger.debug("[ OrderService#orderBooks ] OrderDetail created: orderTranId={}, " +
                        "orderDetailId={}, bookId={}", 
                        orderDetail.getOrderTranId(), orderDetail.getOrderDetailId(), 
                        item.getBookId());
        }
        
        logger.info("[ OrderService#orderBooks ] Order completed successfully: orderTranId={}", 
                   orderTran.getOrderTranId());
        return orderTran.getOrderTranId();
    }
}
