package pro.kensait.berrybooks.service.customer;

/**
 * メールアドレス重複時にスローするカスタム例外クラス
 * 
 * <p>顧客登録時にメールアドレスが既に登録されている場合にスローされます。</p>
 */
public class EmailAlreadyExistsException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 重複したメールアドレス
     */
    private final String email;
    
    /**
     * コンストラクタ
     * 
     * @param email 重複したメールアドレス
     */
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
        this.email = email;
    }
    
    /**
     * 重複したメールアドレスを取得します
     * 
     * @return メールアドレス
     */
    public String getEmail() {
        return email;
    }
}

