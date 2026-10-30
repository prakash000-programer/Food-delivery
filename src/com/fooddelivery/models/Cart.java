package com.fooddelivery.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private final String cartId;
    private String restaurantId;
    private final List<MenuItem> items = new ArrayList<>();
    private final Map<String, Integer> quantities = new HashMap<>();

    public Cart(String cartId) {
        if (cartId == null || cartId.trim().isEmpty()) {
            throw new IllegalArgumentException("Cart ID is required.");
        }
        this.cartId = cartId;
    }

    public String getCartId() { return cartId; }
    public String getRestaurantId() { return restaurantId; }
    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }
    public int getQuantity(String itemId) {
        Integer quantity = quantities.get(itemId);
        return quantity == null ? 0 : quantity;
    }
    public boolean isEmpty() { return items.isEmpty(); }
    public double getTotalAmount() { return calculateTotal(); }

    public void addItem(String restaurantId, MenuItem item, int quantity) {
        if (restaurantId == null || restaurantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }
        if (item == null || item.getItemId() == null || item.getItemId().trim().isEmpty()) {
            throw new IllegalArgumentException("A menu item with an ID is required.");
        }
        if (!restaurantId.equals(item.getRestaurantId())) {
            throw new IllegalArgumentException("Item does not belong to the selected restaurant.");
        }
        if (!item.isAvailable()) {
            throw new IllegalStateException("Item is unavailable: " + item.getName());
        }
        if (!Double.isFinite(item.getPrice()) || item.getPrice() <= 0) {
            throw new IllegalArgumentException("Item price must be greater than zero.");
        }
        if (this.restaurantId != null && !this.restaurantId.equals(restaurantId)) {
            throw new IllegalStateException("Clear the cart before adding from another restaurant.");
        }
        int existing = getQuantity(item.getItemId());
        if (quantity < 1 || quantity > 10 - existing) {
            throw new IllegalArgumentException("Quantity must be between 1 and 10 per item.");
        }
        if (existing == 0) { items.add(item); }
        quantities.put(item.getItemId(), existing + quantity);
        this.restaurantId = restaurantId;
    }

    // Removes all units of the selected item.
    public void removeItem(String itemId) {
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID is required.");
        }
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemId().equals(itemId)) {
                items.remove(i);
                quantities.remove(itemId);
                break;
            }
        }
        if (items.isEmpty()) { restaurantId = null; }
    }

    public double calculateTotal() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice() * getQuantity(item.getItemId());
        }
        return total;
    }

    public void clearCart() {
        items.clear();
        quantities.clear();
        restaurantId = null;
    }
}
