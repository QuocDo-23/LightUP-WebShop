package code.web.lightup.controller.Admin.Products;

import code.web.lightup.model.Category;
import code.web.lightup.model.ProductWithDetails;
import code.web.lightup.service.CategoryService;
import code.web.lightup.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {
    private ProductService productDAO;
    private CategoryService categoryDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductService();
        categoryDAO = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String search = req.getParameter("search");
        String categoryIdStr = req.getParameter("categoryId");
        String status = req.getParameter("status");

        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryIdStr);
            } catch (NumberFormatException e) {
            }
        }

        int page = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        int itemsPerPage = 10;
        int offset = (page - 1) * itemsPerPage;

        List<ProductWithDetails> products = productDAO.getProductsWithPagination(
                search, categoryId, status, offset, itemsPerPage
        );

        int totalFiltered = productDAO.countProductsWithFilter(search, categoryId, status);
        int totalPages = (int) Math.ceil((double) totalFiltered / itemsPerPage);

        if (totalPages < 1) {
            totalPages = 1;
        }

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;


        int totalCount = productDAO.getTotalProductCount();
        int activeCount = productDAO.getProductCountByStatus("active");
        int lowStockCount = productDAO.getLowStockProductCount(20);
        int outOfStockCount = productDAO.getOutOfStockProductCount();

        String success = req.getParameter("success");
        if ("add".equals(success)) {
            req.setAttribute("successMessage", "Thêm sản phẩm thành công!");
        }

        List<Category> categories = categoryDAO.getProductCategories();

        // Set attributes cho JSP
        req.setAttribute("products", products);
        req.setAttribute("currentPages", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalProducts", totalCount);
        req.setAttribute("activeProducts", activeCount);
        req.setAttribute("lowStockProducts", lowStockCount);
        req.setAttribute("outOfStockProducts", outOfStockCount);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", "products");



        req.setAttribute("searchParam", search != null ? search : "");
        req.setAttribute("categoryParam", categoryIdStr != null ? categoryIdStr : "");
        req.setAttribute("statusParam", status != null ? status : "");

        req.getRequestDispatcher("/views/admin/products.jsp").forward(req, resp);
    }
}