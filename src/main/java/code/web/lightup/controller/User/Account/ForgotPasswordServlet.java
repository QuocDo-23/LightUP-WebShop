package code.web.lightup.controller.User.Account;


import code.web.lightup.model.OTPService;
import code.web.lightup.model.User;
import code.web.lightup.service.EmailService;
import code.web.lightup.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private UserService userService;
    private EmailService emailService;
    private OTPService otpService;

    @Override
    public void init() {
        userService = new UserService();
        emailService = new EmailService();
        otpService = new OTPService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/user/forgot_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.isEmpty()) {
            request.setAttribute("error", "Email không hợp lệ");
            request.getRequestDispatcher("/views/user/forgot_password.jsp").forward(request, response);
            return;
        }

        Optional<User> userOpt = userService.getUserLoginInfo(email);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            if("google".equalsIgnoreCase(user.getAuthProvider())) {
                request.setAttribute("error", "Tài khoản này đăng ký qua Google. Vui lòng đăng nhập bằng Google.");
                request.getRequestDispatcher("/views/user/forgot_password.jsp").forward(request, response);
                return;
            }
            String otp = emailService.generateOTP();

            otpService.saveOTP(email, otp);

            boolean emailSent = emailService.sendOTPEmail(email, otp);
            if (emailSent) {
                request.getSession().setAttribute("resetEmail", email);
                response.sendRedirect(request.getContextPath() + "/verify-otp");
                return;
            } else {
                request.setAttribute("error", "Không thể gửi email. Vui lòng thử lại sau.");
                request.getRequestDispatcher("/views/user/forgot_password.jsp").forward(request, response);
                return;
            }

        }
        request.getSession().setAttribute("resetEmail",email);
        request.setAttribute("message", "Vui lòng kiểm tra email của bạn để đặt lại mật khẩu.");
        response.sendRedirect(request.getContextPath() + "/verify-otp");

    }
}