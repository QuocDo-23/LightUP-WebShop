package code.web.lightup.controller.User.VNPay;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String responseCode = request.getParameter("vnp_ResponseCode");
        String orderId      = request.getParameter("vnp_TxnRef");
        String amount       = request.getParameter("vnp_Amount");

        if ("00".equals(responseCode)) {
            request.setAttribute("vnpaySuccess", true);
            request.setAttribute("orderId", orderId);
            response.sendRedirect(request.getContextPath() + "/order-success");
        } else {
            request.setAttribute("error", "Thanh toán VNPay thất bại. Mã lỗi: " + responseCode);
            response.sendRedirect(request.getContextPath() + "/payment");
        }
    }
}