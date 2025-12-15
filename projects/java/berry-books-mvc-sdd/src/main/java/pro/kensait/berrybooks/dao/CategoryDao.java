package pro.kensait.berrybooks.dao;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pro.kensait.berrybooks.entity.Category;

/**
 * CategoryDao - Data Access Object for Category entity
 * 
 * Provides CRUD operations for categories.
 */
@ApplicationScoped
public class CategoryDao {
    
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * Find category by ID
     * 
     * @param categoryId the category ID
     * @return the category, or null if not found
     */
    public Category findById(Integer categoryId) {
        return em.find(Category.class, categoryId);
    }
    
    /**
     * Find all categories
     * 
     * @return list of all categories
     */
    public List<Category> findAll() {
        TypedQuery<Category> query = em.createQuery(
            "SELECT c FROM Category c ORDER BY c.categoryId", Category.class);
        return query.getResultList();
    }
    
    /**
     * Persist a new category
     * 
     * @param category the category to persist
     */
    public void persist(Category category) {
        em.persist(category);
    }
    
    /**
     * Update an existing category
     * 
     * @param category the category to update
     * @return the updated category
     */
    public Category update(Category category) {
        return em.merge(category);
    }
    
    /**
     * Remove a category
     * 
     * @param category the category to remove
     */
    public void remove(Category category) {
        if (!em.contains(category)) {
            category = em.merge(category);
        }
        em.remove(category);
    }
}
