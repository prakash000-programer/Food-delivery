package com.fooddelivery.cli;

import com.fooddelivery.models.Restaurant;
import com.fooddelivery.services.RestaurantService;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RestaurantCli {
    private final RestaurantService restaurantService;
    private final Scanner scanner;
    private final PrintStream out;

    public RestaurantCli(RestaurantService service, Scanner scanner) { this(service, scanner, System.out); }
    public RestaurantCli(RestaurantService service, Scanner scanner, PrintStream out) {
        if (service == null || scanner == null || out == null) {
            throw new IllegalArgumentException("Service, scanner and output are required.");
        }
        this.restaurantService = service;
        this.scanner = scanner;
        this.out = out;
    }
    public void showMenu(String actorId, String role) {
        while (true) {
            out.println("\n1. List restaurants\n2. Add restaurant\n3. Update draft details\n4. Update timings\n5. Mark open/closed\n6. Activate restaurant\n7. Delete restaurant\n8. View restaurant\n9. Back");
            try {
                String choice = read("Choose: ");
                switch (choice) {
                    case "1": displayRestaurants(); break;
                    case "2":
                        String id = read("Restaurant ID: ");
                        String ownerId = "ADMIN".equals(role) ? read("Owner ID: ") : actorId;
                        String name = read("Name: ");
                        String cuisine = read("Cuisine (North Indian / South Indian / Chinese / Italian / Fast Food): ");
                        String city = read("City: ");
                        LocalTime opening = LocalTime.parse(read("Opening time (HH:mm): "));
                        LocalTime closing = LocalTime.parse(read("Closing time (HH:mm): "));
                        double minimum = Double.parseDouble(read("Minimum order value: "));
                        restaurantService.addRestaurant(actorId, role,
                                new Restaurant(id, ownerId, name, cuisine, city, opening, closing, minimum));
                        out.println("Restaurant added as a closed draft.");
                        break;
                    case "3":
                        String detailsId = read("Restaurant ID: ");
                        String newName = read("Name: ");
                        String newCuisine = read("Cuisine: ");
                        String newCity = read("City: ");
                        restaurantService.updateDetails(actorId, role, detailsId, newName, newCuisine, newCity);
                        out.println("Restaurant details updated.");
                        break;
                    case "4":
                        String timingId = read("Restaurant ID: ");
                        LocalTime newOpening = LocalTime.parse(read("Opening time (HH:mm): "));
                        LocalTime newClosing = LocalTime.parse(read("Closing time (HH:mm): "));
                        restaurantService.updateTimings(actorId, role, timingId, newOpening, newClosing);
                        out.println("Timings updated.");
                        break;
                    case "5":
                        String openId = read("Restaurant ID: ");
                        boolean open = readBoolean("Open (true / false): ");
                        restaurantService.changeAvailability(actorId, role, openId, open);
                        out.println("Restaurant availability updated.");
                        break;
                    case "6":
                        restaurantService.activateRestaurant(actorId, role, read("Restaurant ID: "));
                        out.println("Restaurant activated.");
                        break;
                    case "7":
                        restaurantService.deleteRestaurant(actorId, role, read("Restaurant ID: "));
                        out.println("Restaurant deleted.");
                        break;
                    case "8":
                        Restaurant restaurant = restaurantService.getRestaurant(read("Restaurant ID: "));
                        out.println(restaurant);
                        out.println("Accepting orders now: " + restaurant.canAcceptOrders());
                        break;
                    case "9": return;
                    default: out.println("Choose an option from 1 to 9.");
                }
            } catch (NoSuchElementException e) { return;
            } catch (NumberFormatException e) { out.println("Enter a valid number for minimum order value.");
            } catch (DateTimeParseException e) { out.println("Enter a valid time such as 09:30.");
            } catch (IllegalArgumentException | IllegalStateException e) { out.println(e.getMessage()); }
        }
    }
    public void displayRestaurants() {
        List<Restaurant> restaurants = restaurantService.getRestaurants();
        if (restaurants.isEmpty()) { out.println("No restaurants found."); return; }
        for (Restaurant restaurant : restaurants) { out.println(restaurant); }
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
