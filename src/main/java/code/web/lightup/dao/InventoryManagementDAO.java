package code.web.lightup.dao;

import code.web.lightup.model.InventoryTransaction;
import code.web.lightup.model.Product;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;
import code.web.lightup.dao.InventoryTransactionDAO;

import java.util.List;

public class InventoryManagementDAO {

    private final Jdbi jdbi;

    public InventoryManagementDAO() {
        this.jdbi = BaseDao.get();
    }

    public List<Product> getAllProducts() {

        String sql = """
                SELECT *
                FROM product
                ORDER BY inventory_quantity ASC
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public List<InventoryTransaction> getRecentTransactions() {

        String sql = """
        SELECT
            it.*,
            p.name AS product_name
        FROM inventory_transaction it
        LEFT JOIN product p
            ON it.product_id = p.id
        ORDER BY it.created_at DESC
        LIMIT 100
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(InventoryTransaction.class)
                        .list()
        );
    }
    public boolean importStock(
            int productId,
            int quantity,
            String reason
    ) {

        String sql = """
        UPDATE product
        SET inventory_quantity =
            inventory_quantity + ?,
            last_import_date = NOW()
        WHERE id = ?
        """;

        return jdbi.withHandle(handle -> {

            int rows = handle.createUpdate(sql)
                    .bind(0, quantity)
                    .bind(1, productId)
                    .execute();

            if (rows > 0) {

                InventoryTransactionDAO transactionDAO =
                        new InventoryTransactionDAO();

                transactionDAO.addTransaction(
                        productId,
                        "IMPORT",
                        quantity,
                        reason,
                        null,
                        null
                );
            }

            return rows > 0;
        });
    }
    public void exportStock(int productId, int quantity) {

        String sql = """
            UPDATE product
            SET inventory_quantity = inventory_quantity - :quantity,
                last_sale_date = NOW()
            WHERE id = :productId
            """;

        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("quantity", quantity)
                        .bind("productId", productId)
                        .execute()
        );
    }
    public Product getProductById(int productId) {

        String sql = """
            SELECT *
            FROM product
            WHERE id = :id
            """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", productId)
                        .mapToBean(Product.class)
                        .findOne()
                        .orElse(null)
        );
    }
    public List<Product> getInventoryAlerts() {

        String sql = """
        SELECT *
        FROM product
        ORDER BY inventory_quantity ASC
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Product.class)
                        .list()
        );
    }
}