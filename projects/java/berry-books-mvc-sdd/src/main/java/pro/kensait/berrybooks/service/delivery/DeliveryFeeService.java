package pro.kensait.berrybooks.service.delivery;

import java.math.BigDecimal;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DeliveryFeeService - Business logic for delivery fee calculation
 * 
 * Calculates delivery fees based on address and total price.
 */
@ApplicationScoped
public class DeliveryFeeService {
    
    private static final Logger logger = LoggerFactory.getLogger(DeliveryFeeService.class);
    
    // Business rule constants
    private static final int STANDARD_DELIVERY_FEE = 800;
    private static final int OKINAWA_DELIVERY_FEE = 1700;
    private static final int FREE_DELIVERY_THRESHOLD = 5000;
    
    /**
     * Calculate delivery fee based on address and total price
     * 
     * Business rules:
     * - Standard delivery fee: 800 yen
     * - Okinawa delivery fee: 1700 yen
     * - Free delivery for orders 5000 yen or more
     * 
     * @param address the delivery address
     * @param totalPrice the total order price
     * @return the delivery fee
     */
    public int calculateDeliveryFee(String address, int totalPrice) {
        logger.info("[ DeliveryFeeService#calculateDeliveryFee ] address={}, totalPrice={}", 
                   address, totalPrice);
        
        // Check if free delivery applies
        if (isFreeDelivery(totalPrice)) {
            logger.info("[ DeliveryFeeService#calculateDeliveryFee ] Free delivery (totalPrice >= {})", 
                       FREE_DELIVERY_THRESHOLD);
            return 0;
        }
        
        // Check if Okinawa delivery
        if (isOkinawa(address)) {
            logger.info("[ DeliveryFeeService#calculateDeliveryFee ] Okinawa delivery fee: {}", 
                       OKINAWA_DELIVERY_FEE);
            return OKINAWA_DELIVERY_FEE;
        }
        
        // Standard delivery
        logger.info("[ DeliveryFeeService#calculateDeliveryFee ] Standard delivery fee: {}", 
                   STANDARD_DELIVERY_FEE);
        return STANDARD_DELIVERY_FEE;
    }
    
    /**
     * Calculate delivery fee (BigDecimal version)
     * 
     * @param address the delivery address
     * @param totalPrice the total order price
     * @return the delivery fee as BigDecimal
     */
    public BigDecimal calculateDeliveryFee(String address, BigDecimal totalPrice) {
        int totalPriceInt = totalPrice != null ? totalPrice.intValue() : 0;
        int fee = calculateDeliveryFee(address, totalPriceInt);
        return BigDecimal.valueOf(fee);
    }
    
    /**
     * Check if address is in Okinawa
     * 
     * @param address the delivery address
     * @return true if address is in Okinawa, false otherwise
     */
    public boolean isOkinawa(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return address.contains("沖縄");
    }
    
    /**
     * Check if order qualifies for free delivery
     * 
     * @param totalPrice the total order price
     * @return true if free delivery applies, false otherwise
     */
    public boolean isFreeDelivery(int totalPrice) {
        return totalPrice >= FREE_DELIVERY_THRESHOLD;
    }
    
    /**
     * Check if order qualifies for free delivery (BigDecimal version)
     * 
     * @param totalPrice the total order price
     * @return true if free delivery applies, false otherwise
     */
    public boolean isFreeDelivery(BigDecimal totalPrice) {
        if (totalPrice == null) {
            return false;
        }
        return totalPrice.compareTo(BigDecimal.valueOf(FREE_DELIVERY_THRESHOLD)) >= 0;
    }
    
    /**
     * Get standard delivery fee
     * 
     * @return the standard delivery fee
     */
    public int getStandardDeliveryFee() {
        return STANDARD_DELIVERY_FEE;
    }
    
    /**
     * Get Okinawa delivery fee
     * 
     * @return the Okinawa delivery fee
     */
    public int getOkinawaDeliveryFee() {
        return OKINAWA_DELIVERY_FEE;
    }
    
    /**
     * Get free delivery threshold
     * 
     * @return the free delivery threshold
     */
    public int getFreeDeliveryThreshold() {
        return FREE_DELIVERY_THRESHOLD;
    }
}
