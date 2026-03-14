package code.web.lightup.model.News;

import org.jdbi.v3.core.mapper.reflect.ColumnName;
import java.sql.Timestamp;

public class News {
    private int id;
    private Integer categoryId;
    private String title;
    private String description;
    private Timestamp dateOfPosting;
    private String slug;
    private String mainImg;
    private boolean feature;
    private String categoryName;

    public News() {
    }

    public News(int id, Integer categoryId, String title, String description,
                Timestamp dateOfPosting, String slug, String mainImg, boolean feature) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.dateOfPosting = dateOfPosting;
        this.slug = slug;
        this.mainImg = mainImg;
        this.feature = feature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    @ColumnName("category_id")
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDateOfPosting() {
        return dateOfPosting;
    }

    @ColumnName("date_of_posting")
    public void setDateOfPosting(Timestamp dateOfPosting) {
        this.dateOfPosting = dateOfPosting;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getMainImg() {
        return mainImg;
    }

    @ColumnName("mainImg")
    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public boolean isFeature() {
        return feature;
    }

    public void setFeature(boolean feature) {
        this.feature = feature;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @ColumnName("categoryName")
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}