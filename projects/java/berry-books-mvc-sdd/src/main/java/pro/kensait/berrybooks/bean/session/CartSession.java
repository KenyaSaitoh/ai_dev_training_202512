package pro.kensait.berrybooks.bean.session;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CartSession - Session-scoped bean for shopping cart management
 * 
 * Manages the shopping cart state across the user session.
 */
@Named
@SessionScoped
public class CartSession implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<CartItem> cartItems = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private BigDecimal deliveryPrice = BigDecimal.ZERO;
    private String deliveryAddress;
    
    // Constructors
    
    public CartSession() {
    }
    
    // Getters and Setters
    
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }
    
    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    // Business methods
    
    /**
     * Clear all cart data
     */
    public void clear() {
        cartItems.clear();
        totalPrice = BigDecimal.ZERO;
        deliveryPrice = BigDecimal.ZERO;
        deliveryAddress = null;
    }
    
    /**
     * Add item to cart or update quantity if already exists
     * 
     * @param item the cart item to add
     */
    public void addItem(CartItem item) {
        if (item == null) {
            return;
        }
        
        // Check if item already exists in cart
        CartItem existingItem = findItemByBookId(item.getBookId());
        if (existingItem != null) {
            // Update quantity
            existingItem.setCount(existingItem.getCount() + item.getCount());
        } else {
            // Add new item
            cartItems.add(item);
        }
        
        // Recalculate total
        calculateTotalPrice();
    }
    
    /**
     * Remove item from cart by book ID
     * 
     * @param bookId the book ID to remove
     */
    public void removeItem(Integer bookId) {
        cartItems.removeIf(item -> item.getBookId().equals(bookId));
        calculateTotalPrice();
    }
    
    /**
     * Remove items marked for removal
     */
    public void removeMarkedItems() {
        cartItems.removeIf(CartItem::isRemove);
        calculateTotalPrice();
    }
    
    /**
     * Find cart item by book ID
     * 
     * @param bookId the book ID to find
     * @return the cart item, or null if not found
     */
    public CartItem findItemByBookId(Integer bookId) {
        return cartItems.stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Calculate total price of all items in cart
     */
    public void calculateTotalPrice() {
        totalPrice = cartItems.stream()
                .map(CartItem::getSubtotalAsBigDecimal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Get grand total (total price + delivery price)
     * 
     * @return grand total
     */
    public BigDecimal getGrandTotal() {
        return totalPrice.add(deliveryPrice);
    }
    
    /**
     * Check if cart is empty
     * 
     * @return true if cart is empty, false otherwise
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
    
    /**
     * Get number of items in cart
     * 
     * @return number of items
     */
    public int getItemCount() {
        return cartItems.size();
    }
    
    /**
     * Get total quantity of all items
     * 
     * @return total quantity
     */
    public int getTotalQuantity() {
        return cartItems.stream()
                .mapToInt(CartItem::getCount)
                .sum();
    }
}
