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
     *Lấy TẤT CẢ categories (cả cấp 1 và cấp 2+)
     */
    public List<Category> getProductCategories() {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM categories " +
                                        "ORDER BY COALESCE(parent_id, id) ASC, sort_order, id"
                        )
                        .mapToBean(Category.class)
                        .list()
        );
    }

    /**
     *  Lấy danh mục gốc (parent_id = NULL) - Cấp 1
     */
    public List<Category> getRootCategories() {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM categories " +
                                        "WHERE parent_id IS NULL " +
                                        "ORDER BY sort_order, id"
                        )
                        .mapToBean(Category.class)
                        .list()
        );
    }

    /**
     * Lấy danh mục con của một danh mục cha cụ thể
     */
    public List<Category> getChildCategories(int parentId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM categories " +
                                        "WHERE parent_id = :parentId " +
                                        "ORDER BY sort_order, id"
                        )
                        .bind("parentId", parentId)
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

    /**
     * Thêm category mới
     */
    public int insertCategory(String name, Integer parentId, int sortOrder) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO categories (name, parent_id, sort_order) " +
                                        "VALUES (:name, :parentId, :sortOrder)"
                        )
                        .bind("name", name)
                        .bind("parentId", parentId)
                        .bind("sortOrder", sortOrder)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(-1)
        );
    }

    /**
     * Cập nhật category
     */
    public boolean updateCategory(int id, String name, Integer parentId, int sortOrder) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "UPDATE categories " +
                                        "SET name = :name, parent_id = :parentId, sort_order = :sortOrder " +
                                        "WHERE id = :id"
                        )
                        .bind("id", id)
                        .bind("name", name)
                        .bind("parentId", parentId)
                        .bind("sortOrder", sortOrder)
                        .execute() > 0
        );
    }

    /**
     * Xóa category theo ID
     */
    public boolean deleteCategory(int id) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "DELETE FROM categories WHERE id = :id"
                        )
                        .bind("id", id)
                        .execute() > 0
        );
    }

    /**
     *Đếm số sản phẩm trong một category
     */
    public int getProductCountInCategory(int categoryId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT COUNT(*) FROM products WHERE category_id = :categoryId"
                        )
                        .bind("categoryId", categoryId)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0)
        );
    }

    /**
     * Lấy số danh mục con
     */
    public int getChildCategoryCount(int parentId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT COUNT(*) FROM categories WHERE parent_id = :parentId"
                        )
                        .bind("parentId", parentId)
                        .mapTo(Integer.class)
                        .findFirst()
                        .orElse(0)
        );
    }

    /**
     * Toggle trạng thái category (active/inactive)
     */
    public boolean toggleCategoryStatus(int id) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "UPDATE categories " +
                                        "SET status = CASE " +
                                        "    WHEN status = 'active' THEN 'inactive' " +
                                        "    ELSE 'active' " +
                                        "END " +
                                        "WHERE id = :id"
                        )
                        .bind("id", id)
                        .execute() > 0
        );
    }

    /**
     * Chuyển tất cả sản phẩm từ một category sang category khác
     */
    public boolean moveProductsToCategory(int fromCategoryId, int toCategoryId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "UPDATE products " +
                                        "SET category_id = :toCategoryId " +
                                        "WHERE category_id = :fromCategoryId"
                        )
                        .bind("fromCategoryId", fromCategoryId)
                        .bind("toCategoryId", toCategoryId)
                        .execute() > 0
        );
    }
}