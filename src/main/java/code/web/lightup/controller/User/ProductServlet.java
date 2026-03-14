package code.web.lightup.controller.User;


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
import java.util.*;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductService productService;
    private CategoryService categoryService;

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

            String[] priceRanges = request.getParameterValues("price");

            List<Category> categories = categoryService.getSubCategories();

            if (priceRanges != null && priceRanges.length > 0) {
                List<ProductWithDetails> filteredProducts =
                        productService.filterProductsByPrice(priceRanges);

                request.setAttribute("products", filteredProducts);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/views/user/products.jsp").forward(request, response);
                return;
            }

            List<ProductWithDetails> allProducts =
                    productService.getAllProductsWithDetails();

            Map<Integer, List<ProductWithDetails>> productsByCategory = new HashMap<>();

            for (ProductWithDetails product : allProducts) {
                int catId = product.getCategoryId();
                List<ProductWithDetails> list =
                        productsByCategory.computeIfAbsent(catId, k -> new ArrayList<>());


                if (list.size() < 8) {
                    list.add(product);
                }
            }

            request.setAttribute("categories", categories);
            request.setAttribute("productsByCategory", productsByCategory);
            request.setAttribute("totalProducts", allProducts.size());
            request.getRequestDispatcher("/views/user/products.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}