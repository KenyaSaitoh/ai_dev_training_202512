package pro.kensait.berrybooks.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Publisher Entity (出版社マスタ)
 * 
 * Represents a book publisher in the system.
 */
@Entity
@Table(name = "PUBLISHER")
public class Publisher implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUBLISHER_ID")
    private Integer publisherId;
    
    @Column(name = "PUBLISHER_NAME", nullable = false, length = 30)
    private String publisherName;
    
    // Constructors
    
    public Publisher() {
    }
    
    public Publisher(Integer publisherId, String publisherName) {
        this.publisherId = publisherId;
        this.publisherName = publisherName;
    }
    
    // Getters and Setters
    
    public Integer getPublisherId() {
        return publisherId;
    }
    
    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }
    
    public String getPublisherName() {
        return publisherName;
    }
    
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
    
    // hashCode, equals, toString
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((publisherId == null) ? 0 : publisherId.hashCode());
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
        Publisher other = (Publisher) obj;
        if (publisherId == null) {
            if (other.publisherId != null)
                return false;
        } else if (!publisherId.equals(other.publisherId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Publisher [publisherId=" + publisherId + ", publisherName=" + publisherName + "]";
    }
}
