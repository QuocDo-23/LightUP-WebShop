package code.web.lightup.controller.Admin.categories;

import code.web.lightup.model.Category;
import code.web.lightup.service.CategoryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/categories")
public class AdminCategoryViewServlet extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Category> rootCategories = categoryService.getRootCategories();

        Map<Integer, List<Category>> childrenMap = new HashMap<>();

        for (Category root : rootCategories) {
            List<Category> children = categoryService.getChildCategories(root.getId());
            childrenMap.put(root.getId(), children);

            for (Category child : children) {
                List<Category> grandchildren = categoryService.getChildCategories(child.getId());
                childrenMap.put(child.getId(), grandchildren);
            }
        }

        String successCat = req.getParameter("successCat");
        String errorCat = req.getParameter("errorCat");

        if ("add".equals(successCat)) {
            req.setAttribute("successMessage", "Thêm danh mục thành công!");
        } else if ("edit".equals(successCat)) {
            req.setAttribute("successMessage", "Cập nhật danh mục thành công!");
        } else if ("delete".equals(successCat)) {
            req.setAttribute("successMessage", "Xóa danh mục thành công!");
        } else if ("empty".equals(errorCat)) {
            req.setAttribute("errorMessage", "Tên danh mục không được để trống.");
        } else if ("fail".equals(errorCat)) {
            req.setAttribute("errorMessage", "Thao tác thất bại, vui lòng thử lại.");
        } else if ("notfound".equals(errorCat)) {
            req.setAttribute("errorMessage", "Danh mục không tồn tại.");
        } else if ("invalid".equals(errorCat)) {
            req.setAttribute("errorMessage", "Dữ liệu không hợp lệ.");
        }

        req.setAttribute("rootCategories", rootCategories);
        req.setAttribute("childrenMap", childrenMap);
        req.setAttribute("categoryService", categoryService);
        req.setAttribute("currentPage", "categories");

        req.getRequestDispatcher("/views/admin/Products/categories.jsp").forward(req, resp);
    }
}