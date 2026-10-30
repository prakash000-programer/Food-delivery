package com.fooddelivery.repository;

import com.fooddelivery.models.OrderItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderItemRepository {
    private final Map<String, List<OrderItem>> itemsByOrderId = new HashMap<>();

    public void save(String orderId, List<OrderItem> items) {
        validateOrderId(orderId);
        if (itemsByOrderId.containsKey(orderId)) {
            throw new IllegalStateException("Items are already saved for this order.");
        }
        if (items == null || items.isEmpty() || items.contains(null)) {
            throw new IllegalArgumentException("An order must contain valid items.");
        }
        itemsByOrderId.put(orderId, new ArrayList<>(items));
    }

    public List<OrderItem> findByOrderId(String orderId) {
        validateOrderId(orderId);
        List<OrderItem> items = itemsByOrderId.get(orderId);
        if (items == null) { return Collections.emptyList(); }
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    public void removeByOrderId(String orderId) {
        validateOrderId(orderId);
        itemsByOrderId.remove(orderId);
    }

    private void validateOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required.");
        }
    }
}
