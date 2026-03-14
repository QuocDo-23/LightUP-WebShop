//package code.web.lightup.service;
//
//import code.web.webgroup9.dao.ArticleContentDAO;
//import code.web.webgroup9.dao.ArticleDAO;
//import code.web.webgroup9.model.Articles.Article;
//import code.web.webgroup9.model.Articles.ArticlesContent;
//
//import java.util.List;
//
//public class NewsService {
//    private final ArticleDAO articleDAO;
//    private final ArticleContentDAO articleContentDAO;
//
//    public NewsService() {
//        this.articleDAO = new ArticleDAO();
//        this.articleContentDAO = new ArticleContentDAO();
//    }
//
//    public List<Article> getFeaturedArticles(int limit) {
//        return articleDAO.getFeaturedArticle(limit);
//    }
//
//    public List<Article> getArticlesWithPagination(int page, int pageSize, String sortBy) {
//        return articleDAO.getArticleWithPagination(page, pageSize, sortBy);
//    }
//
//    public int getTotalArticles() {
//        return articleDAO.getTotalArticle();
//    }
//
//    public Article getArticleById(int id) {
//        return articleDAO.getArticleById(id);
//    }
//
//    public List<Article> getArticle(int limit) {
//        return articleDAO.getArticle(limit);
//    }
//
//    public Article getArticlesBySlug(String slug) {
//        return articleDAO.getArticleBySlug(slug);
//    }
//
//    public List<Article> getRelatedArticles(int currentArticleId, Integer categoryId, int limit) {
//        return articleContentDAO.getRelatedArticles(currentArticleId, categoryId, limit);
//    }
//
//    public List<ArticlesContent> getContentByArticleId(int articleId) {
//        return articleContentDAO.getContentByArticleId(articleId);
//    }
//
//
//}
