package code.web.lightup.model.Cart;

import code.web.lightup.model.ProductWithDetails;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements Serializable {
    Map<Integer, CartItem> cartData;
    private User user;

    public Cart() {
        cartData = new HashMap<>();
    }

    public void updateCustomerInfo(User user) {
        this.user = user;
    }
    /**
     * This is a method add item
     *
     * @param p:        product
     * @param quantity: quantity of product
     */
    public void addItem(ProductWithDetails p, int quantity) {

        if (quantity <= 0) quantity = 1;

        if (cartData.containsKey(p.getId())) {
            cartData.get(p.getId()).updateQuantity(quantity);
        } else {
            cartData.put(
                    p.getId(),
                    new CartItem(p, quantity, p.getPrice())
            );
        }
    }

    /**
     * This is a method update quantity of item
     *
     * @param pID:      product id
     * @param quantity: quantity of item
     * @return true if success, false if fail
     */
    public boolean updateItem(int pID, int quantity) {
        if (get(pID) == null) return false;

        if (quantity <= 0) {
            quantity = 1;
        }

        cartData.get(pID).setQuantity(quantity);

        return true;
    }

    /**
     * This is a method remove item
     *
     * @param pID: product id
     * @return remove an item
     */
    public CartItem removeItem(int pID) {
        if (get(pID) == null) return null;
        return cartData.remove(pID);
    }

    /**
     * This is a method remove all item
     *
     * @return remove all item
     */
    public List<CartItem> removeAll() {
        ArrayList<CartItem> cartItems = new ArrayList<>(cartData.values());
        cartData.clear();
        return cartItems;
    }

    /**
     * This is a method get list item
     *
     * @return list all item
     */
    public List<CartItem> getListItem() {

        return new ArrayList<>(cartData.values());
    }

    /**
     * This is a method get product
     *
     * @param id: id
     * @return id of product
     */
    public CartItem get(int id) {
        return cartData.get(id);
    }

    /**
     * This is a method get total quantity
     *
     * @return total quantity in cart
     */
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : getListItem()) {
            total += item.getQuantity();
        }
        return total;
    }
    public int getTotalItems() {
        return cartData.size();
    }

    /**
     * This is a method calculate total price
     *
     * @return total price in cart
     */
    public double getTotalPrice() {
        double totalPrice = 0;

        for (CartItem item : getListItem()) {
            totalPrice += item.getQuantity() * item.getProduct().getDiscountedPrice();
        }
        return totalPrice;
    }
}
