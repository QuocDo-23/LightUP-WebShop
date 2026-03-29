package code.web.lightup.controller.User;

import code.web.lightup.model.Cart.Cart;
import code.web.lightup.model.User;
import code.web.lightup.service.CartService;
import code.web.lightup.service.UserService;
import code.web.lightup.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String errorParam = request.getParameter("error");
        if ("email_exists".equals(errorParam)) {
            request.setAttribute("error",
                    "Email này đã đăng ký bằng mật khẩu. Vui lòng đăng nhập thường.");
        }
        request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email");
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu");
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
            return;
        }
        if (password.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
            return;
        }
        if ( !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("error", "Email không hợp lệ");
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
            return;
        }

        Optional<User> userInfoOpt = userService.getUserLoginInfo(email);
        if(userInfoOpt.isPresent()){
            User userInfor = userInfoOpt.get();

            if(userInfor.getLockUntil() != null && LocalDateTime.now(ZoneOffset.UTC).isBefore(userInfor.getLockUntil()) ) {
                long  remainng = ChronoUnit.MINUTES.between(LocalDateTime.now(ZoneOffset.UTC), userInfor.getLockUntil());
                request.setAttribute("error", "Tài khoản bị khóa. Vui lòng thử lại sau " + remainng + " phút.");
                request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
                return;
            }
        }

        Optional<User> userOpt = userService.login(email, password);

        if (userOpt.isPresent()) {
            userService.resetFailedLoginAttempts(email);

            User user = userOpt.get();

            if (SessionUtil.isLoggedIn(request)) {
                Integer oldUserId = SessionUtil.getUserId(request);
                Cart currentCart = (Cart) request.getSession().getAttribute("cart");
                if (oldUserId != null && currentCart != null && !currentCart.getListItem().isEmpty()) {
                    new CartService().addCartToDb(oldUserId, currentCart);
                }
            }
            Cart guestCart = (Cart) request.getSession().getAttribute("cart");
            SessionUtil.setUserSession(request, user);

            Cart mergedCart = cartService.mergeOnLogin(user.getId(), guestCart);
            request.getSession().setAttribute("cart", mergedCart);


            String contextPath = request.getContextPath();
            String redirectUrl;

            if ("payment".equals(redirect)) {
                redirectUrl = contextPath + "/payment";
            } else if ("cart".equals(redirect)) {
                redirectUrl = contextPath + "/cart";
            } else if (SessionUtil.isAdmin(request)) {
                redirectUrl = contextPath + "/admin/dashboard";
            } else {
                redirectUrl = contextPath + "/";
            }
            response.sendRedirect(redirectUrl);
        } else {
            if (userInfoOpt.isPresent()) {
                User userInfo = userInfoOpt.get();

                if ("google".equalsIgnoreCase(userInfo.getAuthProvider())) {
                    request.setAttribute("error",
                            "Tài khoản này đăng ký qua Google. Vui lòng đăng nhập bằng Google.");
                    request.setAttribute("email", email);
                    request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
                    return;
                }

                userService.recordFailedLoginAttempt(email);

                Optional<User> updatedUser = userService.getUserLoginInfo(email);
                if (updatedUser.isPresent() && updatedUser.get().getFailedAttempts() >= 5) {
                    request.setAttribute("error",
                            "Tài khoản bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau 15 phút.");
                } else {
                    request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
                }

            } else {
                request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
            }
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
        }
    }
}