package com.fooddelivery.repository;

import com.fooddelivery.models.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuItemRepository {
    private final Map<String, MenuItem> items = new LinkedHashMap<>();

    public void save(MenuItem item) {
        if (item == null) { throw new IllegalArgumentException("Menu item is required."); }
        if (items.containsKey(item.getItemId())) {
            throw new IllegalStateException("Menu item ID already exists.");
        }
        items.put(item.getItemId(), item);
    }
    public MenuItem findById(String itemId) {
        validateId(itemId);
        return items.get(itemId);
    }
    public List<MenuItem> findByRestaurantId(String restaurantId) {
        validateId(restaurantId);
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem item : items.values()) {
            if (item.getRestaurantId().equals(restaurantId)) { result.add(item); }
        }
        return result;
    }
    public void deleteById(String itemId) {
        validateId(itemId);
        MenuItem item = items.remove(itemId);
        // Existing carts retain this object, so they can remove it at checkout.
        if (item != null) { item.changeAvailability(false); }
    }
    public void deleteByRestaurantId(String restaurantId) {
        for (MenuItem item : findByRestaurantId(restaurantId)) { deleteById(item.getItemId()); }
    }
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID is required.");
        }
    }
}
