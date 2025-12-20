package pro.kensait.berrybooks.service.delivery;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.util.AddressUtil;
import java.math.BigDecimal;

/**
 * 配送料金計算のビジネスロジッククラス
 * 
 * <p>配送先住所と注文金額に基づいて配送料金を計算します。</p>
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
     * 送料無料の閾値（5000円）
     */
    private static final BigDecimal FREE_DELIVERY_THRESHOLD = new BigDecimal("5000");
    
    /**
     * 沖縄県の配送料金（1700円）
     */
    private static final BigDecimal OKINAWA_DELIVERY_FEE = new BigDecimal("1700");
    
    /**
     * 通常の配送料金（800円）
     */
    private static final BigDecimal STANDARD_DELIVERY_FEE = new BigDecimal("800");
    
    /**
     * 送料無料（0円）
     */
    private static final BigDecimal FREE_DELIVERY_FEE = BigDecimal.ZERO;
    
    /**
     * 配送料金を計算します
     * 
     * @param totalPrice 注文金額合計
     * @param deliveryAddress 配送先住所
     * @return 配送料金
     */
    public BigDecimal calculateDeliveryFee(BigDecimal totalPrice, String deliveryAddress) {
        logger.info("[ DeliveryFeeService#calculateDeliveryFee ] totalPrice={}, deliveryAddress={}", 
                    totalPrice, deliveryAddress);
        
        // 購入金額5000円以上は送料無料
        if (totalPrice.compareTo(FREE_DELIVERY_THRESHOLD) >= 0) {
            logger.debug("Free delivery: totalPrice >= 5000");
            return FREE_DELIVERY_FEE;
        }
        
        // 沖縄県判定
        if (AddressUtil.isOkinawa(deliveryAddress)) {
            logger.debug("Okinawa delivery: 1700 yen");
            return OKINAWA_DELIVERY_FEE;
        }
        
        // その他の地域
        logger.debug("Standard delivery: 800 yen");
        return STANDARD_DELIVERY_FEE;
    }
}

