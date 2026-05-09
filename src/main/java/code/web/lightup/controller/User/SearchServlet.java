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

@WebServlet(name = "SearchServlet", urlPatterns = {"/search-suggestions", "/search"})
public class SearchServlet extends HttpServlet {

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
        String servletPath = request.getServletPath();

        if ("/search-suggestions".equals(servletPath)) {
            handleSearchSuggestions(request, response, query);
        }
        else if ("/search".equals(servletPath)) {
            handleFullSearch(request, response, query);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


    private void handleSearchSuggestions(HttpServletRequest request, HttpServletResponse response, String query)
            throws ServletException, IOException {

        if (query != null && query.trim().length() >= 2) {
            List<ProductWithDetails> searchResults = productService.searchProducts(query.trim());
            request.setAttribute("searchResults", searchResults);
        }

        request.getRequestDispatcher("/views/layout/search-suggestions.jsp").forward(request, response);
    }


    private void handleFullSearch(HttpServletRequest request, HttpServletResponse response, String query)
            throws ServletException, IOException {


        if (query == null || query.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String trimmedQuery = query.trim();

        List<ProductWithDetails> searchResults = productService.searchProducts(trimmedQuery);

        request.setAttribute("searchResults", searchResults);
        request.setAttribute("searchQuery", trimmedQuery);
        request.setAttribute("resultCount", searchResults.size());

        request.getRequestDispatcher("/views/user/search-results.jsp").forward(request, response);
    }
}