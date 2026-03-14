package code.web.lightup.model.News;

public class NewsContent {
    private int id;
    private int articleId;
    private String content;
    private ContentType contentType;
    private int displayOrder;

    public NewsContent() {
    }

    public NewsContent(int id, int articleId, String content, ContentType contentType, int displayOrder) {
        this.id = id;
        this.articleId = articleId;
        this.content = content;
        this.contentType = contentType;
        this.displayOrder = displayOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}