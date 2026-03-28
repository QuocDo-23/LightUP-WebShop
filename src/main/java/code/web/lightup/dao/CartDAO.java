package code.web.lightup.dao;

import code.web.lightup.model.Cart.CartItemDB;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

public class CartDAO {
    private final Jdbi jdbi;

    public CartDAO() {
        this.jdbi = BaseDao.get();
    }


    /**
     * Lấy cart_id đang active của user.
     */
    public int getOrCreateCartId(int userId) {
        return jdbi.inTransaction(handle -> {
            Optional<Integer> existingId = handle.createQuery(
                            "SELECT id FROM Cart WHERE user_id = :uid AND status = 'active' LIMIT 1"
                    )
                    .bind("uid", userId)
                    .mapTo(Integer.class)
                    .findFirst();

            if (existingId.isPresent()) {
                return existingId.get();
            }

            return handle.createUpdate(
                            "INSERT INTO Cart (user_id, status) VALUES (:uid, 'active')"
                    )
                    .bind("uid", userId)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();
        });
    }

    /**
     * Đánh dấu 'ordered' sau khi đặt hàng thành công.
     */
    public void markOrdered(int cartId) {
        jdbi.useHandle(handle ->
                handle.createUpdate("UPDATE Cart SET status = 'ordered' WHERE id = :id")
                        .bind("id", cartId)
                        .execute()
        );
    }


    /**
     * Lấy tất cả items trong cart của user (join để có thông tin sản phẩm).
     * Dùng khi load giỏ hàng từ DB (ví dụ sau khi đăng nhập lại).
     */
    public List<CartItemDB> getItemsByUserId(int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT ci.product_id, ci.quantity, ci.price_at_add " +
                                        "FROM Cart_Item ci " +
                                        "JOIN Cart c ON ci.cart_id = c.id " +
                                        "WHERE c.user_id = :uid AND c.status = 'active'"
                        )
                        .bind("uid", userId)
                        .map((rs, ctx) -> new CartItemDB(
                                rs.getInt("product_id"),
                                rs.getInt("quantity"),
                                rs.getDouble("price_at_add")
                        ))
                        .list()
        );
    }

    /**
     * Thêm hoặc cập nhật số lượng
     */
    public void upsertItem(int cartId, int productId, int quantity, double priceAtAdd) {
        jdbi.useHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO Cart_Item (cart_id, product_id, quantity, price_at_add) " +
                                        "VALUES (:cid, :pid, :qty, :price) " +
                                        "ON DUPLICATE KEY UPDATE quantity = :qty, price_at_add = :price"
                        )
                        .bind("cid", cartId)
                        .bind("pid", productId)
                        .bind("qty", quantity)
                        .bind("price", priceAtAdd)
                        .execute()
        );
    }

    /**
     * Xóa một item khỏi cart DB.
     */
    public void removeItem(int cartId, int productId) {
        jdbi.useHandle(handle ->
                handle.createUpdate(
                                "DELETE FROM Cart_Item WHERE cart_id = :cid AND product_id = :pid"
                        )
                        .bind("cid", cartId)
                        .bind("pid", productId)
                        .execute()
        );
    }

    /**
     * Xóa toàn bộ  của cart (dùng khi đặt hàng xong).
     */
    public void clearCart(int cartId) {
        jdbi.useHandle(handle ->
                handle.createUpdate("DELETE FROM Cart_Item WHERE cart_id = :cid")
                        .bind("cid", cartId)
                        .execute()
        );
    }



}