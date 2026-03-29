package code.web.lightup.controller.User;

import code.web.lightup.model.Cart.Cart;
import code.web.lightup.model.Cart.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@WebServlet(name = "CheckoutSelectServlet", value = "/checkout-select")
public class CheckoutSelectServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart fullCart = (Cart) session.getAttribute("cart");

        if (fullCart == null || fullCart.getListItem().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] selectedIds = request.getParameterValues("selectedIds");

        if (selectedIds == null || selectedIds.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Set<Integer> selectedSet = new HashSet<>();
        for (String id : selectedIds) {
            try { selectedSet.add(Integer.parseInt(id)); }
            catch (NumberFormatException ignored) {}
        }

        Cart checkoutCart = new Cart();
        for (CartItem item : fullCart.getListItem()) {
            if (selectedSet.contains(item.getProduct().getId())) {
                checkoutCart.addItem(item.getProduct(), item.getQuantity());
            }
        }

        session.setAttribute("checkoutCart", checkoutCart);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}