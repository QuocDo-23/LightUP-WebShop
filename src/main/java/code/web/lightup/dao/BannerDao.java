package code.web.lightup.dao;

import code.web.lightup.model.Banner;
import code.web.lightup.util.BaseDao;
import java.util.List;
import org.jdbi.v3.core.Jdbi;


public class BannerDao {
    private final Jdbi jdbi;

    public BannerDao() {
        this.jdbi = BaseDao.get();
    }

    public List<Banner> getBannerByPosition(String position) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, title, img, position, " +
                                "link_banner, status, display_number, start_date, end_date " +
                                "FROM banner " +
                                "WHERE status = 'active' AND position = :position " +
                                "AND CURRENT_DATE BETWEEN start_date AND end_date " +
                                "ORDER BY display_number ASC")
                        .bind("position", position)
                        .mapToBean(Banner.class)
                        .list());
    }
}