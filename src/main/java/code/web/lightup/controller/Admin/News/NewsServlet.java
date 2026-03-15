package code.web.lightup.controller.Admin.News;

import code.web.lightup.model.News.News;
import code.web.lightup.model.User;
import code.web.lightup.service.NewsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/admin/news")
public class NewsServlet extends HttpServlet {

    private NewsService newsService;

    @Override
    public void init() throws ServletException {
        newsService = new NewsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        String searchKeyword = request.getParameter("search");
        List<News> newsList;

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            newsList = newsService.searchArticles(searchKeyword, 50, 0);
        } else {
            newsList = newsService.getArticle(50);
        }

        request.setAttribute("newsList", newsList);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("currentPage", "news");
        request.getRequestDispatcher("/views/admin/news.jsp").forward(request, response);
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
        String message = "";

        String idStr = request.getParameter("id");
        int articleId = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;

        String title = request.getParameter("newsTitle");
        String imageLink = request.getParameter("imageLink");
        String newsCategoryStr = request.getParameter("newsCategory");
        String content = request.getParameter("newsContent");

        int categoryId = (newsCategoryStr != null && !newsCategoryStr.isEmpty()) ? Integer.parseInt(newsCategoryStr) : 2;

        News news = new News();
        news.setId(articleId);
        news.setTitle(title);
        news.setMainImg(imageLink);
        news.setCategoryId(categoryId);
        news.setDescription(content);
        news.setDateOfPosting(Timestamp.valueOf(LocalDateTime.now()));

        news.setSlug(title != null ? title.toLowerCase().replaceAll("\\s+", "-") : "");

        news.setFeature(categoryId == 1);

        boolean success = false;

        /**
         * thêm tin mới
         */
        if ("add".equals(action)) {
            int newId = newsService.insertArticle(news);
            success = newId > 0;
            message = success ? "Thêm tin tức thành công!" : "Thêm tin tức thất bại!";
        }

        /**
         * cập nhật tin
         */
        else if ("update".equals(action)) {
            int updatedRows = newsService.updateArticle(news);
            success = updatedRows > 0;
            message = success ? "Cập nhật tin tức thành công!" : "Cập nhật tin tức thất bại!";
        }

        /**
         * xóa tin tức
         */
        else if ("delete".equals(action)) {
            int deletedRows = newsService.deleteArticle(articleId);
            success = deletedRows > 0;
            message = success ? "Xóa tin tức thành công!" : "Xóa tin tức thất bại!";
        }

        response.sendRedirect(request.getContextPath() + "/admin/news?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}