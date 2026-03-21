package code.web.lightup.controller.Admin.Customer;

import code.web.lightup.model.Address;
import code.web.lightup.model.Order;
import code.web.lightup.model.User;
import code.web.lightup.service.AddressService;
import code.web.lightup.service.OrderService;
import code.web.lightup.service.UserService;
import code.web.lightup.util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/customer-detail")
public class CustomerDetailServlet extends HttpServlet {

    private UserService userService;
    private AddressService addressService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        addressService = new AddressService();
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/customers");
            return;
        }

        try {
            int customerId = Integer.parseInt(idStr);

            Optional<User> customerOpt = userService.getUserById(customerId);
            if (!customerOpt.isPresent()) {
                response.sendRedirect(request.getContextPath() + "/admin/customers");
                return;
            }
            User customer = customerOpt.get();

            List<Address> addresses = addressService.getAddressByUserId(customerId);

            List<Order> orders = orderService.getOrdersByUserId(customerId);

            request.setAttribute("customer", customer);
            request.setAttribute("addresses", addresses);
            request.setAttribute("orders", orders);
            request.setAttribute("currentPage", "customers");

            request.getRequestDispatcher("/views/admin/Customer/customer_detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/customers");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int customerId = Integer.parseInt(idStr);

                if ("lock".equals(action)) {
                    userService.updateUserStatus(customerId, "banned");
                } else if ("unlock".equals(action)) {
                    userService.updateUserStatus(customerId, "active");
                    userService.unlockUser(customerId);
                }  else if ("changeRole".equals(action)) {
                    String roleIdStr = request.getParameter("newRoleId");
                    if (roleIdStr != null && !roleIdStr.isEmpty()) {
                        try {
                            int newRoleId = Integer.parseInt(roleIdStr);
                            userService.updateUserRole(customerId, newRoleId);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } else if ("resetPassword".equals(action)) {
                    String newPassword = generateRandomPassword();
                    String hashedPassword = PasswordUtil.hashPassword(newPassword);

                    boolean success = userService.updatePasswordById(customerId, hashedPassword);

                    if (success) {
                        session.setAttribute("newPassword", newPassword);
                        session.setAttribute("passwordResetFor", customerId);
                    }
                }

                response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + customerId);
                return;

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/customers");
    }

    private String generateRandomPassword() {
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        String OTHER_CHAR = "!@#$%&";

        String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int rndCharAt = random.nextInt(PASSWORD_ALLOW_BASE.length());
            char rndChar = PASSWORD_ALLOW_BASE.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }
}