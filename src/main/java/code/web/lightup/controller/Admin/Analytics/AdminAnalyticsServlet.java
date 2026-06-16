package code.web.lightup.controller.Admin.Analytics;

import code.web.lightup.dao.AnalyticsDAO;
import code.web.lightup.dao.OrderDAO;
import code.web.lightup.dao.UserDAO;
import code.web.lightup.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import code.web.lightup.dao.InventoryDAO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/analytics")
public class AdminAnalyticsServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private InventoryDAO inventoryDAO;
    private AnalyticsDAO analyticsDAO;

    @Override
    public void init() {
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
        inventoryDAO = new InventoryDAO();
        analyticsDAO = new AnalyticsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        List<Map<String, Object>> monthlyRevenue = orderDAO.getMonthlyRevenue(6);


        int pending     = orderDAO.getOrderCountByStatus("pending");
        int delivering  = orderDAO.getOrderCountByStatus("delivering");
        int delivered   = orderDAO.getOrderCountByStatus("delivered");
        int cancelled   = orderDAO.getOrderCountByStatus("cancelled");


        double totalRevenue = orderDAO.getTotalRevenue();
        int totalOrders = orderDAO.getTotalOrderCount();
        int totalCustomers = userDAO.getTotalCustomerCount();
        int processingOrders = pending + delivering;
        int todayImport = inventoryDAO.getTodayImportQuantity();

        int todaySale = inventoryDAO.getTodaySaleQuantity();

        int lowStock = inventoryDAO.getLowStockCount();

        int deadStock = inventoryDAO.getDeadStockCount();


        int lowStockPage = 1;

        String lowStockPageParam =
                request.getParameter("lowStockPage");

        if(lowStockPageParam != null){

            lowStockPage =
                    Integer.parseInt(lowStockPageParam);
        }



        int deadStockPage = 1;

        String deadStockPageParam =
                request.getParameter("deadStockPage");

        if(deadStockPageParam != null){

            deadStockPage =
                    Integer.parseInt(deadStockPageParam);
        }

        int limit = 10;

        int lowStockOffset =
                (lowStockPage - 1) * limit;

        int deadStockOffset =
                (deadStockPage - 1) * limit;

        request.setAttribute("todayImport", todayImport);

        request.setAttribute("todaySale", todaySale);

        request.setAttribute("lowStock", lowStock);

        request.setAttribute("deadStock", deadStock);
        request.setAttribute("monthlyRevenue", monthlyRevenue);

        request.setAttribute("pending", pending);
        request.setAttribute("delivering", delivering);
        request.setAttribute("delivered", delivered);
        request.setAttribute("cancelled", cancelled);

        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("processingOrders", processingOrders);
        request.setAttribute(
                "todayImports",
                analyticsDAO.getTodayImports()
        );

        request.setAttribute(
                "todaySales",
                analyticsDAO.getTodaySales()
        );

        request.setAttribute("currentPage", "analytics");

        request.setAttribute(
                "lowStockProducts",
                analyticsDAO.getLowStockProducts(
                        lowStockOffset,
                        limit
                )
        );

        request.setAttribute(
                "deadStockProducts",
                analyticsDAO.getDeadStockProducts(
                        deadStockOffset,
                        limit
                )
        );
        int totalLowStockProducts =
                analyticsDAO.countLowStockProducts();

        int totalLowStockPages =
                (int)Math.ceil(
                        (double) totalLowStockProducts / limit
                );

        request.setAttribute(
                "lowStockPage",
                lowStockPage
        );

        request.setAttribute(
                "totalLowStockPages",
                totalLowStockPages
        );

        int totalDeadStockProducts =
                analyticsDAO.countDeadStockProducts();

        int totalDeadStockPages =
                (int)Math.ceil(
                        (double) totalDeadStockProducts / limit
                );

        request.setAttribute(
                "deadStockPage",
                deadStockPage
        );

        request.setAttribute(
                "totalDeadStockPages",
                totalDeadStockPages
        );
        request.getRequestDispatcher("/views/admin/Analytic/analytics.jsp")
                .forward(request, response);
    }
}
