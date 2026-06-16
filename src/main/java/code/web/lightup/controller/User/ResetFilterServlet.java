package code.web.lightup.controller.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/reset-filter")
public class ResetFilterServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        request.getSession()
                .removeAttribute(
                        "selectedPrices"
                );

        response.sendRedirect(
                request.getContextPath()
                        + "/products"
        );
    }
}