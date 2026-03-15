package code.web.lightup.controller.Admin.Order;

import code.web.lightup.model.Order;
import code.web.lightup.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderListServlet", urlPatterns = {"/admin/orders"})
public class OrderListServlet extends HttpServlet {

    private OrderService OrderListServlet;

    @Override
    public void init() throws ServletException {
        OrderListServlet = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        listOrders(request, response);
    }
    
    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int pageSize = 10;

        String pageParam = request.getParameter("page");
        String pageSizeParam = request.getParameter("pageSize");

        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        if (pageSizeParam != null) {
            try {
                pageSize = Integer.parseInt(pageSizeParam);
                if (pageSize < 5) pageSize = 5;
                if (pageSize > 100) pageSize = 100;
            } catch (NumberFormatException e) {
                pageSize = 10;
            }
        }

        String status = request.getParameter("status");
        String searchKeyword = request.getParameter("search");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");

        if (status == null) status = "all";
        if (sortBy == null) sortBy = "order_date";
        if (sortOrder == null) sortOrder = "DESC";

        List<Order> orders = OrderListServlet.getOrdersWithPagination(
                page, pageSize, status, searchKeyword, sortBy, sortOrder
        );

        int totalRecords = OrderListServlet.countOrders(status, searchKeyword);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        Map<String, Integer> statusStats = OrderListServlet.getOrderStatusStatistics();

        // Set attributes
        request.setAttribute("orders", orders);
        request.setAttribute("currentPages", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("status", status);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);
        request.setAttribute("statusStats", statusStats);
        request.setAttribute("currentPage", "orders");


        request.getRequestDispatcher("/views/admin/Orders/orders-admin.jsp").forward(request, response);
    }
}
