package code.web.lightup.controller.User.CartController;

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
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "UpdateCart", value = "/update")
public class UpdateCart extends HttpServlet {
    private static CartService cartService;
    private static ProductService productService;

    public UpdateCart() {
        cartService = new CartService();
        productService = new ProductService();
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                            session.setAttribute("errorMsg",
                                    "Chỉ còn " + available + " sản phẩm trong kho!");
                            cart.updateItem(productId, available);
                        } else {
                            cart.updateItem(productId, qty);
                        }
                    } else {
                        cart.updateItem(productId, qty);
                    }
                }

                if (SessionUtil.isLoggedIn(request)) {
                    Integer userId = SessionUtil.getUserId(request);
                    cartService.addCartToDb(userId, cart);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String from = request.getParameter("from");
        if ("mini".equals(from)) {
            response.sendRedirect(request.getHeader("Referer"));
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}