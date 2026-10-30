package com.fooddelivery.models;

public final class MenuItem {
    private final String itemId;
    private final String restaurantId;
    private final String name;
    private double price;
    private final String category;
    private boolean available;

    public MenuItem(String itemId, String restaurantId, String name, double price,
                    String category, boolean available) {
        requireText(itemId, "Item ID");
        requireText(restaurantId, "Restaurant ID");
        requireText(name, "Item name");
        if (!"Veg".equals(category) && !"Non-Veg".equals(category)) {
            throw new IllegalArgumentException("Category must be Veg or Non-Veg.");
        }
        this.itemId = itemId;
        this.restaurantId = restaurantId;
        this.name = name.trim();
        this.category = category;
        updatePrice(price);
        this.available = available;
    }

    public String getItemId() { return itemId; }
    public String getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return available; }

    public void updatePrice(double price) {
        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero and finite.");
        }
        this.price = price;
    }
    public void changeAvailability(boolean available) { this.available = available; }

    private static void requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required.");
        }
    }
    @Override
    public String toString() {
        return String.format("%s | %s | %s | Rs.%.2f | %s", itemId, name, category,
                price, available ? "Available" : "Unavailable");
    }
}
