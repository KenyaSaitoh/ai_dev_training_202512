package pro.kensait.berrybooks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 顧客情報のエンティティクラス
 * 
 * <p>顧客の基本情報と認証情報を表すエンティティです。</p>
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 顧客ID（主キー、自動採番）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    
    /**
     * 顧客名
     */
    @Column(name = "CUSTOMER_NAME", length = 30, nullable = false)
    private String customerName;
    
    /**
     * メールアドレス（一意）
     */
    @Column(name = "EMAIL", length = 30, nullable = false, unique = true)
    private String email;
    
    /**
     * パスワード（平文保存、学習用のみ）
     */
    @Column(name = "PASSWORD", length = 60, nullable = false)
    private String password;
    
    /**
     * 生年月日
     */
    @Column(name = "BIRTHDAY")
    private LocalDate birthday;
    
    /**
     * 住所
     */
    @Column(name = "ADDRESS", length = 120)
    private String address;
    
    /**
     * デフォルトコンストラクタ
     */
    public Customer() {
    }
    
    /**
     * 顧客IDを取得します
     * 
     * @return 顧客ID
     */
    public Integer getCustomerId() {
        return customerId;
    }
    
    /**
     * 顧客IDを設定します
     * 
     * @param customerId 顧客ID
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
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
     * @return 生年月日
     */
    public LocalDate getBirthday() {
        return birthday;
    }
    
    /**
     * 生年月日を設定します
     * 
     * @param birthday 生年月日
     */
    public void setBirthday(LocalDate birthday) {
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) 
                || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Customer[customerId=" + customerId + ", customerName=" + customerName + ", email=" + email + "]";
    }
}

