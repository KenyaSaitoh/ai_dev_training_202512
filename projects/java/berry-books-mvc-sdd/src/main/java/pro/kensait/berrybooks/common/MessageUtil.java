package pro.kensait.berrybooks.common;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * メッセージリソース（messages.properties）からメッセージを取得するユーティリティクラス
 * 
 * <p>このクラスは、アプリケーション全体で使用するメッセージリソースへのアクセスを提供します。</p>
 */
public final class MessageUtil {
    
    /**
     * リソースバンドル名
     */
    private static final String BUNDLE_NAME = "messages";
    
    /**
     * プライベートコンストラクタ（インスタンス化を防止）
     */
    private MessageUtil() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    /**
     * 指定されたキーに対応するメッセージを取得します
     * 
     * @param key メッセージキー
     * @return メッセージ文字列。キーが見つからない場合はキー自体を返す
     */
    public static String getMessage(String key) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            // キーが見つからない場合はキー自体を返す
            return "!" + key + "!";
        }
    }
    
    /**
     * 指定されたキーに対応するメッセージを取得し、プレースホルダーを置換します
     * 
     * @param key メッセージキー
     * @param args プレースホルダーに置換する引数
     * @return プレースホルダーが置換されたメッセージ文字列
     */
    public static String getMessage(String key, Object... args) {
        String message = getMessage(key);
        return String.format(message, args);
    }
}

