package pro.kensait.berrybooks.util;

import java.util.HashMap;
import java.util.Map;

/**
 * SettlementType - Enum for settlement/payment types
 * 
 * Defines the available payment methods for orders.
 */
public enum SettlementType {
    
    BANK_TRANSFER(1, "銀行振込"),
    CREDIT_CARD(2, "クレジットカード"),
    CASH_ON_DELIVERY(3, "代金引換");
    
    private final Integer code;
    private final String displayName;
    
    private static final Map<Integer, SettlementType> CODE_MAP = new HashMap<>();
    
    static {
        for (SettlementType type : values()) {
            CODE_MAP.put(type.code, type);
        }
    }
    
    SettlementType(Integer code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get SettlementType from code
     * 
     * @param code the settlement type code
     * @return the SettlementType, or null if not found
     */
    public static SettlementType fromCode(Integer code) {
        return CODE_MAP.get(code);
    }
    
    /**
     * Get display name by code
     * 
     * @param code the settlement type code
     * @return the display name, or "Unknown" if not found
     */
    public static String getDisplayNameByCode(Integer code) {
        SettlementType type = fromCode(code);
        return type != null ? type.getDisplayName() : "Unknown";
    }
    
    /**
     * Get all settlement type codes
     * 
     * @return array of all codes
     */
    public static Integer[] getAllCodes() {
        Integer[] codes = new Integer[values().length];
        int i = 0;
        for (SettlementType type : values()) {
            codes[i++] = type.getCode();
        }
        return codes;
    }
    
    /**
     * Get all settlement types as a map (code -> display name)
     * 
     * @return map of code to display name
     */
    public static Map<Integer, String> getAllAsMap() {
        Map<Integer, String> map = new HashMap<>();
        for (SettlementType type : values()) {
            map.put(type.getCode(), type.getDisplayName());
        }
        return map;
    }
}
