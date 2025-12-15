package pro.kensait.berrybooks.client.dto;

/**
 * REST API用のエラーレスポンス
 * berry-books-rest APIのエラーレスポンスに使用
 */
public record ErrorResponse(
        String code,
        String message
        ) {
}
