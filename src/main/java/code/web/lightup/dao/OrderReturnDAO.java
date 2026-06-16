package code.web.lightup.dao;

import code.web.lightup.model.OrderReturn;
import code.web.lightup.model.ReturnImage;
import code.web.lightup.util.BaseDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jdbi.v3.core.Jdbi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderReturnDAO {

    private final Jdbi jdbi;
    private final Gson gson = new Gson();

    public OrderReturnDAO() {
        this.jdbi = BaseDao.get();
    }

    /**
     * Chèn yêu cầu trả hàng mới
     */
    public int insertOrderReturn(OrderReturn orderReturn) {
        String sql = "INSERT INTO order_returns (order_id, user_id, reason, description, status, evidence_images, request_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String imagesJson = gson.toJson(orderReturn.getEvidenceImages() != null ? orderReturn.getEvidenceImages() : new ArrayList<>());

        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind(0, orderReturn.getOrderId())
                .bind(1, orderReturn.getUserId())
                .bind(2, orderReturn.getReason())
                .bind(3, orderReturn.getDescription())
                .bind(4, orderReturn.getStatus())
                .bind(5, imagesJson)
                .bind(6, orderReturn.getRequestDate())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Integer.class)
                .findOne()
                .orElse(-1));
    }

    /**
     * Lấy yêu cầu trả hàng theo ID
     */
    public OrderReturn getOrderReturnById(int id) {
        String sql = "SELECT * FROM order_returns WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, id)
                    .map((rs, ctx) -> {
                        OrderReturn orderReturn = new OrderReturn();
                        orderReturn.setId(rs.getInt("id"));
                        orderReturn.setOrderId(rs.getInt("order_id"));
                        orderReturn.setUserId(rs.getInt("user_id"));
                        orderReturn.setReason(rs.getString("reason"));
                        orderReturn.setDescription(rs.getString("description"));
                        orderReturn.setStatus(rs.getString("status"));
                        orderReturn.setAdminNotes(rs.getString("admin_notes"));

                        // Parse JSON images
                        String imagesJson = rs.getString("evidence_images");
                        if (imagesJson != null && !imagesJson.isEmpty()) {
                            try {
                                List<String> images = gson.fromJson(imagesJson, new TypeToken<List<String>>(){}.getType());
                                orderReturn.setEvidenceImages(images);
                            } catch (Exception e) {
                                orderReturn.setEvidenceImages(new ArrayList<>());
                            }
                        } else {
                            orderReturn.setEvidenceImages(new ArrayList<>());
                        }

                        java.sql.Timestamp requestTs = rs.getTimestamp("request_date");
                        if (requestTs != null) orderReturn.setRequestDate(requestTs.toLocalDateTime());

                        java.sql.Timestamp approvalTs = rs.getTimestamp("approval_date");
                        if (approvalTs != null) orderReturn.setApprovalDate(approvalTs.toLocalDateTime());

                        java.sql.Timestamp completionTs = rs.getTimestamp("completion_date");
                        if (completionTs != null) orderReturn.setCompletionDate(completionTs.toLocalDateTime());

                        Double refundAmount = (Double) rs.getObject("refund_amount");
                        if (refundAmount != null) orderReturn.setRefundAmount(refundAmount);

                        java.sql.Timestamp refundTs = rs.getTimestamp("refund_date");
                        if (refundTs != null) orderReturn.setRefundDate(refundTs.toLocalDateTime());

                        return orderReturn;
                    })
                    .findOne()
                    .orElse(null);
        });
    }

    /**
     * Lấy yêu cầu trả hàng theo Order ID
     */
    public OrderReturn getOrderReturnByOrderId(int orderId) {
        String sql = "SELECT * FROM order_returns WHERE order_id = ? ORDER BY request_date DESC LIMIT 1";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, orderId)
                    .map((rs, ctx) -> mapOrderReturn(rs))
                    .findOne()
                    .orElse(null);
        });
    }

    /**
     * Lấy tất cả yêu cầu trả hàng của một user
     */
    public List<OrderReturn> getOrderReturnsByUserId(int userId) {
        String sql = "SELECT * FROM order_returns WHERE user_id = ? ORDER BY request_date DESC";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, userId)
                    .map((rs, ctx) -> mapOrderReturn(rs))
                    .list();
        });
    }

    /**
     * Lấy tất cả yêu cầu trả hàng
     */
    public List<OrderReturn> getAllOrderReturns() {
        String sql = "SELECT * FROM order_returns ORDER BY request_date DESC";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .map((rs, ctx) -> mapOrderReturn(rs))
                    .list();
        });
    }

    /**
     * Lấy yêu cầu trả hàng theo trạng thái
     */
    public List<OrderReturn> getOrderReturnsByStatus(String status) {
        String sql = "SELECT * FROM order_returns WHERE status = ? ORDER BY request_date DESC";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, status)
                    .map((rs, ctx) -> mapOrderReturn(rs))
                    .list();
        });
    }

    /**
     * Cập nhật trạng thái yêu cầu trả hàng
     */
    public boolean updateOrderReturnStatus(int id, String status) {
        String sql = "UPDATE order_returns SET status = ? WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createUpdate(sql)
                    .bind(0, status)
                    .bind(1, id)
                    .execute() > 0;
        });
    }

    /**
     * Duyệt yêu cầu trả hàng
     */
    public boolean approveOrderReturn(int id, double refundAmount) {
        String sql = "UPDATE order_returns SET status = ?, approval_date = ?, refund_amount = ? WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createUpdate(sql)
                    .bind(0, "approved")
                    .bind(1, LocalDateTime.now())
                    .bind(2, refundAmount)
                    .bind(3, id)
                    .execute() > 0;
        });
    }

    /**
     * Từ chối yêu cầu trả hàng
     */
    public boolean rejectOrderReturn(int id, String adminNotes) {
        String sql = "UPDATE order_returns SET status = ?, admin_notes = ? WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createUpdate(sql)
                    .bind(0, "rejected")
                    .bind(1, adminNotes)
                    .bind(2, id)
                    .execute() > 0;
        });
    }

    /**
     * Hoàn thành yêu cầu trả hàng
     */
    public boolean completeOrderReturn(int id, LocalDateTime refundDate) {
        String sql = "UPDATE order_returns SET status = ?, completion_date = ?, refund_date = ? WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createUpdate(sql)
                    .bind(0, "completed")
                    .bind(1, LocalDateTime.now())
                    .bind(2, refundDate)
                    .bind(3, id)
                    .execute() > 0;
        });
    }

    /**
     * Cập nhật ghi chú của admin
     */
    public boolean updateAdminNotes(int id, String adminNotes) {
        String sql = "UPDATE order_returns SET admin_notes = ? WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createUpdate(sql)
                    .bind(0, adminNotes)
                    .bind(1, id)
                    .execute() > 0;
        });
    }

    /**
     * Xóa yêu cầu trả hàng
     */
    public boolean deleteOrderReturn(int id) {
        String sql = "DELETE FROM order_returns WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createUpdate(sql)
                    .bind(0, id)
                    .execute() > 0;
        });
    }

    /**
     * Chèn ảnh minh chứng
     */
    public int insertReturnImage(ReturnImage image) {
        String sql = "INSERT INTO return_images (return_id, image_path, file_name, upload_date) VALUES (?, ?, ?, ?)";

        return jdbi.withHandle(handle -> handle.createUpdate(sql)
                .bind(0, image.getReturnId())
                .bind(1, image.getImagePath())
                .bind(2, image.getFileName())
                .bind(3, image.getUploadDate())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Integer.class)
                .findOne()
                .orElse(-1));
    }

    /**
     * Lấy danh sách ảnh theo return ID
     */
    public List<ReturnImage> getReturnImages(int returnId) {
        String sql = "SELECT * FROM return_images WHERE return_id = ? ORDER BY upload_date ASC";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, returnId)
                    .map((rs, ctx) -> {
                        ReturnImage image = new ReturnImage();
                        image.setId(rs.getInt("id"));
                        image.setReturnId(rs.getInt("return_id"));
                        image.setImagePath(rs.getString("image_path"));
                        image.setFileName(rs.getString("file_name"));

                        java.sql.Timestamp uploadTs = rs.getTimestamp("upload_date");
                        if (uploadTs != null) image.setUploadDate(uploadTs.toLocalDateTime());

                        return image;
                    })
                    .list();
        });
    }

    /**
     * Xóa ảnh
     */
    public boolean deleteReturnImage(int imageId) {
        String sql = "DELETE FROM return_images WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createUpdate(sql)
                    .bind(0, imageId)
                    .execute() > 0;
        });
    }

    /**
     * Đếm yêu cầu trả hàng theo trạng thái
     */
    public int countOrderReturnsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM order_returns WHERE status = ?";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, status)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(0);
        });
    }

    /**
     * Kiểm tra xem đơn hàng đã có yêu cầu trả hàng chưa
     */
    public boolean hasOrderReturn(int orderId) {
        String sql = "SELECT COUNT(*) FROM order_returns WHERE order_id = ?";

        return jdbi.withHandle(handle -> {
            Integer count = handle.createQuery(sql)
                    .bind(0, orderId)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(0);
            return count > 0;
        });
    }

    private OrderReturn mapOrderReturn(java.sql.ResultSet rs) throws java.sql.SQLException {
        OrderReturn orderReturn = new OrderReturn();
        orderReturn.setId(rs.getInt("id"));
        orderReturn.setOrderId(rs.getInt("order_id"));
        orderReturn.setUserId(rs.getInt("user_id"));
        orderReturn.setReason(rs.getString("reason"));
        orderReturn.setDescription(rs.getString("description"));
        orderReturn.setStatus(rs.getString("status"));
        orderReturn.setAdminNotes(rs.getString("admin_notes"));

        String imagesJson = rs.getString("evidence_images");
        if (imagesJson != null && !imagesJson.isEmpty()) {
            try {
                List<String> images = gson.fromJson(imagesJson, new TypeToken<List<String>>(){}.getType());
                orderReturn.setEvidenceImages(images);
            } catch (Exception e) {
                orderReturn.setEvidenceImages(new ArrayList<>());
            }
        } else {
            orderReturn.setEvidenceImages(new ArrayList<>());
        }

        java.sql.Timestamp requestTs = rs.getTimestamp("request_date");
        if (requestTs != null) orderReturn.setRequestDate(requestTs.toLocalDateTime());

        java.sql.Timestamp approvalTs = rs.getTimestamp("approval_date");
        if (approvalTs != null) orderReturn.setApprovalDate(approvalTs.toLocalDateTime());

        java.sql.Timestamp completionTs = rs.getTimestamp("completion_date");
        if (completionTs != null) orderReturn.setCompletionDate(completionTs.toLocalDateTime());

        Double refundAmount = (Double) rs.getObject("refund_amount");
        if (refundAmount != null) orderReturn.setRefundAmount(refundAmount);

        java.sql.Timestamp refundTs = rs.getTimestamp("refund_date");
        if (refundTs != null) orderReturn.setRefundDate(refundTs.toLocalDateTime());

        return orderReturn;
    }
}