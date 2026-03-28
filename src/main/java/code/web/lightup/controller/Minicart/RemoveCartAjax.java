package code.web.lightup.controller.User.CartController;

import code.web.lightup.model.Cart.Cart;
import code.web.lightup.service.CartService;
import code.web.lightup.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * POST /remove-mini
 * Xoá sản phẩm khỏi giỏ, forward fragment để AJAX refresh mini cart.
 */
@WebServlet(name = "RemoveCartAjax", value = "/remove-mini")
public class RemoveCartAjax extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() {
        cartService = new CartService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                cart.removeItem(productId);
                session.setAttribute("cart", cart);

                if (SessionUtil.isLoggedIn(request)) {
                    Integer userId = SessionUtil.getUserId(request);
                    cartService.addCartToDb(userId, cart);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/views/layout/cart-mini-fragment.jsp")
                .forward(request, response);
    }
}