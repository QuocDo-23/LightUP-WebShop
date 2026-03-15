package code.web.lightup.controller.Admin.Products;


import code.web.lightup.model.ProductWithDetails;
import code.web.lightup.service.CategoryService;
import code.web.lightup.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/admin/products/view")
public class ProductViewServlet extends HttpServlet {
    private ProductService productDAO;



    @Override
    public void init() throws ServletException {
        productDAO = new ProductService();
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
            Optional<ProductWithDetails> productOpt = productDAO.getProductById(productId);

            if (productOpt.isPresent()) {
                req.setAttribute("product", productOpt.get());
                req.setAttribute("currentPage", "products");

                req.getRequestDispatcher("/views/admin/Products/product_detail.jsp").forward(req, resp);
            } else {
                req.setAttribute("errorMessage", "Không tìm thấy sản phẩm với ID: " + productId);
                resp.sendRedirect(req.getContextPath() + "/Admin/products");
            }

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/Admin/products");
        }
    }
}