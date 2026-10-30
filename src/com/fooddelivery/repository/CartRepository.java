package com.fooddelivery.repository;

import com.fooddelivery.models.Cart;
import java.util.HashMap;
import java.util.Map;

public class CartRepository {
    private final Map<String, Cart> cartsByCustomerId = new HashMap<>();
    private int nextCartId = 1;

    public Cart getOrCreateCart(String customerId) {
        validateCustomerId(customerId);
        Cart cart = cartsByCustomerId.get(customerId);
        if (cart == null) {
            cart = new Cart("CART-" + nextCartId++);
            cartsByCustomerId.put(customerId, cart);
        }
        return cart;
    }

    public Cart findByCustomerId(String customerId) {
        validateCustomerId(customerId);
        return cartsByCustomerId.get(customerId);
    }

    public void removeByCustomerId(String customerId) {
        validateCustomerId(customerId);
        cartsByCustomerId.remove(customerId);
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID is required.");
        }
    }
}
