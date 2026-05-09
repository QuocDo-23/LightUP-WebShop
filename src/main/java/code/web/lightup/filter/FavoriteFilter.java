package code.web.lightup.filter;

import code.web.lightup.dao.FavoriteDAO;
import code.web.lightup.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/*")
public class FavoriteFilter implements Filter {

    FavoriteDAO dao = new FavoriteDAO();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        try {


            String uri = req.getRequestURI();

            if (uri.contains(".css") || uri.contains(".js") ||
                    uri.contains(".png") || uri.contains(".jpg") ||
                    uri.contains(".jpeg") || uri.contains(".gif")) {

                chain.doFilter(request, response);
                return;
            }

            Integer userId = SessionUtil.getUserId(req);

            if (userId != null) {
                req.setAttribute("favoriteCount", dao.countFavorite(userId));
                req.setAttribute("favoriteList", dao.getFavoriteByUserId(userId));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        chain.doFilter(request, response);
    }
}