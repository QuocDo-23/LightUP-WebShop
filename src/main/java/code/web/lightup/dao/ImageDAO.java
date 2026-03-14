package code.web.lightup.dao;


import code.web.lightup.model.Image;
import code.web.lightup.util.BaseDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ImageDAO {
    private final Jdbi jdbi;

    public ImageDAO() {
        this.jdbi = BaseDao.get();
    }


    public List<Image> getImagesByProductId(int productId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM Image " +
                                        "WHERE type = 'product' AND ref_id = :productId " +
                                        "ORDER BY id"
                        )
                        .bind("productId", productId)
                        .mapToBean(Image.class)
                        .list()
        );
    }


    public Image getMainImage(int productId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM Image " +
                                        "WHERE type = 'product' AND ref_id = :productId " +
                                        "LIMIT 1"
                        )
                        .bind("productId", productId)
                        .mapToBean(Image.class)
                        .findFirst()
                        .orElse(null)
        );
    }
}

