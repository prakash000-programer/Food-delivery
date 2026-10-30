package com.fooddelivery.services;

import com.fooddelivery.models.Payment;
import com.fooddelivery.repository.PaymentRepository;
import java.util.*;

public class PaymentService {
    private final PaymentRepository repo;
    private int counter = 1;

    public PaymentService(PaymentRepository repo) { this.repo = repo; }

    public Payment createPayment(String orderId, double amount, String type) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than 0");
        String t = type.toUpperCase();
        if (!t.equals(Payment.UPI) && !t.equals(Payment.CARD) && !t.equals(Payment.CASH))
            throw new IllegalArgumentException("Payment type must be UPI, CARD or CASH");
        if (repo.findByOrderId(orderId).isPresent())
            throw new IllegalStateException("Payment already exists for order " + orderId);
        Payment p = new Payment("PAY" + String.format("%03d", counter++), orderId, amount, t);
        repo.save(p);
        return p;
    }

    public boolean processPayment(String paymentId) {
        return getPayment(paymentId).processPayment();
    }

    public boolean refundPayment(String paymentId) {
        return getPayment(paymentId).refundPayment();
    }

    public Payment getPayment(String paymentId) {
        return repo.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found: " + paymentId));
    }

    public Payment getPaymentByOrder(String orderId) {
        return repo.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException("No payment for order: " + orderId));
    }

    public List<Payment> getAllPayments() { return repo.findAll(); }
}