package pro.kensait.berrybooks.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.bean.session.CartItem;
import pro.kensait.berrybooks.bean.session.CartSession;
import pro.kensait.berrybooks.bean.session.CustomerBean;
import pro.kensait.berrybooks.dao.StockDao;
import pro.kensait.berrybooks.entity.Book;
import pro.kensait.berrybooks.entity.Stock;
import pro.kensait.berrybooks.service.book.BookService;
import pro.kensait.berrybooks.service.delivery.DeliveryFeeService;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CartBean - Session-scoped bean for shopping cart operations
 * 
 * Manages cart operations including add, remove, and checkout.
 */
@Named
@SessionScoped
public class CartBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CartBean.class);
    
    @Inject
    private BookService bookService;
    
    @Inject
    private StockDao stockDao;
    
    @Inject
    private CartSession cartSession;
    
    @Inject
    private CustomerBean customerBean;
    
    @Inject
    private DeliveryFeeService deliveryFeeService;
    
    // Constructors
    
    public CartBean() {
    }
    
    // Business methods
    
    /**
     * Add book to cart
     * 
     * @param bookId the book ID to add
     * @param count the quantity to add
     * @return navigation outcome
     */
    public String addBook(Integer bookId, Integer count) {
        logger.info("[ CartBean#addBook ] bookId={}, count={}", bookId, count);
        
        try {
            // Validate count
            if (count == null || count <= 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "入力エラー", "数量は1以上を指定してください。"));
                return null;
            }
            
            // Get book details
            Book book = bookService.getBook(bookId);
            if (book == null) {
                logger.warn("[ CartBean#addBook ] Book not found: bookId={}", bookId);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "エラー", "書籍が見つかりません。"));
                return null;
            }
            
            // Get stock information
            Stock stock = stockDao.findById(bookId);
            if (stock == null || stock.getQuantity() < count) {
                logger.warn("[ CartBean#addBook ] Insufficient stock: bookId={}, requested={}, available={}", 
                        bookId, count, stock != null ? stock.getQuantity() : 0);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "在庫不足", "在庫が不足しています。"));
                return null;
            }
            
            // Create cart item
            CartItem cartItem = new CartItem(
                    book.getBookId(),
                    book.getBookName(),
                    book.getPublisher().getPublisherName(),
                    book.getPrice(),
                    count,
                    stock.getVersion()
            );
            
            // Add to cart
            cartSession.addItem(cartItem);
            
            logger.info("[ CartBean#addBook ] Book added to cart: bookId={}, count={}", bookId, count);
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "カートに追加", "書籍をカートに追加しました。"));
            
            return null; // Stay on current page
            
        } catch (Exception e) {
            logger.error("[ CartBean#addBook ] Failed to add book to cart", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "エラー", "カートへの追加に失敗しました。"));
            return null;
        }
    }
    
    /**
     * Remove selected books from cart
     * 
     * @return navigation outcome
     */
    public String removeSelectedBooks() {
        logger.info("[ CartBean#removeSelectedBooks ]");
        
        try {
            // Remove items marked for removal
            cartSession.removeMarkedItems();
            
            logger.info("[ CartBean#removeSelectedBooks ] Selected items removed");
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "削除完了", "選択した書籍をカートから削除しました。"));
            
            return null; // Stay on cart page
            
        } catch (Exception e) {
            logger.error("[ CartBean#removeSelectedBooks ] Failed to remove items", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "エラー", "削除に失敗しました。"));
            return null;
        }
    }
    
    /**
     * Clear all items from cart
     * 
     * @return navigation outcome
     */
    public String clearCart() {
        logger.info("[ CartBean#clearCart ]");
        
        try {
            cartSession.clear();
            
            logger.info("[ CartBean#clearCart ] Cart cleared");
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "カートクリア", "カートを空にしました。"));
            
            return "cartClear?faces-redirect=true";
            
        } catch (Exception e) {
            logger.error("[ CartBean#clearCart ] Failed to clear cart", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "エラー", "カートのクリアに失敗しました。"));
            return null;
        }
    }
    
    /**
     * Proceed to order (calculate delivery fee and navigate to order page)
     * 
     * @return navigation outcome
     */
    public String proceedToOrder() {
        logger.info("[ CartBean#proceedToOrder ]");
        
        try {
            // Check if cart is empty
            if (cartSession.isEmpty()) {
                logger.warn("[ CartBean#proceedToOrder ] Cart is empty");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "エラー", "カートが空です。"));
                return null;
            }
            
            // Check if user is logged in
            if (!customerBean.isLoggedIn()) {
                logger.warn("[ CartBean#proceedToOrder ] User not logged in");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "エラー", "ログインしてください。"));
                return "index?faces-redirect=true";
            }
            
            // Set delivery address from customer
            String address = customerBean.getCustomerAddress();
            cartSession.setDeliveryAddress(address);
            
            // Calculate delivery fee
            BigDecimal totalPrice = cartSession.getTotalPrice();
            BigDecimal deliveryFee = deliveryFeeService.calculateDeliveryFee(address, totalPrice);
            cartSession.setDeliveryPrice(deliveryFee);
            
            logger.info("[ CartBean#proceedToOrder ] Proceeding to order: totalPrice={}, deliveryFee={}", 
                    totalPrice, deliveryFee);
            
            return "bookOrder?faces-redirect=true";
            
        } catch (Exception e) {
            logger.error("[ CartBean#proceedToOrder ] Failed to proceed to order", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "エラー", "注文手続きに進めませんでした。"));
            return null;
        }
    }
    
    /**
     * View cart
     * 
     * @return navigation outcome
     */
    public String viewCart() {
        logger.info("[ CartBean#viewCart ]");
        return "cartView?faces-redirect=true";
    }
    
    /**
     * Get cart item count
     * 
     * @return number of items in cart
     */
    public int getCartItemCount() {
        return cartSession.getItemCount();
    }
    
    /**
     * Get total quantity in cart
     * 
     * @return total quantity
     */
    public int getTotalQuantity() {
        return cartSession.getTotalQuantity();
    }
    
    /**
     * Check if cart is empty
     * 
     * @return true if cart is empty, false otherwise
     */
    public boolean isCartEmpty() {
        return cartSession.isEmpty();
    }
}
