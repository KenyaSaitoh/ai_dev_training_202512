package pro.kensait.berrybooks.web.book;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pro.kensait.berrybooks.dao.StockDao;
import pro.kensait.berrybooks.entity.Book;
import pro.kensait.berrybooks.entity.Category;
import pro.kensait.berrybooks.entity.Stock;
import pro.kensait.berrybooks.service.book.BookService;
import pro.kensait.berrybooks.service.category.CategoryService;
import pro.kensait.berrybooks.web.cart.CartItem;
import pro.kensait.berrybooks.web.cart.CartSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.List;

/**
 * 書籍検索画面のコントローラー
 * 
 * <p>書籍検索画面のビューとビジネスロジックの橋渡しを行います。</p>
 */
@Named
@ViewScoped
public class BookSearchBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(BookSearchBean.class);
    
    /**
     * 書籍サービス
     */
    @Inject
    private BookService bookService;
    
    /**
     * カテゴリサービス
     */
    @Inject
    private CategoryService categoryService;
    
    /**
     * カートセッション
     */
    @Inject
    private CartSession cartSession;
    
    /**
     * 在庫DAO
     */
    @Inject
    private StockDao stockDao;
    
    /**
     * 検索パラメータ
     */
    private SearchParam searchParam;
    
    /**
     * 検索結果の書籍リスト
     */
    private List<Book> bookList;
    
    /**
     * カテゴリリスト
     */
    private List<Category> categoryList;
    
    /**
     * 初期化処理
     * カテゴリリストを取得します。
     */
    @PostConstruct
    public void init() {
        searchParam = new SearchParam();
        categoryList = categoryService.findAll();
        logger.info("[ BookSearchBean#init ] categoryList size={}", categoryList.size());
    }
    
    /**
     * 書籍を検索します
     * 
     * @return 検索結果画面への遷移
     */
    public String search() {
        logger.info("[ BookSearchBean#search ]");
        bookList = bookService.searchBook(searchParam);
        logger.info("[ BookSearchBean#search ] search result size={}", bookList.size());
        return "bookSelect?faces-redirect=true";
    }
    
    /**
     * カートに書籍を追加します
     * 
     * <p>在庫情報を取得し、在庫バージョン番号を保存してカートに追加します（BR-012）。</p>
     * 
     * @param book 追加する書籍
     * @return カート画面への遷移
     */
    public String addToCart(Book book) {
        logger.info("[ BookSearchBean#addToCart ] bookId={}", book.getBookId());
        
        // 在庫情報を取得（BR-012: 在庫バージョン番号を保存）
        Stock stock = stockDao.findByBookId(book.getBookId());
        
        if (stock == null) {
            logger.error("[ BookSearchBean#addToCart ] Stock not found for bookId={}", book.getBookId());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "在庫情報が見つかりません", null));
            return null;
        }
        
        // CartItemを作成
        CartItem cartItem = new CartItem(
            book.getBookId(),
            book.getBookName(),
            book.getPublisher().getPublisherName(),
            book.getPrice(),
            1, // 初期数量は1
            stock.getVersion() // 在庫バージョン番号を保存（BR-012）
        );
        
        // カートに追加
        cartSession.addItem(cartItem);
        
        // 成功メッセージを表示
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "カートに追加しました", null));
        
        logger.info("[ BookSearchBean#addToCart ] Added to cart: bookId={}, version={}", 
            book.getBookId(), stock.getVersion());
        
        return "cartView?faces-redirect=true";
    }
    
    /**
     * 検索パラメータを取得します
     * 
     * @return 検索パラメータ
     */
    public SearchParam getSearchParam() {
        return searchParam;
    }
    
    /**
     * 検索パラメータを設定します
     * 
     * @param searchParam 検索パラメータ
     */
    public void setSearchParam(SearchParam searchParam) {
        this.searchParam = searchParam;
    }
    
    /**
     * 検索結果の書籍リストを取得します
     * 
     * @return 書籍リスト
     */
    public List<Book> getBookList() {
        return bookList;
    }
    
    /**
     * 検索結果の書籍リストを設定します
     * 
     * @param bookList 書籍リスト
     */
    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
    
    /**
     * カテゴリリストを取得します
     * 
     * @return カテゴリリスト
     */
    public List<Category> getCategoryList() {
        return categoryList;
    }
    
    /**
     * カテゴリリストを設定します
     * 
     * @param categoryList カテゴリリスト
     */
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}

