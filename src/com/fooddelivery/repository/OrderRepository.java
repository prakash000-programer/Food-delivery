package com.fooddelivery.repository;

import com.fooddelivery.models.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Simple in-memory repository for Order. Swap the backing Map for a real
 * database call later without changing the OrderService API.
 */
public class OrderRepository {

    private final Map<String, Order> orders = new HashMap<>();

    public Order save(Order order) {
        orders.put(order.getOrderId(), order);
        return order;
    }

    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public List<Order> findByUserId(String userId) {
        return orders.values().stream()
                .filter(o -> o.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public void deleteById(String orderId) {
        orders.remove(orderId);
    }
}