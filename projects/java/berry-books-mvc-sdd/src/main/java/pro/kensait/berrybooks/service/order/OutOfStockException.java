package pro.kensait.berrybooks.service.order;

/**
 * 在庫不足時にスローされるカスタム例外クラス
 * 
 * <p>注文処理時に在庫数が不足している場合にスローされます。</p>
 */
public class OutOfStockException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 書籍ID
     */
    private final Integer bookId;
    
    /**
     * 書籍名
     */
    private final String bookName;
    
    /**
     * OutOfStockExceptionのコンストラクタ
     * 
     * @param bookId 書籍ID
     * @param bookName 書籍名
     */
    public OutOfStockException(Integer bookId, String bookName) {
        super("在庫不足: " + bookName + " (bookId=" + bookId + ")");
        this.bookId = bookId;
        this.bookName = bookName;
    }
    
    /**
     * 書籍IDを取得します
     * 
     * @return 書籍ID
     */
    public Integer getBookId() {
        return bookId;
    }
    
    /**
     * 書籍名を取得します
     * 
     * @return 書籍名
     */
    public String getBookName() {
        return bookName;
    }
}

