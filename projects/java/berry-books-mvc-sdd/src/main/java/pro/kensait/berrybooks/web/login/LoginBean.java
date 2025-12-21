package pro.kensait.berrybooks.web.login;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.entity.Customer;
import pro.kensait.berrybooks.service.customer.CustomerService;
import pro.kensait.berrybooks.web.customer.CustomerBean;

/**
 * ログイン処理のコントローラー
 * 
 * <p>ログイン認証とナビゲーション制御を行います。</p>
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(LoginBean.class);
    
    @Inject
    private CustomerService customerService;
    
    @Inject
    private CustomerBean customerBean;
    
    /**
     * メールアドレス
     */
    private String email;
    
    /**
     * パスワード
     */
    private String password;
    
    /**
     * ログイン済みフラグ
     */
    private boolean loggedIn = false;
    
    /**
     * デフォルトコンストラクタ
     */
    public LoginBean() {
    }
    
    /**
     * ログイン処理を実行します
     * 
     * @return 書籍検索画面へのナビゲーション（認証成功時）、nullで現在のページに留まる（認証失敗時）
     */
    public String processLogin() {
        logger.info("[ LoginBean#processLogin ] email={}", email);
        
        // 認証処理
        Customer customer = customerService.authenticate(email, password);
        
        if (customer != null) {
            // 認証成功: CustomerBeanに顧客情報を設定
            customerBean.setCustomer(customer);
            loggedIn = true;
            
            logger.info("[ LoginBean#processLogin ] Login successful: customerId={}", customer.getCustomerId());
            // 書籍選択画面（検索結果）に遷移
            return "bookSelect?faces-redirect=true";
        } else {
            // 認証失敗: エラーメッセージを表示
            logger.info("[ LoginBean#processLogin ] Login failed: email={}", email);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "BIZ-002", "メールアドレスまたはパスワードが正しくありません"));
            return null;
        }
    }
    
    /**
     * ログアウト処理を実行します
     * 
     * @return ログイン画面へのナビゲーション
     */
    public String processLogout() {
        logger.info("[ LoginBean#processLogout ]");
        
        // セッションを無効化
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        
        // トップページへ遷移
        return "index?faces-redirect=true";
    }
    
    /**
     * ログイン済みかどうかを確認します
     * 
     * @return ログイン済みの場合はtrue、それ以外はfalse
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    /**
     * ログイン状態を設定します
     * 
     * @param loggedIn ログイン状態
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    
    // Getters and Setters
    
    /**
     * メールアドレスを取得します
     * 
     * @return メールアドレス
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * メールアドレスを設定します
     * 
     * @param email メールアドレス
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * パスワードを取得します
     * 
     * @return パスワード
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * パスワードを設定します
     * 
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

