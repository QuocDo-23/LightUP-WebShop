package code.web.lightup.controller.User;


import code.web.lightup.dao.OrderDAO;
import code.web.lightup.model.OrderItem;
import code.web.lightup.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import code.web.lightup.model.Order;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserOrderReviewServlet", urlPatterns = {"/order-review"})
public class OrderReviewServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }


        String orderIdStr = request.getParameter("id");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            response.sendRedirect("orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);


            Order order = orderDAO.getOrderById(orderId);


            if (order == null || order.getUserId() != user.getId()) {
                response.sendRedirect("orders");
                return;
            }


            if (!"delivered".equals(order.getStatus())) {
                request.setAttribute("errorMessage", "Chỉ có thể đánh giá đơn hàng đã giao thành công!");
                request.getRequestDispatcher("orders").forward(request, response);
                return;
            }


            if (orderDAO.hasOrderReview(orderId)) {
                request.setAttribute("errorMessage", "Đơn hàng này đã được đánh giá!");
                request.getRequestDispatcher("orders").forward(request, response);
                return;
            }


            List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(orderId);


            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);


            request.getRequestDispatcher("/views/user/order_review.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("orders");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {

            int orderId = Integer.parseInt(request.getParameter("orderId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String reviewText = request.getParameter("reviewText");

            // Validate
            if (rating < 1 || rating > 5) {
                request.setAttribute("errorMessage", "Đánh giá không hợp lệ!");
                doGet(request, response);
                return;
            }


            Order order = orderDAO.getOrderById(orderId);
            if (order == null || order.getUserId() != user.getId()) {
                response.sendRedirect("orders");
                return;
            }


            if (!"delivered".equals(order.getStatus())) {
                request.setAttribute("errorMessage", "Chỉ có thể đánh giá đơn hàng đã giao thành công!");
                doGet(request, response);
                return;
            }


            if (orderDAO.hasOrderReview(orderId)) {
                request.setAttribute("errorMessage", "Đơn hàng này đã được đánh giá!");
                doGet(request, response);
                return;
            }


            boolean success = orderDAO.addOrderReview(orderId, user.getId(), rating, reviewText);

            if (success) {

                session.setAttribute("successMessage", "Cảm ơn bạn đã đánh giá đơn hàng!");
                response.sendRedirect("orders");
            } else {

                request.setAttribute("errorMessage", "Có lỗi xảy ra khi gửi đánh giá. Vui lòng thử lại!");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            doGet(request, response);
        }
    }
}