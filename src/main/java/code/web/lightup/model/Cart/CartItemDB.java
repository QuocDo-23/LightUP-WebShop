package code.web.lightup.model.Cart;

public class CartItemDB{
    public final int productId;
    public final int quantity;
    public final double priceAtAdd;


    public CartItemDB(int productId, int quantity, double priceAtAdd) {
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtAdd = priceAtAdd;
    }
    public int getProductId() {
        return productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getPriceAtAdd() {
        return priceAtAdd;
    }

}
