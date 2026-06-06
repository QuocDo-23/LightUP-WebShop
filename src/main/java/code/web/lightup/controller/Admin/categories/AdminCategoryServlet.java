package code.web.lightup.controller.Admin.Products;

import code.web.lightup.service.CategoryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/admin/categories/add")
public class AdminCategoryServlet extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String name        = req.getParameter("categoryName");
        String parentIdStr = req.getParameter("parentId");
        String sortStr     = req.getParameter("sortOrder");
        boolean isAjax     = "true".equals(req.getParameter("ajax"));


        if (name == null || name.trim().isEmpty()) {
            if (isAjax) {
                sendJson(resp, false, -1, null, "Tên danh mục không được để trống.");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/products?errorCat=empty");
            }
            return;
        }

        Integer parentId = null;
        if (parentIdStr != null && !parentIdStr.trim().isEmpty()) {
            try {
                parentId = Integer.parseInt(parentIdStr);
            } catch (NumberFormatException e) {
                if (isAjax) {
                    sendJson(resp, false, -1, null, "Danh mục cha không hợp lệ.");
                    return;
                }
            }
        }

        int sortOrder = 0;
        if (sortStr != null && !sortStr.trim().isEmpty()) {
            try {
                sortOrder = Integer.parseInt(sortStr);
            } catch (NumberFormatException ignored) { }
        }


        int newId = categoryService.addCategory(name.trim(), parentId, sortOrder);

        if (isAjax) {

            if (newId > 0) {
                sendJson(resp, true, newId, name.trim(), null);
            } else {
                sendJson(resp, false, -1, null, "Thêm danh mục thất bại, vui lòng thử lại.");
            }
        } else {

            if (newId > 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/products?successCat=add");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/products?errorCat=fail");
            }
        }
    }


    private void sendJson(HttpServletResponse resp,
                          boolean success,
                          int categoryId,
                          String categoryName,
                          String message) throws IOException {

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        if (success) {
            String safeName = categoryName.replace("\"", "\\\"");
            out.print("{\"success\":true,"
                    + "\"categoryId\":"   + categoryId + ","
                    + "\"categoryName\":\"" + safeName + "\"}");
        } else {
            String safeMsg = (message != null ? message : "Lỗi không xác định")
                    .replace("\"", "\\\"");
            out.print("{\"success\":false,"
                    + "\"message\":\"" + safeMsg + "\"}");
        }
        out.flush();
    }
}