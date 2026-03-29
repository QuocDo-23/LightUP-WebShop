
package code.web.lightup.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/",
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
            "/cate_products",
            "/product-detail",
            "/CSS/",
            "/JS/",
            "/IMG/",
            "/images/"
    );

    private static final List<String> PROTECTED_URLS = Arrays.asList(
            "/profile.jsp",
            "/order.jsp",
            "/checkout.jsp",
            "/review"
    );


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

            if (session == null || session.getAttribute("userId") == null) {

                String redirectUrl = contextPath + "/login?redirect=" +
                        java.net.URLEncoder.encode(requestURI, StandardCharsets.UTF_8);
                httpResponse.sendRedirect(redirectUrl);
                return;
            }
        }

        chain.doFilter(request, response);
    }

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


    private boolean isProtectedResource(String path) {
        for (String protectedUrl : PROTECTED_URLS) {
            if (path.equals(protectedUrl) || path.startsWith(protectedUrl)) {
                return true;
            }
        }
        return false;
    }
}