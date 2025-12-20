package pro.kensait.berrybooks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * 出版社マスタのエンティティクラス
 * 
 * <p>出版社の基本情報を表すエンティティです。</p>
 */
@Entity
@Table(name = "PUBLISHER")
public class Publisher implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 出版社ID（主キー、自動採番）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUBLISHER_ID")
    private Integer publisherId;
    
    /**
     * 出版社名
     */
    @Column(name = "PUBLISHER_NAME", length = 30, nullable = false)
    private String publisherName;
    
    /**
     * デフォルトコンストラクタ
     */
    public Publisher() {
    }
    
    /**
     * 出版社IDを取得します
     * 
     * @return 出版社ID
     */
    public Integer getPublisherId() {
        return publisherId;
    }
    
    /**
     * 出版社IDを設定します
     * 
     * @param publisherId 出版社ID
     */
    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }
    
    /**
     * 出版社名を取得します
     * 
     * @return 出版社名
     */
    public String getPublisherName() {
        return publisherName;
    }
    
    /**
     * 出版社名を設定します
     * 
     * @param publisherName 出版社名
     */
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (publisherId != null ? publisherId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Publisher)) {
            return false;
        }
        Publisher other = (Publisher) object;
        if ((this.publisherId == null && other.publisherId != null) 
                || (this.publisherId != null && !this.publisherId.equals(other.publisherId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Publisher[publisherId=" + publisherId + ", publisherName=" + publisherName + "]";
    }
}

