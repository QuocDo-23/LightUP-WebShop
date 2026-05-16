package code.web.lightup.controller.User;

import code.web.lightup.dao.FavoriteDAO;
import code.web.lightup.model.Product;
import code.web.lightup.model.Banner;
import code.web.lightup.model.ProductWithDetails;
import code.web.lightup.service.BannerService;
import code.web.lightup.service.CategoryService;
import code.web.lightup.service.NewsService;
import code.web.lightup.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("")
public class IndexServlet extends HttpServlet {
    private ProductService productService;
    private CategoryService categoryService;
    private NewsService newsService;
    private BannerService bannerService;
    private FavoriteDAO favoriteDao;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        categoryService = new CategoryService();
        newsService = new NewsService();
        bannerService = new BannerService();
        favoriteDao = new FavoriteDAO();

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductWithDetails> listProducts = productService.getFeaturedProducts();
        Integer userId = code.web.lightup.util.SessionUtil.getUserId(request);
        if (userId != null) {
            java.util.Set<Integer> favIds = favoriteDao.getFavoriteProductIds(userId);

            if (!favIds.isEmpty()) {
                for (Product p : listProducts) {
                    if (favIds.contains(p.getId())) {
                        p.setFavorite(true);
                    }
                }
            }
        }

        request.setAttribute("categories", categoryService.getSubCategories());
        request.setAttribute("listProducts", listProducts);
        request.setAttribute("listArticle", newsService.getArticle(4));
        List<Banner> HBanner = bannerService.getBannerByPosition("home");

        request.setAttribute("banners", HBanner);

        request.getRequestDispatcher("/views/user/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}