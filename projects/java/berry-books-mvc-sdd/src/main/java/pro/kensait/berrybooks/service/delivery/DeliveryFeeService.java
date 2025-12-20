package pro.kensait.berrybooks.service.delivery;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * 配送料金計算のビジネスロジッククラス
 * 
 * <p>配送先住所と購入金額に基づいて配送料金を計算します。</p>
 * 
 * <p>料金計算ルール（BR-020）：</p>
 * <ul>
 *   <li>購入金額5000円以上: 送料無料</li>
 *   <li>沖縄県: 1700円</li>
 *   <li>その他: 800円</li>
 * </ul>
 */
@ApplicationScoped
public class DeliveryFeeService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryFeeService.class);

    /**
     * 通常の配送料金（800円）
     */
    private static final BigDecimal STANDARD_DELIVERY_FEE = new BigDecimal("800");
    
    /**
     * 沖縄県の配送料金（1700円）
     */
    private static final BigDecimal OKINAWA_DELIVERY_FEE = new BigDecimal("1700");
    
    /**
     * 送料無料の閾値（5000円）
     */
    private static final BigDecimal FREE_DELIVERY_THRESHOLD = new BigDecimal("5000");
    
    /**
     * 送料無料（0円）
     */
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * 配送料金を計算します（通常800円、沖縄県1700円、5000円以上は送料無料）
     * 
     * @param deliveryAddress 配送先住所
     * @param totalPrice 注文金額合計
     * @return 配送料金
     */
    public BigDecimal calculateDeliveryFee(String deliveryAddress, BigDecimal totalPrice) {
        logger.info("[ DeliveryFeeService#calculateDeliveryFee ] address={}, totalPrice={}", 
                deliveryAddress, totalPrice);

        // 購入金額が5000円以上の場合は送料無料
        if (totalPrice.compareTo(FREE_DELIVERY_THRESHOLD) < 0) {
            // 5000円未満の場合
            
            // 配送先住所が沖縄県の場合は1700円
            if (deliveryAddress != null && deliveryAddress.startsWith("沖縄県")) {
                logger.info("[ DeliveryFeeService ] 沖縄県への配送料金: {}", OKINAWA_DELIVERY_FEE);
                return OKINAWA_DELIVERY_FEE;
            }
            
            // 通常配送料金は800円
            logger.info("[ DeliveryFeeService ] 通常配送料金: {}", STANDARD_DELIVERY_FEE);
            return STANDARD_DELIVERY_FEE;
        }
        
        // 5000円以上の場合は送料無料
        logger.info("[ DeliveryFeeService ] 送料無料（購入金額{}円 >= {}円）", 
                totalPrice, FREE_DELIVERY_THRESHOLD);
        return ZERO;
    }

    /**
     * 配送先住所が沖縄県かどうかを判定します
     * 
     * @param deliveryAddress 配送先住所
     * @return 沖縄県の場合true、それ以外false
     */
    public boolean isOkinawa(String deliveryAddress) {
        return deliveryAddress != null && deliveryAddress.startsWith("沖縄県");
    }

    /**
     * 送料無料対象かどうかを判定します
     * 
     * @param totalPrice 注文金額合計
     * @return 送料無料の場合true、それ以外false
     */
    public boolean isFreeDelivery(BigDecimal totalPrice) {
        return totalPrice.compareTo(FREE_DELIVERY_THRESHOLD) < 0 == false;
    }
}

