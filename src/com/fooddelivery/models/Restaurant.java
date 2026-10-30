
package fooddelivery.models;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    // Attributes
    private String restaurantId;
    private String name;
    private String cuisineType;
    private String city;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private double minimumOrderValue;
    private boolean isOpen;

    // One Restaurant contains multiple MenuItems
    private List<MenuItem> menuItems;

    // Constructor
    public Restaurant(String restaurantId, String name,
                       String cuisineType, String city,
                       LocalTime openingTime,
                       LocalTime closingTime,
                       double minimumOrderValue) {

        if (restaurantId == null || restaurantId.isBlank()
                || name == null || name.isBlank()
                || cuisineType == null || cuisineType.isBlank()
                || city == null || city.isBlank()
                || openingTime == null || closingTime == null
                || minimumOrderValue < 0) {
            throw new IllegalArgumentException(
                    "Invalid restaurant details");
        }

        this.restaurantId = restaurantId;
        this.name = name;
        this.cuisineType = cuisineType;
        this.city = city;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minimumOrderValue = minimumOrderValue;
        this.isOpen = true;

        this.menuItems = new ArrayList<>();
    }

    // Update Restaurant Details
    public void updateDetails(String name, String cuisineType,
                              String city, LocalTime openingTime,
                              LocalTime closingTime,
                              double minimumOrderValue) {

        if (name == null || name.isBlank()
                || cuisineType == null || cuisineType.isBlank()
                || city == null || city.isBlank()
                || openingTime == null || closingTime == null
                || minimumOrderValue < 0) {
            throw new IllegalArgumentException(
                    "Invalid restaurant details");
        }

        this.name = name;
        this.cuisineType = cuisineType;
        this.city = city;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minimumOrderValue = minimumOrderValue;
    }

    // Change Restaurant Availability
    public void changeAvailability(boolean isOpen) {
        this.isOpen = isOpen;
    }

    // Add Menu Item
    public void addMenuItem(MenuItem menuItem) {

        if (menuItem == null) {
            throw new IllegalArgumentException(
                    "Menu item cannot be null");
        }

        menuItems.add(menuItem);
    }

    // Remove Menu Item
    public void removeMenuItem(String itemId) {

        menuItems.removeIf(
                item -> item.getItemId().equals(itemId));
    }

    // Getters
    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getCity() {
        return city;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public double getMinimumOrderValue() {
        return minimumOrderValue;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public List<MenuItem> getMenuItems() {
        return new ArrayList<>(menuItems);
    }

    @Override
    public String toString() {
        return "\nRestaurant ID: " + restaurantId
                + "\nName: " + name
                + "\nCuisine Type: " + cuisineType
                + "\nCity: " + city
                + "\nOpening Time: " + openingTime
                + "\nClosing Time: " + closingTime
                + "\nMinimum Order Value: Rs." + minimumOrderValue
                + "\nStatus: " + (isOpen ? "Open" : "Closed");
    }
}
