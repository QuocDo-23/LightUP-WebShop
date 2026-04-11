package code.web.lightup.controller.Admin.Order;

import code.web.lightup.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "OrderStatusUpdateServlet", urlPatterns = {"/admin/orders/updateStatus"})
public class OrderStatusUpdateServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        updateOrderStatus(request, response);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String newStatus = request.getParameter("status");

            boolean success = orderService.updateOrderStatus(orderId, newStatus);

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Cập nhật trạng thái thành công\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Cập nhật thất bại\"}");
            }

        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi: " + e.getMessage() + "\"}");
        }
    }
}
