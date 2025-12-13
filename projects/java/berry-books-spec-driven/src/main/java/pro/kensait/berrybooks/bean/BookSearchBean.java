package pro.kensait.berrybooks.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.entity.Book;
import pro.kensait.berrybooks.service.book.BookService;
import pro.kensait.berrybooks.service.category.CategoryService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BookSearchBean - Session-scoped bean for book search functionality
 * 
 * Manages book search operations and displays search results.
 */
@Named
@SessionScoped
public class BookSearchBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(BookSearchBean.class);
    
    @Inject
    private BookService bookService;
    
    @Inject
    private CategoryService categoryService;
    
    private Integer categoryId;
    private String keyword;
    private List<Book> bookList = new ArrayList<>();
    private Map<Integer, String> categoryMap;
    
    // Constructors
    
    public BookSearchBean() {
    }
    
    // Initialization
    
    /**
     * Initialize bean - load categories and all books
     */
    @PostConstruct
    public void init() {
        logger.info("[ BookSearchBean#init ]");
        
        try {
            // Load category map for dropdown
            categoryMap = categoryService.getCategoryMap();
            
            // Load all books initially
            loadAllBooks();
            
        } catch (Exception e) {
            logger.error("[ BookSearchBean#init ] Initialization failed", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "初期化エラー", "書籍データの読み込みに失敗しました。"));
        }
    }
    
    // Getters and Setters
    
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public List<Book> getBookList() {
        return bookList;
    }
    
    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
    
    public Map<Integer, String> getCategoryMap() {
        return categoryMap;
    }
    
    public void setCategoryMap(Map<Integer, String> categoryMap) {
        this.categoryMap = categoryMap;
    }
    
    // Business methods
    
    /**
     * Search books using static query (JPQL)
     * 
     * @return navigation outcome
     */
    public String search() {
        logger.info("[ BookSearchBean#search ] categoryId={}, keyword={}", categoryId, keyword);
        
        try {
            // Determine search type based on parameters
            if (categoryId != null && categoryId > 0 && keyword != null && !keyword.trim().isEmpty()) {
                // Search by both category and keyword
                bookList = bookService.searchBook(categoryId, keyword.trim());
                logger.info("[ BookSearchBean#search ] Search by category and keyword: found {} books", 
                        bookList.size());
                
            } else if (categoryId != null && categoryId > 0) {
                // Search by category only
                bookList = bookService.searchBook(categoryId);
                logger.info("[ BookSearchBean#search ] Search by category: found {} books", 
                        bookList.size());
                
            } else if (keyword != null && !keyword.trim().isEmpty()) {
                // Search by keyword only
                bookList = bookService.searchBook(keyword.trim());
                logger.info("[ BookSearchBean#search ] Search by keyword: found {} books", 
                        bookList.size());
                
            } else {
                // No search criteria - load all books
                loadAllBooks();
            }
            
            if (bookList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "検索結果", "該当する書籍が見つかりませんでした。"));
            }
            
            return "bookSelect?faces-redirect=true";
            
        } catch (Exception e) {
            logger.error("[ BookSearchBean#search ] Search failed", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "検索エラー", "書籍検索に失敗しました。"));
            return null;
        }
    }
    
    /**
     * Search books using dynamic query (Criteria API)
     * 
     * @return navigation outcome
     */
    public String search2() {
        logger.info("[ BookSearchBean#search2 ] categoryId={}, keyword={}", categoryId, keyword);
        
        try {
            // Use Criteria API for dynamic query
            bookList = bookService.searchBookWithCriteria(categoryId, keyword);
            
            logger.info("[ BookSearchBean#search2 ] Dynamic search: found {} books", bookList.size());
            
            if (bookList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "検索結果", "該当する書籍が見つかりませんでした。"));
            }
            
            return "bookSelect?faces-redirect=true";
            
        } catch (Exception e) {
            logger.error("[ BookSearchBean#search2 ] Dynamic search failed", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "検索エラー", "書籍検索に失敗しました。"));
            return null;
        }
    }
    
    /**
     * Load all books
     * 
     * @return navigation outcome
     */
    public String loadAllBooks() {
        logger.info("[ BookSearchBean#loadAllBooks ]");
        
        try {
            bookList = bookService.getBooksAll();
            logger.info("[ BookSearchBean#loadAllBooks ] Loaded {} books", bookList.size());
            
            // Clear search criteria
            categoryId = null;
            keyword = null;
            
            return "bookSelect?faces-redirect=true";
            
        } catch (Exception e) {
            logger.error("[ BookSearchBean#loadAllBooks ] Failed to load books", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "読み込みエラー", "書籍データの読み込みに失敗しました。"));
            return null;
        }
    }
    
    /**
     * Refresh book list (reload current search)
     */
    public void refreshBookList() {
        logger.info("[ BookSearchBean#refreshBookList ]");
        
        try {
            if (categoryId != null || (keyword != null && !keyword.trim().isEmpty())) {
                search();
            } else {
                loadAllBooks();
            }
        } catch (Exception e) {
            logger.error("[ BookSearchBean#refreshBookList ] Refresh failed", e);
        }
    }
    
    /**
     * Clear search criteria
     */
    public void clearSearch() {
        logger.info("[ BookSearchBean#clearSearch ]");
        categoryId = null;
        keyword = null;
    }
    
    /**
     * Get number of search results
     * 
     * @return number of books in result list
     */
    public int getResultCount() {
        return bookList != null ? bookList.size() : 0;
    }
}
