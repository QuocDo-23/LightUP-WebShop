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
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/products/edit")
public class AdminProductEditServlet extends HttpServlet {
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

        String idParam = req.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);

            // Lấy thông tin sản phẩm
            Optional<ProductWithDetails> productOpt = productDAO.getProductById(productId);

            if (productOpt.isEmpty()) {
                req.setAttribute("error", "Không tìm thấy sản phẩm");
                resp.sendRedirect(req.getContextPath() + "/admin/products");
                return;
            }

            ProductWithDetails product = productOpt.get();

            List<Category> categories = categoryDAO.getProductCategories();

            // Set attributes
            req.setAttribute("product", product);
            req.setAttribute("categories", categories);
            req.setAttribute("currentPage", "products");

            req.getRequestDispatcher("/views/admin/Products/product_edit.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String idParam = req.getParameter("productId");

        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);

            ProductWithDetails product = new ProductWithDetails();
            product.setId(productId);

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
            if (imageLinks != null) {
                product.setImages(
                        java.util.Arrays.stream(imageLinks)
                                .filter(s -> s != null && !s.trim().isEmpty())
                                .toList()
                );
            }
            boolean success = productDAO.updateProduct(product);

            if (success) {
                resp.sendRedirect(req.getContextPath() + "/admin/products?success=edit");
            } else {
                // Thất bại - quay lại form với thông báo lỗi
                req.setAttribute("error", "Có lỗi xảy ra khi cập nhật sản phẩm");
                List<Category> categories = categoryDAO.getProductCategories();
                req.setAttribute("categories", categories);
                req.setAttribute("product", product);
                req.getRequestDispatcher("/views/admin/Products/product_edit.jsp").forward(req, resp);
            }

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Dữ liệu nhập không hợp lệ: " + e.getMessage());

            try {
                int productId = Integer.parseInt(idParam);
                Optional<ProductWithDetails> productOpt = productDAO.getProductById(productId);
                if (productOpt.isPresent()) {
                    req.setAttribute("product", productOpt.get());
                }
            } catch (Exception ex) {
                // Ignore
            }

            List<Category> categories = categoryDAO.getProductCategories();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/views/admin/Products/product_edit.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());

            try {
                int productId = Integer.parseInt(idParam);
                Optional<ProductWithDetails> productOpt = productDAO.getProductById(productId);
                if (productOpt.isPresent()) {
                    req.setAttribute("product", productOpt.get());
                }
            } catch (Exception ex) {

            }

            List<Category> categories = categoryDAO.getProductCategories();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/views/admin/Products/product_edit.jsp").forward(req, resp);
        }
    }
}