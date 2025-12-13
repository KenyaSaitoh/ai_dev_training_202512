package pro.kensait.berrybooks.service.order;

/**
 * OutOfStockException
 * 
 * Thrown when attempting to order a book that is out of stock or has insufficient quantity.
 */
public class OutOfStockException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final Integer bookId;
    private final String bookName;
    private final Integer requestedQuantity;
    private final Integer availableQuantity;
    
    public OutOfStockException(Integer bookId, String bookName, 
                              Integer requestedQuantity, Integer availableQuantity) {
        super(String.format("Out of stock: bookId=%d, bookName=%s, requested=%d, available=%d",
                           bookId, bookName, requestedQuantity, availableQuantity));
        this.bookId = bookId;
        this.bookName = bookName;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }
    
    public OutOfStockException(Integer bookId, String bookName, 
                              Integer requestedQuantity, Integer availableQuantity, String message) {
        super(message);
        this.bookId = bookId;
        this.bookName = bookName;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }
    
    public Integer getBookId() {
        return bookId;
    }
    
    public String getBookName() {
        return bookName;
    }
    
    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }
    
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
}
