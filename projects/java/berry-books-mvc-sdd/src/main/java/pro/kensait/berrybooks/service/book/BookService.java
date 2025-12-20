package pro.kensait.berrybooks.service.book;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pro.kensait.berrybooks.dao.BookDao;
import pro.kensait.berrybooks.entity.Book;
import pro.kensait.berrybooks.web.book.SearchParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * 書籍検索のビジネスロジッククラス
 * 
 * <p>書籍検索に関するビジネスロジックを提供します。</p>
 */
@ApplicationScoped
public class BookService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    
    /**
     * 書籍データアクセスオブジェクト
     */
    @Inject
    private BookDao bookDao;
    
    /**
     * 書籍を検索します
     * 
     * <p>検索パラメータに応じて以下の検索を実行します：</p>
     * <ul>
     *   <li>カテゴリIDとキーワードが指定されている場合: カテゴリとキーワードで検索</li>
     *   <li>キーワードのみが指定されている場合: キーワードで検索</li>
     *   <li>カテゴリIDのみが指定されている場合: カテゴリで検索</li>
     *   <li>どちらも未指定の場合: 全書籍を取得</li>
     * </ul>
     * 
     * @param searchParam 検索パラメータ
     * @return 検索結果の書籍リスト
     */
    public List<Book> searchBook(SearchParam searchParam) {
        Integer categoryId = searchParam.getCategoryId();
        String keyword = searchParam.getKeyword();
        
        logger.info("[ BookService#searchBook ] categoryId={}, keyword={}", categoryId, keyword);
        
        // カテゴリとキーワードの両方が指定されている場合
        if (categoryId != null && keyword != null && !keyword.trim().isEmpty()) {
            return bookDao.findByCategoryAndKeyword(categoryId, keyword);
        }
        // キーワードのみが指定されている場合
        else if (keyword != null && !keyword.trim().isEmpty()) {
            return bookDao.findByKeyword(keyword);
        }
        // カテゴリIDのみが指定されている場合
        else if (categoryId != null) {
            return bookDao.findByCategory(categoryId);
        }
        // どちらも未指定の場合は全書籍を取得
        else {
            return bookDao.findAll();
        }
    }
    
    /**
     * 書籍IDで書籍を検索します
     * 
     * @param bookId 書籍ID
     * @return 書籍エンティティ（存在しない場合はnull）
     */
    public Book findBookById(Integer bookId) {
        logger.info("[ BookService#findBookById ] bookId={}", bookId);
        return bookDao.findById(bookId);
    }
}

