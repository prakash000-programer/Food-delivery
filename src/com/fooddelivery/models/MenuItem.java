
package fooddelivery.models;

public class MenuItem {

    // Attributes
    private String itemId;
    private String name;
    private double price;
    private String category;
    private boolean available;

    // Constructor
    public MenuItem(String itemId, String name,
                    double price, String category) {

        if (itemId == null || itemId.isBlank()
                || name == null || name.isBlank()
                || category == null || category.isBlank()
                || price < 0) {
            throw new IllegalArgumentException(
                    "Invalid menu item details");
        }

        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = true;
    }

    // Update Menu Item Price
    public void updatePrice(double price) {

        if (price < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative");
        }

        this.price = price;
    }

    // Change Menu Item Availability
    public void changeAvailability(boolean available) {
        this.available = available;
    }

    // Getters
    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "\nItem ID: " + itemId
                + "\nName: " + name
                + "\nPrice: Rs." + price
                + "\nCategory: " + category
                + "\nAvailability: "
                + (available ? "Available" : "Not Available");
    }
}
