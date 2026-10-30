package com.fooddelivery.models;

/**
 * Represents a user of the food delivery platform.
 * Fields match the class diagram: userId, name, phone, email, role, city.
 */
public class User {

    private String userId;
    private String name;
    private String phone;
    private String email;
    private String role; // e.g. "CUSTOMER", "RESTAURANT_OWNER", "ADMIN"
    private String city;

    public User(String userId, String name, String phone, String email, String role, String city) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.city = city;
    }

    // ---------- Getters & Setters ----------

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // ---------- Behaviour ----------

    /**
     * Simplified login check. The diagram has no password field, so this
     * verifies the given identifier matches this user's registered email
     * or phone number. Swap in real credential checking if your team adds
     * a password/auth field later.
     */
    public boolean login(String emailOrPhone) {
        boolean success = emailOrPhone != null
                && (emailOrPhone.equalsIgnoreCase(this.email) || emailOrPhone.equals(this.phone));

        System.out.println(success
                ? "[User] " + name + " logged in successfully."
                : "[User] Login failed for identifier: " + emailOrPhone);

        return success;
    }

    /**
     * Updates the mutable profile fields. Pass null or blank to leave a
     * field unchanged.
     */
    public void updateProfile(String name, String phone, String email, String city) {
        if (name != null && !name.isBlank()) this.name = name;
        if (phone != null && !phone.isBlank()) this.phone = phone;
        if (email != null && !email.isBlank()) this.email = email;
        if (city != null && !city.isBlank()) this.city = city;

        System.out.println("[User] Profile updated for userId=" + userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}