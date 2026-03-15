package code.web.lightup.service;

import code.web.lightup.dao.ProductDAO;
import code.web.lightup.model.ProductWithDetails;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<ProductWithDetails> getAllProductsWithDetails() {
        return productDAO.getAllProductsWithDetails();
    }

    public List<ProductWithDetails> getProductsByCategory(int categoryId) {
        return productDAO.getProductsByCategory(categoryId);
    }

    public Optional<ProductWithDetails> getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public List<ProductWithDetails> searchProducts(String keyword) {
        return productDAO.searchProducts(keyword);
    }

    public void updateRating(int productId, double rating) {
        productDAO.updateRating(productId, rating);
    }

    public List<ProductWithDetails> getFeaturedProducts() {
        return productDAO.getFeaturedProductsByReview(8);
    }

    public int countProductsByCategory(int categoryId) {
        return productDAO.countProductsByCategory(categoryId);
    }

    public List<ProductWithDetails> getProductsByCategoryWithPagination(int categoryId, int offset, int limit) {
        return productDAO.getProductsByCategoryWithPagination(categoryId, offset, limit);
    }

    public boolean decreaseProductQuantity(int productId, int quantity) {
        return productDAO.decreaseProductQuantity(productId, quantity);
    }

    public List<ProductWithDetails> filterProductsByPrice(String[] priceRanges) {

        List<ProductWithDetails> allProducts =
                productDAO.getAllProductsWithDetails();

        List<ProductWithDetails> result = new ArrayList<>();

        for (ProductWithDetails product : allProducts) {

            double price = product.getDiscountedPrice();

            for (String range : priceRanges) {

                String[] parts = range.split("-");
                double min = Double.parseDouble(parts[0]);
                double max = Double.parseDouble(parts[1]);

                if (price >= min && price <= max) {
                    result.add(product);
                    break;
                }
            }
        }
        return result;
    }
    public List<ProductWithDetails> getProductsWithPagination(String search, Integer categoryId, String status, int offset, int limit) {
        return productDAO.getProductsWithPagination(search, categoryId, status, offset, limit);
    }
    public int countProductsWithFilter(String search, Integer categoryId, String status) {
        return productDAO
                .countProductsWithFilter(search, categoryId, status);
    }

    public int getTotalProductCount() {
        return productDAO.getTotalProductCount();
    }

    public int getProductCountByStatus(String status) {
        return productDAO.getProductCountByStatus(status);
    }


    public int getLowStockProductCount(int threshold) {
        return productDAO.getLowStockProductCount(threshold);
    }

    public int getOutOfStockProductCount() {
        return productDAO.getOutOfStockProductCount();
    }
    public boolean insertProduct(ProductWithDetails product) {
        return productDAO.insertProduct(product);
    }
    public boolean updateProduct(ProductWithDetails product) {
        return productDAO.updateProduct(product);
    }
    public boolean deleteProduct(int productId) {
        return productDAO.deleteProduct(productId);
    }



}
