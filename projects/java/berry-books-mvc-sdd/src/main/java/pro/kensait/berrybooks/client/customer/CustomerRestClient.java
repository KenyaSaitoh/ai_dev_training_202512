package pro.kensait.berrybooks.client.customer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.entity.Customer;

/**
 * berry-books-rest APIへのREST APIクライアント
 * 
 * <p>CUSTOMERテーブルへのアクセスをREST API経由で行います。</p>
 */
@ApplicationScoped
public class CustomerRestClient {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerRestClient.class);
    
    /**
     * berry-books-rest APIのベースURL
     */
    private static final String BASE_URL = "http://localhost:8080/berry-books-rest";
    
    /**
     * 顧客IDで顧客を取得します
     * 
     * @param customerId 顧客ID
     * @return 顧客エンティティ。見つからない場合はnull
     */
    public Customer getCustomer(Integer customerId) {
        logger.info("[ CustomerRestClient#getCustomer ] customerId={}", customerId);
        
        Client client = ClientBuilder.newClient();
        try {
            Response response = client.target(BASE_URL)
                    .path("customers")
                    .path(String.valueOf(customerId))
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == 200) {
                Customer customer = response.readEntity(Customer.class);
                logger.info("[ CustomerRestClient#getCustomer ] Customer found: {}", customer);
                return customer;
            } else if (response.getStatus() == 404) {
                logger.info("[ CustomerRestClient#getCustomer ] Customer not found: customerId={}", customerId);
                return null;
            } else {
                logger.error("[ CustomerRestClient#getCustomer ] Unexpected status: {}", response.getStatus());
                throw new RuntimeException("Failed to get customer: HTTP " + response.getStatus());
            }
        } finally {
            client.close();
        }
    }
    
    /**
     * メールアドレスで顧客を検索します
     * 
     * @param email メールアドレス
     * @return 顧客エンティティ。見つからない場合はnull
     */
    public Customer getCustomerByEmail(String email) {
        logger.info("[ CustomerRestClient#getCustomerByEmail ] email={}", email);
        
        Client client = ClientBuilder.newClient();
        try {
            Response response = client.target(BASE_URL)
                    .path("customers")
                    .path("query_email")
                    .queryParam("email", email)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            
            if (response.getStatus() == 200) {
                Customer customer = response.readEntity(Customer.class);
                logger.info("[ CustomerRestClient#getCustomerByEmail ] Customer found: {}", customer);
                return customer;
            } else if (response.getStatus() == 404) {
                logger.info("[ CustomerRestClient#getCustomerByEmail ] Customer not found: email={}", email);
                return null;
            } else {
                logger.error("[ CustomerRestClient#getCustomerByEmail ] Unexpected status: {}", response.getStatus());
                throw new RuntimeException("Failed to get customer by email: HTTP " + response.getStatus());
            }
        } finally {
            client.close();
        }
    }
    
    /**
     * 顧客を新規登録します
     * 
     * @param customer 顧客エンティティ
     * @return 登録された顧客エンティティ（IDが採番されている）
     * @throws EmailAlreadyExistsException メールアドレスが重複している場合
     */
    public Customer registerCustomer(Customer customer) {
        logger.info("[ CustomerRestClient#registerCustomer ] email={}", customer.getEmail());
        
        Client client = ClientBuilder.newClient();
        try {
            Response response = client.target(BASE_URL)
                    .path("customers")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(customer, MediaType.APPLICATION_JSON));
            
            if (response.getStatus() == 201 || response.getStatus() == 200) {
                Customer registeredCustomer = response.readEntity(Customer.class);
                logger.info("[ CustomerRestClient#registerCustomer ] Customer registered: {}", registeredCustomer);
                return registeredCustomer;
            } else if (response.getStatus() == 409) {
                logger.warn("[ CustomerRestClient#registerCustomer ] Email already exists: email={}", customer.getEmail());
                throw new pro.kensait.berrybooks.service.customer.EmailAlreadyExistsException(customer.getEmail());
            } else {
                logger.error("[ CustomerRestClient#registerCustomer ] Unexpected status: {}", response.getStatus());
                throw new RuntimeException("Failed to register customer: HTTP " + response.getStatus());
            }
        } finally {
            client.close();
        }
    }
}

