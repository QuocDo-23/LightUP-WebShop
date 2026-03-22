package code.web.lightup.dao;


import code.web.lightup.model.Review;
import code.web.lightup.model.ReviewStatistics;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ReviewDAO {

    private final Jdbi jdbi;

    public ReviewDAO() {
        this.jdbi = BaseDao.get();
    }

    // ================= USER =================

    // Lấy review theo sản phẩm
    public List<Review> getReviewsByProductId(int productId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT r.*, u.name AS user_name " +
                                        "FROM Review_Product r " +
                                        "LEFT JOIN User u ON r.user_id = u.id " +
                                        "WHERE r.product_id = :productId " +
                                        "ORDER BY r.date DESC"
                        )
                        .bind("productId", productId)
                        .mapToBean(Review.class)
                        .list()
        );
    }

    // Thống kê theo sản phẩm
    public ReviewStatistics getReviewStatistics(int productId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT " +
                                        "COUNT(*) AS total_reviews, " +
                                        "COALESCE(AVG(rating),0) AS average_rating, " +
                                        "SUM(CASE WHEN rating=5 THEN 1 ELSE 0 END) AS five_stars, " +
                                        "SUM(CASE WHEN rating=4 THEN 1 ELSE 0 END) AS four_stars, " +
                                        "SUM(CASE WHEN rating=3 THEN 1 ELSE 0 END) AS three_stars, " +
                                        "SUM(CASE WHEN rating=2 THEN 1 ELSE 0 END) AS two_stars, " +
                                        "SUM(CASE WHEN rating=1 THEN 1 ELSE 0 END) AS one_star " +
                                        "FROM Review_Product WHERE product_id=:productId"
                        )
                        .bind("productId", productId)
                        .mapToBean(ReviewStatistics.class)
                        .one()
        );
    }

    // Thêm review
    public int addReview(Review review) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO Review_Product(product_id,user_id,text,img,rating,date,status) " +
                                        "VALUES(:productId,:userId,:text,:img,:rating,NOW(),1)"
                        )
                        .bind("productId", review.getProductId())
                        .bind("userId", review.getUserId())
                        .bind("text", review.getText())
                        .bind("img", review.getImg())
                        .bind("rating", review.getRating())
                        .execute()
        );
    }

    // ================= ADMIN =================

    // Lấy toàn bộ review
    public List<Review> getAllReviews() {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT r.*, u.name AS user_name, p.name AS product_name\n" +
                                        "FROM Review_Product r\n" +
                                        "LEFT JOIN User u ON r.user_id = u.id\n" +
                                        "LEFT JOIN Product p ON r.product_id = p.id\n" +
                                        "ORDER BY r.date DESC"
                        )
                        .mapToBean(Review.class)
                        .list()
        );
    }


    public List<Review> getReviewsByStatus(int status) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT r.*, u.name AS user_name " +
                                        "FROM Review_Product r " +
                                        "LEFT JOIN User u ON r.user_id = u.id " +
                                        "WHERE r.status = :status " +
                                        "ORDER BY r.date DESC"
                        )
                        .bind("status", status)
                        .mapToBean(Review.class)
                        .list()
        );
    }

    // Duyệt / ẩn review
    public int updateReviewStatus(int reviewId, int status) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "UPDATE Review_Product SET status=:status WHERE id=:id"
                        )
                        .bind("status", status)
                        .bind("id", reviewId)
                        .execute()
        );
    }

    // Xóa review
    public int deleteReview(int reviewId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "DELETE FROM Review_Product WHERE id=:id"
                        )
                        .bind("id", reviewId)
                        .execute()
        );
    }


    public ReviewStatistics getAdminReviewStatistics() {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT " +
                                        "COUNT(*) AS total_reviews, " +
                                        "COALESCE(AVG(rating),0) AS average_rating, " +
                                        "SUM(CASE WHEN rating=5 THEN 1 ELSE 0 END) AS five_stars, " +
                                        "SUM(CASE WHEN rating=4 THEN 1 ELSE 0 END) AS four_stars, " +
                                        "SUM(CASE WHEN rating=3 THEN 1 ELSE 0 END) AS three_stars, " +
                                        "SUM(CASE WHEN rating=2 THEN 1 ELSE 0 END) AS two_stars, " +
                                        "SUM(CASE WHEN rating=1 THEN 1 ELSE 0 END) AS one_star " +
                                        "FROM Review_Product"
                        )
                        .mapToBean(ReviewStatistics.class)
                        .one()
        );
    }


    public boolean hasOrderReview(int orderId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT COUNT(*) FROM review_order WHERE order_id = :orderId"
                        )
                        .bind("orderId", orderId)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }}
