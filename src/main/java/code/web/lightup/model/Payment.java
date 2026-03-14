package code.web.lightup.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Payment implements Serializable {
    private int id;
    private int orderId;
    private String paymentMethod;
    private double amount;
    private String status;
    private LocalDate payDate;
    private String paymentStatus;

    public Payment() {
        this.status = "pending";
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public java.sql.Date getOrderDateAsSqlDate() {
        return java.sql.Date.valueOf(payDate);
    }

    public String getPaymentStatus() {
        return status;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.status = paymentStatus;
    }
}
