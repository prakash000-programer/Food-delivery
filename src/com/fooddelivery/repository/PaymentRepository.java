package com.fooddelivery.repository;

import com.fooddelivery.models.Payment;
import java.util.*;

public class PaymentRepository {
    private final Map<String, Payment> payments = new LinkedHashMap<>();

    public void save(Payment p) { payments.put(p.getPaymentId(), p); }

    public Optional<Payment> findById(String id) { return Optional.ofNullable(payments.get(id)); }

    public Optional<Payment> findByOrderId(String orderId) {
        return payments.values().stream().filter(p -> p.getOrderId().equals(orderId)).findFirst();
    }

    public List<Payment> findAll() { return new ArrayList<>(payments.values()); }

    public boolean exists(String id) { return payments.containsKey(id); }
}