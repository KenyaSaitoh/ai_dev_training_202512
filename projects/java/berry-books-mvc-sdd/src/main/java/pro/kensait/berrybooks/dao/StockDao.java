package pro.kensait.berrybooks.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pro.kensait.berrybooks.entity.Stock;

/**
 * 在庫情報のデータアクセスクラス
 * 
 * <p>在庫エンティティに対するCRUD操作を提供します。</p>
 */
@ApplicationScoped
public class StockDao {
    
    /**
     * エンティティマネージャ
     */
    @PersistenceContext(unitName = "BerryBooksPU")
    private EntityManager em;
    
    /**
     * 書籍IDで在庫情報を取得します
     * 
     * @param bookId 書籍ID
     * @return 在庫情報（存在しない場合はnull）
     */
    public Stock findByBookId(Integer bookId) {
        return em.find(Stock.class, bookId);
    }
    
    /**
     * 在庫情報を更新します
     * 
     * @param stock 在庫情報
     * @return 更新された在庫情報
     */
    public Stock update(Stock stock) {
        return em.merge(stock);
    }
    
    /**
     * 在庫数を減らします（楽観的ロック対応）
     * 
     * @param bookId 書籍ID
     * @param quantity 減らす数量
     * @return 更新された在庫情報
     */
    public Stock decreaseQuantity(Integer bookId, Integer quantity) {
        Stock stock = findByBookId(bookId);
        if (stock != null) {
            stock.setQuantity(stock.getQuantity() - quantity);
            return em.merge(stock);
        }
        return null;
    }
}





