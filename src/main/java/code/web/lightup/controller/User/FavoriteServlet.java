package code.web.lightup.controller.User;
import code.web.lightup.dao.FavoriteDAO;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import code.web.lightup.util.SessionUtil;

@WebServlet("/favorite")
public class FavoriteServlet extends HttpServlet {

    FavoriteDAO dao = new FavoriteDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        try {
            Integer userId = SessionUtil.getUserId(request);

            if (userId == null) {
                response.getWriter().write("login");
                return;
            }

            String pid = request.getParameter("productId");
            System.out.println("PID GUI LEN = " + pid);


            if (pid == null || pid.isEmpty()) {
                response.getWriter().write("error");
                return;
            }

            int productId = Integer.parseInt(pid);

            boolean favorite;

            if (dao.isFavorite(userId, productId)) {
                dao.removeFavorite(userId, productId);
                favorite = false;
            } else {
                dao.addFavorite(userId, productId);
                favorite = true;
            }

            response.getWriter().write(String.valueOf(favorite));

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("error");
        }
    }
}
