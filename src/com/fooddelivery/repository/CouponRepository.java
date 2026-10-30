package com.fooddelivery.repository;

import com.fooddelivery.models.Coupon;
import java.util.*;

public class CouponRepository {
    private final Map<String, Coupon> coupons = new LinkedHashMap<>();

    public void save(Coupon c) { coupons.put(c.getCouponCode(), c); }

    public Optional<Coupon> findByCode(String code) {
        return Optional.ofNullable(coupons.get(code.toUpperCase()));
    }

    public List<Coupon> findAll() { return new ArrayList<>(coupons.values()); }

    public boolean delete(String code) { return coupons.remove(code.toUpperCase()) != null; }
}