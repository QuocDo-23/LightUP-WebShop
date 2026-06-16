package code.web.lightup.controller.Admin.Inventory.Alert;

import code.web.lightup.dao.InventoryManagementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/inventory/alert")
public class AdminInventoryAlertServlet extends HttpServlet {

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
                inventoryDAO.getInventoryAlerts()
        );

        request.setAttribute(
                "currentPage",
                "inventory-alert"
        );

        request.getRequestDispatcher(
                "/views/admin/Inventory/inventory_alert.jsp"
        ).forward(request,response);
    }
}