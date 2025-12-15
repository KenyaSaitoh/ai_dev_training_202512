package pro.kensait.berrybooks.dao;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import pro.kensait.berrybooks.entity.Book;

/**
 * BookDao - Data Access Object for Book entity
 * 
 * Provides CRUD operations and search methods for books.
 */
@ApplicationScoped
public class BookDao {
    
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * Find book by ID
     * 
     * @param bookId the book ID
     * @return the book, or null if not found
     */
    public Book findById(Integer bookId) {
        return em.find(Book.class, bookId);
    }
    
    /**
     * Find all books
     * 
     * @return list of all books
     */
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery(
            "SELECT b FROM Book b ORDER BY b.bookId", Book.class);
        return query.getResultList();
    }
    
    /**
     * Find books by category
     * 
     * @param categoryId the category ID
     * @return list of books in the category
     */
    public List<Book> findByCategory(Integer categoryId) {
        TypedQuery<Book> query = em.createQuery(
            "SELECT b FROM Book b WHERE b.category.categoryId = :categoryId ORDER BY b.bookId", 
            Book.class);
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }
    
    /**
     * Find books by keyword (searches book name and author)
     * 
     * @param keyword the search keyword
     * @return list of matching books
     */
    public List<Book> findByKeyword(String keyword) {
        String likePattern = "%" + keyword + "%";
        TypedQuery<Book> query = em.createQuery(
            "SELECT b FROM Book b " +
            "WHERE b.bookName LIKE :keyword OR b.author LIKE :keyword " +
            "ORDER BY b.bookId", 
            Book.class);
        query.setParameter("keyword", likePattern);
        return query.getResultList();
    }
    
    /**
     * Find books by category and keyword
     * 
     * @param categoryId the category ID
     * @param keyword the search keyword
     * @return list of matching books
     */
    public List<Book> findByCategoryAndKeyword(Integer categoryId, String keyword) {
        String likePattern = "%" + keyword + "%";
        TypedQuery<Book> query = em.createQuery(
            "SELECT b FROM Book b " +
            "WHERE b.category.categoryId = :categoryId " +
            "AND (b.bookName LIKE :keyword OR b.author LIKE :keyword) " +
            "ORDER BY b.bookId", 
            Book.class);
        query.setParameter("categoryId", categoryId);
        query.setParameter("keyword", likePattern);
        return query.getResultList();
    }
    
    /**
     * Find books using Criteria API (dynamic query)
     * 
     * @param categoryId the category ID (optional, null for all categories)
     * @param keyword the search keyword (optional, null for no keyword search)
     * @return list of matching books
     */
    public List<Book> findByCriteriaAPI(Integer categoryId, String keyword) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        
        // Build predicates dynamically
        Predicate predicate = cb.conjunction(); // Start with "true" predicate
        
        // Add category filter if provided
        if (categoryId != null) {
            predicate = cb.and(predicate, 
                cb.equal(book.get("category").get("categoryId"), categoryId));
        }
        
        // Add keyword filter if provided
        if (keyword != null && !keyword.trim().isEmpty()) {
            String likePattern = "%" + keyword + "%";
            Predicate namePredicate = cb.like(book.get("bookName"), likePattern);
            Predicate authorPredicate = cb.like(book.get("author"), likePattern);
            predicate = cb.and(predicate, cb.or(namePredicate, authorPredicate));
        }
        
        cq.where(predicate);
        cq.orderBy(cb.asc(book.get("bookId")));
        
        TypedQuery<Book> query = em.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Persist a new book
     * 
     * @param book the book to persist
     */
    public void persist(Book book) {
        em.persist(book);
    }
    
    /**
     * Update an existing book
     * 
     * @param book the book to update
     * @return the updated book
     */
    public Book update(Book book) {
        return em.merge(book);
    }
    
    /**
     * Remove a book
     * 
     * @param book the book to remove
     */
    public void remove(Book book) {
        if (!em.contains(book)) {
            book = em.merge(book);
        }
        em.remove(book);
    }
}
