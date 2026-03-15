package code.web.lightup.filter;

import code.web.lightup.model.User;
import code.web.lightup.service.CategoryService;
import code.web.lightup.service.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class HeaderFilter implements Filter {

    private CategoryService categoryService;
    private OrderService orderDAO;


    @Override
    public void init(FilterConfig filterConfig) {
        categoryService = new CategoryService();
        orderDAO = new OrderService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                int pendingOrderCount = orderDAO.countOrdersByUserId(user.getId());
                request.setAttribute("orderCount", pendingOrderCount);
            }
        }
            req.setAttribute("categories", categoryService.getSubCategories());
        chain.doFilter(request, response);
    }
}
