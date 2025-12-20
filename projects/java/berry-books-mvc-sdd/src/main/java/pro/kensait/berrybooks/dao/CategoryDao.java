package pro.kensait.berrybooks.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pro.kensait.berrybooks.entity.Category;
import java.util.List;

/**
 * カテゴリマスタのデータアクセスクラス
 * 
 * <p>カテゴリエンティティに対するCRUD操作を提供します。</p>
 */
@ApplicationScoped
public class CategoryDao {
    
    /**
     * エンティティマネージャ
     */
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * 全カテゴリを取得します（カテゴリID昇順）
     * 
     * @return カテゴリリスト
     */
    public List<Category> findAll() {
        TypedQuery<Category> query = em.createQuery(
            "SELECT c FROM Category c ORDER BY c.categoryId ASC", 
            Category.class
        );
        return query.getResultList();
    }
}

