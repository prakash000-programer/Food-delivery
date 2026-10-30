package com.fooddelivery.services;

import com.fooddelivery.models.User;
import com.fooddelivery.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Business logic layer for User. CLI classes and Main should talk to this,
 * never to UserRepository directly.
 */
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String userId, String name, String phone, String email, String role, String city) {
        if (userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User already exists: " + userId);
        }
        User user = new User(userId, name, phone, email, role, city);
        userRepository.save(user);
        return user;
    }

    public boolean login(String userId, String emailOrPhone) {
        return userRepository.findById(userId)
                .map(u -> u.login(emailOrPhone))
                .orElseGet(() -> {
                    System.out.println("[UserService] No user found with id " + userId);
                    return false;
                });
    }

    public void updateProfile(String userId, String name, String phone, String email, String city) {
        userRepository.findById(userId).ifPresentOrElse(
                u -> u.updateProfile(name, phone, email, city),
                () -> System.out.println("[UserService] No user found with id " + userId)
        );
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}