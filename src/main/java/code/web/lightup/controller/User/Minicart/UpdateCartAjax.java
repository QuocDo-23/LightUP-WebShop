package code.web.lightup.controller.User.Minicart;

import code.web.lightup.model.Cart.Cart;
import code.web.lightup.model.ProductWithDetails;
import code.web.lightup.service.CartService;
import code.web.lightup.service.ProductService;
import code.web.lightup.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;


@WebServlet(name = "UpdateCartAjax", value = "/update-mini")
public class UpdateCartAjax extends HttpServlet {

    private CartService cartService;
    private ProductService productService;

    @Override
    public void init() {
        cartService = new CartService();
        productService = new ProductService();
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
                int qty       = Integer.parseInt(request.getParameter("qty"));

                if (qty <= 0) {
                    cart.removeItem(productId);
                } else {
                    Optional<ProductWithDetails> product = productService.getProductById(productId);
                    if (product.isPresent()) {
                        int available = product.get().getInventoryQuantity();
                        if (qty > available) {
                            cart.updateItem(productId, available);
                            request.setAttribute("stockWarning",
                                    "Chỉ còn " + available + " sản phẩm trong kho!");
                        } else {
                            cart.updateItem(productId, qty);
                        }
                    } else {
                        cart.updateItem(productId, qty);
                    }
                }
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