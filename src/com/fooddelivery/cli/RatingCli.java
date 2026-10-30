package com.fooddelivery.cli;

import com.fooddelivery.models.Rating;
import com.fooddelivery.services.RatingService;

import java.util.List;
import java.util.Scanner;

public class RatingCLI {

    private final RatingService service;
    private final Scanner scanner;

    public RatingCLI(RatingService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    /** Call this from the team's Main menu. Returns when user chooses Back. */
    public void start() {
        while (true) {
            System.out.println("\n===== RATING MENU =====");
            System.out.println("1. Submit rating");
            System.out.println("2. View all ratings");
            System.out.println("3. View ratings by user");
            System.out.println("4. View ratings by restaurant");
            System.out.println("5. View ratings by delivery partner");
            System.out.println("6. Average restaurant rating");
            System.out.println("7. Average partner rating");
            System.out.println("8. Delete rating");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": submit(); break;
                    case "2": printList(service.getAllRatings()); break;
                    case "3": printList(service.getRatingsByUser(readText("User ID: "))); break;
                    case "4": printList(service.getRatingsByRestaurant(readText("Restaurant ID: "))); break;
                    case "5": printList(service.getRatingsByPartner(readText("Partner ID: "))); break;
                    case "6": averageRestaurant(); break;
                    case "7": averagePartner(); break;
                    case "8": delete(); break;
                    case "0": return;
                    default: System.out.println("Invalid choice. Try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void submit() {
        String userId = readText("User ID: ");
        String restaurantId = readText("Restaurant ID: ");
        String partnerId = readText("Delivery Partner ID: ");
        int restaurantRating = readInt("Restaurant rating (1-5): ");
        int partnerRating = readInt("Partner rating (1-5): ");
        String review = readText("Review (optional): ");
        Rating r = service.submitRating(userId, restaurantId, partnerId,
                restaurantRating, partnerRating, review);
        System.out.println("Rating submitted successfully! ID: " + r.getRatingId());
    }

    private void averageRestaurant() {
        String id = readText("Restaurant ID: ");
        System.out.printf("Average restaurant rating: %.2f / 5%n", service.getAverageRestaurantRating(id));
    }

    private void averagePartner() {
        String id = readText("Partner ID: ");
        System.out.printf("Average partner rating: %.2f / 5%n", service.getAveragePartnerRating(id));
    }

    private void delete() {
        boolean ok = service.deleteRating(readText("Rating ID: "));
        System.out.println(ok ? "Rating deleted." : "Rating not found.");
    }

    private void printList(List<Rating> list) {
        if (list.isEmpty()) {
            System.out.println("No ratings found.");
            return;
        }
        for (Rating r : list) {
            System.out.println(r);
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }
}
