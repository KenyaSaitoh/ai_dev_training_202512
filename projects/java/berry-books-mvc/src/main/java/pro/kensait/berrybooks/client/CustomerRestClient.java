package pro.kensait.berrybooks.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pro.kensait.berrybooks.client.dto.CustomerTO;
import pro.kensait.berrybooks.client.dto.ErrorResponse;
import pro.kensait.berrybooks.entity.Customer;
import pro.kensait.berrybooks.service.customer.EmailAlreadyExistsException;

/**
 * berry-books-rest APIを呼び出すRESTクライアント
 * Customer関連のAPI呼び出しを担当
 */
@ApplicationScoped
public class CustomerRestClient {
    private static final Logger logger = LoggerFactory.getLogger(
            CustomerRestClient.class);

    private String baseUrl;
    private Client client;

    @PostConstruct
    public void init() {
        // 設定ファイルからベースURLを読み込む
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
                baseUrl = props.getProperty("customer.api.base-url", 
                        "http://localhost:8080/berry-books-rest/customers");
            } else {
                baseUrl = "http://localhost:8080/berry-books-rest/customers";
            }
        } catch (IOException e) {
            logger.warn("Failed to load config.properties, using default URL", e);
            baseUrl = "http://localhost:8080/berry-books-rest/customers";
        }

        // JAX-RS Clientを作成
        client = ClientBuilder.newClient();
        logger.info("CustomerRestClient initialized with baseUrl: " + baseUrl);
    }

    /**
     * メールアドレスで顧客を検索する
     * GET /customers/query_email?email={email}
     */
    public Customer findByEmail(String email) {
        logger.info("[ CustomerRestClient#findByEmail ] email=" + email);

        WebTarget target = client.target(baseUrl)
                .path("/query_email")
                .queryParam("email", email);

        try (Response response = target.request(MediaType.APPLICATION_JSON).get()) {
            if (response.getStatus() == 200) {
                CustomerTO customerTO = response.readEntity(CustomerTO.class);
                return toCustomer(customerTO);
            } else if (response.getStatus() == 404) {
                // 顧客が見つからない場合
                logger.info("Customer not found: " + email);
                return null;
            } else {
                // その他のエラー
                logger.error("Unexpected response status: " + response.getStatus());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error calling REST API: findByEmail", e);
            return null;
        }
    }

    /**
     * 顧客IDで顧客を検索する
     * GET /customers/{customerId}
     */
    public Customer findById(Integer customerId) {
        logger.info("[ CustomerRestClient#findById ] customerId=" + customerId);

        WebTarget target = client.target(baseUrl)
                .path("/" + customerId);

        try (Response response = target.request(MediaType.APPLICATION_JSON).get()) {
            if (response.getStatus() == 200) {
                CustomerTO customerTO = response.readEntity(CustomerTO.class);
                return toCustomer(customerTO);
            } else if (response.getStatus() == 404) {
                // 顧客が見つからない場合
                logger.info("Customer not found: " + customerId);
                return null;
            } else {
                // その他のエラー
                logger.error("Unexpected response status: " + response.getStatus());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error calling REST API: findById", e);
            return null;
        }
    }

    /**
     * 顧客を新規登録する
     * POST /customers/
     */
    public Customer register(Customer customer) {
        logger.info("[ CustomerRestClient#register ] customer=" + customer);

        // CustomerエンティティからCustomerTOへ変換
        CustomerTO requestTO = new CustomerTO(
                null, // customerId は新規登録時はnull
                customer.getCustomerName(),
                customer.getPassword(),
                customer.getEmail(),
                customer.getBirthday(),
                customer.getAddress()
        );

        WebTarget target = client.target(baseUrl).path("/");

        try (Response response = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.json(requestTO))) {
            
            if (response.getStatus() == 200) {
                // 登録成功
                CustomerTO responseTO = response.readEntity(CustomerTO.class);
                return toCustomer(responseTO);
                
            } else if (response.getStatus() == 409) {
                // メールアドレス重複
                ErrorResponse error = response.readEntity(ErrorResponse.class);
                logger.warn("Customer already exists: " + error.message());
                throw new EmailAlreadyExistsException(customer.getEmail(), error.message());
                
            } else {
                // その他のエラー
                logger.error("Unexpected response status: " + response.getStatus());
                throw new RuntimeException("Failed to register customer: HTTP " + response.getStatus());
            }
            
        } catch (EmailAlreadyExistsException e) {
            // そのまま再スロー
            throw e;
        } catch (Exception e) {
            logger.error("Error calling REST API: register", e);
            throw new RuntimeException("Failed to register customer", e);
        }
    }

    /**
     * CustomerTOをCustomerエンティティに変換
     */
    private Customer toCustomer(CustomerTO customerTO) {
        Customer customer = new Customer();
        customer.setCustomerId(customerTO.customerId());
        customer.setCustomerName(customerTO.customerName());
        customer.setPassword(customerTO.password());
        customer.setEmail(customerTO.email());
        customer.setBirthday(customerTO.birthday());
        customer.setAddress(customerTO.address());
        return customer;
    }
}
