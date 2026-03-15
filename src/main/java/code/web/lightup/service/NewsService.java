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

    public List<News> searchArticles(String searchKeyword, int limit, int offset) {
        return newsDAO.searchArticles(searchKeyword, limit, offset);
    }

    public int insertArticle(News news) {
        return newsDAO.insertArticle(news);
    }

    public int updateArticle(News news) {
        return  newsDAO.updateArticle(news);
    }

    public int deleteArticle(int articleId) {
        return  newsDAO.deleteArticle(articleId);
    }

    public int getNextDisplayOrder(int articleId) {
        return  newsContentDAO.getNextDisplayOrder(articleId);
    }

    public boolean insertContent(NewsContent content) {
        return newsContentDAO.insertContent(content);
    }

    public boolean updateContent(NewsContent content) {
        return newsContentDAO.updateContent(content);
    }

    public boolean deleteContent(int contentId) {
        return  newsContentDAO.deleteContent(contentId);
    }
}
