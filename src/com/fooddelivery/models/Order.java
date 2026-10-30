package com.fooddelivery.models;

import java.time.LocalDateTime;

/**
 * Represents an order placed by a User at a Restaurant.
 * Fields match the class diagram (orderId, status, orderTime, itemsTotal,
 * deliveryFee, gst, discount, finalAmount), plus userId/restaurantId which
 * the diagram shows only as associations but which a real implementation
 * needs to actually link an Order back to who placed it and where.
 */
public class Order {

    private String orderId;
    private String userId;
    private String restaurantId;
    private String status; // e.g. CREATED, PLACED, CONFIRMED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    private LocalDateTime orderTime;
    private double itemsTotal;
    private double deliveryFee;
    private double gst;
    private double discount;
    private double finalAmount;

    public Order(String orderId, String userId, String restaurantId,
                 double itemsTotal, double deliveryFee, double gst, double discount) {
        this.orderId = orderId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.itemsTotal = itemsTotal;
        this.deliveryFee = deliveryFee;
        this.gst = gst;
        this.discount = discount;
        this.status = "CREATED";
        this.orderTime = null;
        this.finalAmount = 0.0;
    }

    // ---------- Getters & Setters ----------

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public double getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(double itemsTotal) {
        this.itemsTotal = itemsTotal;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    // ---------- Behaviour ----------

    /** Marks the order as placed, stamps the order time, and totals it up. */
    public void placeOrder() {
        this.orderTime = LocalDateTime.now();
        this.status = "PLACED";
        calculateTotal();
        System.out.println("[Order] " + orderId + " placed at " + orderTime);
    }

    /** Moves the order to a new status (e.g. CONFIRMED, OUT_FOR_DELIVERY, DELIVERED). */
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("[Order] " + orderId + " status -> " + newStatus);
    }

    /** Cancels the order, unless it has already been delivered. */
    public void cancelOrder() {
        if ("DELIVERED".equalsIgnoreCase(status)) {
            System.out.println("[Order] " + orderId + " cannot be cancelled - already delivered.");
            return;
        }
        this.status = "CANCELLED";
        System.out.println("[Order] " + orderId + " cancelled.");
    }

    /** Recomputes finalAmount = itemsTotal + deliveryFee + gst - discount (floored at 0). */
    public double calculateTotal() {
        this.finalAmount = itemsTotal + deliveryFee + gst - discount;
        if (this.finalAmount < 0) {
            this.finalAmount = 0;
        }
        return this.finalAmount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", status='" + status + '\'' +
                ", orderTime=" + orderTime +
                ", itemsTotal=" + itemsTotal +
                ", deliveryFee=" + deliveryFee +
                ", gst=" + gst +
                ", discount=" + discount +
                ", finalAmount=" + finalAmount +
                '}';
    }
}