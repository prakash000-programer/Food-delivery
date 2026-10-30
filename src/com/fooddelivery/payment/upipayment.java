package com.fooddelivery.payment;

public class UpiPayment implements PaymentMethod {

    @Override
    public boolean pay(double amount) {
        if (amount <= 0) return false;
        System.out.println("[UPI] Paid Rs." + amount);
        return true;
    }

    @Override
    public boolean refund(double amount) {
        if (amount <= 0) return false;
        System.out.println("[UPI] Refunded Rs." + amount);
        return true;
    }
}