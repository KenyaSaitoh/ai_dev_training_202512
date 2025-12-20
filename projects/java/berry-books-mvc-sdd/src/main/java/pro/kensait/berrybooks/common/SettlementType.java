package pro.kensait.berrybooks.common;

/**
 * 決済方法を表す列挙型
 * 
 * <p>注文時に顧客が選択できる決済方法の種類を定義します。</p>
 */
public enum SettlementType {
    
    /**
     * 銀行振込
     */
    BANK_TRANSFER(1, "銀行振り込み"),
    
    /**
     * クレジットカード
     */
    CREDIT_CARD(2, "クレジットカード"),
    
    /**
     * 着払い
     */
    CASH_ON_DELIVERY(3, "着払い");
    
    /**
     * 決済方法コード
     */
    private final int code;
    
    /**
     * 決済方法の日本語ラベル
     */
    private final String label;
    
    /**
     * コンストラクタ
     * 
     * @param code 決済方法コード
     * @param label 決済方法の日本語ラベル
     */
    SettlementType(int code, String label) {
        this.code = code;
        this.label = label;
    }
    
    /**
     * 決済方法コードを取得します
     * 
     * @return 決済方法コード
     */
    public int getCode() {
        return code;
    }
    
    /**
     * 決済方法の日本語ラベルを取得します
     * 
     * @return 決済方法の日本語ラベル
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * 決済方法コードから対応するSettlementTypeを取得します
     * 
     * @param code 決済方法コード
     * @return 対応するSettlementType
     * @throws IllegalArgumentException 無効なコードが指定された場合
     */
    public static SettlementType fromCode(int code) {
        for (SettlementType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid settlement type code: " + code);
    }
    
    /**
     * 決済方法コードから対応する決済方法名を取得します
     * 
     * @param code 決済方法コード
     * @return 決済方法の日本語ラベル
     * @throws IllegalArgumentException 無効なコードが指定された場合
     */
    public static String getNameByCode(Integer code) {
        if (code == null) {
            return "";
        }
        return fromCode(code).getLabel();
    }
}

