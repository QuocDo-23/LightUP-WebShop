package code.web.lightup.controller.User;


import code.web.lightup.model.Image;
import code.web.lightup.model.ProductWithDetails;
import code.web.lightup.model.Review;
import code.web.lightup.model.ReviewStatistics;

import code.web.lightup.service.ImageService;
import code.web.lightup.service.ProductService;
import code.web.lightup.service.ReviewService;
import code.web.lightup.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@WebServlet("/product-detail")
public class ProductDetailServlet extends HttpServlet {

    private ProductService productService;
    private ReviewService reviewService;
    private ImageService imageService;
    private OrderService orderService;


    @Override
    public void init() {
        productService = new ProductService();
        reviewService = new ReviewService();
        imageService = new ImageService();
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect("products");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("products");
            return;
        }

        Optional<ProductWithDetails> productOpt = productService.getProductById(productId);

        if (productOpt.isEmpty()) {
            response.sendRedirect("products");
            return;
        }

        ProductWithDetails product = productOpt.get();
        int soldQuantity =
                productService.getSoldQuantityByProductId(
                        productId
                );


        List<Image> images = imageService.getImagesByProductId(productId);


        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        ReviewStatistics stats = reviewService.getReviewStatistics(productId);


        List<ProductWithDetails> relatedProducts =
                productService.getProductsByCategory(product.getCategoryId());

        relatedProducts.removeIf(p -> p.getId() == productId);

        if (relatedProducts.size() > 4) {
            relatedProducts = relatedProducts.subList(0, 4);
        }


        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        HttpSession session = request.getSession(false);

        boolean canReview = false;
        boolean isLoggedIn = false;

        if (session != null) {

            Integer userId =
                    (Integer) session.getAttribute("userId");

            if (userId != null) {

                isLoggedIn = true;

                canReview =
                        orderService.hasPurchasedProduct(
                                userId,
                                productId
                        );
            }
        }

        request.setAttribute("isLoggedIn", isLoggedIn);
        request.setAttribute("canReview", canReview);
        request.setAttribute("product", product);
        request.setAttribute("soldQuantity", soldQuantity);
        request.setAttribute("images", images);
        request.setAttribute("reviews", reviews);
        request.setAttribute("stats", stats);
        request.setAttribute("relatedProducts", relatedProducts);
        request.setAttribute("vndFormat", vndFormat);

        request.getRequestDispatcher("/views/user/product-detail.jsp").forward(request, response);
    }
}