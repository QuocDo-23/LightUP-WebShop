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

    public List<Category> getSubCategories() {
        return categoryDAO.getSubCategories();
    }

    public Category getCategoryById(int id) {
        return categoryDAO.getCategoryById(id);
    }
    public List<Category> getProductCategories() {
        return categoryDAO.getProductCategories();
    }

}