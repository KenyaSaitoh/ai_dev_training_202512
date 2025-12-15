package pro.kensait.berrybooks.util;

/**
 * AddressUtil - Utility for address handling
 * 
 * Provides address parsing and validation logic.
 */
public class AddressUtil {
    
    /**
     * Check if the address is in Okinawa prefecture
     * 
     * @param address the delivery address
     * @return true if address contains "沖縄", false otherwise
     */
    public static boolean isOkinawa(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }
        return address.contains("沖縄");
    }
    
    /**
     * Validate address format
     * 
     * @param address the address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        // Basic validation: address should have at least 5 characters
        return address.trim().length() >= 5;
    }
    
    /**
     * Normalize address (trim whitespace)
     * 
     * @param address the address to normalize
     * @return normalized address
     */
    public static String normalize(String address) {
        if (address == null) {
            return "";
        }
        return address.trim();
    }
}
