package code.web.lightup.dao;
import code.web.lightup.model.News.News;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class NewsDAO {

    private final Jdbi jdbi;

    public NewsDAO() {
        this.jdbi = BaseDao.get();
    }

    /**
     * Lấy 4 bài viết nổi bật
     */
    public List<News> getFeaturedArticle(int featuredLimit) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT a.*, c.name AS categoryName, " +
                                        "(SELECT img FROM Image WHERE type = 'Articles' AND ref_id = a.id ORDER BY id LIMIT 1) as mainImg " +
                                        "FROM Articles a " +
                                        "LEFT JOIN Categories c ON a.category_id = c.id " +
                                        "WHERE a.feature = TRUE " +
                                        "ORDER BY a.date_of_posting DESC " +
                                        "LIMIT :featuredLimit"
                        )
                        .bind("featuredLimit", featuredLimit)
                        .mapToBean(News.class)
                        .list()
        );
    }

    /**
     * Lấy 4 bài viết cho trang chủ
     */
    public List<News> getArticle(int limit) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT a.*, c.name AS categoryName, " +
                                        "(SELECT img FROM Image WHERE type = 'Articles' AND ref_id = a.id ORDER BY id LIMIT 1) as mainImg " +
                                        "FROM Articles a " +
                                        "LEFT JOIN Categories c ON a.category_id = c.id " +
                                        "ORDER BY a.date_of_posting DESC " +
                                        "LIMIT :limit"
                        )
                        .bind("limit", limit)
                        .mapToBean(News.class)
                        .list()
        );
    }

    /**
     * Lấy bài viết với phân trang và hình ảnh
     */
    public List<News> getArticleWithPagination(int page, int pageSize, String sortBy) {
        int offset = (page - 1) * pageSize;

        String orderClause;
        if ("oldest".equals(sortBy)) {
            orderClause = "a.date_of_posting ASC";
        } else {
            orderClause = "a.date_of_posting DESC";
        }

        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT a.*, c.name as category_name, " +
                                        "(SELECT img FROM Image WHERE type = 'Articles' AND ref_id = a.id LIMIT 1) as main_img " +
                                        "FROM Articles a " +
                                        "LEFT JOIN Categories c ON a.category_id = c.id " +
                                        "WHERE (a.feature = FALSE OR a.feature IS NULL) " +
                                        "ORDER BY " + orderClause + " " +
                                        "LIMIT :limit OFFSET :offset"
                        )
                        .bind("limit", pageSize)
                        .bind("offset", offset)
                        .mapToBean(News.class)
                        .list()
        );
    }


    /**
     * Đếm tổng số bài viết
     */
    public int getTotalArticle() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM Articles"
                                + " WHERE (feature = FALSE OR feature IS NULL)")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    /**
     * Lấy bài viết theo ID
     */
    public News getArticleById(int id) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT a.*, c.name AS categoryName, " +
                                        "(SELECT img FROM Image WHERE type = 'Articles' AND ref_id = a.id ORDER BY id LIMIT 1) as mainImg " +
                                        "FROM Articles a " +
                                        "LEFT JOIN Categories c ON a.category_id = c.id " +
                                        "WHERE a.id = :id"
                        )
                        .bind("id", id)
                        .mapToBean(News.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    /**
     * Lấy bài viết theo slug
     */
    public News getArticleBySlug(String slug) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT a.*, c.name AS categoryName, " +
                                        "(SELECT img FROM Image WHERE type = 'Articles' AND ref_id = a.id ORDER BY id LIMIT 1) as mainImg " +
                                        "FROM Articles a " +
                                        "LEFT JOIN Categories c ON a.category_id = c.id " +
                                        "WHERE a.slug = :slug"
                        )
                        .bind("slug", slug)
                        .mapToBean(News.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    /**
     * Chức năng của admin
     */

    /**
     * Lấy bài viết theo danh mục
     */
    public List<News> getArticlesByCategory(int categoryId, int limit, int offset) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT a.*, c.name AS categoryName, " +
                                        "(SELECT img FROM Image WHERE type = 'Articles' AND ref_id = a.id ORDER BY id LIMIT 1) as mainImg " +
                                        "FROM articles a " +
                                        "LEFT JOIN categories c ON a.category_id = c.id " +
                                        "WHERE a.category_id = :categoryId " +
                                        "ORDER BY a.date_of_posting DESC " +
                                        "LIMIT :limit OFFSET :offset"
                        )
                        .bind("categoryId", categoryId)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(News.class)
                        .list()
        );
    }

    /**
     * Tìm kiếm bài viết theo từ khóa
     */
    public List<News> searchArticles(String keyword, int limit, int offset) {
        String searchPattern = "%" + keyword + "%";
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT a.*, c.name AS categoryName, " +
                                        "(SELECT img FROM Image WHERE type = 'Articles' AND ref_id = a.id ORDER BY id LIMIT 1) as mainImg " +
                                        "FROM articles a " +
                                        "LEFT JOIN categories c ON a.category_id = c.id " +
                                        "WHERE a.title LIKE :keyword " +
                                        "OR a.description LIKE :keyword " +
                                        "ORDER BY a.date_of_posting DESC " +
                                        "LIMIT :limit OFFSET :offset"
                        )
                        .bind("keyword", searchPattern)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(News.class)
                        .list()
        );
    }

    /**
     * Thêm bài viết mới
     * @return ID của bài viết vừa tạo
     */
    public int insertArticle(News article) {
        return jdbi.inTransaction(handle -> {
            // 1. Insert bài viết (không có cột mainImg)
            int articleId = handle.createUpdate(
                            "INSERT INTO articles (category_id, title, description, date_of_posting, slug, feature) " +
                                    "VALUES (:categoryId, :title, :description, :dateOfPosting, :slug, :feature)"
                    )
                    .bind("categoryId", article.getCategoryId())
                    .bind("title", article.getTitle())
                    .bind("description", article.getDescription())
                    .bind("dateOfPosting", article.getDateOfPosting())
                    .bind("slug", article.getSlug())
                    .bind("feature", article.isFeature())
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();

            // 2. Insert ảnh vào bảng Image nếu có
            if (article.getMainImg() != null && !article.getMainImg().isEmpty()) {
                handle.createUpdate("INSERT INTO Image (type, ref_id, img) VALUES ('Articles', :refId, :img)")
                        .bind("refId", articleId)
                        .bind("img", article.getMainImg())
                        .execute();
            }

            return articleId;
        });
    }

    /**
     * Cập nhật bài viết
     */
    public int updateArticle(News article) {
        return jdbi.inTransaction(handle -> {
            // 1. Update thông tin bài viết
            int updatedRows = handle.createUpdate(
                            "UPDATE articles SET " +
                                    "category_id = :categoryId, " +
                                    "title = :title, " +
                                    "description = :description, " +
                                    "date_of_posting = :dateOfPosting, " +
                                    "slug = :slug, " +
                                    "feature = :feature " +
                                    "WHERE id = :id"
                    )
                    .bind("categoryId", article.getCategoryId())
                    .bind("title", article.getTitle())
                    .bind("description", article.getDescription())
                    .bind("dateOfPosting", article.getDateOfPosting())
                    .bind("slug", article.getSlug())
                    .bind("feature", article.isFeature())
                    .bind("id", article.getId())
                    .execute();

            // 2. Update ảnh (Xóa ảnh cũ và thêm ảnh mới hoặc update nếu logic cho phép nhiều ảnh)
            // Ở đây ta giả sử mỗi bài viết chỉ có 1 ảnh chính, ta sẽ update ảnh đó
            if (article.getMainImg() != null && !article.getMainImg().isEmpty()) {
                // Kiểm tra xem đã có ảnh chưa
                int imageCount = handle.createQuery("SELECT COUNT(*) FROM Image WHERE type = 'Articles' AND ref_id = :id")
                        .bind("id", article.getId())
                        .mapTo(Integer.class)
                        .one();

                if (imageCount > 0) {
                    handle.createUpdate("UPDATE Image SET img = :img WHERE type = 'Articles' AND ref_id = :id")
                            .bind("img", article.getMainImg())
                            .bind("id", article.getId())
                            .execute();
                } else {
                    handle.createUpdate("INSERT INTO Image (type, ref_id, img) VALUES ('Articles', :id, :img)")
                            .bind("id", article.getId())
                            .bind("img", article.getMainImg())
                            .execute();
                }
            }

            return updatedRows;
        });
    }

    /**
     * Xóa bài viết
     */
    public int deleteArticle(int id) {
        return jdbi.inTransaction(handle -> {
            // 1. Xóa ảnh liên quan
            handle.createUpdate("DELETE FROM Image WHERE type = 'Articles' AND ref_id = :id")
                    .bind("id", id)
                    .execute();

            // 2. Xóa bài viết
            return handle.createUpdate("DELETE FROM articles WHERE id = :id")
                    .bind("id", id)
                    .execute();
        });
    }


    /**
     * Đếm tổng số bài viết theo danh mục
     */
    public int countArticlesByCategory(int categoryId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT COUNT(*) FROM articles WHERE category_id = :categoryId"
                        )
                        .bind("categoryId", categoryId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    /**
     * Đếm tổng số bài viết
     */
    public int countAllArticles() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM articles")
                        .mapTo(Integer.class)
                        .one()
        );
    }

}
