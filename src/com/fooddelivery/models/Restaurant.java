package com.fooddelivery.models;

import java.time.LocalTime;
import java.util.Arrays;

public final class Restaurant {
    private final String restaurantId;
    private final String ownerId;
    private String name;
    private String cuisineType;
    private String city;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private final double minimumOrderValue;
    private boolean open;
    private boolean activated;

    public Restaurant(String restaurantId, String ownerId, String name, String cuisineType,
                      String city, LocalTime openingTime, LocalTime closingTime, double minimumOrderValue) {
        requireText(restaurantId, "Restaurant ID");
        requireText(ownerId, "Owner ID");
        if (!Double.isFinite(minimumOrderValue) || minimumOrderValue < 0) {
            throw new IllegalArgumentException("Minimum order value must be finite and non-negative.");
        }
        this.restaurantId = restaurantId;
        this.ownerId = ownerId;
        updateDetails(name, cuisineType, city);
        updateTimings(openingTime, closingTime);
        this.minimumOrderValue = minimumOrderValue;
    }

    public String getRestaurantId() { return restaurantId; }
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getCuisineType() { return cuisineType; }
    public String getCity() { return city; }
    public LocalTime getOpeningTime() { return openingTime; }
    public LocalTime getClosingTime() { return closingTime; }
    public double getMinimumOrderValue() { return minimumOrderValue; }
    public boolean isOpen() { return open; }
    public boolean isActivated() { return activated; }

    public void updateDetails(String name, String cuisineType, String city) {
        if (activated) {
            throw new IllegalStateException("Name, cuisine and city cannot change after activation.");
        }
        requireText(name, "Name");
        requireText(city, "City");
        if (!Arrays.asList("North Indian", "South Indian", "Chinese", "Italian", "Fast Food").contains(cuisineType)) {
            throw new IllegalArgumentException("Cuisine must be North Indian, South Indian, Chinese, Italian or Fast Food.");
        }
        this.name = name.trim();
        this.cuisineType = cuisineType;
        this.city = city.trim();
    }

    public void updateTimings(LocalTime openingTime, LocalTime closingTime) {
        if (openingTime == null || closingTime == null || openingTime.equals(closingTime)) {
            throw new IllegalArgumentException("Opening and closing times must be present and different.");
        }
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
    public void activate() { activated = true; }
    public void changeAvailability(boolean open) {
        if (open && !activated) {
            throw new IllegalStateException("Activate the restaurant before marking it open.");
        }
        this.open = open;
    }
    public boolean canAcceptOrders() { return canAcceptOrders(LocalTime.now()); }
    public boolean canAcceptOrders(LocalTime time) {
        if (time == null) { throw new IllegalArgumentException("Time is required."); }
        if (!activated || !open) { return false; }
        if (openingTime.isBefore(closingTime)) {
            return !time.isBefore(openingTime) && time.isBefore(closingTime);
        }
        // An overnight opening window, for example 22:00 to 03:00.
        return !time.isBefore(openingTime) || time.isBefore(closingTime);
    }
    private static void requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required.");
        }
    }
    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | %s-%s | Min Rs.%.2f | %s | %s",
                restaurantId, name, cuisineType, city, openingTime, closingTime, minimumOrderValue,
                activated ? "Activated" : "Draft", open ? "Marked open" : "Marked closed");
    }
}
