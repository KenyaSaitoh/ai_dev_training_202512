package pro.kensait.berrybooks.util;

/**
 * 住所処理のユーティリティクラス
 * 
 * <p>住所に関する処理（沖縄県判定など）を提供します。</p>
 */
public final class AddressUtil {
    
    /**
     * プライベートコンストラクタ（インスタンス化を防止）
     */
    private AddressUtil() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    /**
     * 指定された住所が沖縄県かどうかを判定します
     * 
     * @param address 住所文字列
     * @return 住所に「沖縄」が含まれる場合はtrue、それ以外はfalse
     */
    public static boolean isOkinawa(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }
        return address.contains("沖縄");
    }
}

