package pro.kensait.berrybooks.service.customer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.dao.CustomerDao;
import pro.kensait.berrybooks.entity.Customer;

/**
 * CustomerService - Business logic for customer operations
 * 
 * Provides customer registration, authentication, and retrieval functionality.
 */
@ApplicationScoped
public class CustomerService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    @Inject
    private CustomerDao customerDao;
    
    /**
     * Register a new customer
     * 
     * @param customer the customer to register
     * @return the registered customer with generated ID
     * @throws EmailAlreadyExistsException if email already exists
     */
    @Transactional
    public Customer registerCustomer(Customer customer) throws EmailAlreadyExistsException {
        logger.info("[ CustomerService#registerCustomer ] email={}", customer.getEmail());
        
        // Check if email already exists
        if (customerDao.emailExists(customer.getEmail())) {
            logger.warn("[ CustomerService#registerCustomer ] Email already exists: {}", 
                       customer.getEmail());
            throw new EmailAlreadyExistsException(customer.getEmail());
        }
        
        // Persist the customer
        customerDao.persist(customer);
        logger.info("[ CustomerService#registerCustomer ] Customer registered successfully: customerId={}", 
                   customer.getCustomerId());
        
        return customer;
    }
    
    /**
     * Authenticate customer with email and password
     * 
     * @param email the email address
     * @param password the password
     * @return the authenticated customer, or null if authentication fails
     */
    public Customer authenticate(String email, String password) {
        logger.info("[ CustomerService#authenticate ] email={}", email);
        
        Customer customer = customerDao.findByEmail(email);
        
        if (customer == null) {
            logger.warn("[ CustomerService#authenticate ] Customer not found: email={}", email);
            return null;
        }
        
        if (!customer.authenticate(password)) {
            logger.warn("[ CustomerService#authenticate ] Invalid password for email={}", email);
            return null;
        }
        
        logger.info("[ CustomerService#authenticate ] Authentication successful: customerId={}", 
                   customer.getCustomerId());
        return customer;
    }
    
    /**
     * Get customer by ID
     * 
     * @param customerId the customer ID
     * @return the customer, or null if not found
     */
    public Customer getCustomer(Integer customerId) {
        logger.info("[ CustomerService#getCustomer ] customerId={}", customerId);
        return customerDao.findById(customerId);
    }
    
    /**
     * Get customer by email
     * 
     * @param email the email address
     * @return the customer, or null if not found
     */
    public Customer getCustomerByEmail(String email) {
        logger.info("[ CustomerService#getCustomerByEmail ] email={}", email);
        return customerDao.findByEmail(email);
    }
}
