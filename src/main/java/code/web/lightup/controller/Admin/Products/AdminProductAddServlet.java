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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@WebServlet("/admin/products/add")
public class AdminProductAddServlet extends HttpServlet {
    private ProductService productService;
    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Category> categories = categoryService.getProductCategories();
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", "products");

        req.getRequestDispatcher("/views/admin/Products/product_add.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            ProductWithDetails product = new ProductWithDetails();

            product.setName(req.getParameter("productName"));
            product.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
            product.setPrice(Double.parseDouble(req.getParameter("price")));
            product.setInventoryQuantity(Integer.parseInt(req.getParameter("stock")));

            String reviewParam = req.getParameter("review");
            if (reviewParam != null && !reviewParam.trim().isEmpty()) {
                product.setReview(new BigDecimal(reviewParam));
            } else {
                product.setReview(BigDecimal.ZERO);
            }

            String discountParam = req.getParameter("discountId");
            if (discountParam != null && !discountParam.trim().isEmpty()) {
                product.setDiscountId(Integer.parseInt(discountParam));
            }

            String status = req.getParameter("status");
            product.setStatus(status != null ? status : "active");

            product.setDescription(req.getParameter("description"));
            product.setMaterial(req.getParameter("material"));
            product.setVoltage(req.getParameter("voltage"));
            product.setDimensions(req.getParameter("dimension"));
            product.setType(req.getParameter("type"));
            product.setColor(req.getParameter("color"));
            product.setStyle(req.getParameter("style"));

            String warrantyParam = req.getParameter("warranty");
            if (warrantyParam != null && !warrantyParam.trim().isEmpty()) {
                product.setWarranty(warrantyParam);
            }

            String[] imageLinks = req.getParameterValues("imageLinks");
            if (imageLinks != null && imageLinks.length > 0) {
                product.setImages(
                        Arrays.stream(imageLinks)
                                .filter(s -> s != null && !s.trim().isEmpty())
                                .toList()
                );
            }
            boolean success = productService.insertProduct(product);

            if (success) {
                resp.sendRedirect(req.getContextPath() + "/admin/products?success=add");
            } else {
                req.setAttribute("error", "Có lỗi xảy ra khi thêm sản phẩm");
                List<Category> categories = categoryService.getProductCategories();
                req.setAttribute("categories", categories);
                req.setAttribute("product", product); 
                req.getRequestDispatcher("/views/admin/Products/product_add.jsp").forward(req, resp);
            }

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Dữ liệu nhập không hợp lệ: " + e.getMessage());
            List<Category> categories = categoryService.getProductCategories();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/views/admin/Products/product_add.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            List<Category> categories = categoryService.getProductCategories();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/views/admin/Products/product_add.jsp").forward(req, resp);
        }
    }
}