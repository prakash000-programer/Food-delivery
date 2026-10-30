
package com.fooddelivery.cli;

import com.fooddelivery.models.MenuItem;
import java.util.Scanner;

public class MenuItemCli {

    // Display Menu Item Details
    public void displayMenuItem(MenuItem menuItem) {

        System.out.println("\n========== MENU ITEM DETAILS ==========");

        System.out.println("Item ID       : " + menuItem.getItemId());
        System.out.println("Item Name     : " + menuItem.getName());
        System.out.println("Price         : Rs." + menuItem.getPrice());
        System.out.println("Category      : " + menuItem.getCategory());
        System.out.println("Availability  : " +
                (menuItem.isAvailable() ? "Available" : "Not Available"));

        System.out.println("=======================================");
    }

    // Update Menu Item Price
    public void updateMenuItemPrice(
            MenuItem menuItem, Scanner scanner) {

        try {
            System.out.println("\n========== UPDATE MENU ITEM PRICE ==========");

            System.out.println("Current Price: Rs." + menuItem.getPrice());

            System.out.print("Enter New Price: ");
            double newPrice =
                    Double.parseDouble(scanner.nextLine());

            menuItem.updatePrice(newPrice);

            System.out.println("Menu item price updated successfully!");

        } catch (IllegalArgumentException e) {

            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    // Change Menu Item Availability
    public void changeMenuItemAvailability(
            MenuItem menuItem, Scanner scanner) {

        System.out.println("\n========== MENU ITEM AVAILABILITY ==========");

        System.out.println("1. Available");
        System.out.println("2. Not Available");

        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {

            menuItem.changeAvailability(true);
            System.out.println("Menu item is now AVAILABLE.");

        } else if (choice.equals("2")) {

            menuItem.changeAvailability(false);
            System.out.println("Menu item is now NOT AVAILABLE.");

        } else {

            System.out.println("Invalid choice!");
        }
    }
}
