package com.fooddelivery.services;

import com.fooddelivery.models.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.repository.MenuItemRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final Predicate<String> hasActiveOrders;

    // The order module supplies a real check for orders not Delivered or Cancelled.
    public RestaurantService(RestaurantRepository restaurantRepository,
                             MenuItemRepository menuItemRepository, Predicate<String> hasActiveOrders) {
        if (restaurantRepository == null || menuItemRepository == null || hasActiveOrders == null) {
            throw new IllegalArgumentException("Repositories and active-order check are required.");
        }
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.hasActiveOrders = hasActiveOrders;
    }
    public void addRestaurant(String actorId, String role, Restaurant restaurant) {
        if (restaurant == null) { throw new IllegalArgumentException("Restaurant is required."); }
        requireManagementAccess(actorId, role, restaurant);
        restaurantRepository.save(restaurant);
    }
    public Restaurant getRestaurant(String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) { throw new IllegalArgumentException("Restaurant not found."); }
        return restaurant;
    }
    public List<Restaurant> getRestaurants() { return restaurantRepository.findAll(); }
    public void updateDetails(String actorId, String role, String restaurantId,
                              String name, String cuisineType, String city) {
        Restaurant restaurant = managedRestaurant(actorId, role, restaurantId);
        restaurant.updateDetails(name, cuisineType, city);
    }
    public void updateTimings(String actorId, String role, String restaurantId,
                              LocalTime openingTime, LocalTime closingTime) {
        managedRestaurant(actorId, role, restaurantId).updateTimings(openingTime, closingTime);
    }
    public void activateRestaurant(String actorId, String role, String restaurantId) {
        managedRestaurant(actorId, role, restaurantId).activate();
    }
    public void changeAvailability(String actorId, String role, String restaurantId, boolean open) {
        managedRestaurant(actorId, role, restaurantId).changeAvailability(open);
    }
    public void deleteRestaurant(String actorId, String role, String restaurantId) {
        Restaurant restaurant = managedRestaurant(actorId, role, restaurantId);
        if (hasActiveOrders.test(restaurantId)) {
            throw new IllegalStateException("Cannot delete a restaurant with active orders.");
        }
        restaurant.changeAvailability(false);
        menuItemRepository.deleteByRestaurantId(restaurantId);
        restaurantRepository.deleteById(restaurantId);
    }
    private Restaurant managedRestaurant(String actorId, String role, String restaurantId) {
        Restaurant restaurant = getRestaurant(restaurantId);
        requireManagementAccess(actorId, role, restaurant);
        return restaurant;
    }
    // Identity and role come from the user module, not from console prompts.
    public static void requireManagementAccess(String actorId, String role, Restaurant restaurant) {
        if (actorId == null || actorId.trim().isEmpty() || restaurant == null) {
            throw new IllegalArgumentException("User and restaurant are required.");
        }
        if ("ADMIN".equals(role)) { return; }
        if ("RESTAURANT_OWNER".equals(role) && actorId.equals(restaurant.getOwnerId())) { return; }
        throw new IllegalStateException("Only an admin or this restaurant's owner can manage it.");
    }
}
