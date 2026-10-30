package com.fooddelivery.services;

import com.fooddelivery.models.MenuItem;
import com.fooddelivery.models.Restaurant;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import java.util.List;

public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        if (menuItemRepository == null || restaurantRepository == null) {
            throw new IllegalArgumentException("Menu and restaurant repositories are required.");
        }
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }
    public void addMenuItem(String actorId, String role, MenuItem item) {
        if (item == null) { throw new IllegalArgumentException("Menu item is required."); }
        RestaurantService.requireManagementAccess(actorId, role, restaurant(item.getRestaurantId()));
        menuItemRepository.save(item);
    }
    public MenuItem getMenuItem(String itemId) {
        MenuItem item = menuItemRepository.findById(itemId);
        if (item == null) { throw new IllegalArgumentException("Menu item not found."); }
        return item;
    }
    public List<MenuItem> getMenuItems(String restaurantId) {
        restaurant(restaurantId);
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
    public void updatePrice(String actorId, String role, String itemId, double price) {
        managedItem(actorId, role, itemId).updatePrice(price);
    }
    public void changeAvailability(String actorId, String role, String itemId, boolean available) {
        managedItem(actorId, role, itemId).changeAvailability(available);
    }
    public void removeMenuItem(String actorId, String role, String itemId) {
        managedItem(actorId, role, itemId);
        menuItemRepository.deleteById(itemId);
    }
    private MenuItem managedItem(String actorId, String role, String itemId) {
        MenuItem item = getMenuItem(itemId);
        RestaurantService.requireManagementAccess(actorId, role, restaurant(item.getRestaurantId()));
        return item;
    }
    private Restaurant restaurant(String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant == null) { throw new IllegalArgumentException("Restaurant not found."); }
        return restaurant;
    }
}
