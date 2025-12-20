package pro.kensait.berrybooks.web.customer;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import pro.kensait.berrybooks.entity.Customer;

/**
 * 顧客管理のManagedBean（セッションスコープ）
 * 
 * <p>ログイン状態とログイン済み顧客情報を保持します。</p>
 */
@Named
@SessionScoped
public class CustomerBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ログイン済み顧客情報
     */
    private Customer customer;
    
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
        // ログイン画面に遷移
        return "/index.xhtml?faces-redirect=true";
    }
}

