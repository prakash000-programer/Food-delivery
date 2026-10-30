package com.fooddelivery;

import com.fooddelivery.cli.OrderCli;
import com.fooddelivery.cli.UserCli;
import com.fooddelivery.models.Order;
import com.fooddelivery.models.User;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.services.OrderService;
import com.fooddelivery.services.UserService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Wire up repositories + services once; the CLI menus reuse these instances.
        UserRepository userRepository = new UserRepository();
        OrderRepository orderRepository = new OrderRepository();
        UserService userService = new UserService(userRepository);
        OrderService orderService = new OrderService(orderRepository);

        System.out.println("===== Automated test run =====");
        runAutomatedTest(userService, orderService);

        System.out.println("\n===== Interactive menu =====");
        Scanner scanner = new Scanner(System.in);
        UserCli userCli = new UserCli(userService, scanner);
        OrderCli orderCli = new OrderCli(orderService, scanner);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. User operations");
            System.out.println("2. Order operations");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    userCli.showMenu();
                    break;
                case "2":
                    orderCli.showMenu();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }

    /**
     * Exercises every method on User and Order, across the service + repository
     * layers, without needing any console input. Good first thing to run after
     * pulling the project to confirm your two entities work end to end.
     */
    private static void runAutomatedTest(UserService userService, OrderService orderService) {
        // 1. Register a user
        User user = userService.registerUser("U001", "Arun Kumar", "9876543210",
                "arun@example.com", "CUSTOMER", "Coimbatore");
        System.out.println("Registered: " + user);

        // 2. Login (success case)
        userService.login("U001", "arun@example.com");
        // Login (failure case)
        userService.login("U001", "wrong@example.com");

        // 3. Update profile
        userService.updateProfile("U001", "Arun K", null, null, "Chennai");
        userService.getUser("U001").ifPresent(u -> System.out.println("After update: " + u));

        // 4. Place an order for this user
        Order order = orderService.createOrder("O001", "U001", "R001",
                450.0, 30.0, 22.5, 50.0);
        System.out.println("Final amount: " + order.getFinalAmount());

        // 5. Walk the order through its lifecycle
        orderService.updateOrderStatus("O001", "CONFIRMED");
        orderService.updateOrderStatus("O001", "OUT_FOR_DELIVERY");
        orderService.updateOrderStatus("O001", "DELIVERED");

        // 6. Try cancelling a delivered order (should be blocked)
        orderService.cancelOrder("O001");

        // 7. List all orders placed by this user
        System.out.println("Orders for U001:");
        orderService.getOrdersForUser("U001").forEach(System.out::println);
    }
}