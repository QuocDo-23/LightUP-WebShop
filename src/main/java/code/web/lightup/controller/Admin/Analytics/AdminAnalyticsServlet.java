package code.web.lightup.controller.Admin.Analytics;


import code.web.lightup.dao.OrderDAO;
import code.web.lightup.dao.UserDAO;
import code.web.lightup.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/analytics")
public class AdminAnalyticsServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private UserDAO userDAO;

    @Override
    public void init() {
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }


        List<Map<String, Object>> monthlyRevenue = orderDAO.getMonthlyRevenue(6);


        int pending     = orderDAO.getOrderCountByStatus("pending");
        int delivering  = orderDAO.getOrderCountByStatus("delivering");
        int delivered   = orderDAO.getOrderCountByStatus("delivered");
        int cancelled   = orderDAO.getOrderCountByStatus("cancelled");


        double totalRevenue = orderDAO.getTotalRevenue();
        int totalOrders = orderDAO.getTotalOrderCount();
        int totalCustomers = userDAO.getTotalCustomerCount();
        int processingOrders = pending + delivering;


        request.setAttribute("monthlyRevenue", monthlyRevenue);

        request.setAttribute("pending", pending);
        request.setAttribute("delivering", delivering);
        request.setAttribute("delivered", delivered);
        request.setAttribute("cancelled", cancelled);

        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("processingOrders", processingOrders);

        request.setAttribute("currentPage", "analytics");

        request.getRequestDispatcher("/Admin/analytics.jsp")
                .forward(request, response);
    }
}
