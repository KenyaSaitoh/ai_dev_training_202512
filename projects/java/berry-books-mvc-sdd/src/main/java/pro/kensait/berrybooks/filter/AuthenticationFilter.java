package pro.kensait.berrybooks.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.kensait.berrybooks.bean.session.CustomerBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * AuthenticationFilter - Servlet filter for authentication
 * 
 * Protects pages by checking if user is logged in.
 * Public pages (login, registration) are accessible without authentication.
 */
@WebFilter(urlPatterns = {"*.xhtml"})
public class AuthenticationFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    
    // List of public pages that don't require authentication
    private static final List<String> PUBLIC_PAGES = Arrays.asList(
            "index.xhtml",
            "customerInput.xhtml",
            "customerOutput.xhtml"
    );
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("[ AuthenticationFilter#init ] Authentication filter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get request URI
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String page = requestURI.substring(contextPath.length());
        
        // Remove leading slash if present
        if (page.startsWith("/")) {
            page = page.substring(1);
        }
        
        logger.debug("[ AuthenticationFilter#doFilter ] Checking authentication for: {}", page);
        
        // Check if page is public
        if (isPublicPage(page)) {
            logger.debug("[ AuthenticationFilter#doFilter ] Public page, allowing access: {}", page);
            chain.doFilter(request, response);
            return;
        }
        
        // Check if page is a resource (CSS, JS, images)
        if (isResourceRequest(page)) {
            logger.debug("[ AuthenticationFilter#doFilter ] Resource request, allowing access: {}", page);
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        HttpSession session = httpRequest.getSession(false);
        CustomerBean customerBean = null;
        
        if (session != null) {
            customerBean = (CustomerBean) session.getAttribute("customerBean");
        }
        
        if (customerBean != null && customerBean.isLoggedIn()) {
            // User is logged in, allow access
            logger.debug("[ AuthenticationFilter#doFilter ] User authenticated, allowing access: {}", page);
            chain.doFilter(request, response);
            
        } else {
            // User is not logged in, redirect to login page
            logger.info("[ AuthenticationFilter#doFilter ] User not authenticated, redirecting to login: {}", page);
            
            // Store original request URL for redirect after login (optional)
            session = httpRequest.getSession(true);
            session.setAttribute("originalRequest", requestURI);
            
            httpResponse.sendRedirect(contextPath + "/index.xhtml");
        }
    }
    
    @Override
    public void destroy() {
        logger.info("[ AuthenticationFilter#destroy ] Authentication filter destroyed");
    }
    
    /**
     * Check if the page is public (doesn't require authentication)
     * 
     * @param page the page name
     * @return true if public, false otherwise
     */
    private boolean isPublicPage(String page) {
        return PUBLIC_PAGES.stream().anyMatch(page::endsWith);
    }
    
    /**
     * Check if the request is for a resource (CSS, JS, images, etc.)
     * 
     * @param page the page name
     * @return true if resource request, false otherwise
     */
    private boolean isResourceRequest(String page) {
        return page.startsWith("jakarta.faces.resource/") ||
               page.startsWith("resources/") ||
               page.contains("/resources/") ||
               page.endsWith(".css") ||
               page.endsWith(".js") ||
               page.endsWith(".jpg") ||
               page.endsWith(".jpeg") ||
               page.endsWith(".png") ||
               page.endsWith(".gif") ||
               page.endsWith(".ico");
    }
}
