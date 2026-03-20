package code.web.lightup.controller.Admin.Review;


import code.web.lightup.model.Review;
import code.web.lightup.model.ReviewStatistics;
import code.web.lightup.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/reviews")
public class AdminReviewServlet extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Review> reviews = reviewService.getAllReviews();
        ReviewStatistics statistics = reviewService.getAdminReviewStatistics();

        request.setAttribute("reviews", reviews);
        request.setAttribute("statistics", statistics);
        request.setAttribute("currentPage", "reviews");

        request.getRequestDispatcher("/views/admin/Review/reviews.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            int status = Integer.parseInt(request.getParameter("status"));
            reviewService.updateReviewStatus(reviewId, status);

        } else if ("delete".equals(action)) {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            reviewService.deleteReview(reviewId);
        }

        response.sendRedirect(request.getContextPath() + "/admin/reviews");
    }
}
