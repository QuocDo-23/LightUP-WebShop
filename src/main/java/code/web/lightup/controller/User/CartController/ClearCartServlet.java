package code.web.lightup.controller.User.CartController;

import code.web.lightup.model.Cart.Cart;
import code.web.lightup.service.CartService;
import code.web.lightup.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/clear-cart")
public class ClearCartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Cart cart =
                (Cart) session.getAttribute("cart");

        if (cart != null) {

            cart.removeAll();

            if (SessionUtil.isLoggedIn(request)) {

                Integer userId =
                        SessionUtil.getUserId(request);

                cartService.addCartToDb(
                        userId,
                        cart
                );
            }
        }

        response.sendRedirect(
                request.getContextPath() + "/cart"
        );
    }
}