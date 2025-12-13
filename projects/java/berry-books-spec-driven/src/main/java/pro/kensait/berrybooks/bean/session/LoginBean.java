package pro.kensait.berrybooks.bean.session;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.entity.Customer;
import pro.kensait.berrybooks.service.customer.CustomerService;

import java.io.Serializable;

/**
 * LoginBean - Session-scoped bean for login management
 * 
 * Handles user authentication and login/logout operations.
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
    
    private String email;
    private String password;
    
    // Constructors
    
    public LoginBean() {
    }
    
    // Getters and Setters
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    // Business methods
    
    /**
     * Authenticate user and login
     * 
     * @return navigation outcome
     */
    public String login() {
        logger.info("[ LoginBean#login ] email={}", email);
        
        try {
            // Authenticate user
            Customer customer = customerService.authenticate(email, password);
            
            if (customer != null) {
                // Login successful
                customerBean.setCustomer(customer);
                logger.info("[ LoginBean#login ] Login successful: customerId={}", 
                        customer.getCustomerId());
                
                // Clear form fields
                clearForm();
                
                return "bookSearch?faces-redirect=true";
                
            } else {
                // Login failed
                logger.warn("[ LoginBean#login ] Login failed: invalid credentials for email={}", email);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "ログインエラー", "メールアドレスまたはパスワードが正しくありません。"));
                return null;
            }
            
        } catch (Exception e) {
            logger.error("[ LoginBean#login ] Login error", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "ログインエラー", "ログイン処理中にエラーが発生しました。"));
            return null;
        }
    }
    
    /**
     * Logout the current user
     * 
     * @return navigation outcome
     */
    public String logout() {
        logger.info("[ LoginBean#logout ]");
        
        clearForm();
        
        // Delegate to CustomerBean for logout
        return customerBean.logout();
    }
    
    /**
     * Clear login form fields
     */
    private void clearForm() {
        email = null;
        password = null;
    }
    
    /**
     * Check if user is logged in
     * 
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return customerBean.isLoggedIn();
    }
    
    /**
     * Navigate to registration page
     * 
     * @return navigation outcome
     */
    public String goToRegistration() {
        logger.info("[ LoginBean#goToRegistration ]");
        return "customerInput?faces-redirect=true";
    }
}
