package code.web.lightup.dao;

import code.web.lightup.model.Product;
import code.web.lightup.model.ProductWithDetails;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;
import code.web.lightup.model.InventoryTransaction;

import java.util.List;

public class AnalyticsDAO {

    private final Jdbi jdbi;

    public AnalyticsDAO() {
        this.jdbi = BaseDao.get();
    }

    public List<ProductWithDetails> getLowStockProducts(
            int offset,
            int limit
    ) {

        String sql = """
                SELECT p.*,
                
                       (SELECT img
                        FROM Image
                        WHERE type='product'
                        AND ref_id = p.id
                        LIMIT 1) AS main_image
                
                FROM product p
                
                WHERE p.inventory_quantity <= p.min_stock
                
                ORDER BY p.inventory_quantity ASC
                
                LIMIT :limit
                OFFSET :offset
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(ProductWithDetails.class)
                        .list()
        );
    }
    public int countLowStockProducts() {

        String sql = """
        SELECT COUNT(*)
        FROM product
        WHERE inventory_quantity <= min_stock
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<ProductWithDetails> getDeadStockProducts(
            int offset,
            int limit
    ) {

        String sql = """
                SELECT p.*,
            
                       (SELECT img
                        FROM Image
                        WHERE type='product'
                        AND ref_id = p.id
                        LIMIT 1) AS main_image,
            
                       DATEDIFF(
                           NOW(),
                           p.last_sale_date
                       ) AS dead_days
            
                FROM product p
            
                WHERE p.last_sale_date IS NULL
                   OR p.last_sale_date <
                      DATE_SUB(
                          NOW(),
                          INTERVAL 6 MONTH
                      )
            
                ORDER BY p.last_sale_date ASC
            
                LIMIT :limit
                OFFSET :offset
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(ProductWithDetails.class)
                        .list()
        );
    }
    public int countDeadStockProducts() {

        String sql = """
        SELECT COUNT(*)
        FROM product
        WHERE last_sale_date IS NULL
           OR last_sale_date <
              DATE_SUB(
                  NOW(),
                  INTERVAL 6 MONTH
              )
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
    public List<InventoryTransaction> getTodayImports() {

        String sql = """
        SELECT
            it.*,
            p.name AS product_name
        FROM inventory_transaction it
        JOIN product p
            ON p.id = it.product_id
        WHERE DATE(it.created_at)=CURDATE()
          AND it.transaction_type='IMPORT'
        ORDER BY it.created_at DESC
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(InventoryTransaction.class)
                        .list()
        );
    }
    public List<InventoryTransaction> getTodaySales() {

        String sql = """
        SELECT
            it.*,
            p.name AS product_name
        FROM inventory_transaction it
        JOIN product p
            ON p.id = it.product_id
        WHERE DATE(it.created_at)=CURDATE()
          AND it.transaction_type='SALE'
        ORDER BY it.created_at DESC
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(InventoryTransaction.class)
                        .list()
        );
    }
}