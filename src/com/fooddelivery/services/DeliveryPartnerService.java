package com.fooddelivery.services;

import com.fooddelivery.models.DeliveryPartner;
import com.fooddelivery.repository.DeliveryPartnerRepository;

import java.util.List;

public class DeliveryPartnerService {

    private final DeliveryPartnerRepository repository;
    private int idCounter = 1;

    public DeliveryPartnerService(DeliveryPartnerRepository repository) {
        this.repository = repository;
    }

    public DeliveryPartner registerPartner(String name, String phone, String city) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone must be a 10-digit number.");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty.");
        }
        if (repository.existsByPhone(phone)) {
            throw new IllegalArgumentException("A partner with this phone already exists.");
        }
        String id = String.format("DP%03d", idCounter++);
        DeliveryPartner partner = new DeliveryPartner(id, name.trim(), phone, city.trim());
        return repository.save(partner);
    }

    public DeliveryPartner getPartner(String partnerId) {
        return repository.findById(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery partner not found: " + partnerId));
    }

    public List<DeliveryPartner> getAllPartners() {
        return repository.findAll();
    }

    public List<DeliveryPartner> getAvailablePartnersByCity(String city) {
        List<DeliveryPartner> inCity = repository.findByCity(city);
        inCity.removeIf(p -> !DeliveryPartner.AVAILABLE.equals(p.getStatus()));
        return inCity;
    }

    public boolean acceptDelivery(String partnerId) {
        DeliveryPartner partner = getPartner(partnerId);
        boolean ok = partner.acceptDelivery();
        if (ok) repository.save(partner);
        return ok;
    }

    public boolean rejectDelivery(String partnerId) {
        DeliveryPartner partner = getPartner(partnerId);
        boolean ok = partner.rejectDelivery();
        if (ok) repository.save(partner);
        return ok;
    }

    public boolean updateStatus(String partnerId, String newStatus) {
        DeliveryPartner partner = getPartner(partnerId);
        boolean ok = partner.updateStatus(newStatus.toUpperCase());
        if (ok) repository.save(partner);
        return ok;
    }

    /** Call after a delivery is completed: adds earnings and frees the partner. */
    public boolean completeDelivery(String partnerId, double earning) {
        DeliveryPartner partner = getPartner(partnerId);
        if (!partner.addEarnings(earning)) {
            return false;
        }
        partner.updateStatus(DeliveryPartner.AVAILABLE);
        repository.save(partner);
        return true;
    }

    public boolean addEarnings(String partnerId, double amount) {
        DeliveryPartner partner = getPartner(partnerId);
        boolean ok = partner.addEarnings(amount);
        if (ok) repository.save(partner);
        return ok;
    }

    public double getTotalEarnings(String partnerId) {
        return getPartner(partnerId).getTotalEarnings();
    }

    public boolean removePartner(String partnerId) {
        return repository.deleteById(partnerId);
    }
}
