package code.web.lightup.controller.Admin.DashBoard;

import code.web.lightup.model.DashboardStats;
import code.web.lightup.model.User;
import code.web.lightup.service.OrderService;
import code.web.lightup.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private OrderService orderService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        DashboardStats stats = new DashboardStats();

        stats.setTotalCustomers(userService.getTotalCustomerCount());
        stats.setPendingOrders(orderService.getOrderCountByStatus("pending"));
        stats.setMonthRevenue(orderService.getCurrentMonthRevenue());
        stats.setMonthOrders(orderService.getCurrentMonthOrderCount());
        stats.setTopProducts(orderService.getTopSellingProducts(5));

        request.setAttribute("stats", stats);
        request.setAttribute("currentPage", "dashboard");

        request.getRequestDispatcher("/views/admin/dashboard.jsp")
                .forward(request, response);
    }
}
