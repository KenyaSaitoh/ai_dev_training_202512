package pro.kensait.berrybooks.entity;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Customer Entity (顧客)
 * 
 * Represents a customer in the system with authentication information.
 * Note: Password is stored in plain text for learning purposes only.
 * In production, passwords should be hashed using BCrypt or similar.
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "CUSTOMER_NAME", nullable = false, length = 30)
    private String customerName;
    
    @NotNull
    @Email
    @Size(min = 1, max = 30)
    @Column(name = "EMAIL", nullable = false, unique = true, length = 30)
    private String email;
    
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "PASSWORD", nullable = false, length = 60)
    private String password;
    
    @Column(name = "BIRTHDAY")
    private LocalDate birthday;
    
    @Size(max = 120)
    @Column(name = "ADDRESS", length = 120)
    private String address;
    
    // Constructors
    
    public Customer() {
    }
    
    public Customer(Integer customerId, String customerName, String email, 
                   String password, LocalDate birthday, String address) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.address = address;
    }
    
    // Getters and Setters
    
    public Integer getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
     * Authenticate customer with password
     * 
     * @param inputPassword the password to check
     * @return true if password matches, false otherwise
     */
    public boolean authenticate(String inputPassword) {
        return password != null && password.equals(inputPassword);
    }
    
    // hashCode, equals, toString
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (customerId == null) {
            if (other.customerId != null)
                return false;
        } else if (!customerId.equals(other.customerId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Customer [customerId=" + customerId + ", customerName=" + customerName 
                + ", email=" + email + ", birthday=" + birthday + ", address=" + address + "]";
    }
}
