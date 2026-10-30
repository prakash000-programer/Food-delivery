package com.fooddelivery.models;

import com.fooddelivery.payment.*;
import java.time.LocalDateTime;

public class Payment {
    public static final String UPI = "UPI";
    public static final String CARD = "CARD";
    public static final String CASH = "CASH";

    public static final String PENDING = "PENDING";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String REFUNDED = "REFUNDED";

    private String paymentId;
    private String orderId;          // links Payment to its Order (1 order -> 1 payment)
    private double amount;
    private String paymentType;
    private String paymentStatus;
    private LocalDateTime paymentTime;

    public Payment(String paymentId, String orderId, double amount, String paymentType) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentType = paymentType.toUpperCase();
        this.paymentStatus = PENDING;
    }

    // Picks the PaymentMethod implementation based on paymentType
    private PaymentMethod getMethod() {
        switch (paymentType) {
            case UPI:  return new UpiPayment();
            case CARD: return new CardPayment();
            case CASH: return new CashPayment();
            default:   return null;
        }
    }

    public boolean processPayment() {
        if (!PENDING.equals(paymentStatus)) return false;
        PaymentMethod method = getMethod();
        boolean ok = method != null && method.pay(amount);
        paymentStatus = ok ? SUCCESS : FAILED;
        paymentTime = LocalDateTime.now();
        return ok;
    }

    public boolean refundPayment() {
        if (!SUCCESS.equals(paymentStatus)) return false;
        PaymentMethod method = getMethod();
        boolean ok = method != null && method.refund(amount);
        if (ok) paymentStatus = REFUNDED;
        return ok;
    }

    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public String getPaymentType() { return paymentType; }
    public String getPaymentStatus() { return paymentStatus; }
    public LocalDateTime getPaymentTime() { return paymentTime; }

    @Override
    public String toString() {
        return "Payment{id=" + paymentId + ", order=" + orderId + ", amount=" + amount
                + ", type=" + paymentType + ", status=" + paymentStatus + ", time=" + paymentTime + "}";
    }
}