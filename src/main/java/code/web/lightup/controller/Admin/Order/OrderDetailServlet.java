package code.web.lightup.controller.Admin.Order;

import code.web.lightup.model.Order;
import code.web.lightup.service.OrderService;
import code.web.lightup.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "OrderDetailServlet", urlPatterns = {"/admin/orders/detail"})
public class OrderDetailServlet extends HttpServlet {

    private OrderService orderService;
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        viewOrderDetail(request, response);
    }
    
    private void viewOrderDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));

            Order order = orderService.getOrderById(orderId);

            if (order == null) {
                request.setAttribute("errorMessage", "Không tìm thấy đơn hàng!");
                request.getRequestDispatcher("/admin/orders").forward(request, response);
                return;
            }

            order.setItems(orderService.getOrderItemsByOrderId(orderId));

            boolean hasReview = reviewService.hasOrderReview(orderId);

            request.setAttribute("order", order);
            request.setAttribute("hasReview", hasReview);
            request.setAttribute("currentPage", "orders");

            request.getRequestDispatcher("/views/admin/Orders/order-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Mã đơn hàng không hợp lệ!");
            request.getRequestDispatcher("/admin/orders").forward(request, response);
        }
    }
}
