package code.web.lightup.service;



import code.web.lightup.dao.PaymentDAO;
import code.web.lightup.model.Payment;

import java.util.List;

public class PaymentService {
    private final PaymentDAO paymentDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }

    public boolean insertPayment(Payment payment) {
        return paymentDAO.insertPayment(payment);
    }

    public Payment getPaymentByOrderId(int orderId) {
        return paymentDAO.getPaymentByOrderId(orderId);
    }

    public Payment getPaymentById(int paymentId) {
        return paymentDAO.getPaymentById(paymentId);
    }

    public boolean updatePaymentatus(int paymentId, String status) {
        return paymentDAO.updatePaymentatus(paymentId, status);
    }

    public boolean deletePayment(int paymentId) {
        return paymentDAO.deletePayment(paymentId);
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }


}
