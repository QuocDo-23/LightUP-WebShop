package code.web.lightup.util;

import code.web.lightup.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Utility class for session management
 */
public class SessionUtil {

    /**
     * Lấy user từ session
     */
    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }

    /**
     * Kiểm tra user đã login chưa
     */
    public static boolean isLoggedIn(HttpServletRequest request) {

        return getCurrentUser(request) != null;
    }

    /**
     * Kiểm tra user có phải admin không
     */
    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            return user != null && "Admin".equalsIgnoreCase(user.getRoleName());
        }
        return false;
    }
    /**
     * Lấy user ID từ session
     */
    public static Integer getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Integer) session.getAttribute("userId");
        }
        return null;
    }

    /**
     * Set user info vào session
     */
    public static void setUserSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.getRoleName());
    }

    /**
     * Clear session
     */
    public static void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}