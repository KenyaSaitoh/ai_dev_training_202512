package pro.kensait.berrybooks.service.customer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.client.customer.CustomerRestClient;
import pro.kensait.berrybooks.entity.Customer;

/**
 * 顧客管理のビジネスロジック
 * 
 * <p>顧客登録、認証、検索の機能を提供します。</p>
 */
@ApplicationScoped
public class CustomerService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    @Inject
    private CustomerRestClient customerRestClient;
    
    /**
     * 顧客を新規登録します
     * 
     * @param customer 顧客エンティティ
     * @return 登録された顧客エンティティ（IDが採番されている）
     * @throws EmailAlreadyExistsException メールアドレスが重複している場合
     */
    public Customer register(Customer customer) {
        logger.info("[ CustomerService#register ] email={}", customer.getEmail());
        
        // REST API経由で顧客を登録
        Customer registeredCustomer = customerRestClient.registerCustomer(customer);
        
        logger.info("[ CustomerService#register ] Customer registered successfully: customerId={}", 
                    registeredCustomer.getCustomerId());
        return registeredCustomer;
    }
    
    /**
     * メールアドレスとパスワードで認証します
     * 
     * @param email メールアドレス
     * @param password パスワード（平文）
     * @return 認証成功した場合は顧客エンティティ、失敗した場合はnull
     */
    public Customer authenticate(String email, String password) {
        logger.info("[ CustomerService#authenticate ] email={}", email);
        
        // REST API経由でメールアドレスから顧客を取得
        Customer customer = customerRestClient.getCustomerByEmail(email);
        
        if (customer == null) {
            logger.info("[ CustomerService#authenticate ] Customer not found: email={}", email);
            return null;
        }
        
        // パスワードが一致するかチェック（平文比較、BR-031）
        if (!customer.getPassword().equals(password)) {
            logger.info("[ CustomerService#authenticate ] Password mismatch: email={}", email);
            return null;
        }
        
        logger.info("[ CustomerService#authenticate ] Authentication successful: customerId={}", 
                    customer.getCustomerId());
        return customer;
    }
    
    /**
     * メールアドレスで顧客を検索します
     * 
     * @param email メールアドレス
     * @return 顧客エンティティ。見つからない場合はnull
     */
    public Customer findByEmail(String email) {
        logger.info("[ CustomerService#findByEmail ] email={}", email);
        
        // REST API経由で顧客を検索
        Customer customer = customerRestClient.getCustomerByEmail(email);
        
        if (customer != null) {
            logger.info("[ CustomerService#findByEmail ] Customer found: customerId={}", 
                        customer.getCustomerId());
        } else {
            logger.info("[ CustomerService#findByEmail ] Customer not found: email={}", email);
        }
        
        return customer;
    }
}

