package code.web.lightup.service;



import code.web.lightup.dao.OrderDAO;
import code.web.lightup.model.OrderItem;
import code.web.lightup.model.Order;

import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    public int insertOrder(Order order) {
        return orderDAO.insertOrder(order);
    }

    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }

    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.getOrdersByUserId(userId);
    }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    public boolean deleteOrder(int orderId) {
        return orderDAO.deleteOrder(orderId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public boolean insertOrderItem(OrderItem item) {
        return orderDAO.insertOrderItem(item);
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        return orderDAO.getOrderItemsByOrderId(orderId);

    }
    public OrderItem getOrderItemById(int itemId) {
        return orderDAO.getOrderItemById(itemId);
    }

    public boolean deleteOrderItem(int itemId) {
        return orderDAO.deleteOrderItem(itemId);
    }

    public boolean deleteOrderItemsByOrderId(int orderId) {
        return orderDAO.deleteOrderItemsByOrderId(orderId);
    }
    public int countOrdersByUserId(int userId) {
        return orderDAO.countOrdersByUserId(userId);
    }
    public int getOrderCountByStatus(String status) {
        return orderDAO.getOrderCountByStatus(status);
    }

    public double getCurrentMonthRevenue() {
        return orderDAO.getCurrentMonthRevenue();
    }

    public int getCurrentMonthOrderCount() {
        return orderDAO.getCurrentMonthOrderCount();
    }
    public List<Map<String, Object>> getTopSellingProducts(int limit) {
        return orderDAO.getTopSellingProducts(limit);
    }
    public List<Order> getOrdersWithPagination(int page, int pageSize, String status, String searchKeyword, String sortBy, String sortOrder
                                                ) {
        return orderDAO.getOrdersWithPagination(page, pageSize, status, searchKeyword, sortBy, sortOrder);
    }
    public int countOrders(String status, String searchKeyword) {
        return orderDAO.countOrders(status, searchKeyword);}
    public Map<String, Integer> getOrderStatusStatistics() {
        return orderDAO.getOrderStatusStatistics();
    }



}
