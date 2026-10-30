package com.fooddelivery.payment;

public class CardPayment implements PaymentMethod {

    @Override
    public boolean pay(double amount) {
        if (amount <= 0) return false;
        System.out.println("[CARD] Paid Rs." + amount);
        return true;
    }

    @Override
    public boolean refund(double amount) {
        if (amount <= 0) return false;
        System.out.println("[CARD] Refunded Rs." + amount);
        return true;
    }
}