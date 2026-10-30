package com.fooddelivery.services;

import com.fooddelivery.models.Order;
import com.fooddelivery.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

/**
 * Business logic layer for Order. CLI classes and Main should talk to this,
 * never to OrderRepository directly.
 */
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String orderId, String userId, String restaurantId,
                              double itemsTotal, double deliveryFee, double gst, double discount) {
        Order order = new Order(orderId, userId, restaurantId, itemsTotal, deliveryFee, gst, discount);
        order.placeOrder();
        orderRepository.save(order);
        return order;
    }

    public void updateOrderStatus(String orderId, String status) {
        orderRepository.findById(orderId).ifPresentOrElse(
                o -> o.updateStatus(status),
                () -> System.out.println("[OrderService] No order found with id " + orderId)
        );
    }

    public void cancelOrder(String orderId) {
        orderRepository.findById(orderId).ifPresentOrElse(
                Order::cancelOrder,
                () -> System.out.println("[OrderService] No order found with id " + orderId)
        );
    }

    public double getOrderTotal(String orderId) {
        return orderRepository.findById(orderId)
                .map(Order::calculateTotal)
                .orElse(0.0);
    }

    public List<Order> getOrdersForUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}