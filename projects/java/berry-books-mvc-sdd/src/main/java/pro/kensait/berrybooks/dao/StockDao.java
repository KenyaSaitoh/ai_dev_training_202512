package pro.kensait.berrybooks.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import pro.kensait.berrybooks.entity.Stock;

/**
 * StockDao - Data Access Object for Stock entity
 * 
 * Provides CRUD operations with optimistic locking support.
 */
@ApplicationScoped
public class StockDao {
    
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * Find stock by book ID
     * 
     * @param bookId the book ID
     * @return the stock, or null if not found
     */
    public Stock findById(Integer bookId) {
        return em.find(Stock.class, bookId);
    }
    
    /**
     * Update stock with optimistic locking
     * 
     * This method will throw OptimisticLockException if the version
     * has been changed by another transaction.
     * 
     * @param stock the stock to update
     * @return the updated stock
     * @throws OptimisticLockException if version mismatch occurs
     */
    public Stock update(Stock stock) throws OptimisticLockException {
        return em.merge(stock);
    }
    
    /**
     * Persist a new stock
     * 
     * @param stock the stock to persist
     */
    public void persist(Stock stock) {
        em.persist(stock);
    }
    
    /**
     * Remove a stock
     * 
     * @param stock the stock to remove
     */
    public void remove(Stock stock) {
        if (!em.contains(stock)) {
            stock = em.merge(stock);
        }
        em.remove(stock);
    }
    
    /**
     * Refresh stock entity from database
     * 
     * This is useful to get the latest version after a failed update.
     * 
     * @param stock the stock to refresh
     */
    public void refresh(Stock stock) {
        if (em.contains(stock)) {
            em.refresh(stock);
        }
    }
}
