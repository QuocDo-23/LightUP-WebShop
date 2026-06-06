package code.web.lightup.controller.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import code.web.lightup.model.User;
import code.web.lightup.service.UserService;
import code.web.lightup.util.PasswordUtil;

import java.io.IOException;


@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private UserService userService;
    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("activeTab", "changePassword");

        request.getRequestDispatcher(
                "/views/user/change_password.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String currentPassword =
                request.getParameter("currentPassword");

        String newPassword =
                request.getParameter("newPassword");


        String confirmPassword =
                request.getParameter("confirmPassword");

        User currentUser =
                (User) request.getSession()
                        .getAttribute("user");

        if (currentUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }
        if ("google".equalsIgnoreCase(currentUser.getAuthProvider())
                || "facebook".equalsIgnoreCase(currentUser.getAuthProvider())) {

            request.setAttribute(
                    "message",
                    "Tài khoản đăng nhập bằng Google/Facebook không hỗ trợ đổi mật khẩu"
            );

            request.setAttribute(
                    "messageType",
                    "error"
            );

            request.setAttribute(
                    "activeTab",
                    "changePassword"
            );

            request.getRequestDispatcher(
                    "/views/user/change_password.jsp"
            ).forward(request, response);

            return;
        }

        boolean correctPassword =
                PasswordUtil.verifyPassword(
                        currentPassword,
                        currentUser.getPassword()
                );

        if (!correctPassword) {

            request.setAttribute(
                    "message",
                    "Mật khẩu hiện tại không đúng"
            );

            request.setAttribute(
                    "messageType",
                    "error"
            );

            request.setAttribute(
                    "activeTab",
                    "changePassword"
            );

            request.getRequestDispatcher(
                    "/views/user/change_password.jsp"
            ).forward(request, response);

            return;
        }
        if (newPassword.equals(currentPassword)) {

            request.setAttribute(
                    "message",
                    "Mật khẩu mới phải khác mật khẩu hiện tại"
            );

            request.setAttribute(
                    "messageType",
                    "error"
            );

            request.setAttribute(
                    "activeTab",
                    "changePassword"
            );

            request.getRequestDispatcher(
                    "/views/user/change_password.jsp"
            ).forward(request, response);

            return;
        }
        if (!newPassword.equals(confirmPassword)) {

            request.setAttribute(
                    "message",
                    "Xác nhận mật khẩu không khớp"
            );

            request.setAttribute(
                    "messageType",
                    "error"
            );

            request.setAttribute(
                    "activeTab",
                    "changePassword"
            );

            request.getRequestDispatcher(
                    "/views/user/change_password.jsp"
            ).forward(request, response);

            return;
        }
        if (newPassword.length() < 6) {

            request.setAttribute(
                    "message",
                    "Mật khẩu mới phải từ 6 ký tự trở lên"
            );

            request.setAttribute(
                    "messageType",
                    "error"
            );

            request.setAttribute(
                    "activeTab",
                    "changePassword"
            );

            request.getRequestDispatcher(
                    "/views/user/change_password.jsp"
            ).forward(request, response);

            return;
        }

        String hashedPassword =
                PasswordUtil.hashPassword(newPassword);

        boolean updated =
                userService.updatePasswordById(
                        currentUser.getId(),
                        hashedPassword
                );

        if (!updated) {

            request.setAttribute(
                    "message",
                    "Không thể cập nhật mật khẩu"
            );

            request.setAttribute(
                    "messageType",
                    "error"
            );

            request.setAttribute(
                    "activeTab",
                    "changePassword"
            );

            request.getRequestDispatcher(
                    "/views/user/change_password.jsp"
            ).forward(request, response);

            return;
        }

        currentUser.setPassword(hashedPassword);

        request.getSession()
                .setAttribute(
                        "user",
                        currentUser
                );

        request.setAttribute(
                "message",
                "Đổi mật khẩu thành công!"
        );

        request.setAttribute(
                "messageType",
                "success"
        );

        request.setAttribute(
                "activeTab",
                "changePassword"
        );

        request.getRequestDispatcher(
                "/views/user/change_password.jsp"
        ).forward(request, response);

       }

}