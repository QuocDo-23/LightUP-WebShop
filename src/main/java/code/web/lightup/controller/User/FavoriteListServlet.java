package code.web.lightup.controller.User;

import code.web.lightup.dao.FavoriteDAO;
import code.web.lightup.model.Product;
import code.web.lightup.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/favorite-list")
public class FavoriteListServlet extends HttpServlet {

    FavoriteDAO dao = new FavoriteDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = SessionUtil.getUserId(request);

        // chưa login
        if (userId == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        // lấy danh sách yêu thích
        List<Product> favoriteProducts =
                dao.getFavoriteByUserId(userId);

        // gửi qua jsp
        request.setAttribute(
                "favoriteProducts",
                favoriteProducts
        );

        // mở page
        request.getRequestDispatcher(
                "/views/user/favorite-list.jsp"
        ).forward(request, response);
    }
}