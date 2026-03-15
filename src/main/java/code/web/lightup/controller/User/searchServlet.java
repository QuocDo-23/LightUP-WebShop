package code.web.lightup.controller.User;


import code.web.lightup.model.ProductWithDetails;
import code.web.lightup.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search-suggestions"})
public class searchServlet extends HttpServlet {

    private ProductService productService;
    @Override
    public void init() throws ServletException {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String query = request.getParameter("q");

        if (query != null && query.trim().length() >= 2) {
            List<ProductWithDetails> searchResults = productService.searchProducts(query.trim());
            request.setAttribute("searchResults", searchResults);
        }

        request.getRequestDispatcher("/views/layout/search.jsp").forward(request, response);
    }
}