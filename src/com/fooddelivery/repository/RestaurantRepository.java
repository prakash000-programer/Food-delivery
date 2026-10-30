package com.fooddelivery.repository;

import com.fooddelivery.models.Restaurant;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class RestaurantRepository {
    private final Map<String, Restaurant> restaurants = new LinkedHashMap<>();

    public void save(Restaurant restaurant) {
        if (restaurant == null) { throw new IllegalArgumentException("Restaurant is required."); }
        if (restaurants.containsKey(restaurant.getRestaurantId())) {
            throw new IllegalStateException("Restaurant ID already exists.");
        }
        restaurants.put(restaurant.getRestaurantId(), restaurant);
    }
    public Restaurant findById(String restaurantId) {
        validateId(restaurantId);
        return restaurants.get(restaurantId);
    }
    public List<Restaurant> findAll() { return new ArrayList<>(restaurants.values()); }
    public void deleteById(String restaurantId) {
        validateId(restaurantId);
        restaurants.remove(restaurantId);
    }
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }
    }
}
