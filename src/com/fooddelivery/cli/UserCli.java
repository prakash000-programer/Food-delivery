package com.fooddelivery.cli;

import com.fooddelivery.models.User;
import com.fooddelivery.services.UserService;

import java.util.Scanner;

/** Console menu for exercising UserService. */
public class UserCli {

    private final UserService userService;
    private final Scanner scanner;

    public UserCli(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    public void showMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("3. Update Profile");
            System.out.println("4. View User");
            System.out.println("5. View All Users");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    loginUser();
                    break;
                case "3":
                    updateProfile();
                    break;
                case "4":
                    viewUser();
                    break;
                case "5":
                    viewAllUsers();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void registerUser() {
        System.out.print("User ID: ");
        String userId = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Role (CUSTOMER/RESTAURANT_OWNER/ADMIN): ");
        String role = scanner.nextLine().trim();
        System.out.print("City: ");
        String city = scanner.nextLine().trim();

        try {
            User user = userService.registerUser(userId, name, phone, email, role, city);
            System.out.println("Registered: " + user);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void loginUser() {
        System.out.print("User ID: ");
        String userId = scanner.nextLine().trim();
        System.out.print("Email or Phone: ");
        String identifier = scanner.nextLine().trim();
        userService.login(userId, identifier);
    }

    private void updateProfile() {
        System.out.print("User ID: ");
        String userId = scanner.nextLine().trim();
        System.out.print("New Name (blank to skip): ");
        String name = scanner.nextLine().trim();
        System.out.print("New Phone (blank to skip): ");
        String phone = scanner.nextLine().trim();
        System.out.print("New Email (blank to skip): ");
        String email = scanner.nextLine().trim();
        System.out.print("New City (blank to skip): ");
        String city = scanner.nextLine().trim();

        userService.updateProfile(userId, name, phone, email, city);
    }

    private void viewUser() {
        System.out.print("User ID: ");
        String userId = scanner.nextLine().trim();
        userService.getUser(userId).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No user found with id " + userId)
        );
    }

    private void viewAllUsers() {
        userService.getAllUsers().forEach(System.out::println);
    }
}