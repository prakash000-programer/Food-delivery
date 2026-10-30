package com.fooddelivery.payment;

public class CashPayment implements PaymentMethod {

    @Override
    public boolean pay(double amount) {
        if (amount <= 0) return false;
        System.out.println("[CASH] Paid Rs." + amount);
        return true;
    }

    @Override
    public boolean refund(double amount) {
        if (amount <= 0) return false;
        System.out.println("[CASH] Refunded Rs." + amount);
        return true;
    }
}