package code.web.lightup.controller.User;

import code.web.lightup.model.News.News;
import code.web.lightup.model.News.NewsContent;
import code.web.lightup.service.NewsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/news_content")
public class NewsContentServlet extends HttpServlet {

    private NewsService newsService = new NewsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String slugParam = request.getParameter("slug");

        News news = null;

        try {
            if (idParam != null && !idParam.isEmpty()) {
                int articleId = Integer.parseInt(idParam);
                news = newsService.getArticleById(articleId);
            } else if (slugParam != null && !slugParam.isEmpty()) {
                news = newsService.getArticlesBySlug(slugParam);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Thiếu ID hoặc Slug của bài viết.");
                return;
            }

            //không tìm thấy bài viết
            if (news == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Không tìm thấy bài viết.");
                return;
            }

            // Lấy nội dung của bài viết
            List<NewsContent> contents = newsService.getContentByArticleId(news.getId());

            // Lấy bài viết liên quan (cùng danh mục, trừ bài hiện tại)
            List<News> relatedArticles = newsService.getRelatedArticles(news.getId(), news.getCategoryId(), 3);

            request.setAttribute("news", news);
            request.setAttribute("contents", contents);
            request.setAttribute("relatedArticles", relatedArticles);

            request.getRequestDispatcher("/views/user/news_content.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Định dạng ID không hợp lệ.");
        }
    }
}