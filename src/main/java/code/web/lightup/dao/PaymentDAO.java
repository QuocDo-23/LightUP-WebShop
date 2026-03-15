package code.web.lightup.dao;



import code.web.lightup.model.Payment;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class PaymentDAO {

    private final Jdbi jdbi;

    public PaymentDAO() {
        this.jdbi = BaseDao.get();
    }

    /**
     * Insert payment mới
     */
    public boolean insertPayment(Payment payment) {
        String sql = "INSERT INTO payment (order_id, payment_method, amount, status, pay_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        return jdbi.withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind(0, payment.getOrderId())
                    .bind(1, payment.getPaymentMethod())
                    .bind(2, payment.getAmount())
                    .bind(3, payment.getStatus())
                    .bind(4, payment.getPayDate())
                    .execute();
            return rows > 0;
        });
    }

    /**
     * Lấy payment theo order ID
     */
    public Payment getPaymentByOrderId(int orderId) {
        String sql = "SELECT * FROM payment WHERE order_id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, orderId)
                    .map((rs, ctx) -> {
                        Payment payment = new Payment();
                        payment.setId(rs.getInt("id"));
                        payment.setOrderId(rs.getInt("order_id"));
                        payment.setPaymentMethod(rs.getString("payment_method"));
                        payment.setAmount(rs.getDouble("amount"));
                        payment.setStatus(rs.getString("status"));
                        payment.setPayDate(rs.getDate("pay_date").toLocalDate());
                        return payment;
                    })
                    .findOne()
                    .orElse(null);
        });
    }

    /**
     * Lấy payment theo ID
     */
    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT * FROM payment WHERE id = ?";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .bind(0, paymentId)
                    .map((rs, ctx) -> {
                        Payment payment = new Payment();
                        payment.setId(rs.getInt("id"));
                        payment.setOrderId(rs.getInt("order_id"));
                        payment.setPaymentMethod(rs.getString("payment_method"));
                        payment.setAmount(rs.getDouble("amount"));
                        payment.setStatus(rs.getString("status"));
                        payment.setPayDate(rs.getDate("pay_date").toLocalDate());
                        return payment;
                    })
                    .findOne()
                    .orElse(null);
        });
    }

    /**
     * Cập nhật trạng thái payment
     */
    public boolean updatePaymentatus(int paymentId, String status) {
        String sql = "UPDATE payment SET status = ? WHERE id = ?";

        return jdbi.withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind(0, status)
                    .bind(1, paymentId)
                    .execute();
            return rows > 0;
        });
    }

    /**
     * Xóa payment
     */
    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM payment WHERE id = ?";

        return jdbi.withHandle(handle -> {
            int rows = handle.createUpdate(sql)
                    .bind(0, paymentId)
                    .execute();
            return rows > 0;
        });
    }

    /**
     * Lấy tất cả payment
     */
    public List<Payment> getAllPayments() {
        String sql = "SELECT * FROM payment ORDER BY pay_date DESC";

        return jdbi.withHandle(handle -> {
            return handle.createQuery(sql)
                    .map((rs, ctx) -> {
                        Payment payment = new Payment();
                        payment.setId(rs.getInt("id"));
                        payment.setOrderId(rs.getInt("order_id"));
                        payment.setPaymentMethod(rs.getString("payment_method"));
                        payment.setAmount(rs.getDouble("amount"));
                        payment.setStatus(rs.getString("status"));
                        payment.setPayDate(rs.getDate("pay_date").toLocalDate());
                        return payment;
                    })
                    .list();
        });
    }
}