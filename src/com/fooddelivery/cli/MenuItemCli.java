package com.fooddelivery.cli;

import com.fooddelivery.models.MenuItem;
import com.fooddelivery.services.MenuItemService;
import java.io.PrintStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MenuItemCli {
    private final MenuItemService menuItemService;
    private final Scanner scanner;
    private final PrintStream out;

    public MenuItemCli(MenuItemService service, Scanner scanner) { this(service, scanner, System.out); }
    public MenuItemCli(MenuItemService service, Scanner scanner, PrintStream out) {
        if (service == null || scanner == null || out == null) {
            throw new IllegalArgumentException("Service, scanner and output are required.");
        }
        this.menuItemService = service;
        this.scanner = scanner;
        this.out = out;
    }

    // The user module supplies the logged-in user's ID and role.
    public void showMenu(String actorId, String role, String restaurantId) {
        while (true) {
            out.println("\n1. View menu\n2. Add menu item\n3. Update price\n4. Change availability\n5. Remove item\n6. Back");
            try {
                String choice = read("Choose: ");
                switch (choice) {
                    case "1": displayMenu(restaurantId); break;
                    case "2":
                        String id = read("Item ID: ");
                        String name = read("Name: ");
                        double price = Double.parseDouble(read("Price: "));
                        String category = read("Category (Veg / Non-Veg): ");
                        boolean available = readBoolean("Available (true / false): ");
                        menuItemService.addMenuItem(actorId, role,
                                new MenuItem(id, restaurantId, name, price, category, available));
                        out.println("Menu item added.");
                        break;
                    case "3":
                        String priceItemId = selectedItem(restaurantId).getItemId();
                        double newPrice = Double.parseDouble(read("New price: "));
                        menuItemService.updatePrice(actorId, role, priceItemId, newPrice);
                        out.println("Price updated.");
                        break;
                    case "4":
                        String availabilityId = selectedItem(restaurantId).getItemId();
                        boolean status = readBoolean("Available (true / false): ");
                        menuItemService.changeAvailability(actorId, role, availabilityId, status);
                        out.println("Availability updated.");
                        break;
                    case "5":
                        String removeId = selectedItem(restaurantId).getItemId();
                        menuItemService.removeMenuItem(actorId, role, removeId);
                        out.println("Menu item removed.");
                        break;
                    case "6": return;
                    default: out.println("Choose an option from 1 to 6.");
                }
            } catch (NoSuchElementException e) { return;
            } catch (NumberFormatException e) { out.println("Enter a valid number for price.");
            } catch (IllegalArgumentException | IllegalStateException e) { out.println(e.getMessage()); }
        }
    }
    public void displayMenu(String restaurantId) {
        List<MenuItem> items = menuItemService.getMenuItems(restaurantId);
        if (items.isEmpty()) { out.println("No menu items found."); return; }
        for (MenuItem item : items) { out.println(item); }
    }
    private MenuItem selectedItem(String restaurantId) {
        MenuItem item = menuItemService.getMenuItem(read("Item ID: "));
        if (!item.getRestaurantId().equals(restaurantId)) {
            throw new IllegalArgumentException("Item does not belong to the selected restaurant.");
        }
        return item;
    }
    private String read(String prompt) { out.print(prompt); return scanner.nextLine().trim(); }
    private boolean readBoolean(String prompt) {
        String value = read(prompt);
        if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
            throw new IllegalArgumentException("Enter true or false.");
        }
        return Boolean.parseBoolean(value);
    }
}
