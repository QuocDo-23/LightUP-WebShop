package code.web.lightup.controller.User.CartController;

import code.web.lightup.model.Cart.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UpdateCart", value = "/update")
public class UpdateCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int qty = Integer.parseInt(request.getParameter("qty"));

                if (qty <= 0) {
                    cart.removeItem(productId);
                } else {
                    cart.updateItem(productId, qty);
                }
            } catch (Exception e) {

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