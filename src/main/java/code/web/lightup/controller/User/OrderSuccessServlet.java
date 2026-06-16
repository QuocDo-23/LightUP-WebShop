package code.web.lightup.controller.User;


import code.web.lightup.model.Cart.Cart;
import code.web.lightup.model.OrderItem;
import code.web.lightup.model.Payment;
import code.web.lightup.service.CartService;
import code.web.lightup.service.OrderService;
import code.web.lightup.service.PaymentService;
import code.web.lightup.util.SessionUtil;
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
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderService();
        paymentDAO = new PaymentService();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Integer orderId = resolveOrderId(request, session);

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

        deductPurchasedItemsFromCart(request, session, orderItems, orderId);

        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        request.setAttribute("payment", payment);
        request.setAttribute("currencyFormat", currencyFormat);

        session.removeAttribute("orderId");

        request.getRequestDispatcher("/views/user/order-success.jsp").forward(request, response);
    }
    private Integer resolveOrderId(HttpServletRequest request, HttpSession session) {
        String orderIdParam = request.getParameter("orderId");

        if (orderIdParam != null && !orderIdParam.isBlank()) {
            try {
                return Integer.parseInt(orderIdParam);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        Object sessionOrderId = session.getAttribute("orderId");

        if (sessionOrderId instanceof Integer) {
            return (Integer) sessionOrderId;
        }

        if (sessionOrderId instanceof String) {
            try {
                return Integer.parseInt((String) sessionOrderId);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }

    private void deductPurchasedItemsFromCart(HttpServletRequest request, HttpSession session,
                                              List<OrderItem> orderItems, int orderId) {
        String adjustedKey = "cartAdjustedForOrder_" + orderId;

        if (session.getAttribute(adjustedKey) != null) {
            return;
        }

        Cart fullCart = (Cart) session.getAttribute("cart");

        if (fullCart == null || orderItems == null || orderItems.isEmpty()) {
            session.setAttribute(adjustedKey, true);
            return;
        }

        for (OrderItem orderItem : orderItems) {
            int productId = orderItem.getProductId();
            int purchasedQty = orderItem.getQuantity();

            int currentQty = fullCart.getQuantityOfProduct(productId);
            int remainingQty = currentQty - purchasedQty;

            if (remainingQty <= 0) {
                fullCart.removeItem(productId);
            } else {
                fullCart.updateItem(productId, remainingQty);
            }
        }

        if (fullCart.getTotalItems() <= 0) {
            session.removeAttribute("cart");
        } else {
            session.setAttribute("cart", fullCart);
        }

        session.removeAttribute("checkoutCart");
        session.removeAttribute("checkoutType");

        if (SessionUtil.isLoggedIn(request)) {
            Integer userId = SessionUtil.getUserId(request);
            Cart updatedCart = (Cart) session.getAttribute("cart");

            if (updatedCart == null) {
                updatedCart = new Cart();
            }

            cartService.addCartToDb(userId, updatedCart);
        }

        session.setAttribute(adjustedKey, true);
    }
}