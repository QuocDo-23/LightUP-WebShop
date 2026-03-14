package code.web.lightup.controller.User;


import code.web.lightup.model.OrderItem;
import code.web.lightup.model.Payment;
import code.web.lightup.service.OrderService;
import code.web.lightup.service.PaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import code.web.lightup.model.Order;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@WebServlet("/order-success")
public class OrderSuccessServlet extends HttpServlet {

    private OrderService orderDAO;
    private PaymentService paymentDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderService();
        paymentDAO = new PaymentService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();


        Integer orderId = (Integer) session.getAttribute("orderId");

        if (orderId == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }


        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(orderId);

        Payment payment = paymentDAO.getPaymentByOrderId(orderId);

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        request.setAttribute("payment", payment);
        request.setAttribute("currencyFormat", currencyFormat);

        session.removeAttribute("orderId");

        request.getRequestDispatcher("/views/user/order-success.jsp").forward(request, response);
    }
}