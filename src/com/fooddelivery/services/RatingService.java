package com.fooddelivery.services;

import com.fooddelivery.models.Rating;
import com.fooddelivery.repository.RatingRepository;

import java.util.List;

public class RatingService {

    private final RatingRepository repository;
    private int idCounter = 1;

    public RatingService(RatingRepository repository) {
        this.repository = repository;
    }

    public Rating submitRating(String userId, String restaurantId, String partnerId,
                               int restaurantRating, int partnerRating, String review) {
        if (userId == null || userId.trim().isEmpty()
                || restaurantId == null || restaurantId.trim().isEmpty()
                || partnerId == null || partnerId.trim().isEmpty()) {
            throw new IllegalArgumentException("userId, restaurantId and partnerId are required.");
        }
        String id = String.format("RT%03d", idCounter);
        Rating rating = new Rating(id, userId, restaurantId, partnerId,
                restaurantRating, partnerRating, review);

        if (!rating.validateRating()) {
            throw new IllegalArgumentException(
                    "Invalid rating. Ratings must be between 1 and 5 and review at most 500 characters.");
        }
        idCounter++;
        return repository.save(rating);
    }

    public Rating getRating(String ratingId) {
        return repository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found: " + ratingId));
    }

    public List<Rating> getAllRatings() {
        return repository.findAll();
    }

    public List<Rating> getRatingsByUser(String userId) {
        return repository.findByUserId(userId);
    }

    public List<Rating> getRatingsByRestaurant(String restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }

    public List<Rating> getRatingsByPartner(String partnerId) {
        return repository.findByPartnerId(partnerId);
    }

    public double getAverageRestaurantRating(String restaurantId) {
        List<Rating> list = repository.findByRestaurantId(restaurantId);
        if (list.isEmpty()) return 0.0;
        double sum = 0;
        for (Rating r : list) sum += r.getRestaurantRating();
        return sum / list.size();
    }

    public double getAveragePartnerRating(String partnerId) {
        List<Rating> list = repository.findByPartnerId(partnerId);
        if (list.isEmpty()) return 0.0;
        double sum = 0;
        for (Rating r : list) sum += r.getPartnerRating();
        return sum / list.size();
    }

    public boolean deleteRating(String ratingId) {
        return repository.deleteById(ratingId);
    }
}
