package code.web.lightup.controller.User;

import code.web.lightup.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/forgot_password")
public class ForgotPasswordServlet extends HttpServlet {

    private UserService userService;

    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/user/forgot_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email của bạn!");
            request.getRequestDispatcher("/views/user/forgot_password.jsp").forward(request, response);
            return;
        }

        boolean exists = userService.emailExists(email);

        if(!exists){
            request.setAttribute("error", "Email không tồn tại!");
        }
        else{
            request.setAttribute("massage", "Yêu cầu của bạn đã gửi thành công! Vui lòng kiểm tra email.");
        }

        request.getRequestDispatcher("/views/user/forgot_password.jsp")
                .forward(request, response);

    }
}