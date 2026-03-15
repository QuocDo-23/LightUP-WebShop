package code.web.lightup.controller.Admin.Products;

import code.web.lightup.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/products/delete")
public class AdminProductDeleteServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=invalid_id");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            boolean success = productService.deleteProduct(productId);

            if (success) {
                resp.sendRedirect(req.getContextPath() + "/admin/products?success=delete");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/products?error=delete_failed");
            }

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=invalid_id");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/admin/products?error=system_error");
        }
    }
}