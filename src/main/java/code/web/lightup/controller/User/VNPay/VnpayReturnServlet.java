package code.web.lightup.controller.User.VNPay;

import code.web.lightup.model.Order;
import code.web.lightup.service.OrderService;
import code.web.lightup.service.PaymentService;
import code.web.lightup.util.VnpayConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/vnpay-return")
public class VnpayReturnServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            if (!VnpayConstants.validateSignature(request)) {
                request.setAttribute("error", "Chữ ký không hợp lệ");
                request.getRequestDispatcher("/views/user/order-fail.jsp").forward(request, response);
                return;
            }

            String responseCode = request.getParameter("vnp_ResponseCode");
            String txnRef = request.getParameter("vnp_TxnRef");
            String transactionNo = request.getParameter("vnp_TransactionNo");

            if ("00".equals(responseCode)) {
                int orderId = Integer.parseInt(txnRef);

                orderService.updateOrderStatus(orderId, "processing");
                paymentService.updatePaymentStatusByOrderId(orderId, "success");

                response.sendRedirect(request.getContextPath() + "/order-success?orderId=" + txnRef);
            } else {
                int orderId = Integer.parseInt(txnRef);

                orderService.updateOrderStatus(orderId, "pending");
                paymentService.updatePaymentStatusByOrderId(orderId, "failed");

                response.sendRedirect(request.getContextPath() + "/order-fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/order-fail");
        }
    }
}