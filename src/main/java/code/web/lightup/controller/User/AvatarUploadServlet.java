package code.web.lightup.controller.User;

import code.web.lightup.model.User;
import code.web.lightup.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebServlet("/upload-avatar")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize       = 5 * 1024 * 1024,
        maxRequestSize    = 10 * 1024 * 1024
)
public class AvatarUploadServlet extends HttpServlet {

    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng đăng nhập tài khoản.\"}");
            return;
        }

        Part filePart;
        try {
            filePart = request.getPart("avatar");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Không tìm thấy file.\"}");
            return;
        }

        if (filePart == null || filePart.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng chọn ảnh.\"}");
            return;
        }

        String contentType = filePart.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Chỉ chấp nhận ảnh JPG, PNG, WEBP, GIF.\"}");
            return;
        }

        if (filePart.getSize() > 5 * 1024 * 1024) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Ảnh không được vượt quá 5MB.\"}");
            return;
        }

        String uploadDir = getServletContext().getRealPath("/uploads/avatars");
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String oldAvatar = user.getAvatarImg();
        if (oldAvatar != null && oldAvatar.startsWith("/uploads/avatars/")) {
            File oldFile = new File(getServletContext().getRealPath(oldAvatar));
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        String extension = getExtensionFromContentType(contentType);
        String newFileName = "avatar_" + user.getId() + "_" + UUID.randomUUID() + extension;
        String savePath = uploadDir + File.separator + newFileName;
        String avatarUrl = "/uploads/avatars/" + newFileName;

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false,\"message\":\"Lỗi khi lưu ảnh.\"}");
            return;
        }

        boolean updated = userService.updateAvatar(user.getId(), avatarUrl);
        if (!updated) {
            new File(savePath).delete();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false,\"message\":\"Lỗi khi cập nhật ảnh vào hệ thống.\"}");
            return;
        }

        user.setAvatarImg(avatarUrl);
        session.setAttribute("user", user);

        String contextPath = request.getContextPath();
        response.getWriter().write(
                "{\"success\":true,\"avatarUrl\":\"" + contextPath + avatarUrl + "\"," +
                "\"message\":\"Cập nhật ảnh đại diện thành công!\"}"
        );
    }

    private String getExtensionFromContentType(String contentType) {
        switch (contentType) {
            case "image/png":  return ".png";
            case "image/webp": return ".webp";
            case "image/gif":  return ".gif";
            default:           return ".jpg";
        }
    }
}
