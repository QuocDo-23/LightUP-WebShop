package code.web.lightup.controller.User;


import code.web.lightup.model.Review;
import code.web.lightup.model.ReviewStatistics;
import code.web.lightup.service.ProductService;
import code.web.lightup.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/review")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
public class ReviewServlet extends HttpServlet {

    private ReviewService reviewService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        reviewService = new ReviewService();
        productService = new ProductService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            String productId = request.getParameter("productId");
            response.sendRedirect("login?redirect=product-detail?id=" + productId);
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String comment = request.getParameter("comment");


            String ratingParam = request.getParameter("rating");

            String parentIdParam = request.getParameter("parentId");
            Integer parentId = null;

            if (parentIdParam != null && !parentIdParam.isEmpty()) {
                parentId = Integer.parseInt(parentIdParam);
            }

// chỉ check rating khi là comment cha
            if (parentId == null) {
                if (ratingParam == null || ratingParam.isEmpty()) {
                    response.sendRedirect("product-detail?id=" + productId + "&reviewError=noRating");
                    return;
                }
            }

            int rating = 5;

            if (parentId == null) {
                rating = Integer.parseInt(ratingParam);
            }

            if (rating < 1 || rating > 5) {
                request.setAttribute("error", "Rating phải từ 1 đến 5 sao");
                request.getRequestDispatcher("product-detail?id=" + productId).forward(request, response);
                return;
            }


            Review review = new Review();
            review.setProductId(productId);
            review.setUserId(userId);
            review.setRating(rating);
            review.setText(comment);
            review.setParentId(parentId);

            boolean success = reviewService.addReview(review);

            if (success) {
                updateProductRating(productId);

                response.sendRedirect("product-detail?id=" + productId + "&reviewSuccess=true");
            } else {
                request.setAttribute("error", "Không thể gửi đánh giá, vui lòng thử lại");
                response.sendRedirect("product-detail?id=" + productId + "&reviewError=invalidRating");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("product-detail");
        } catch (Exception e) {
            e.printStackTrace();
            String productId2 = request.getParameter("productId");
            response.sendRedirect("product-detail?id=" + productId2 + "&reviewError=true");
        }
    }

    private void updateProductRating(int productId) {
        try {
            ReviewStatistics stats = reviewService.getReviewStatistics(productId);
            productService.updateRating(productId, stats.getAverageRating());

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}