package code.web.lightup.service;

import code.web.lightup.dao.OrderDAO;
import code.web.lightup.dao.OrderReturnDAO;
import code.web.lightup.model.OrderReturn;
import code.web.lightup.model.ReturnImage;

import java.time.LocalDateTime;
import java.util.List;

public class OrderReturnService {
    private final OrderReturnDAO orderReturnDAO;

    public OrderReturnService() {
        this.orderReturnDAO = new OrderReturnDAO();
    }

    /**
     * Tạo yêu cầu trả hàng mới
     */
    public int createOrderReturn(OrderReturn orderReturn) {
        return orderReturnDAO.insertOrderReturn(orderReturn);
    }

    /**
     * Lấy thông tin yêu cầu trả hàng
     */
    public OrderReturn getOrderReturn(int id) {
        return orderReturnDAO.getOrderReturnById(id);
    }

    /**
     * Lấy yêu cầu trả hàng của một đơn hàng
     */
    public OrderReturn getOrderReturnByOrderId(int orderId) {
        return orderReturnDAO.getOrderReturnByOrderId(orderId);
    }

    /**
     * Lấy danh sách yêu cầu trả hàng của user
     */
    public List<OrderReturn> getOrderReturnsByUser(int userId) {
        return orderReturnDAO.getOrderReturnsByUserId(userId);
    }

    /**
     * Lấy tất cả yêu cầu trả hàng (dành cho admin)
     */
    public List<OrderReturn> getAllOrderReturns() {
        return orderReturnDAO.getAllOrderReturns();
    }

    /**
     * Lấy yêu cầu trả hàng theo trạng thái (dành cho admin)
     */
    public List<OrderReturn> getOrderReturnsByStatus(String status) {
        return orderReturnDAO.getOrderReturnsByStatus(status);
    }

    /**
     * Cập nhật trạng thái yêu cầu trả hàng
     */
    public boolean updateOrderReturnStatus(int id, String status) {
        return orderReturnDAO.updateOrderReturnStatus(id, status);
    }

    /**
     * Duyệt yêu cầu trả hàng
     */
    public boolean approveOrderReturn(int id, double refundAmount) {
        boolean approved = orderReturnDAO.approveOrderReturn(id, refundAmount);
        if (approved) {
            // Cập nhật trạng thái đơn hàng thành "returning"
            OrderReturn orderReturn = orderReturnDAO.getOrderReturnById(id);
            if (orderReturn != null) {
                OrderDAO orderDAO = new OrderDAO();
                orderDAO.updateOrderStatus(orderReturn.getOrderId(), "returning");
            }
        }
        return approved;
    }

    /**
     * Từ chối yêu cầu trả hàng
     */
    public boolean rejectOrderReturn(int id, String adminNotes) {
        return orderReturnDAO.rejectOrderReturn(id, adminNotes);
    }

    /**
     * Hoàn thành yêu cầu trả hàng
     */
    public boolean completeOrderReturn(int id, LocalDateTime refundDate) {
        boolean completed = orderReturnDAO.completeOrderReturn(id, refundDate);
        if (completed) {
            // Cập nhật trạng thái đơn hàng thành "refunded"
            OrderReturn orderReturn = orderReturnDAO.getOrderReturnById(id);
            if (orderReturn != null) {
                OrderDAO orderDAO = new OrderDAO();
                orderDAO.updateOrderStatus(orderReturn.getOrderId(), "refunded");
            }
        }
        return completed;
    }

    /**
     * Cập nhật ghi chú của admin
     */
    public boolean updateAdminNotes(int id, String adminNotes) {
        return orderReturnDAO.updateAdminNotes(id, adminNotes);
    }

    /**
     * Xóa yêu cầu trả hàng
     */
    public boolean deleteOrderReturn(int id) {
        return orderReturnDAO.deleteOrderReturn(id);
    }

    /**
     * Thêm ảnh minh chứng
     */
    public int addReturnImage(ReturnImage image) {
        return orderReturnDAO.insertReturnImage(image);
    }

    /**
     * Lấy danh sách ảnh minh chứng
     */
    public List<ReturnImage> getReturnImages(int returnId) {
        return orderReturnDAO.getReturnImages(returnId);
    }

    /**
     * Xóa ảnh minh chứng
     */
    public boolean deleteReturnImage(int imageId) {
        return orderReturnDAO.deleteReturnImage(imageId);
    }

    /**
     * Đếm yêu cầu trả hàng theo trạng thái
     */
    public int countOrderReturnsByStatus(String status) {
        return orderReturnDAO.countOrderReturnsByStatus(status);
    }

    /**
     * Kiểm tra xem đơn hàng có yêu cầu trả hàng chưa
     */
    public boolean hasOrderReturn(int orderId) {
        return orderReturnDAO.hasOrderReturn(orderId);
    }
}