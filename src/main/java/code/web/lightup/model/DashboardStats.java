package code.web.lightup.model;

import java.util.List;
import java.util.Map;

public class DashboardStats {

    private double monthRevenue;
    private int monthOrders;
    private int pendingOrders;
    private int totalCustomers;
    private List<Map<String, Object>> topProducts;

    public DashboardStats() {
    }

    public double getMonthRevenue() {
        return monthRevenue;
    }

    public void setMonthRevenue(double monthRevenue) {
        this.monthRevenue = monthRevenue;
    }

    public int getMonthOrders() {
        return monthOrders;
    }

    public void setMonthOrders(int monthOrders) {
        this.monthOrders = monthOrders;
    }

    public int getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(int pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public List<Map<String, Object>> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(List<Map<String, Object>> topProducts) {
        this.topProducts = topProducts;
    }
}
