package code.web.lightup.controller.User;

import code.web.lightup.model.News.News;
import code.web.lightup.service.NewsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/news")
public class NewsServlet extends HttpServlet {
    private NewsService newsService;

    @Override
    public void init() {
        newsService = new NewsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy tham số phân trang và sắp xếp
        String pageParam = request.getParameter("page");
        String sortParam = request.getParameter("sort");

        int currentPage = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        String sortBy = sortParam != null ? sortParam : "newest";
        int pageSize = 4;

        // Lấy bài viết nổi bật
        List<News> featuredArticles = newsService.getFeaturedArticles(4);

        // Lấy bài viết theo trang
        List<News> articles = newsService.getArticlesWithPagination(currentPage, pageSize, sortBy);

        // Tính tổng số trang
        int totalArticles = newsService.getTotalArticles();
        int totalPages = (int) Math.ceil((double) totalArticles / pageSize);

        request.setAttribute("featuredArticles", featuredArticles);
        request.setAttribute("articles", articles);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("sortBy", sortBy);

        request.getRequestDispatcher("/views/user/news.jsp").forward(request, response);
    }
}