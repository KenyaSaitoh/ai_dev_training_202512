package pro.kensait.berrybooks.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.web.customer.CustomerBean;
import java.io.IOException;

/**
 * 認証フィルター
 * 
 * <p>未ログインユーザーの保護ページアクセスを制限します。</p>
 * 
 * <p>公開ページ（認証不要）：</p>
 * <ul>
 *   <li>index.xhtml - ログイン画面</li>
 *   <li>customerInput.xhtml - 新規登録画面</li>
 *   <li>customerOutput.xhtml - 登録完了画面</li>
 * </ul>
 * 
 * <p>注意: このフィルターはweb.xmlで定義されているため、
 * @WebFilterアノテーションは使用しません（二重定義を防ぐため）。</p>
 */
public class AuthenticationFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    
    /**
     * 公開ページ（認証不要）のパス
     */
    private static final String[] PUBLIC_PAGES = {
        "/index.xhtml",
        "/customerInput.xhtml",
        "/customerOutput.xhtml"
    };
    
    /**
     * 静的リソースのパス
     */
    private static final String RESOURCES_PATH = "/resources/";
    
    /**
     * Faces Servletのパス
     */
    private static final String FACES_SERVLET_PATH = "/jakarta.faces.resource/";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("AuthenticationFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        logger.debug("AuthenticationFilter: path={}", path);
        
        // 静的リソースはスキップ
        if (path.startsWith(RESOURCES_PATH) || path.startsWith(FACES_SERVLET_PATH)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 公開ページはスキップ
        if (isPublicPage(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // セッションからCustomerBeanを取得
        HttpSession session = httpRequest.getSession(false);
        CustomerBean customerBean = null;
        
        if (session != null) {
            customerBean = (CustomerBean) session.getAttribute("customerBean");
            logger.info("[ AuthenticationFilter#doFilter ] Session exists: sessionId={}, customerBean={}", session.getId(), customerBean);
        } else {
            logger.info("[ AuthenticationFilter#doFilter ] No session exists");
        }
        
        // ログイン済みかチェック
        if (customerBean != null && customerBean.getCustomer() != null) {
            // ログイン済み: アクセスを許可
            logger.info("[ AuthenticationFilter#doFilter ] Access granted to protected page: {}, customerId={}", path, customerBean.getCustomer().getCustomerId());
            chain.doFilter(request, response);
        } else {
            // 未ログイン: ログイン画面にリダイレクト
            logger.info("[ AuthenticationFilter#doFilter ] Unauthorized access to protected page: {}", path);
            httpResponse.sendRedirect(contextPath + "/index.xhtml");
        }
    }
    
    @Override
    public void destroy() {
        logger.info("AuthenticationFilter destroyed");
    }
    
    /**
     * 指定されたパスが公開ページかどうかを判定します
     * 
     * @param path リクエストパス
     * @return 公開ページの場合はtrue、それ以外はfalse
     */
    private boolean isPublicPage(String path) {
        for (String publicPage : PUBLIC_PAGES) {
            if (path.equals(publicPage)) {
                return true;
            }
        }
        return false;
    }
}

