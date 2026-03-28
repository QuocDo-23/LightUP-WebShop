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

import java.io.IOException;
import java.util.Optional;

/**
 * GET /add-cart-ajax?pID=...&quantity=...
 *
 * Luồng:
 *  1. Thêm sản phẩm vào Cart trong session
 *  2. Đặt cart vào request attribute
 *  3. Forward sang cart-mini-fragment.jsp -> trả HTML thô
 *  4. JS nhét HTML vào #mini-cart-body, mở panel, tự đóng sau 3 giây
 */
@WebServlet(name = "AddCartAjax", value = "/add-cart-ajax")
public class AddCartAjax extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() {
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            int pID      = Integer.parseInt(request.getParameter("pID"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) cart = new Cart();

            ProductService productService = new ProductService();
            Optional<ProductWithDetails> product = productService.getProductById(pID);

            if (product.isPresent()) {
                cart.addItem(product.get(), quantity);
                session.setAttribute("cart", cart);

                if (SessionUtil.isLoggedIn(request)) {
                    Integer userId = SessionUtil.getUserId(request);
                    cartService.addCartToDb(userId, cart);
                }

                request.setAttribute("cart", cart);
                request.getRequestDispatcher(
                        "/views/layout/cart-mini-fragment.jsp"
                ).forward(request, response);

            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("<p class='mini-cart-empty'>Không tìm thấy sản phẩm.</p>");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("<p class='mini-cart-empty'>Tham số không hợp lệ.</p>");
        }
    }
}