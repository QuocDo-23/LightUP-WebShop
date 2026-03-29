package code.web.lightup.service;

import code.web.lightup.dao.CartDAO;
import code.web.lightup.model.Cart.Cart;
import code.web.lightup.model.Cart.CartItem;
import code.web.lightup.model.Cart.CartItemDB;
import code.web.lightup.model.ProductWithDetails;

import java.util.List;
import java.util.Optional;


public class CartService {

    private final CartDAO cartDAO;
    private final ProductService productService;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.productService = new ProductService();
    }


    public void addCartToDb(int userId, Cart cart) {
        int cartId = cartDAO.getOrCreateCartId(userId);

        cartDAO.clearCart(cartId);

        for (CartItem item : cart.getListItem()) {
            cartDAO.upsertItem(
                    cartId,
                    item.getProduct().getId(),
                    item.getQuantity(),
                    item.getProduct().getDiscountedPrice()
            );
        }
    }

    public Cart mergeOnLogin(int userId, Cart guestCart) {
        int cartId = cartDAO.getOrCreateCartId(userId);

        Cart mergedCart = loadCartFromDb(userId);

        if (guestCart == null || guestCart.getListItem().isEmpty()) {
            return mergedCart;
        }

        for (CartItem guestItem : guestCart.getListItem()) {
            int pid = guestItem.getProduct().getId();
            if (mergedCart.get(pid) != null) {
                int dbQty    = mergedCart.get(pid).getQuantity();
                int guestQty = guestItem.getQuantity();
                mergedCart.get(pid).setQuantity(Math.max(dbQty, guestQty));
            } else {
                mergedCart.addItem(guestItem.getProduct(), guestItem.getQuantity());
            }
        }

        // Lưu giỏ đã merge vào DB (dùng upsert, KHÔNG clearCart trước)
        for (CartItem item : mergedCart.getListItem()) {
            cartDAO.upsertItem(
                    cartId,
                    item.getProduct().getId(),
                    item.getQuantity(),
                    item.getProduct().getDiscountedPrice()
            );
        }

        return mergedCart;
    }


    /**
     * Đọc giỏ hàng từ DB và tạo đối tượng Cart đầy đủ.
     */
    public Cart loadCartFromDb(int userId) {
        Cart cart = new Cart();
        List<CartItemDB> rows = cartDAO.getItemsByUserId(userId);

        for (CartItemDB row : rows) {
            Optional<ProductWithDetails> productOpt = productService.getProductById(row.productId);
            productOpt.ifPresent(p -> cart.addItem(p, row.quantity));
        }

        return cart;
    }


    public void markOrdered(int cartId) {
        cartDAO.markOrdered(cartId);

    }
    public void removeItem(int cartId, int productId) {
        cartDAO.removeItem(cartId, productId);
    }

    public Cart getCartByUserId(int userId) {
        CartDAO cartDAO = new CartDAO();
        ProductService productService = new ProductService();

        Cart cart = new Cart();

        List<CartItemDB> items = cartDAO.getItemsByUserId(userId);

        for (CartItemDB item : items) {
            int productId = item.getProductId();
            int quantity = item.getQuantity();

            Optional<ProductWithDetails> productOpt =
                    productService.getProductById(productId);

            if (productOpt.isPresent()) {
                cart.addItem(productOpt.get(), quantity);
            }
        }

        return cart;
    }
}