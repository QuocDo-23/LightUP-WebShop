package code.web.lightup.controller.Admin.Inventory.Export;

import code.web.lightup.dao.InventoryManagementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import code.web.lightup.model.Product;
import java.io.IOException;
import code.web.lightup.dao.InventoryTransactionDAO;

@WebServlet("/admin/inventory/export")
public class AdminExportInventoryServlet extends HttpServlet {

    private InventoryManagementDAO inventoryDAO;
    private InventoryTransactionDAO transactionDAO;

    @Override
    public void init() {
        inventoryDAO = new InventoryManagementDAO();
        transactionDAO = new InventoryTransactionDAO();
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
                "inventory-export"
        );

        request.getRequestDispatcher(
                        "/views/admin/Inventory/export_inventory.jsp")
                .forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        int productId =
                Integer.parseInt(
                        request.getParameter("productId"));

        int quantity =
                Integer.parseInt(
                        request.getParameter("quantity"));

        String reason =
                request.getParameter("reason");

        Product product =
                inventoryDAO.getProductById(productId);

        if(product == null){

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/inventory/export");

            return;
        }

        if(quantity > product.getInventoryQuantity()){

            request.setAttribute(
                    "error",
                    "Số lượng xuất vượt quá tồn kho!"
            );

            doGet(request,response);

            return;
        }

        inventoryDAO.exportStock(
                productId,
                quantity
        );

        transactionDAO.addTransaction(
                productId,
                "SALE",
                -quantity,
                reason,
                null,
                null
        );

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/inventory"
        );
    }
}