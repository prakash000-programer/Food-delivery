package com.fooddelivery.models;

public class DeliveryPartner {

    public static final String AVAILABLE = "AVAILABLE";
    public static final String BUSY = "BUSY";
    public static final String OFFLINE = "OFFLINE";

    private String partnerId;
    private String name;
    private String phone;
    private String city;
    private String status;
    private double totalEarnings;

    public DeliveryPartner(String partnerId, String name, String phone, String city) {
        this.partnerId = partnerId;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.status = AVAILABLE;
        this.totalEarnings = 0.0;
    }

    public boolean acceptDelivery() {
        if (!AVAILABLE.equals(status)) {
            return false;
        }
        this.status = BUSY;
        return true;
    }

    public boolean rejectDelivery() {
        if (OFFLINE.equals(status)) {
            return false;
        }
        this.status = AVAILABLE;
        return true;
    }

    public boolean updateStatus(String newStatus) {
        if (AVAILABLE.equals(newStatus) || BUSY.equals(newStatus) || OFFLINE.equals(newStatus)) {
            this.status = newStatus;
            return true;
        }
        return false;
    }

    public boolean addEarnings(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.totalEarnings += amount;
        return true;
    }

    public String getPartnerId() { return partnerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getStatus() { return status; }

    public double getTotalEarnings() { return totalEarnings; }

    @Override
    public String toString() {
        return String.format("%-6s | %-15s | %-10s | %-12s | %-9s | Rs.%.2f",
                partnerId, name, phone, city, status, totalEarnings);
    }
}
