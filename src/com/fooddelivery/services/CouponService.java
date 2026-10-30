package com.fooddelivery.services;

import com.fooddelivery.models.Coupon;
import com.fooddelivery.repository.CouponRepository;
import java.time.LocalDate;
import java.util.*;

public class CouponService {
    private final CouponRepository repo;

    public CouponService(CouponRepository repo) { this.repo = repo; }

    public Coupon addCoupon(String code, String type, double value, double minOrder, LocalDate expiry) {
        String t = type.toUpperCase();
        if (!t.equals(Coupon.PERCENT) && !t.equals(Coupon.FLAT))
            throw new IllegalArgumentException("Discount type must be PERCENT or FLAT");
        if (value <= 0) throw new IllegalArgumentException("Discount value must be greater than 0");
        if (t.equals(Coupon.PERCENT) && value > 100)
            throw new IllegalArgumentException("Percent discount cannot exceed 100");
        if (repo.findByCode(code).isPresent())
            throw new IllegalStateException("Coupon already exists: " + code);
        Coupon c = new Coupon(code, t, value, minOrder, expiry);
        repo.save(c);
        return c;
    }

    public Coupon getCoupon(String code) {
        return repo.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("Coupon not found: " + code));
    }

    // Used by Order: returns discount amount for this order (0 if not applicable)
    public double applyCoupon(String code, double orderAmount) {
        return getCoupon(code).calculateDiscount(orderAmount);
    }

    public boolean removeCoupon(String code) { return repo.delete(code); }

    public List<Coupon> getAllCoupons() { return repo.findAll(); }
}