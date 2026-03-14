package code.web.lightup.service;
import code.web.lightup.dao.NewsContentDAO;
import code.web.lightup.dao.NewsDAO;
import code.web.lightup.model.News.News;
import code.web.lightup.model.News.NewsContent;

import java.util.List;

public class NewsService {
    private NewsDAO newsDAO;
    private NewsContentDAO newsContentDAO;

    public NewsService() {
        this.newsDAO = new NewsDAO();
        this.newsContentDAO = new NewsContentDAO();
    }

    public List<News> getFeaturedArticles(int limit) {
        return newsDAO.getFeaturedArticle(limit);
    }

    public List<News> getArticlesWithPagination(int page, int pageSize, String sortBy) {
        return newsDAO.getArticleWithPagination(page, pageSize, sortBy);
    }

    public int getTotalArticles() {
        return newsDAO.getTotalArticle();
    }

    public News getArticleById(int id) {
        return newsDAO.getArticleById(id);
    }

    public List<News> getArticle(int limit) {
        return newsDAO.getArticle(limit);
    }

    public News getArticlesBySlug(String slug) {
        return newsDAO.getArticleBySlug(slug);
    }

    public List<News> getRelatedArticles(int currentArticleId, Integer categoryId, int limit) {
        return newsContentDAO.getRelatedArticles(currentArticleId, categoryId, limit);
    }

    public List<NewsContent> getContentByArticleId(int articleId) {
        return newsContentDAO.getContentByArticleId(articleId);
    }
}
