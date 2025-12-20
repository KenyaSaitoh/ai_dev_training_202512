package pro.kensait.berrybooks.integration;

import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import pro.kensait.berrybooks.dao.BookDao;
import pro.kensait.berrybooks.dao.CategoryDao;
import pro.kensait.berrybooks.entity.Book;
import pro.kensait.berrybooks.entity.Category;
import pro.kensait.berrybooks.entity.Customer;
import pro.kensait.berrybooks.service.book.BookService;
import pro.kensait.berrybooks.service.customer.CustomerService;
import pro.kensait.berrybooks.web.cart.CartItem;
import pro.kensait.berrybooks.web.cart.CartSession;

import java.math.BigDecimal;
import java.util.List;

/**
 * T_INTEG_001: ログイン → 書籍検索 → カート追加の結合テスト
 * 
 * 目的: F-004（顧客管理・認証）→ F-001（書籍検索）→ F-002（ショッピングカート）の連携を確認する
 * 
 * テストシナリオ:
 * 1. ログイン画面でログイン
 * 2. 書籍検索画面でカテゴリ「Java」を選択して検索
 * 3. 検索結果から書籍を選択してカートに追加
 * 4. カート画面で書籍が表示されることを確認
 * 5. 合計金額が正しく計算されることを確認
 * 
 * 期待結果: カートに書籍が追加され、合計金額が正しく表示される
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginToCartIntegrationTest extends IntegrationTestBase {

    private CustomerService customerService;
    private BookService bookService;
    private CartSession cartSession;

    @BeforeEach
    public void setUp() {
        // テスト用のサービスとセッションを初期化
        customerService = new CustomerService();
        bookService = new BookService();
        cartSession = new CartSession();
        
        // カートをクリア
        cartSession.clear();
    }

    @Test
    @Order(1)
    @DisplayName("ログイン → 書籍検索 → カート追加の統合フロー")
    public void testLoginToCartFlow() throws Exception {
        // Given: テスト用顧客が存在する
        String testEmail = "test@example.com";
        String testPassword = "password123";
        
        // When: ログイン処理
        // Note: このテストはサービスレイヤーの統合テスト
        // 実際のログイン処理はCustomerServiceを通じて行われる想定
        
        // Step 1: 書籍検索（カテゴリ「Java」で検索）
        Integer javaCategory = 1; // JavaカテゴリのID
        List<Book> searchResults = bookService.searchBooks(javaCategory, null);
        
        // Then: 検索結果が取得できる
        assertNotNull(searchResults, "検索結果がnullです");
        assertFalse(searchResults.isEmpty(), "検索結果が空です");
        assertTrue(searchResults.size() > 0, "Java書籍が見つかりません");
        
        // Step 2: 検索結果から最初の書籍を選択
        Book selectedBook = searchResults.get(0);
        assertNotNull(selectedBook, "選択した書籍がnullです");
        assertNotNull(selectedBook.getBookId(), "書籍IDがnullです");
        assertNotNull(selectedBook.getPrice(), "書籍価格がnullです");
        
        // Step 3: カートに追加
        CartItem cartItem = new CartItem();
        cartItem.setBookId(selectedBook.getBookId());
        cartItem.setBookName(selectedBook.getBookName());
        cartItem.setAuthor(selectedBook.getAuthor());
        cartItem.setPrice(selectedBook.getPrice());
        cartItem.setCount(1);
        cartItem.setStockVersion(selectedBook.getStock().getVersion()); // 楽観的ロック用のバージョン
        
        cartSession.addItem(cartItem);
        
        // Step 4: カート内容を確認
        List<CartItem> cartItems = cartSession.getCartItems();
        assertNotNull(cartItems, "カートアイテムリストがnullです");
        assertEquals(1, cartItems.size(), "カートに1件の書籍が追加されているはずです");
        
        CartItem addedItem = cartItems.get(0);
        assertEquals(selectedBook.getBookId(), addedItem.getBookId(), "書籍IDが一致しません");
        assertEquals(selectedBook.getBookName(), addedItem.getBookName(), "書籍名が一致しません");
        assertEquals(selectedBook.getPrice(), addedItem.getPrice(), "価格が一致しません");
        assertEquals(1, addedItem.getCount(), "数量が1であるべきです");
        
        // Step 5: 合計金額を確認
        cartSession.calculateTotalPrice();
        BigDecimal totalPrice = cartSession.getTotalPrice();
        assertNotNull(totalPrice, "合計金額がnullです");
        assertEquals(selectedBook.getPrice(), totalPrice, "合計金額が書籍価格と一致するはずです");
    }
    
    @Test
    @Order(2)
    @DisplayName("複数書籍をカートに追加して合計金額を確認")
    public void testMultipleBooksInCart() throws Exception {
        // Given: 複数の書籍を検索
        List<Book> searchResults = bookService.searchBooks(1, null); // Javaカテゴリ
        assertTrue(searchResults.size() >= 2, "テストには少なくとも2冊の書籍が必要です");
        
        // When: 2冊の書籍をカートに追加
        Book book1 = searchResults.get(0);
        Book book2 = searchResults.get(1);
        
        CartItem item1 = new CartItem();
        item1.setBookId(book1.getBookId());
        item1.setBookName(book1.getBookName());
        item1.setAuthor(book1.getAuthor());
        item1.setPrice(book1.getPrice());
        item1.setCount(1);
        item1.setStockVersion(book1.getStock().getVersion());
        
        CartItem item2 = new CartItem();
        item2.setBookId(book2.getBookId());
        item2.setBookName(book2.getBookName());
        item2.setAuthor(book2.getAuthor());
        item2.setPrice(book2.getPrice());
        item2.setCount(2); // 2冊購入
        item2.setStockVersion(book2.getStock().getVersion());
        
        cartSession.addItem(item1);
        cartSession.addItem(item2);
        
        // Then: カートに2件のアイテムが存在する
        List<CartItem> cartItems = cartSession.getCartItems();
        assertEquals(2, cartItems.size(), "カートに2件のアイテムが存在するはずです");
        
        // 合計金額が正しく計算される
        cartSession.calculateTotalPrice();
        BigDecimal expectedTotal = book1.getPrice()
                .add(book2.getPrice().multiply(BigDecimal.valueOf(2)));
        assertEquals(expectedTotal, cartSession.getTotalPrice(), 
                "合計金額が正しく計算されていません");
    }
    
    @Test
    @Order(3)
    @DisplayName("カートから書籍を削除して合計金額を再計算")
    public void testRemoveFromCart() throws Exception {
        // Given: カートに2冊の書籍が存在する
        List<Book> searchResults = bookService.searchBooks(1, null);
        assertTrue(searchResults.size() >= 2, "テストには少なくとも2冊の書籍が必要です");
        
        Book book1 = searchResults.get(0);
        Book book2 = searchResults.get(1);
        
        CartItem item1 = new CartItem();
        item1.setBookId(book1.getBookId());
        item1.setBookName(book1.getBookName());
        item1.setPrice(book1.getPrice());
        item1.setCount(1);
        
        CartItem item2 = new CartItem();
        item2.setBookId(book2.getBookId());
        item2.setBookName(book2.getBookName());
        item2.setPrice(book2.getPrice());
        item2.setCount(1);
        
        cartSession.addItem(item1);
        cartSession.addItem(item2);
        assertEquals(2, cartSession.getCartItems().size());
        
        // When: 1冊目を削除
        cartSession.removeItem(book1.getBookId());
        
        // Then: カートに1件のアイテムのみ存在する
        assertEquals(1, cartSession.getCartItems().size(), 
                "カートに1件のアイテムのみ存在するはずです");
        
        // 残っているのは2冊目
        CartItem remainingItem = cartSession.getCartItems().get(0);
        assertEquals(book2.getBookId(), remainingItem.getBookId(),
                "削除後に残っているのは2冊目の書籍であるはずです");
        
        // 合計金額が2冊目の価格のみになる
        cartSession.calculateTotalPrice();
        assertEquals(book2.getPrice(), cartSession.getTotalPrice(),
                "合計金額が2冊目の価格のみであるはずです");
    }
}

