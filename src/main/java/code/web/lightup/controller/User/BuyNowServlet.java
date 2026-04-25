package code.web.lightup.controller.User;

import code.web.lightup.model.Cart.Cart;
import code.web.lightup.model.ProductWithDetails;
import code.web.lightup.service.ProductService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;


@WebServlet("/buy-now")
public class BuyNowServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        int pID = Integer.parseInt(request.getParameter("pID"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        Optional<ProductWithDetails> product = productService.getProductById(pID);
        if (product.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Cart buyNowCart = new Cart();
        buyNowCart.addItem(product.get(), quantity);

        session.setAttribute("checkoutCart", buyNowCart);
        session.setAttribute("checkoutType", "buy-now");

        response.sendRedirect(request.getContextPath() + "/payment");
    }
}
