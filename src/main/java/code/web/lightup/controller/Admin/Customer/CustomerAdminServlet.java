package code.web.lightup.controller.Admin.Customer;

import code.web.lightup.model.User;
import code.web.lightup.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/customers")
public class CustomerAdminServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String status  = request.getParameter("status");

        List<User> customers;

        if (keyword != null && !keyword.trim().isEmpty()) {
            customers = userService.searchCustomers(keyword.trim());
        } else {
            customers = userService.getAllCustomers();
        }

        if (status != null && !status.trim().isEmpty()) {
            final String s = status.trim();
            customers = customers.stream()
                    .filter(u -> s.equalsIgnoreCase(u.getStatus()))
                    .collect(Collectors.toList());
        }

        request.setAttribute("customers", customers);
        request.setAttribute("currentPage", "customers");

        request.getRequestDispatcher("/views/admin/Customer/customers.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}