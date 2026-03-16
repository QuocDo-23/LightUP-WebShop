package code.web.lightup.controller.User;


import code.web.lightup.model.Review;
import code.web.lightup.model.ReviewStatistics;
import code.web.lightup.service.ProductService;
import code.web.lightup.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/review")
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
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

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

            boolean success = reviewService.addReview(review);

            if (success) {
                updateProductRating(productId);

                response.sendRedirect("product-detail?id=" + productId + "&reviewSuccess=true");
            } else {
                request.setAttribute("error", "Không thể gửi đánh giá, vui lòng thử lại");
                request.getRequestDispatcher("product-detail?id=" + productId).forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            request.getRequestDispatcher("product-detail").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi gửi đánh giá");
            request.getRequestDispatcher("product-detail").forward(request, response);
        }
    }

    private void updateProductRating(int productId) {
        try {
            ReviewStatistics stats = reviewService.getReviewStatistics(productId);
            productService.updateRating(productId, stats.getAverageRating());

        } catch (Exception e) {
            e.printStackTrace();
            // Có thể log lỗi ở đây
        }
    }
}