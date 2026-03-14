package code.web.lightup.model;

public class Category {
    private int id;
    private String name;
    private Integer parentId;
    private int sortOrder;
    private String imgCate;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getImgCate() {
        return imgCate;
    }

    public void setImgCate(String img) {
        this.imgCate = img;
    }
}
