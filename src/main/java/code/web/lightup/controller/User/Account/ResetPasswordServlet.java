package code.web.lightup.controller.User.Account;


import code.web.lightup.model.OTPService;
import code.web.lightup.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private UserService userService;
    private OTPService otpService;

    @Override
    public void init() {
        userService = new UserService();
        otpService = new OTPService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Boolean otpVerified = (Boolean) request.getSession().getAttribute("otpVerified");
        String email = (String) request.getSession().getAttribute("resetEmail");

        if (otpVerified == null || !otpVerified || email == null) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        request.getRequestDispatcher("/views/user/reset_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        Boolean otpVerified = (Boolean) request.getSession().getAttribute("otpVerified");
        String email = (String) request.getSession().getAttribute("resetEmail");

        if (otpVerified == null || !otpVerified || email == null) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu mới");
            request.getRequestDispatcher("/views/user/reset_password.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
            request.getRequestDispatcher("/views/user/reset_password.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp");
            request.getRequestDispatcher("/views/user/reset_password.jsp").forward(request, response);
            return;
        }

        boolean updated = userService.updatePassword(email, newPassword);

        if (updated) {
            otpService.removeOTP(email);

            HttpSession session = request.getSession();
            session.removeAttribute("resetEmail");
            session.removeAttribute("otpVerified");

            response.sendRedirect(
                    request.getContextPath() + "/login?success=password_reset"
            );
        } else {
            request.setAttribute("error", "Không thể đặt lại mật khẩu. Vui lòng thử lại.");
            request.getRequestDispatcher("/views/user/reset_password.jsp").forward(request, response);
        }
    }
}