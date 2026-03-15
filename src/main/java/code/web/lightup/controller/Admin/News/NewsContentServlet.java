package code.web.lightup.controller.Admin.News;

import code.web.lightup.model.News.ContentType;
import code.web.lightup.model.News.News;
import code.web.lightup.model.News.NewsContent;
import code.web.lightup.service.NewsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import code.web.lightup.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/news-content")
public class NewsContentServlet extends HttpServlet {

    private NewsService newsService;

    @Override
    public void init() throws ServletException {
        newsService = new NewsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Kiểm tra quyền admin
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User admin = (User) session.getAttribute("user");
        if (admin.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/views/user/index.jsp");
            return;
        }

        String articleIdStr = request.getParameter("articleId");
        if (articleIdStr == null || articleIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/views/admin/news.jsp");
            return;
        }

        try {
            int articleId = Integer.parseInt(articleIdStr);
            News article = newsService.getArticleById(articleId);

            if (article == null) {
                response.sendRedirect(request.getContextPath() + "/views/admin/news.jsp");
                return;
            }

            List<NewsContent> contents = newsService.getContentByArticleId(articleId);

            request.setAttribute("article", article);
            request.setAttribute("contents", contents);
            request.setAttribute("currentPage", "news");
            request.getRequestDispatcher("/views/admin/news_content.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/views/admin/news.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User admin = (User) session.getAttribute("user");
        if (admin.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/views/user/index.jsp");
            return;
        }

        String action = request.getParameter("action");
        String articleIdStr = request.getParameter("articleId");

        if (articleIdStr == null || articleIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/views/admin/news.jsp");
            return;
        }

        int articleId = Integer.parseInt(articleIdStr);
        String message = "";

        try {
            if ("add".equals(action)) {
                String contentText = request.getParameter("content");
                String typeStr = request.getParameter("contentType");
                String orderStr = request.getParameter("displayOrder");

                NewsContent content = new NewsContent();
                content.setArticleId(articleId);
                content.setContent(contentText);
                content.setContentType(ContentType.valueOf(typeStr));

                int order = (orderStr != null && !orderStr.isEmpty())
                        ? Integer.parseInt(orderStr)
                        : newsService.getNextDisplayOrder(articleId);
                content.setDisplayOrder(order);

                boolean success = newsService.insertContent(content);
                message = success ? "Thêm nội dung thành công!" : "Thêm nội dung thất bại!";

            } else if ("update".equals(action)) {
                int contentId = Integer.parseInt(request.getParameter("contentId"));
                String contentText = request.getParameter("content");
                String typeStr = request.getParameter("contentType");
                int order = Integer.parseInt(request.getParameter("displayOrder"));

                NewsContent content = new NewsContent();
                content.setId(contentId);
                content.setArticleId(articleId);
                content.setContent(contentText);
                content.setContentType(ContentType.valueOf(typeStr));
                content.setDisplayOrder(order);

                boolean success = newsService.updateContent(content);
                message = success ? "Cập nhật nội dung thành công!" : "Cập nhật nội dung thất bại!";

            } else if ("delete".equals(action)) {
                int contentId = Integer.parseInt(request.getParameter("contentId"));
                boolean success = newsService.deleteContent(contentId);
                message = success ? "Xóa nội dung thành công!" : "Xóa nội dung thất bại!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Đã xảy ra lỗi: " + e.getMessage();
        }

        response.sendRedirect(request.getContextPath() + "/views/admin/news_content?articleId=" + articleId + "&message=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}