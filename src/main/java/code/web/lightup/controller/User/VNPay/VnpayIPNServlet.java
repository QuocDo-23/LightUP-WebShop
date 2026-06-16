package code.web.lightup.controller.User.VNPay;

import code.web.lightup.util.VnpayConstants;
import code.web.lightup.service.OrderService;
import code.web.lightup.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/vnpay-ipn")
public class VnpayIPNServlet extends HttpServlet {

    private OrderService orderService = new OrderService();
    private PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Kiểm tra chữ ký
            if (!VnpayConstants.validateSignature(request)) {
                out.print("{\"RspCode\":\"97\",\"Message\":\"Invalid checksum\"}");
                return;
            }

            String responseCode = request.getParameter("vnp_ResponseCode");
            String txnRef = request.getParameter("vnp_TxnRef");
            String transactionNo = request.getParameter("vnp_TransactionNo");
            String amount = request.getParameter("vnp_Amount");

            if ("00".equals(responseCode)) {
                orderService.updateOrderStatus(Integer.parseInt(txnRef), "processing");
                paymentService.updatePaymentStatusByOrderId(Integer.parseInt(txnRef), "success");

                out.print("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
            } else {
                orderService.updateOrderStatus(Integer.parseInt(txnRef), "failed");
                out.print("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"RspCode\":\"99\",\"Message\":\"Unknown error\"}");
        }
    }
}