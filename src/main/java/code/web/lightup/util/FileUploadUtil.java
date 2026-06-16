package code.web.lightup.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUploadUtil {

    // Các extension được phép
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png", "gif", "webp")
    );

    // Kích thước tối đa cho một ảnh (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    // Đường dẫn upload (tương đối với project)
    private static final String UPLOAD_DIR = "/uploads/returns/";

    /**
     * Upload ảnh và trả về đường dẫn
     * @param part File part từ request
     * @param servletContext ServletContext
     * @return Đường dẫn file nếu thành công, null nếu thất bại
     */
    public static String uploadImage(Part part, ServletContext servletContext) {
        if (part == null) return null;

        String fileName = extractFileName(part);
        if (!isValidFile(fileName, part.getSize())) {
            return null;
        }

        try {
            String uploadPath = servletContext.getRealPath(UPLOAD_DIR);
            Path uploadDir = Paths.get(uploadPath);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String uniqueFileName = generateUniqueFileName(fileName);
            Path filePath = uploadDir.resolve(uniqueFileName);

            try (InputStream input = part.getInputStream()) {
                Files.copy(input, filePath);
            }

            // Trả về đường dẫn relative
            return UPLOAD_DIR + uniqueFileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Upload nhiều ảnh
     * @param parts Danh sách file parts
     * @param servletContext ServletContext
     * @return Danh sách đường dẫn ảnh
     */
    public static List<String> uploadMultipleImages(List<Part> parts, ServletContext servletContext) {
        List<String> uploadedPaths = new ArrayList<>();

        for (Part part : parts) {
            if (part.getSize() > 0) {
                String path = uploadImage(part, servletContext);
                if (path != null) {
                    uploadedPaths.add(path);
                }
            }
        }

        return uploadedPaths;
    }

    /**
     * Xóa ảnh
     * @param filePath Đường dẫn file cần xóa
     * @param servletContext ServletContext
     * @return true nếu xóa thành công
     */
    public static boolean deleteImage(String filePath, ServletContext servletContext) {
        try {
            String fullPath = servletContext.getRealPath(filePath);
            Path path = Paths.get(fullPath);

            if (Files.exists(path)) {
                Files.delete(path);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra file hợp lệ
     */
    private static boolean isValidFile(String fileName, long fileSize) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        // Kiểm tra kích thước
        if (fileSize > MAX_FILE_SIZE) {
            return false;
        }

        // Kiểm tra extension
        String extension = getFileExtension(fileName).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    /**
     * Lấy tên file từ Part
     */
    private static String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    /**
     * Lấy extension của file
     */
    private static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }

    /**
     * Tạo tên file unique
     */
    private static String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf(new Random().nextInt(10000));
        return timestamp + "_" + random + "." + extension;
    }

    /**
     * Kiểm tra xem file có phải ảnh hợp lệ không
     */
    public static boolean isImageValid(String filePath, ServletContext servletContext) {
        try {
            String fullPath = servletContext.getRealPath(filePath);
            Path path = Paths.get(fullPath);

            if (!Files.exists(path)) {
                return false;
            }

            String extension = getFileExtension(filePath).toLowerCase();
            return ALLOWED_EXTENSIONS.contains(extension);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách các extension được phép
     */
    public static Set<String> getAllowedExtensions() {
        return Collections.unmodifiableSet(ALLOWED_EXTENSIONS);
    }

    /**
     * Lấy kích thước tối đa của file (MB)
     */
    public static double getMaxFileSizeMB() {
        return MAX_FILE_SIZE / (1024.0 * 1024.0);
    }
}