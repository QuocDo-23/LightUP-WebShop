package code.web.lightup.dao;

import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

public class FavoriteDAO {

    private final Jdbi jdbi = BaseDao.get();

    public boolean isFavorite(int userId, int productId){

        String sql =
                "SELECT COUNT(*) FROM favorite_product " +
                        "WHERE user_id = ? AND product_id = ?";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, userId)
                        .bind(1, productId)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public void addFavorite(int userId, int productId){

        String sql =
                "INSERT INTO favorite_product(user_id, product_id) " +
                        "VALUES(?, ?)";

        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, userId)
                        .bind(1, productId)
                        .execute()
        );
    }

    public void removeFavorite(int userId, int productId){

        String sql =
                "DELETE FROM favorite_product " +
                        "WHERE user_id = ? AND product_id = ?";

        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind(0, userId)
                        .bind(1, productId)
                        .execute()
        );
    }

    public int countFavorite(int userId){

        String sql =
                "SELECT COUNT(*) FROM favorite_product " +
                        "WHERE user_id = ?";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, userId)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}