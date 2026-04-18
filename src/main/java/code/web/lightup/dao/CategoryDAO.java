package code.web.lightup.dao;

import code.web.lightup.model.Category;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class CategoryDAO {
    private final Jdbi jdbi;

    public CategoryDAO() {
        this.jdbi = BaseDao.get();
    }

    /**
     * Lấy tất cả categories
     */
    public List<Category> getAllCategories() {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM categories ORDER BY sort_order, id"
                        )
                        .mapToBean(Category.class)
                        .list()
        );
    }
    /**
     * Lấy tất cả categories cấp 1
     */

    public List<Category> getProductCategories() {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM categories " +
                                        "WHERE parent_id IS not NULL " +
                                        "ORDER BY sort_order, id"
                        )
                        .mapToBean(Category.class)
                        .list()
        );
    }

    /**
     * Lấy sub-categories (categories có parent_id)
     */
    public List<Category> getSubCategories() {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT c.*, i.img AS imgCate " +
                                        "FROM categories c " +
                                        "LEFT JOIN image i ON i.ref_id = c.id AND i.type = 'category' " +
                                        "WHERE c.parent_id IS NOT NULL " +
                                        "ORDER BY c.sort_order"
                        )
                        .mapToBean(Category.class)
                        .list()
        );
    }

    /**
     * Lấy category theo ID
     */
    public Category getCategoryById(int id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM categories WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Category.class)
                        .findFirst()
                        .orElse(null)
        );
    }
}