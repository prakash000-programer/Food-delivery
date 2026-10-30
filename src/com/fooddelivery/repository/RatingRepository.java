package com.fooddelivery.repository;

import com.fooddelivery.models.Rating;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** In-memory repository (no database needed for the CLI app). */
public class RatingRepository {

    private final Map<String, Rating> store = new LinkedHashMap<>();

    public Rating save(Rating rating) {
        store.put(rating.getRatingId(), rating);
        return rating;
    }

    public Optional<Rating> findById(String ratingId) {
        return Optional.ofNullable(store.get(ratingId));
    }

    public List<Rating> findAll() {
        return new ArrayList<>(store.values());
    }

    public List<Rating> findByUserId(String userId) {
        List<Rating> result = new ArrayList<>();
        for (Rating r : store.values()) {
            if (r.getUserId().equals(userId)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Rating> findByRestaurantId(String restaurantId) {
        List<Rating> result = new ArrayList<>();
        for (Rating r : store.values()) {
            if (r.getRestaurantId().equals(restaurantId)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Rating> findByPartnerId(String partnerId) {
        List<Rating> result = new ArrayList<>();
        for (Rating r : store.values()) {
            if (r.getPartnerId().equals(partnerId)) {
                result.add(r);
            }
        }
        return result;
    }

    public boolean deleteById(String ratingId) {
        return store.remove(ratingId) != null;
    }

    public int count() {
        return store.size();
    }
}
