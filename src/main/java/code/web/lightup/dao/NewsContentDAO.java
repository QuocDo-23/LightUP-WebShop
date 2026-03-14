package code.web.lightup.dao;

import code.web.lightup.model.News.News;
import code.web.lightup.model.News.NewsContent;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class NewsContentDAO {
    private final Jdbi jdbi;

    public NewsContentDAO() {
        this.jdbi = BaseDao.get();
    }

    /**
     * Lấy nd bài viết theo ID
     */
    public List<NewsContent> getContentByArticleId(int articleId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM articles_content " +
                                        "WHERE article_id = :articleId " +
                                        "ORDER BY display_order ASC"
                        )
                        .bind("articleId", articleId)
                        .mapToBean(NewsContent.class)
                        .list()
        );
    }

    /**
     * Lấy tất cả bài viết
     */
    public List<News> getAllArticles() {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT a.*, c.name AS categoryName " +
                                        "FROM articles a " +
                                        "LEFT JOIN categories c ON a.category_id = c.id " +
                                        "ORDER BY a.date_of_posting DESC"
                        )
                        .mapToBean(News.class)
                        .list()
        );
    }

    /**
     * Lấy bài viết liên quan (cùng danh mục, khác ID)
     */
    public List<News> getRelatedArticles(int currentArticleId, Integer categoryId, int limit) {
        if (categoryId == null) {
            // Nếu không có category => lấy bài viết mới nhất
            return jdbi.withHandle(handle ->
                    handle.createQuery(
                                    "SELECT a.*, c.name AS categoryName, " +
                                            "(SELECT img FROM Image WHERE type = 'Articles' AND ref_id = a.id ORDER BY id LIMIT 1) as mainImg " +
                                            "FROM articles a " +
                                            "LEFT JOIN categories c ON a.category_id = c.id " +
                                            "WHERE a.id != :currentId " +
                                            "ORDER BY a.date_of_posting DESC " +
                                            "LIMIT :limit"
                            )
                            .bind("currentId", currentArticleId)
                            .bind("limit", limit)
                            .mapToBean(News.class)
                            .list()
            );
        }

        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT a.*, c.name AS categoryName, " +
                                        "(SELECT img FROM Image WHERE type = 'Articles' AND ref_id = a.id ORDER BY id LIMIT 1) as mainImg " +
                                        "FROM articles a " +
                                        "LEFT JOIN categories c ON a.category_id = c.id " +
                                        "WHERE a.category_id = :categoryId " +
                                        "AND a.id != :currentId " +
                                        "ORDER BY a.date_of_posting DESC " +
                                        "LIMIT :limit"
                        )
                        .bind("categoryId", categoryId)
                        .bind("currentId", currentArticleId)
                        .bind("limit", limit)
                        .mapToBean(News.class)
                        .list()
        );
    }

    /**
     * Thêm nội dung mới vào bài viết
     */
    public boolean insertContent(NewsContent content) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO articles_content (article_id, content, content_type, display_order) " +
                                        "VALUES (:articleId, :content, :contentType, :displayOrder)"
                        )
                        .bind("articleId", content.getArticleId())
                        .bind("content", content.getContent())
                        .bind("contentType", content.getContentType())
                        .bind("displayOrder", content.getDisplayOrder())
                        .execute() > 0
        );
    }

    /**
     * update nội dung bài viết
     */
    public boolean updateContent(NewsContent content) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "UPDATE articles_content SET " +
                                        "content = :content, " +
                                        "content_type = :contentType, " +
                                        "display_order = :displayOrder " +
                                        "WHERE id = :id"
                        )
                        .bind("content", content.getContent())
                        .bind("contentType", content.getContentType())
                        .bind("displayOrder", content.getDisplayOrder())
                        .bind("id", content.getId())
                        .execute() > 0
        );
    }

    /**
     * Xóa nội dung bài viết
     */
    public boolean deleteContent(int id) {
        return jdbi.withHandle(handle ->
                handle.createUpdate("DELETE FROM articles_content WHERE id = :id")
                        .bind("id", id)
                        .execute() > 0
        );
    }

    /**
     * Lấy nội dung theo ID
     */
    public NewsContent getContentById(int id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM articles_content WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(NewsContent.class)
                        .findFirst()
                        .orElse(null)
        );
    }
    
    /**
     * Lấy thứ tự hiển thị tiếp theo cho bài viết
     */
    public int getNextDisplayOrder(int articleId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT COALESCE(MAX(display_order), 0) + 1 FROM articles_content WHERE article_id = :articleId")
                        .bind("articleId", articleId)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}