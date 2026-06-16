package code.web.lightup.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private int id;
    private int categoryId;
    private String name;
    private Integer discountId;
    private double price;
    private int inventoryQuantity;
    private BigDecimal review;
    private String status;
    private String mainImage;
    private boolean favorite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastImportDate;
    private LocalDateTime lastSaleDate;
    private long deadDays;

    private int minStock;
    public long getDeadDays() {
        return deadDays;
    }

    public void setDeadDays(long deadDays) {
        this.deadDays = deadDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public BigDecimal getReview() {
        return review;
    }

    public void setReview(BigDecimal review) {
        this.review = review;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMainImage() {return mainImage;}

    public void setMainImage(String mainImage) {this.mainImage = mainImage;}
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastImportDate() {
        return lastImportDate;
    }

    public void setLastImportDate(LocalDateTime lastImportDate) {
        this.lastImportDate = lastImportDate;
    }

    public LocalDateTime getLastSaleDate() {
        return lastSaleDate;
    }

    public void setLastSaleDate(LocalDateTime lastSaleDate) {
        this.lastSaleDate = lastSaleDate;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }



}
