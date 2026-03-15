package code.web.lightup.controller.User.ProductsController;



import code.web.lightup.model.Category;
import code.web.lightup.model.ProductWithDetails;
import code.web.lightup.service.CategoryService;
import code.web.lightup.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/cate_products")
public class CategoryProductsServlet extends HttpServlet {

    private ProductService productService;
    private CategoryService categoryService;
    private static final int PRODUCTS_PER_PAGE = 12;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect("products");
                return;
            }

            int categoryId = Integer.parseInt(idParam);

            String pageParam = request.getParameter("page");
            int currentPage = 1;
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }


            int offset = (currentPage - 1) * PRODUCTS_PER_PAGE;

            int totalProducts = productService.countProductsByCategory(categoryId);
            int totalPages = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);

            if (currentPage > totalPages && totalPages > 0) {
                currentPage = totalPages;
                offset = (currentPage - 1) * PRODUCTS_PER_PAGE;
            }

            List<ProductWithDetails> products = productService.getProductsByCategoryWithPagination(
                    categoryId, offset, PRODUCTS_PER_PAGE
            );

            Category category = categoryService.getCategoryById(categoryId);
            List<Category> categories = categoryService.getSubCategories();

            if (category == null) {
                response.sendRedirect("products");
                return;
            }

            request.setAttribute("products", products);
            request.setAttribute("category", category);
            request.setAttribute("categories", categories);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);

            request.getRequestDispatcher("/views/user/products.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("products");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}