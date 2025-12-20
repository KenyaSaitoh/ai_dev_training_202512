package pro.kensait.berrybooks.service.category;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.dao.CategoryDao;
import pro.kensait.berrybooks.entity.Category;
import java.util.List;

/**
 * カテゴリ管理のビジネスロジッククラス
 * 
 * <p>カテゴリに関するビジネスロジックを提供します。</p>
 */
@ApplicationScoped
public class CategoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    
    /**
     * カテゴリDAO
     */
    @Inject
    private CategoryDao categoryDao;
    
    /**
     * カテゴリ一覧を取得します
     * 
     * @return カテゴリリスト
     */
    public List<Category> findAll() {
        logger.info("[ CategoryService#findAll ]");
        return categoryDao.findAll();
    }
}

