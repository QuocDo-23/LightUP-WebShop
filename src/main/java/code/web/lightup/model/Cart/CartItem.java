package code.web.lightup.model.Cart;

import code.web.lightup.model.ProductWithDetails;

public class CartItem {
    private ProductWithDetails product;
    private int quantity;
    private double price;

    public CartItem() {
    }

    public CartItem(ProductWithDetails product, int quantity, double price) {
        this.product = (ProductWithDetails) product;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductWithDetails getProduct() {
        return product;
    }

    public void setProduct(ProductWithDetails product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void updateQuantity(int quantity){
        this.quantity += quantity;
    }
}
