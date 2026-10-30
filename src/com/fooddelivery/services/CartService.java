package com.fooddelivery.services;

import com.fooddelivery.models.Cart;
import com.fooddelivery.models.MenuItem;
import com.fooddelivery.repository.CartRepository;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        if (cartRepository == null) {
            throw new IllegalArgumentException("Cart repository is required.");
        }
        this.cartRepository = cartRepository;
    }

    public Cart getCart(String customerId) {
        return cartRepository.getOrCreateCart(customerId);
    }
    public void addItem(String customerId, String restaurantId, MenuItem item, int quantity) {
        getCart(customerId).addItem(restaurantId, item, quantity);
    }
    public void removeItem(String customerId, String itemId) {
        getCart(customerId).removeItem(itemId);
    }
    public double calculateTotal(String customerId) {
        return getCart(customerId).calculateTotal();
    }
    public void clearCart(String customerId) {
        getCart(customerId).clearCart();
    }

    // The caller displays these names to notify the customer at checkout.
    public List<String> removeUnavailableItems(String customerId) {
        Cart cart = getCart(customerId);
        List<String> removedNames = new ArrayList<>();
        for (MenuItem item : cart.getItems()) {
            if (!item.isAvailable()) {
                removedNames.add(item.getName());
                cart.removeItem(item.getItemId());
            }
        }
        return removedNames;
    }
}
