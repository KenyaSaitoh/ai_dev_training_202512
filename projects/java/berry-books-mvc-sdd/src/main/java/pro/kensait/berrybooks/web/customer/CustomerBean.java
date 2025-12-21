package pro.kensait.berrybooks.web.customer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pro.kensait.berrybooks.entity.Customer;
import pro.kensait.berrybooks.service.customer.CustomerService;
import pro.kensait.berrybooks.service.customer.EmailAlreadyExistsException;
import pro.kensait.berrybooks.web.login.LoginBean;

/**
 * 顧客管理のManagedBean（セッションスコープ）
 * 
 * <p>ログイン状態とログイン済み顧客情報を保持します。</p>
 * <p>新規顧客登録のフォーム入力も管理します。</p>
 */
@Named
@SessionScoped
public class CustomerBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerBean.class);
    
    @Inject
    private CustomerService customerService;
    
    @Inject
    private LoginBean loginBean;
    
    /**
     * ログイン済み顧客情報
     */
    private Customer customer;
    
    /**
     * 新規登録フォーム用：顧客名
     */
    @NotBlank(message = "顧客名は必須です")
    @Size(max = 30, message = "顧客名は30文字以内で入力してください")
    private String customerName;
    
    /**
     * 新規登録フォーム用：メールアドレス
     */
    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が正しくありません")
    @Size(max = 30, message = "メールアドレスは30文字以内で入力してください")
    private String email;
    
    /**
     * 新規登録フォーム用：パスワード
     */
    @NotBlank(message = "パスワードは必須です")
    @Size(max = 60, message = "パスワードは60文字以内で入力してください")
    private String password;
    
    /**
     * 新規登録フォーム用：生年月日（文字列形式 yyyy-MM-dd）
     */
    private String birthday;
    
    /**
     * 新規登録フォーム用：住所
     */
    @Size(max = 120, message = "住所は120文字以内で入力してください")
    private String address;
    
    /**
     * デフォルトコンストラクタ
     */
    public CustomerBean() {
    }
    
    /**
     * 顧客情報を取得します
     * 
     * @return 顧客情報
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * 顧客情報を設定します
     * 
     * @param customer 顧客情報
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    /**
     * ログイン状態を確認します
     * 
     * @return ログイン済みの場合はtrue、それ以外はfalse
     */
    public boolean isLoggedIn() {
        return customer != null;
    }
    
    /**
     * ログアウト処理を行います
     * 
     * @return ログイン画面へのナビゲーション
     */
    public String logout() {
        // セッションをクリア
        customer = null;
        
        // セッションから明示的に削除（AuthenticationFilter用）
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            facesContext.getExternalContext().getSessionMap().remove("customerBean");
        }
        
        // ログイン画面に遷移
        return "/index.xhtml?faces-redirect=true";
    }
    
    /**
     * 新規顧客登録処理を実行します
     * 
     * @return 登録完了画面へのナビゲーション（成功時）、nullで現在のページに留まる（失敗時）
     */
    public String register() {
        logger.info("[ CustomerBean#register ] customerName={}, email={}", customerName, email);
        
        try {
            // Customerエンティティを作成
            Customer newCustomer = new Customer();
            newCustomer.setCustomerName(customerName);
            newCustomer.setEmail(email);
            newCustomer.setPassword(password);
            newCustomer.setAddress(address);
            
            // 生年月日を変換（入力がある場合）
            if (birthday != null && !birthday.trim().isEmpty()) {
                try {
                    LocalDate parsedBirthday = LocalDate.parse(birthday.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                    newCustomer.setBirthday(parsedBirthday);
                } catch (DateTimeParseException e) {
                    logger.warn("[ CustomerBean#register ] Invalid birthday format: {}", birthday);
                    addGlobalErrorMessage("生年月日の形式が正しくありません（yyyy-MM-dd形式で入力してください）");
                    return null;
                }
            }
            
            // 顧客を登録
            Customer registeredCustomer = customerService.register(newCustomer);
            
            // セッションに登録済み顧客情報を保存
            this.customer = registeredCustomer;
            
            // 入力フォームをクリア
            clearForm();
            
            // ログイン状態を設定（登録後は自動的にログイン状態にする）
            loginBean.setLoggedIn(true);
            
            logger.info("[ CustomerBean#register ] Registration successful: customerId={}", registeredCustomer.getCustomerId());
            
            // 登録完了画面に遷移
            return "/customerOutput.xhtml?faces-redirect=true";
            
        } catch (EmailAlreadyExistsException e) {
            logger.warn("[ CustomerBean#register ] Email already exists: {}", email);
            addGlobalErrorMessage("このメールアドレスは既に登録されています");
            return null;
        } catch (Exception e) {
            logger.error("[ CustomerBean#register ] Registration failed", e);
            addGlobalErrorMessage("登録処理中にエラーが発生しました");
            return null;
        }
    }
    
    /**
     * 入力フォームをクリアします
     */
    private void clearForm() {
        this.customerName = null;
        this.email = null;
        this.password = null;
        this.birthday = null;
        this.address = null;
    }
    
    /**
     * グローバルエラーメッセージを追加します
     * 
     * @param message エラーメッセージ
     */
    private void addGlobalErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
        }
    }
    
    // ===== Getters and Setters for Form Fields =====
    
    /**
     * 顧客名を取得します
     * 
     * @return 顧客名
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /**
     * 顧客名を設定します
     * 
     * @param customerName 顧客名
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
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
    
    /**
     * 生年月日を取得します
     * 
     * @return 生年月日（文字列形式 yyyy-MM-dd）
     */
    public String getBirthday() {
        return birthday;
    }
    
    /**
     * 生年月日を設定します
     * 
     * @param birthday 生年月日（文字列形式 yyyy-MM-dd）
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    /**
     * 住所を取得します
     * 
     * @return 住所
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * 住所を設定します
     * 
     * @param address 住所
     */
    public void setAddress(String address) {
        this.address = address;
    }
}

