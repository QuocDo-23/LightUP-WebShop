package code.web.lightup.dao;

import code.web.lightup.model.InventoryTransaction;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class InventoryTransactionDAO {

    private final Jdbi jdbi;

    public InventoryTransactionDAO() {
        this.jdbi = BaseDao.get();
    }

    public void addTransaction(
            int productId,
            String transactionType,
            int quantity,
            String reason,
            Integer referenceId,
            Integer createdBy
    ) {

        String sql = """
                INSERT INTO inventory_transaction
                (
                    product_id,
                    transaction_type,
                    quantity,
                    reason,
                    reference_id,
                    created_by
                )
                VALUES
                (
                    :productId,
                    :transactionType,
                    :quantity,
                    :reason,
                    :referenceId,
                    :createdBy
                )
                """;

        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("productId", productId)
                        .bind("transactionType", transactionType)
                        .bind("quantity", quantity)
                        .bind("reason", reason)
                        .bind("referenceId", referenceId)
                        .bind("createdBy", createdBy)
                        .execute()
        );
    }

    public List<InventoryTransaction> getAllTransactions() {

        String sql = """
                SELECT *
                FROM inventory_transaction
                ORDER BY created_at DESC
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(InventoryTransaction.class)
                        .list()
        );
    }
}