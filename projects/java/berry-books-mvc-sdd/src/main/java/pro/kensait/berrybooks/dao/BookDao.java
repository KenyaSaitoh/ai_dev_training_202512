package pro.kensait.berrybooks.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pro.kensait.berrybooks.entity.Book;
import java.util.List;

/**
 * 書籍マスタのデータアクセスクラス
 * 
 * <p>書籍エンティティに対するCRUD操作を提供します。</p>
 */
@ApplicationScoped
public class BookDao {
    
    /**
     * エンティティマネージャ
     */
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * 全書籍を取得します（書籍ID昇順）
     * 在庫情報は@SecondaryTableで自動的にロードされます。
     * 
     * @return 書籍リスト
     */
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery(
            "SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.category " +
            "LEFT JOIN FETCH b.publisher " +
            "ORDER BY b.bookId ASC", 
            Book.class
        );
        return query.getResultList();
    }
    
    /**
     * 書籍IDで書籍を検索します
     * 
     * @param bookId 書籍ID
     * @return 書籍エンティティ（存在しない場合はnull）
     */
    public Book findById(Integer bookId) {
        return em.find(Book.class, bookId);
    }
    
    /**
     * キーワードで書籍を検索します
     * 書籍名または著者名で部分一致検索（LIKE検索）を行います。
     * 
     * @param keyword 検索キーワード
     * @return 書籍リスト
     */
    public List<Book> findByKeyword(String keyword) {
        TypedQuery<Book> query = em.createQuery(
            "SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.category " +
            "LEFT JOIN FETCH b.publisher " +
            "WHERE b.bookName LIKE :keyword OR b.author LIKE :keyword " +
            "ORDER BY b.bookId ASC", 
            Book.class
        );
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }
    
    /**
     * カテゴリで書籍を検索します
     * 
     * @param categoryId カテゴリID
     * @return 書籍リスト
     */
    public List<Book> findByCategory(Integer categoryId) {
        TypedQuery<Book> query = em.createQuery(
            "SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.category " +
            "LEFT JOIN FETCH b.publisher " +
            "WHERE b.category.categoryId = :categoryId " +
            "ORDER BY b.bookId ASC", 
            Book.class
        );
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }
    
    /**
     * カテゴリとキーワードで書籍を検索します
     * カテゴリIDで絞り込み、かつ書籍名または著者名で部分一致検索を行います。
     * 
     * @param categoryId カテゴリID
     * @param keyword 検索キーワード
     * @return 書籍リスト
     */
    public List<Book> findByCategoryAndKeyword(Integer categoryId, String keyword) {
        TypedQuery<Book> query = em.createQuery(
            "SELECT DISTINCT b FROM Book b " +
            "LEFT JOIN FETCH b.category " +
            "LEFT JOIN FETCH b.publisher " +
            "WHERE b.category.categoryId = :categoryId " +
            "AND (b.bookName LIKE :keyword OR b.author LIKE :keyword) " +
            "ORDER BY b.bookId ASC", 
            Book.class
        );
        query.setParameter("categoryId", categoryId);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }
}

