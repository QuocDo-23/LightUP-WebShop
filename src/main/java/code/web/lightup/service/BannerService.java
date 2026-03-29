package code.web.lightup.service;
import code.web.lightup.dao.BannerDao;
import code.web.lightup.model.Banner;

import java.util.List;

public class BannerService {
    private final BannerDao bannerDao;

    public BannerService() {
        this.bannerDao = new BannerDao();
    }

    public List<Banner> getBannerByPosition(String position) {
        return bannerDao.getBannerByPosition(position);
    }
}
