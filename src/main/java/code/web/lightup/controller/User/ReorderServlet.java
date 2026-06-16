package code.web.lightup.controller.User;

import code.web.lightup.model.OrderItem;
import code.web.lightup.model.User;
import code.web.lightup.service.CartService;
import code.web.lightup.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ReorderServlet", urlPatterns = {"/reorder"})
public class ReorderServlet extends HttpServlet {

    private OrderService orderService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        cartService  = new CartService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        int cartId = cartService.getOrCreateCartId(user.getId());

        List<OrderItem> items = orderService.getOrderItemsByOrderId(orderId);
        for (OrderItem item : items) {
            cartService.upsertItem(cartId, item.getProductId(), item.getQuantity(), item.getPrice());
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}