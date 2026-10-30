
package fooddelivery.cli;

import fooddelivery.models.Restaurant;
import java.time.LocalTime;
import java.util.Scanner;

public class RestaurantCli {

    // Display Restaurant Details
    public void displayRestaurant(Restaurant restaurant) {

        System.out.println("\n========== RESTAURANT DETAILS ==========");

        System.out.println("Restaurant ID    : " + restaurant.getRestaurantId());
        System.out.println("Name             : " + restaurant.getName());
        System.out.println("Cuisine Type     : " + restaurant.getCuisineType());
        System.out.println("City             : " + restaurant.getCity());
        System.out.println("Opening Time     : " + restaurant.getOpeningTime());
        System.out.println("Closing Time     : " + restaurant.getClosingTime());
        System.out.println("Minimum Order    : Rs." + restaurant.getMinimumOrderValue());
        System.out.println("Status           : " +
                (restaurant.isOpen() ? "Open" : "Closed"));

        System.out.println("========================================");
    }

    // Update Restaurant Details
    public void updateRestaurantDetails(
            Restaurant restaurant, Scanner scanner) {

        try {
            System.out.println("\n========== UPDATE RESTAURANT ==========");

            System.out.print("Enter Restaurant Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Cuisine Type: ");
            String cuisineType = scanner.nextLine();

            System.out.print("Enter City: ");
            String city = scanner.nextLine();

            System.out.print("Enter Opening Time (HH:mm): ");
            LocalTime openingTime =
                    LocalTime.parse(scanner.nextLine());

            System.out.print("Enter Closing Time (HH:mm): ");
            LocalTime closingTime =
                    LocalTime.parse(scanner.nextLine());

            System.out.print("Enter Minimum Order Value: ");
            double minimumOrderValue =
                    Double.parseDouble(scanner.nextLine());

            restaurant.updateDetails(
                    name,
                    cuisineType,
                    city,
                    openingTime,
                    closingTime,
                    minimumOrderValue
            );

            System.out.println("\nRestaurant details updated successfully!");

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    // Change Restaurant Availability
    public void changeRestaurantAvailability(
            Restaurant restaurant, Scanner scanner) {

        System.out.println("\n========== RESTAURANT AVAILABILITY ==========");

        System.out.println("1. Open Restaurant");
        System.out.println("2. Close Restaurant");

        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {

            restaurant.changeAvailability(true);
            System.out.println("Restaurant is now OPEN.");

        } else if (choice.equals("2")) {

            restaurant.changeAvailability(false);
            System.out.println("Restaurant is now CLOSED.");

        } else {

            System.out.println("Invalid choice!");
        }
    }
}
