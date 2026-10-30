package com.fooddelivery.cli;

import com.fooddelivery.models.DeliveryPartner;
import com.fooddelivery.services.DeliveryPartnerService;

import java.util.List;
import java.util.Scanner;

public class DeliveryPartnerCLI {

    private final DeliveryPartnerService service;
    private final Scanner scanner;

    public DeliveryPartnerCLI(DeliveryPartnerService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    /** Call this from the team's Main menu. Returns when user chooses Back. */
    public void start() {
        while (true) {
            System.out.println("\n===== DELIVERY PARTNER MENU =====");
            System.out.println("1. Register partner");
            System.out.println("2. View all partners");
            System.out.println("3. View partner by ID");
            System.out.println("4. View available partners by city");
            System.out.println("5. Accept delivery");
            System.out.println("6. Reject delivery");
            System.out.println("7. Update status");
            System.out.println("8. Complete delivery (add earnings)");
            System.out.println("9. View total earnings");
            System.out.println("10. Remove partner");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": register(); break;
                    case "2": viewAll(); break;
                    case "3": viewById(); break;
                    case "4": viewAvailableByCity(); break;
                    case "5": accept(); break;
                    case "6": reject(); break;
                    case "7": updateStatus(); break;
                    case "8": completeDelivery(); break;
                    case "9": viewEarnings(); break;
                    case "10": remove(); break;
                    case "0": return;
                    default: System.out.println("Invalid choice. Try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void register() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Phone (10 digits): ");
        String phone = scanner.nextLine().trim();
        System.out.print("City: ");
        String city = scanner.nextLine();
        DeliveryPartner p = service.registerPartner(name, phone, city);
        System.out.println("Partner registered successfully! ID: " + p.getPartnerId());
    }

    private void viewAll() {
        List<DeliveryPartner> list = service.getAllPartners();
        if (list.isEmpty()) {
            System.out.println("No delivery partners found.");
            return;
        }
        printHeader();
        for (DeliveryPartner p : list) {
            System.out.println(p);
        }
    }

    private void viewById() {
        DeliveryPartner p = service.getPartner(readId());
        printHeader();
        System.out.println(p);
    }

    private void viewAvailableByCity() {
        System.out.print("City: ");
        String city = scanner.nextLine().trim();
        List<DeliveryPartner> list = service.getAvailablePartnersByCity(city);
        if (list.isEmpty()) {
            System.out.println("No available partners in " + city + ".");
            return;
        }
        printHeader();
        for (DeliveryPartner p : list) {
            System.out.println(p);
        }
    }

    private void accept() {
        boolean ok = service.acceptDelivery(readId());
        System.out.println(ok ? "Delivery accepted. Status: BUSY" : "Cannot accept. Partner is not AVAILABLE.");
    }

    private void reject() {
        boolean ok = service.rejectDelivery(readId());
        System.out.println(ok ? "Delivery rejected. Status: AVAILABLE" : "Cannot reject. Partner is OFFLINE.");
    }

    private void updateStatus() {
        String id = readId();
        System.out.print("New status (AVAILABLE / BUSY / OFFLINE): ");
        String status = scanner.nextLine().trim();
        boolean ok = service.updateStatus(id, status);
        System.out.println(ok ? "Status updated." : "Invalid status.");
    }

    private void completeDelivery() {
        String id = readId();
        double amount = readDouble("Earning amount: ");
        boolean ok = service.completeDelivery(id, amount);
        System.out.println(ok ? "Delivery completed. Earnings added." : "Earning must be greater than 0.");
    }

    private void viewEarnings() {
        String id = readId();
        System.out.printf("Total earnings: Rs.%.2f%n", service.getTotalEarnings(id));
    }

    private void remove() {
        boolean ok = service.removePartner(readId());
        System.out.println(ok ? "Partner removed." : "Partner not found.");
    }

    private String readId() {
        System.out.print("Partner ID: ");
        return scanner.nextLine().trim();
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }

    private void printHeader() {
        System.out.println("ID     | Name            | Phone      | City         | Status    | Earnings");
        System.out.println("-----------------------------------------------------------------------------");
    }
}
