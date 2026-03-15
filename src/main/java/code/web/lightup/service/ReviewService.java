package code.web.lightup.service;


import code.web.lightup.dao.ReviewDAO;
import code.web.lightup.model.Review;
import code.web.lightup.model.ReviewStatistics;

import java.util.List;

public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
    }


    public List<Review> getReviewsByProductId(int productId) {
        return reviewDAO.getReviewsByProductId(productId);
    }

    public ReviewStatistics getReviewStatistics(int productId) {
        return reviewDAO.getReviewStatistics(productId);
    }

    public boolean addReview(Review review) {
        return reviewDAO.addReview(review) > 0;
    }


    public List<Review> getAllReviews() {
        return reviewDAO.getAllReviews();
    }

    public List<Review> getReviewsByStatus(int status) {
        return reviewDAO.getReviewsByStatus(status);
    }

    public boolean updateReviewStatus(int reviewId, int status) {
        return reviewDAO.updateReviewStatus(reviewId, status) > 0;
    }

    public boolean deleteReview(int reviewId) {
        return reviewDAO.deleteReview(reviewId) > 0;
    }

    public ReviewStatistics getAdminReviewStatistics() {
        return reviewDAO.getAdminReviewStatistics();
    }

    public boolean hasOrderReview(int orderId){
        return reviewDAO.hasOrderReview(orderId);
    }
}
