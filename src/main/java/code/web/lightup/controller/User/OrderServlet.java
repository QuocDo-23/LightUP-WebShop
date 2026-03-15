package code.web.lightup.controller.User;


import code.web.lightup.dao.OrderDAO;
import code.web.lightup.dao.PaymentDAO;
import code.web.lightup.model.OrderItem;
import code.web.lightup.model.Payment;
import code.web.lightup.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import code.web.lightup.model.Order;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderServlet", urlPatterns = {"/orders"})
public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        paymentDAO = new PaymentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        if (user == null) {
            response.sendRedirect("/views/user/login.jsp");
            return;
        }

        List<Order> orders = orderDAO.getOrdersByUserId(user.getId());


        Map<Integer, List<OrderItem>> orderItemsMap = new HashMap<>();
        Map<Integer, Payment> paymentMap = new HashMap<>();

        for (Order order : orders) {
            List<OrderItem> items = orderDAO.getOrderItemsByOrderId(order.getId());
            orderItemsMap.put(order.getId(), items);

            Payment payment = paymentDAO.getPaymentByOrderId(order.getId());
            if (payment != null) {
                paymentMap.put(order.getId(), payment);
            }
        }
        for (Order order : orders) {
            boolean hasReview = orderDAO.hasOrderReview(order.getId());
            order.setHasReview(hasReview);
        }

        request.setAttribute("orders", orders);
        request.setAttribute("orderItemsMap", orderItemsMap);
        request.setAttribute("paymentMap", paymentMap);
        request.setAttribute("activeTab", "orders");;

        request.getRequestDispatcher("/views/user/order.jsp").forward(request, response);
    }
}