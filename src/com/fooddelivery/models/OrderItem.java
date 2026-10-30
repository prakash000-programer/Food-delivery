package com.fooddelivery.models;

// An order keeps a snapshot, so later menu changes cannot change its bill.
public class OrderItem {
    private final String itemName;
    private final int quantity;
    private final double priceAtOrder;

    public OrderItem(String itemName, int quantity, double priceAtOrder) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name is required.");
        }
        if (quantity < 1 || quantity > 10) {
            throw new IllegalArgumentException("Quantity must be between 1 and 10.");
        }
        if (!Double.isFinite(priceAtOrder) || priceAtOrder <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.itemName = itemName;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getPriceAtOrder() { return priceAtOrder; }
    public double calculateSubtotal() { return priceAtOrder * quantity; }

    @Override
    public String toString() {
        return String.format("%s x %d @ Rs.%.2f = Rs.%.2f",
                itemName, quantity, priceAtOrder, calculateSubtotal());
    }
}
