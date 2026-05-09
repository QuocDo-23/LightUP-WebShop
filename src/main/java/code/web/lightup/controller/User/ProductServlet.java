package code.web.lightup.controller.User;

import code.web.lightup.dao.FavoriteDAO;
import code.web.lightup.util.SessionUtil;
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
    private FavoriteDAO favoriteDao;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        categoryService = new CategoryService();
        favoriteDao = new FavoriteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {

            String[] priceRanges = request.getParameterValues("price");

            List<Category> categories = categoryService.getSubCategories();

            Integer userId = SessionUtil.getUserId(request);
            Set<Integer> favIds = new HashSet<>();
            if (userId != null) {
                favIds = favoriteDao.getFavoriteProductIds(userId);
            }


            if (priceRanges != null && priceRanges.length > 0) {
                List<ProductWithDetails> filteredProducts =
                        productService.filterProductsByPrice(priceRanges);

                if (!favIds.isEmpty()) {
                    for (ProductWithDetails p : filteredProducts) {
                        if (favIds.contains(p.getId())) {
                            p.setFavorite(true);
                        }
                    }
                }

                request.setAttribute("products", filteredProducts);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/views/user/products.jsp").forward(request, response);
                return;
            }

            List<ProductWithDetails> allProducts =
                    productService.getAllProductsWithDetails();

            if (!favIds.isEmpty()) {
                for (ProductWithDetails p : allProducts) {
                    if (favIds.contains(p.getId())) {
                        p.setFavorite(true);
                    }
                }
            }

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