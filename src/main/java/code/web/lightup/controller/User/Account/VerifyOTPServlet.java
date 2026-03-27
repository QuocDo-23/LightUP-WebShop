package code.web.lightup.controller.User.Account;

import code.web.lightup.model.OTPService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/verify-otp")
public class VerifyOTPServlet extends HttpServlet {

    private OTPService otpService;

    @Override
    public void init() {
        otpService = new OTPService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = (String) request.getSession().getAttribute("resetEmail");
        if (email == null) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        request.getRequestDispatcher("/views/user/verify_otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String otp = request.getParameter("otp");
        String email = (String) request.getSession().getAttribute("resetEmail");

        if (email == null) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        if (otp == null || otp.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mã OTP");
            request.getRequestDispatcher("/views/user/verify_otp.jsp").forward(request, response);
            return;
        }

        boolean isValid = otpService.verifyOTP(email, otp);

        if (isValid) {
            request.getSession().setAttribute("otpVerified", true);

            response.sendRedirect(request.getContextPath() + "/reset-password");
        } else {
            request.setAttribute("error", "Mã OTP không đúng hoặc đã hết hạn");
            request.getRequestDispatcher("/views/user/verify_otp.jsp").forward(request, response);
        }
    }
}