package pro.kensait.berrybooks.web.cart;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * カート画面のコントローラー
 * 
 * <p>カート画面のビューとビジネスロジックの橋渡しを行います。</p>
 */
@Named
@ViewScoped
public class CartBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(CartBean.class);
    
    /**
     * カートセッション
     */
    @Inject
    private CartSession cartSession;
    
    /**
     * デフォルトコンストラクタ
     */
    public CartBean() {
    }
    
    /**
     * カート内容を更新します
     * 
     * <p>削除フラグがtrueのアイテムを削除し、合計金額を再計算します。</p>
     * 
     * @return null（同じページに留まる）
     */
    public String updateCart() {
        logger.info("[ CartBean#updateCart ]");
        
        // 削除フラグがtrueのアイテムを収集
        List<Integer> itemsToRemove = new ArrayList<>();
        for (CartItem item : cartSession.getCartItems()) {
            if (item.isRemove()) {
                itemsToRemove.add(item.getBookId());
            }
        }
        
        // 削除フラグがtrueのアイテムを削除
        for (Integer bookId : itemsToRemove) {
            cartSession.removeItem(bookId);
            logger.info("[ CartBean#updateCart ] Removed item with bookId={}", bookId);
        }
        
        // 合計金額を再計算
        cartSession.calculateTotalPrice();
        
        if (!itemsToRemove.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "選択した書籍を削除しました", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "カートを更新しました", null));
        }
        
        return null; // 同じページに留まる
    }
    
    /**
     * 注文画面に進みます
     * 
     * <p>カートが空の場合はエラーメッセージを表示し、注文画面に遷移しません（BIZ-005）。</p>
     * 
     * @return 注文画面へのナビゲーションルール、またはnull（エラー時）
     */
    public String proceedToOrder() {
        logger.info("[ CartBean#proceedToOrder ]");
        
        // カートが空かどうかをチェック
        if (cartSession.isEmpty()) {
            logger.warn("[ CartBean#proceedToOrder ] Cart is empty");
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "カートが空です。書籍を追加してください。", null));
            return null; // 同じページに留まる
        }
        
        logger.info("[ CartBean#proceedToOrder ] Proceeding to order screen");
        return "bookOrder?faces-redirect=true";
    }
    
    /**
     * カート全体をクリアします
     * 
     * @return カートクリア確認画面へのナビゲーションルール
     */
    public String clearCart() {
        logger.info("[ CartBean#clearCart ]");
        
        cartSession.clear();
        
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "カートをクリアしました", null));
        
        return null; // 同じページに留まる
    }
    
    /**
     * カートセッションを取得します
     * 
     * @return カートセッション
     */
    public CartSession getCartSession() {
        return cartSession;
    }
    
    /**
     * カートセッションを設定します
     * 
     * @param cartSession カートセッション
     */
    public void setCartSession(CartSession cartSession) {
        this.cartSession = cartSession;
    }
}



