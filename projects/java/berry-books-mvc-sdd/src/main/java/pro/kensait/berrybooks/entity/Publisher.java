package pro.kensait.berrybooks.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * 出版社エンティティ
 * 
 * テーブル: PUBLISHER
 * 目的: 出版社の基本情報を管理
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
     * この出版社が出版した書籍リスト
     */
    @OneToMany(mappedBy = "publisher")
    private List<Book> books;

    // ========================================
    // Constructors
    // ========================================

    public Publisher() {
    }

    public Publisher(Integer publisherId, String publisherName) {
        this.publisherId = publisherId;
        this.publisherName = publisherName;
    }

    // ========================================
    // Getters and Setters
    // ========================================

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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    // ========================================
    // toString, equals, hashCode
    // ========================================

    @Override
    public String toString() {
        return "Publisher [publisherId=" + publisherId + ", publisherName=" + publisherName + "]";
    }

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
}

