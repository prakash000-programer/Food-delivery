package com.fooddelivery.models;

import java.time.LocalDate;

public class Coupon {
    public static final String PERCENT = "PERCENT";
    public static final String FLAT = "FLAT";

    private String couponCode;
    private String discountType;     // PERCENT or FLAT
    private double discountValue;
    private double minimumOrderValue;
    private LocalDate expiryDate;

    public Coupon(String couponCode, String discountType, double discountValue,
                  double minimumOrderValue, LocalDate expiryDate) {
        this.couponCode = couponCode.toUpperCase();
        this.discountType = discountType.toUpperCase();
        this.discountValue = discountValue;
        this.minimumOrderValue = minimumOrderValue;
        this.expiryDate = expiryDate;
    }

    // Not expired
    public boolean isValid() {
        return !LocalDate.now().isAfter(expiryDate);
    }

    // Not expired and order meets minimum value
    public boolean isValid(double orderAmount) {
        return isValid() && orderAmount >= minimumOrderValue;
    }

    public double calculateDiscount(double orderAmount) {
        if (!isValid(orderAmount)) return 0;
        double discount = PERCENT.equals(discountType)
                ? orderAmount * discountValue / 100.0
                : discountValue;
        return Math.min(discount, orderAmount);   // never exceed order amount
    }

    public String getCouponCode() { return couponCode; }
    public String getDiscountType() { return discountType; }
    public double getDiscountValue() { return discountValue; }
    public double getMinimumOrderValue() { return minimumOrderValue; }
    public LocalDate getExpiryDate() { return expiryDate; }

    @Override
    public String toString() {
        return "Coupon{code=" + couponCode + ", type=" + discountType + ", value=" + discountValue
                + ", minOrder=" + minimumOrderValue + ", expiry=" + expiryDate + "}";
    }
}