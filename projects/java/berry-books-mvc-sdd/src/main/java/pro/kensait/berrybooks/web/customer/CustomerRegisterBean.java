package pro.kensait.berrybooks.web.customer;

import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.entity.Customer;
import pro.kensait.berrybooks.service.customer.CustomerService;
import pro.kensait.berrybooks.service.customer.EmailAlreadyExistsException;

/**
 * 新規顧客登録処理のコントローラー
 * 
 * <p>新規顧客の登録フォーム入力と登録処理を制御します。</p>
 */
@Named
@ViewScoped
public class CustomerRegisterBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerRegisterBean.class);
    
    @Inject
    private CustomerService customerService;
    
    /**
     * 登録する顧客情報
     */
    private Customer customer = new Customer();
    
    /**
     * デフォルトコンストラクタ
     */
    public CustomerRegisterBean() {
    }
    
    /**
     * 顧客登録処理を実行します
     * 
     * @return 登録完了画面へのナビゲーション（登録成功時）、nullで現在のページに留まる（登録失敗時）
     */
    public String register() {
        logger.info("[ CustomerRegisterBean#register ] email={}", customer.getEmail());
        
        try {
            // 顧客を登録
            Customer registeredCustomer = customerService.register(customer);
            
            // 登録された顧客情報を保持（登録完了画面で表示するため）
            customer = registeredCustomer;
            
            logger.info("[ CustomerRegisterBean#register ] Registration successful: customerId={}", 
                        registeredCustomer.getCustomerId());
            
            // 登録完了画面に遷移
            return "/customerOutput.xhtml?faces-redirect=true";
            
        } catch (EmailAlreadyExistsException e) {
            // メールアドレス重複エラー
            logger.warn("[ CustomerRegisterBean#register ] Email already exists: email={}", customer.getEmail());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "BIZ-001", "メールアドレスが既に登録されています"));
            return null;
        } catch (Exception e) {
            // その他のエラー
            logger.error("[ CustomerRegisterBean#register ] Registration failed", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "SYS-001", "システムエラーが発生しました"));
            return null;
        }
    }
    
    /**
     * ログイン画面に遷移します
     * 
     * @return ログイン画面へのナビゲーション
     */
    public String navigateToLogin() {
        logger.info("[ CustomerRegisterBean#navigateToLogin ] Navigate to login page");
        return "/index.xhtml?faces-redirect=true";
    }
    
    // Getters and Setters
    
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
}

