package code.web.lightup.model;

import java.time.LocalDateTime;

public class ReturnImage {
    private int id;
    private int returnId;
    private String imagePath;
    private String fileName;
    private LocalDateTime uploadDate;

    public ReturnImage() {}

    public ReturnImage(int returnId, String imagePath, String fileName) {
        this.returnId = returnId;
        this.imagePath = imagePath;
        this.fileName = fileName;
        this.uploadDate = LocalDateTime.now();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "ReturnImage{" +
                "id=" + id +
                ", returnId=" + returnId +
                ", imagePath='" + imagePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}