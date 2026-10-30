package com.fooddelivery.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Rating {

    private String ratingId;
    private String userId;
    private String restaurantId;
    private String partnerId;
    private int restaurantRating;
    private int partnerRating;
    private String review;
    private LocalDateTime createdAt;

    public Rating(String ratingId, String userId, String restaurantId, String partnerId,
                  int restaurantRating, int partnerRating, String review) {
        this.ratingId = ratingId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.partnerId = partnerId;
        this.restaurantRating = restaurantRating;
        this.partnerRating = partnerRating;
        this.review = review;
        this.createdAt = LocalDateTime.now();
    }

    /** Both ratings must be 1-5, review max 500 chars. */
    public boolean validateRating() {
        boolean restaurantOk = restaurantRating >= 1 && restaurantRating <= 5;
        boolean partnerOk = partnerRating >= 1 && partnerRating <= 5;
        boolean reviewOk = review == null || review.length() <= 500;
        return restaurantOk && partnerOk && reviewOk;
    }

    public String getRatingId() { return ratingId; }
    public String getUserId() { return userId; }
    public String getRestaurantId() { return restaurantId; }
    public String getPartnerId() { return partnerId; }
    public int getRestaurantRating() { return restaurantRating; }
    public int getPartnerRating() { return partnerRating; }
    public String getReview() { return review; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return String.format("%s | User: %s | Restaurant: %s (%d/5) | Partner: %s (%d/5) | \"%s\" | %s",
                ratingId, userId, restaurantId, restaurantRating, partnerId, partnerRating,
                review, createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    }
}
