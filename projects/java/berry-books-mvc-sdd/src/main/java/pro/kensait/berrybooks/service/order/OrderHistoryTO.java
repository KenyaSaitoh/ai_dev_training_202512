package pro.kensait.berrybooks.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 注文履歴情報をレイヤー間で転送するTOクラス（Record）
 * 
 * <p>注文履歴一覧表示用の転送オブジェクトです。</p>
 * <p>注文トランザクションと注文明細を結合したデータを保持します。</p>
 */
public record OrderHistoryTO(
        LocalDate orderDate,       // 注文日
        Integer tranId,            // 注文ID
        Integer detailId,          // 注文明細ID
        String bookName,           // 書籍名
        String publisherName,      // 出版社名
        BigDecimal price,          // 価格
        Integer count              // 個数
) {
}

