package com.fooddelivery.repository;

import com.fooddelivery.models.DeliveryPartner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** In-memory repository (no database needed for the CLI app). */
public class DeliveryPartnerRepository {

    private final Map<String, DeliveryPartner> store = new LinkedHashMap<>();

    public DeliveryPartner save(DeliveryPartner partner) {
        store.put(partner.getPartnerId(), partner);
        return partner;
    }

    public Optional<DeliveryPartner> findById(String partnerId) {
        return Optional.ofNullable(store.get(partnerId));
    }

    public List<DeliveryPartner> findAll() {
        return new ArrayList<>(store.values());
    }

    public List<DeliveryPartner> findByCity(String city) {
        List<DeliveryPartner> result = new ArrayList<>();
        for (DeliveryPartner p : store.values()) {
            if (p.getCity().equalsIgnoreCase(city)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<DeliveryPartner> findByStatus(String status) {
        List<DeliveryPartner> result = new ArrayList<>();
        for (DeliveryPartner p : store.values()) {
            if (p.getStatus().equalsIgnoreCase(status)) {
                result.add(p);
            }
        }
        return result;
    }

    public boolean existsById(String partnerId) {
        return store.containsKey(partnerId);
    }

    public boolean existsByPhone(String phone) {
        for (DeliveryPartner p : store.values()) {
            if (p.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(String partnerId) {
        return store.remove(partnerId) != null;
    }

    public int count() {
        return store.size();
    }
}
