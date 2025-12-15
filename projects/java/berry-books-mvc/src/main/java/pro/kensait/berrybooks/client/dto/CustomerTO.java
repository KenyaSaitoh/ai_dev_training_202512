package pro.kensait.berrybooks.client.dto;

import java.time.LocalDate;

/**
 * REST API用の顧客データ転送オブジェクト
 * berry-books-rest APIのレスポンス/リクエストに使用
 */
public record CustomerTO (
        // 顧客ID
        Integer customerId,
        // 顧客名
        String customerName,
        // パスワード
        String password,
        // メールアドレス
        String email,
        // 生年月日
        LocalDate birthday,
        // 住所
        String address) {
}
