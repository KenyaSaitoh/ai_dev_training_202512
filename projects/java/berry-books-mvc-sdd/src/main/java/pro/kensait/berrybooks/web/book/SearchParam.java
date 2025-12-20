package pro.kensait.berrybooks.web.book;

import java.io.Serializable;

/**
 * 書籍検索パラメータを保持するDTOクラス
 * 
 * <p>書籍検索画面からの検索条件を保持します。</p>
 */
public class SearchParam implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * カテゴリID
     */
    private Integer categoryId;
    
    /**
     * 検索キーワード
     */
    private String keyword;
    
    /**
     * デフォルトコンストラクタ
     */
    public SearchParam() {
    }
    
    /**
     * カテゴリIDを取得します
     * 
     * @return カテゴリID
     */
    public Integer getCategoryId() {
        return categoryId;
    }
    
    /**
     * カテゴリIDを設定します
     * 
     * @param categoryId カテゴリID
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * 検索キーワードを取得します
     * 
     * @return 検索キーワード
     */
    public String getKeyword() {
        return keyword;
    }
    
    /**
     * 検索キーワードを設定します
     * 
     * @param keyword 検索キーワード
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}



