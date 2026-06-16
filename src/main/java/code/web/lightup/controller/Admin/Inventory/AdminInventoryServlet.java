package code.web.lightup.controller.Admin.Inventory;

import code.web.lightup.dao.InventoryManagementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/inventory")
public class AdminInventoryServlet extends HttpServlet {
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
                "inventory"
        );

        request.getRequestDispatcher(
                        "/views/admin/Inventory/inventory.jsp")
                .forward(request, response);
    }
}