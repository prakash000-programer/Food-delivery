package com.fooddelivery.cli;

import com.fooddelivery.services.PaymentService;
import java.util.Scanner;

public class PaymentCli {
    private final PaymentService service;
    private final Scanner sc;

    public PaymentCli(PaymentService service, Scanner sc) {
        this.service = service;
        this.sc = sc;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Payment Menu ---");
            System.out.println("1. Create payment");
            System.out.println("2. Process payment");
            System.out.println("3. Refund payment");
            System.out.println("4. View payment by ID");
            System.out.println("5. View payment by order ID");
            System.out.println("6. View all payments");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        System.out.print("Order ID: ");
                        String orderId = sc.nextLine().trim();
                        System.out.print("Amount: ");
                        double amount = Double.parseDouble(sc.nextLine().trim());
                        System.out.print("Type (UPI/CARD/CASH): ");
                        String type = sc.nextLine().trim();
                        System.out.println("Created: " + service.createPayment(orderId, amount, type));
                        break;
                    case "2":
                        System.out.print("Payment ID: ");
                        boolean ok = service.processPayment(sc.nextLine().trim());
                        System.out.println(ok ? "Payment successful." : "Payment failed / not pending.");
                        break;
                    case "3":
                        System.out.print("Payment ID: ");
                        boolean refunded = service.refundPayment(sc.nextLine().trim());
                        System.out.println(refunded ? "Refund successful." : "Refund not possible.");
                        break;
                    case "4":
                        System.out.print("Payment ID: ");
                        System.out.println(service.getPayment(sc.nextLine().trim()));
                        break;
                    case "5":
                        System.out.print("Order ID: ");
                        System.out.println(service.getPaymentByOrder(sc.nextLine().trim()));
                        break;
                    case "6":
                        if (service.getAllPayments().isEmpty()) System.out.println("No payments.");
                        service.getAllPayments().forEach(System.out::println);
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: enter a valid number.");
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}