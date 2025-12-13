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
import pro.kensait.berrybooks.service.customer.EmailAlreadyExistsException;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * CustomerBean - Session-scoped bean for customer management
 * 
 * Manages customer registration and session state.
 */
@Named
@SessionScoped
public class CustomerBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CustomerBean.class);
    
    @Inject
    private CustomerService customerService;
    
    private Customer customer;
    
    // Registration form fields
    private String name;
    private String email;
    private String password;
    private LocalDate birthday;
    private String address;
    
    // Constructors
    
    public CustomerBean() {
    }
    
    // Getters and Setters
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
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
    
    public LocalDate getBirthday() {
        return birthday;
    }
    
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    // Business methods
    
    /**
     * Register a new customer
     * 
     * @return navigation outcome
     */
    public String register() {
        logger.info("[ CustomerBean#register ] name={}, email={}", name, email);
        
        try {
            // Create new customer entity
            Customer newCustomer = new Customer();
            newCustomer.setName(name);
            newCustomer.setEmail(email);
            newCustomer.setPassword(password);
            newCustomer.setBirthday(birthday);
            newCustomer.setAddress(address);
            
            // Register customer
            customer = customerService.registerCustomer(newCustomer);
            
            logger.info("[ CustomerBean#register ] Customer registered successfully: customerId={}", 
                    customer.getCustomerId());
            
            // Clear form fields
            clearForm();
            
            return "customerOutput?faces-redirect=true";
            
        } catch (EmailAlreadyExistsException e) {
            logger.warn("[ CustomerBean#register ] Email already exists: {}", email);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "登録エラー", "このメールアドレスは既に登録されています。"));
            return null;
            
        } catch (Exception e) {
            logger.error("[ CustomerBean#register ] Registration failed", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "登録エラー", "顧客登録に失敗しました。"));
            return null;
        }
    }
    
    /**
     * Check if user is logged in
     * 
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return customer != null;
    }
    
    /**
     * Logout the current user
     * 
     * @return navigation outcome
     */
    public String logout() {
        logger.info("[ CustomerBean#logout ] customerId={}", 
                customer != null ? customer.getCustomerId() : "null");
        
        customer = null;
        clearForm();
        
        // Invalidate session
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        
        return "index?faces-redirect=true";
    }
    
    /**
     * Clear registration form fields
     */
    private void clearForm() {
        name = null;
        email = null;
        password = null;
        birthday = null;
        address = null;
    }
    
    /**
     * Get customer ID
     * 
     * @return customer ID, or null if not logged in
     */
    public Integer getCustomerId() {
        return customer != null ? customer.getCustomerId() : null;
    }
    
    /**
     * Get customer name
     * 
     * @return customer name, or "Guest" if not logged in
     */
    public String getCustomerName() {
        return customer != null ? customer.getName() : "Guest";
    }
    
    /**
     * Get customer email
     * 
     * @return customer email, or null if not logged in
     */
    public String getCustomerEmail() {
        return customer != null ? customer.getEmail() : null;
    }
    
    /**
     * Get customer address
     * 
     * @return customer address, or null if not logged in
     */
    public String getCustomerAddress() {
        return customer != null ? customer.getAddress() : null;
    }
}
