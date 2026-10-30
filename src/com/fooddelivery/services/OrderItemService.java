package com.fooddelivery.services;

import com.fooddelivery.models.Cart;
import com.fooddelivery.models.MenuItem;
import com.fooddelivery.models.OrderItem;
import com.fooddelivery.repository.OrderItemRepository;
import java.util.ArrayList;
import java.util.List;

public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        if (orderItemRepository == null) {
            throw new IllegalArgumentException("Order item repository is required.");
        }
        this.orderItemRepository = orderItemRepository;
    }

    // Call after checkout validation. Clear the cart only after successful placement.
    public List<OrderItem> saveOrderItems(String orderId, Cart cart) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalStateException("Cannot create order items from an empty cart.");
        }
        List<OrderItem> items = new ArrayList<>();
        for (MenuItem item : cart.getItems()) {
            if (!item.isAvailable()) {
                throw new IllegalStateException("Remove unavailable items before checkout.");
            }
            items.add(new OrderItem(item.getName(), cart.getQuantity(item.getItemId()), item.getPrice()));
        }
        orderItemRepository.save(orderId, items);
        return orderItemRepository.findByOrderId(orderId);
    }

    public List<OrderItem> getOrderItems(String orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
    public double calculateTotal(String orderId) {
        double total = 0;
        for (OrderItem item : getOrderItems(orderId)) { total += item.calculateSubtotal(); }
        return total;
    }
    public void removeOrderItems(String orderId) {
        orderItemRepository.removeByOrderId(orderId);
    }
}
