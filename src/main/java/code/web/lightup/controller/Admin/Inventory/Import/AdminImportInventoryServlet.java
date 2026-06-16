package code.web.lightup.controller.Admin.Inventory.Import;

import code.web.lightup.dao.InventoryManagementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/inventory/import")
public class AdminImportInventoryServlet extends HttpServlet {

    private InventoryManagementDAO inventoryDAO;

    @Override
    public void init() {
        inventoryDAO = new InventoryManagementDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "products",
                inventoryDAO.getAllProducts()
        );

        request.setAttribute(
                "currentPage",
                "inventory-import"
        );

        request.getRequestDispatcher(
                        "/views/admin/Inventory/import_inventory.jsp")
                .forward(request,response);
    }
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        int productId =
                Integer.parseInt(request.getParameter("productId"));

        int quantity =
                Integer.parseInt(request.getParameter("quantity"));

        String reason =
                request.getParameter("reason");

        inventoryDAO.importStock(
                productId,
                quantity,
                reason
        );

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/inventory"
        );
    }
}