package code.web.lightup.dao;

import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

public class InventoryDAO {

    private final Jdbi jdbi;

    public InventoryDAO() {
        this.jdbi = BaseDao.get();
    }

    // Tổng nhập hôm nay
    public int getTodayImportQuantity() {

        String sql = """
            SELECT COALESCE(SUM(quantity),0)
            FROM inventory_transaction
            WHERE transaction_type = 'IMPORT'
            AND DATE(created_at) = CURDATE()
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    // Tổng bán hôm nay
    public int getTodaySaleQuantity() {

        String sql = """
            SELECT COALESCE(SUM(ABS(quantity)),0)
            FROM inventory_transaction
            WHERE transaction_type = 'SALE'
            AND DATE(created_at) = CURDATE()
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    // Sản phẩm sắp hết hàng
    public int getLowStockCount() {

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

    // Hàng ế trên 6 tháng
    public int getDeadStockCount() {

        String sql = """
            SELECT COUNT(*)
            FROM product
            WHERE last_sale_date IS NULL
               OR last_sale_date < DATE_SUB(NOW(), INTERVAL 6 MONTH)
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}