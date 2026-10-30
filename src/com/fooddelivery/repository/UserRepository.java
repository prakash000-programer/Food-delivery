package com.fooddelivery.repository;

import com.fooddelivery.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Simple in-memory repository for User. Swap the backing Map for a real
 * database call later without changing the UserService API.
 */
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public User save(User user) {
        users.put(user.getUserId(), user);
        return user;
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public boolean existsById(String userId) {
        return users.containsKey(userId);
    }

    public void deleteById(String userId) {
        users.remove(userId);
    }
}