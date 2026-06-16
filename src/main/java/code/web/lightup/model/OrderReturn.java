package code.web.lightup.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrderReturn {
    private int id;
    private int orderId;
    private int userId;
    private String reason;
    private String description;
    private String status;
    private List<String> evidenceImages;
    private String adminNotes;
    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;
    private LocalDateTime completionDate;
    private double refundAmount;
    private LocalDateTime refundDate;

    public OrderReturn() {}

    public OrderReturn(int orderId, int userId, String reason, String description) {
        this.orderId = orderId;
        this.userId = userId;
        this.reason = reason;
        this.description = description;
        this.status = "pending";
        this.requestDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getEvidenceImages() {
        return evidenceImages;
    }

    public void setEvidenceImages(List<String> evidenceImages) {
        this.evidenceImages = evidenceImages;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public LocalDateTime getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(LocalDateTime refundDate) {
        this.refundDate = refundDate;
    }

    public String getStatusDisplay() {
        switch (this.status) {
            case "pending":
                return "Chờ duyệt";
            case "approved":
                return "Được duyệt";
            case "rejected":
                return "Bị từ chối";
            case "completed":
                return "Hoàn thành";
            default:
                return this.status;
        }
    }

    public String getReasonDisplay() {
        switch (this.reason) {
            case "defective":
                return "Sản phẩm lỗi";
            case "wrong_item":
                return "Sai sản phẩm";
            case "damaged":
                return "Hàng bị hư hỏng";
            case "not_as_described":
                return "Không đúng mô tả";
            case "changed_mind":
                return "Thay đổi ý định";
            case "other":
                return "Lý do khác";
            default:
                return this.reason;
        }
    }

    @Override
    public String toString() {
        return "OrderReturn{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", requestDate=" + requestDate +
                '}';
    }
}