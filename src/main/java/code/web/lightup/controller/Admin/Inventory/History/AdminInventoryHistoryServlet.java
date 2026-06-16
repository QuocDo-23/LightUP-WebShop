package code.web.lightup.controller.Admin.Inventory.History;

import code.web.lightup.dao.InventoryManagementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/inventory/history")
public class AdminInventoryHistoryServlet extends HttpServlet {

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
                "transactions",
                inventoryDAO.getRecentTransactions()
        );

        request.setAttribute(
                "currentPage",
                "inventory-history"
        );

        request.getRequestDispatcher(
                        "/views/admin/Inventory/inventory_history.jsp")
                .forward(request, response);
    }
}