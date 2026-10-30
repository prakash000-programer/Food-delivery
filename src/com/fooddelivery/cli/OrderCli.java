package com.fooddelivery.cli;

import com.fooddelivery.models.Order;
import com.fooddelivery.services.OrderService;

import java.util.Scanner;

/** Console menu for exercising OrderService. */
public class OrderCli {

    private final OrderService orderService;
    private final Scanner scanner;

    public OrderCli(OrderService orderService, Scanner scanner) {
        this.orderService = orderService;
        this.scanner = scanner;
    }

    public void showMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Order Menu ---");
            System.out.println("1. Create Order");
            System.out.println("2. Update Order Status");
            System.out.println("3. Cancel Order");
            System.out.println("4. View Order Total");
            System.out.println("5. View Orders for a User");
            System.out.println("6. View All Orders");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createOrder();
                    break;
                case "2":
                    updateStatus();
                    break;
                case "3":
                    cancelOrder();
                    break;
                case "4":
                    viewTotal();
                    break;
                case "5":
                    viewOrdersForUser();
                    break;
                case "6":
                    viewAllOrders();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void createOrder() {
        System.out.print("Order ID: ");
        String orderId = scanner.nextLine().trim();
        System.out.print("User ID: ");
        String userId = scanner.nextLine().trim();
        System.out.print("Restaurant ID: ");
        String restaurantId = scanner.nextLine().trim();
        double itemsTotal = readDouble("Items Total: ");
        double deliveryFee = readDouble("Delivery Fee: ");
        double gst = readDouble("GST: ");
        double discount = readDouble("Discount: ");

        Order order = orderService.createOrder(orderId, userId, restaurantId, itemsTotal, deliveryFee, gst, discount);
        System.out.println("Created: " + order);
    }

    private void updateStatus() {
        System.out.print("Order ID: ");
        String orderId = scanner.nextLine().trim();
        System.out.print("New Status: ");
        String status = scanner.nextLine().trim();
        orderService.updateOrderStatus(orderId, status);
    }

    private void cancelOrder() {
        System.out.print("Order ID: ");
        String orderId = scanner.nextLine().trim();
        orderService.cancelOrder(orderId);
    }

    private void viewTotal() {
        System.out.print("Order ID: ");
        String orderId = scanner.nextLine().trim();
        System.out.println("Final Amount: " + orderService.getOrderTotal(orderId));
    }

    private void viewOrdersForUser() {
        System.out.print("User ID: ");
        String userId = scanner.nextLine().trim();
        orderService.getOrdersForUser(userId).forEach(System.out::println);
    }

    private void viewAllOrders() {
        orderService.getAllOrders().forEach(System.out::println);
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}