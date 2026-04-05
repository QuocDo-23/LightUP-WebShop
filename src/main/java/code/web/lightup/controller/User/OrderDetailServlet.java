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
import java.util.List;

@WebServlet(name = "UserOrderDetailServlet", urlPatterns = {"/order_detail"})
public class OrderDetailServlet extends HttpServlet {

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
            response.sendRedirect("login.jsp");
            return;
        }


        String orderIdParam = request.getParameter("id");

        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Không tìm thấy mã đơn hàng!");
            response.sendRedirect("orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);


            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                session.setAttribute("errorMessage", "Đơn hàng không tồn tại!");
                response.sendRedirect("orders");
                return;
            }


            if (order.getUserId() != user.getId()) {
                session.setAttribute("errorMessage", "Bạn không có quyền xem đơn hàng này!");
                response.sendRedirect("orders");
                return;
            }


            List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(orderId);


            Payment payment = paymentDAO.getPaymentByOrderId(orderId);


            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);
            request.setAttribute("payment", payment);


            request.getRequestDispatcher("/views/user/order_detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Mã đơn hàng không hợp lệ!");
            response.sendRedirect("orders");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("orders");
        }

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String house = request.getParameter("house");
            String commune = request.getParameter("commune");
            String district = request.getParameter("district");
            String detail = request.getParameter("detail");
            if (house == null || house.trim().isEmpty()
                    || commune == null || commune.trim().isEmpty()
                    || district == null || district.trim().isEmpty()) {

                response.sendRedirect("order_detail?id=" + orderId + "&error=empty_address");
                return;
            }

            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                response.sendRedirect("orders");
                return;
            }

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null || order.getUserId() != user.getId()) {
                response.sendRedirect("orders");
                return;
            }



            if (!order.getStatus().equals("pending") && !order.getStatus().equals("processing")) {
                response.sendRedirect("order_detail?id=" + orderId + "&error=not_allowed");
                return;
            }

            boolean updated = orderDAO.updateShippingAddress(orderId, house, commune, district, detail);

            if (updated) {
                response.sendRedirect("order_detail?id=" + orderId + "&success=updated");
            } else {
                response.sendRedirect("order_detail?id=" + orderId + "&error=fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("orders");
        }
    }
}