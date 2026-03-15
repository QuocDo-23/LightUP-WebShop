package code.web.lightup.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter để kiểm tra authentication
 * Chỉ cho phép truy cập các trang protected nếu đã đăng nhập
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    // Các trang không cần authentication
    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/index.jsp",
            "/products.jsp",
            "/product-detail.jsp",
            "/about.jsp",
            "/contact.jsp",
            "/news.jsp",
            "/cart-detail.jsp",
            "/login.jsp",
            "/register.jsp",
            "/forgot-password.jsp",
            "/create-password.jsp",
            "/login",
            "/register",
            "/forgot-password",
            "/products",
            "/CSS/",
            "/JS/",
            "/IMG/",
            "/images/"
    );

    // Các trang cần authentication
    private static final List<String> PROTECTED_URLS = Arrays.asList(
            "/profile.jsp",
            "/order.jsp",
            "/checkout.jsp",
            "/review"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        if (isPublicResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        if (isProtectedResource(path)) {
            HttpSession session = httpRequest.getSession(false);

            if (session == null || session.getAttribute("user") == null) {
                String redirectUrl = contextPath + "/login.jsp?redirect=" +
                        java.net.URLEncoder.encode(requestURI, "UTF-8");
                httpResponse.sendRedirect(redirectUrl);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    /**
     * Kiểm tra xem resource có phải public không
     */
    private boolean isPublicResource(String path) {
        if (PUBLIC_URLS.contains(path)) {
            return true;
        }

        for (String publicUrl : PUBLIC_URLS) {
            if (publicUrl.endsWith("/") && path.startsWith(publicUrl)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Kiểm tra xem resource có cần authentication không
     */
    private boolean isProtectedResource(String path) {
        for (String protectedUrl : PROTECTED_URLS) {
            if (path.equals(protectedUrl) || path.startsWith(protectedUrl)) {
                return true;
            }
        }
        return false;
    }
}