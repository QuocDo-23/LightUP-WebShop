package code.web.lightup.service;

import code.web.lightup.dao.CategoryDAO;
import code.web.lightup.model.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }


    public List<Category> getRootCategories() {
        return categoryDAO.getRootCategories();
    }


    public List<Category> getChildCategories(int parentId) {
        return categoryDAO.getChildCategories(parentId);
    }


    public List<Category> getSubCategories() {
        return categoryDAO.getSubCategories();
    }


    public Category getCategoryById(int id) {
        return categoryDAO.getCategoryById(id);
    }

    public List<Category> getProductCategories() {
        return categoryDAO.getProductCategories();
    }


    public int addCategory(String name, Integer parentId, int sortOrder) {
        if (name == null || name.trim().isEmpty()) {
            return -1;
        }
        return categoryDAO.insertCategory(name.trim(), parentId, sortOrder);
    }


    public boolean updateCategory(int id, String name, Integer parentId, int sortOrder) {
        if (id <= 0 || name == null || name.trim().isEmpty()) {
            return false;
        }
        return categoryDAO.updateCategory(id, name.trim(), parentId, sortOrder);
    }

    public boolean deleteCategory(int id) {
        if (id <= 0) return false;
        return categoryDAO.deleteCategory(id);
    }


    public boolean deleteCategoryAndMoveProducts(int categoryId, int toCategoryId) {
        if (categoryId <= 0 || toCategoryId <= 0) {
            return false;
        }

        Category category = categoryDAO.getCategoryById(categoryId);
        if (category == null) {
            return false;
        }
        Category targetCategory = categoryDAO.getCategoryById(toCategoryId);
        if (targetCategory == null) {
            return false;
        }

        categoryDAO.moveProductsToCategory(categoryId, toCategoryId);


        return categoryDAO.deleteCategory(categoryId);
    }

    public boolean deleteCategoryWithProducts(int categoryId) {
        if (categoryId <= 0) {
            return false;
        }

        Category category = categoryDAO.getCategoryById(categoryId);
        if (category == null) {
            return false;
        }

        return categoryDAO.deleteCategory(categoryId);
    }


    public int getProductCountInCategory(int categoryId) {
        if (categoryId <= 0) return 0;
        return categoryDAO.getProductCountInCategory(categoryId);
    }


    public int getChildCategoryCount(int parentId) {
        if (parentId <= 0) return 0;
        return categoryDAO.getChildCategoryCount(parentId);
    }


    public boolean toggleCategoryStatus(int categoryId) {
        if (categoryId <= 0) {
            return false;
        }
        return categoryDAO.toggleCategoryStatus(categoryId);
    }

    public int checkCanDeleteCategory(int categoryId) {
        if (categoryId <= 0) return -1;

        Category category = categoryDAO.getCategoryById(categoryId);
        if (category == null) {
            return -1;
        }

        int productCount = getProductCountInCategory(categoryId);
        if (productCount > 0) {
            return 1;
        }

        int childCount = getChildCategoryCount(categoryId);
        if (childCount > 0) {
            return 2;
        }

        return 0;
    }
}