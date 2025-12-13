package pro.kensait.berrybooks.util;

import java.util.ResourceBundle;

/**
 * MessageUtil - Utility for loading messages from properties file
 * 
 * Provides centralized access to application messages.
 */
public class MessageUtil {
    
    private static final String BUNDLE_NAME = "messages";
    private static ResourceBundle bundle;
    
    static {
        try {
            bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        } catch (Exception e) {
            System.err.println("Failed to load message bundle: " + BUNDLE_NAME);
            e.printStackTrace();
        }
    }
    
    /**
     * Get message by key
     * 
     * @param key the message key
     * @return the message value, or the key itself if not found
     */
    public static String get(String key) {
        if (bundle == null) {
            return key;
        }
        
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return key;
        }
    }
    
    /**
     * Get message by key with default value
     * 
     * @param key the message key
     * @param defaultValue the default value if key not found
     * @return the message value, or default value if not found
     */
    public static String get(String key, String defaultValue) {
        if (bundle == null) {
            return defaultValue;
        }
        
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
