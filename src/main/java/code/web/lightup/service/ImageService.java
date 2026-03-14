package code.web.lightup.service;



import code.web.lightup.dao.ImageDAO;
import code.web.lightup.model.Image;

import java.util.List;

public class ImageService {

    private final ImageDAO imageDAO;

    public ImageService() {
        this.imageDAO = new ImageDAO();
    }

    public List<Image> getImagesByProductId(int productId) {
        return imageDAO.getImagesByProductId(productId);
    }

    public Image getMainImage(int productId) {
        return imageDAO.getMainImage(productId);
    }
}