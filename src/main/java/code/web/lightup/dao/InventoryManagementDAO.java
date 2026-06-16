package code.web.lightup.dao;

import code.web.lightup.model.InventoryTransaction;
import code.web.lightup.model.Product;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

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

}