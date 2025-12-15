package pro.kensait.berrybooks.service.book;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.dao.BookDao;
import pro.kensait.berrybooks.entity.Book;

/**
 * BookService - Business logic for book operations
 * 
 * Provides book search and retrieval functionality.
 */
@ApplicationScoped
public class BookService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    
    @Inject
    private BookDao bookDao;
    
    /**
     * Get book by ID
     * 
     * @param bookId the book ID
     * @return the book, or null if not found
     */
    public Book getBook(Integer bookId) {
        logger.info("[ BookService#getBook ] bookId={}", bookId);
        return bookDao.findById(bookId);
    }
    
    /**
     * Get all books
     * 
     * @return list of all books
     */
    public List<Book> getBooksAll() {
        logger.info("[ BookService#getBooksAll ]");
        return bookDao.findAll();
    }
    
    /**
     * Search books by category
     * 
     * @param categoryId the category ID
     * @return list of books in the category
     */
    public List<Book> searchBook(Integer categoryId) {
        logger.info("[ BookService#searchBook ] categoryId={}", categoryId);
        return bookDao.findByCategory(categoryId);
    }
    
    /**
     * Search books by keyword
     * 
     * @param keyword the search keyword
     * @return list of matching books
     */
    public List<Book> searchBook(String keyword) {
        logger.info("[ BookService#searchBook ] keyword={}", keyword);
        return bookDao.findByKeyword(keyword);
    }
    
    /**
     * Search books by category and keyword
     * 
     * @param categoryId the category ID
     * @param keyword the search keyword
     * @return list of matching books
     */
    public List<Book> searchBook(Integer categoryId, String keyword) {
        logger.info("[ BookService#searchBook ] categoryId={}, keyword={}", categoryId, keyword);
        return bookDao.findByCategoryAndKeyword(categoryId, keyword);
    }
    
    /**
     * Search books with dynamic criteria
     * 
     * Uses Criteria API for flexible search.
     * 
     * @param categoryId the category ID (optional, null for all categories)
     * @param keyword the search keyword (optional, null for no keyword search)
     * @return list of matching books
     */
    public List<Book> searchBookWithCriteria(Integer categoryId, String keyword) {
        logger.info("[ BookService#searchBookWithCriteria ] categoryId={}, keyword={}", 
                    categoryId, keyword);
        return bookDao.findByCriteriaAPI(categoryId, keyword);
    }
}
