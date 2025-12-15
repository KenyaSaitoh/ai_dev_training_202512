package pro.kensait.berrybooks.service.category;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.dao.CategoryDao;
import pro.kensait.berrybooks.entity.Category;

/**
 * CategoryService - Business logic for category operations
 * 
 * Provides category retrieval and dropdown support.
 */
@ApplicationScoped
public class CategoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    
    @Inject
    private CategoryDao categoryDao;
    
    /**
     * Get all categories
     * 
     * @return list of all categories
     */
    public List<Category> getAllCategories() {
        logger.info("[ CategoryService#getAllCategories ]");
        return categoryDao.findAll();
    }
    
    /**
     * Get category map for dropdown
     * 
     * Returns a map of category ID to category name for use in JSF dropdowns.
     * 
     * @return map of category ID to category name
     */
    public Map<Integer, String> getCategoryMap() {
        logger.info("[ CategoryService#getCategoryMap ]");
        
        List<Category> categories = categoryDao.findAll();
        Map<Integer, String> categoryMap = new LinkedHashMap<>();
        
        for (Category category : categories) {
            categoryMap.put(category.getCategoryId(), category.getCategoryName());
        }
        
        return categoryMap;
    }
    
    /**
     * Get category by ID
     * 
     * @param categoryId the category ID
     * @return the category, or null if not found
     */
    public Category getCategory(Integer categoryId) {
        logger.info("[ CategoryService#getCategory ] categoryId={}", categoryId);
        return categoryDao.findById(categoryId);
    }
}
