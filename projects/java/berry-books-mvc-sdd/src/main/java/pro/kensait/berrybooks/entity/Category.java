package pro.kensait.berrybooks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * カテゴリマスタのエンティティクラス
 * 
 * <p>書籍のカテゴリを表すエンティティです。</p>
 */
@Entity
@Table(name = "CATEGORY")
public class Category implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * カテゴリID（主キー、自動採番）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Integer categoryId;
    
    /**
     * カテゴリ名
     */
    @Column(name = "CATEGORY_NAME", length = 20, nullable = false)
    private String categoryName;
    
    /**
     * デフォルトコンストラクタ
     */
    public Category() {
    }
    
    /**
     * カテゴリIDを取得します
     * 
     * @return カテゴリID
     */
    public Integer getCategoryId() {
        return categoryId;
    }
    
    /**
     * カテゴリIDを設定します
     * 
     * @param categoryId カテゴリID
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * カテゴリ名を取得します
     * 
     * @return カテゴリ名
     */
    public String getCategoryName() {
        return categoryName;
    }
    
    /**
     * カテゴリ名を設定します
     * 
     * @param categoryName カテゴリ名
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.categoryId == null && other.categoryId != null) 
                || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Category[categoryId=" + categoryId + ", categoryName=" + categoryName + "]";
    }
}

