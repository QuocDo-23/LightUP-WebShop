package code.web.lightup.controller.User;

import code.web.lightup.dao.OrderDAO;
import code.web.lightup.model.Order;
import code.web.lightup.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "OrderCancelServlet", urlPatterns = {"/cancel-order"})
public class OrderCancelServlet extends HttpServlet {

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
                session.setAttribute("errorMessage", "Bạn không có quyền hủy đơn hàng này!");
                response.sendRedirect("orders");
                return;
            }

            if (!order.getStatus().equals("pending") && !order.getStatus().equals("confirmed")) {
                session.setAttribute("errorMessage", "Không thể hủy đơn hàng ở trạng thái này!");
                response.sendRedirect("orders");
                return;
            }

            boolean updated = orderDAO.updateOrderStatus(orderId, "cancelled");

            if (updated) {
                session.setAttribute("successMessage", "Hủy đơn hàng thành công!");
            } else {
                session.setAttribute("errorMessage", "Hủy đơn hàng thất bại, vui lòng thử lại!");
            }

            response.sendRedirect("orders");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Mã đơn hàng không hợp lệ!");
            response.sendRedirect("orders");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("orders");
        }
    }
}