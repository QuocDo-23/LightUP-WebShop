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

@WebServlet(name = "AddCart", value = "/add-cart")
public class AddCart extends HttpServlet {
    private static CartService cartService;

    public AddCart() {
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int pID      = Integer.parseInt(request.getParameter("pID"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        HttpSession session = request.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) cart = new Cart();

        ProductService productService = new ProductService();
        Optional<ProductWithDetails> product = productService.getProductById(pID);

        if (product.isPresent()) {
            ProductWithDetails p = product.get();
            int currentQty = cart.getQuantityOfProduct(pID);
            int available = p.getInventoryQuantity();

            if(currentQty + quantity > available) {
                session.setAttribute("errorMsg", "Không đủ hàng trong kho! Chỉ còn " + available + " sản phẩm.");
                String referer = request.getHeader("Referer");
                if (referer != null) response.sendRedirect(referer);
                else response.sendRedirect(request.getContextPath() + "/");
                return;
            }

            cart.addItem(product.get(), quantity);
            session.setAttribute("cart", cart);
            session.setAttribute("successMsg", "Đã thêm vào giỏ!");

            if (SessionUtil.isLoggedIn(request)) {
                Integer userId = SessionUtil.getUserId(request);
               cartService.addCartToDb(userId, cart);
            }

            String referer = request.getHeader("Referer");
            if (referer != null) response.sendRedirect(referer);
            else response.sendRedirect(request.getContextPath() + "/");

        } else {
            request.setAttribute("msg", "Product not found");
            request.getRequestDispatcher("/views/layout/products.jsp").forward(request, response);
        }
    }



}