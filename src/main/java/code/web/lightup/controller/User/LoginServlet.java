package code.web.lightup.controller.User;

import code.web.lightup.model.User;
import code.web.lightup.service.UserService;
import code.web.lightup.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email");
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu");
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
            return;
        }

        Optional<User> userOpt = userService.login(email, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            SessionUtil.setUserSession(request, user);

            String contextPath = request.getContextPath();
            String redirectUrl;

            if ("payment".equals(redirect)) {
                redirectUrl = contextPath + "/payment";
            } else if ("cart".equals(redirect)) {
                redirectUrl = contextPath + "/cart";
            } else if (SessionUtil.isAdmin(request)) {
                redirectUrl = contextPath + "/admin/dashboard";
            } else {
                redirectUrl = contextPath + "/";
            }
            response.sendRedirect(redirectUrl);
        } else {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
        }
    }
}