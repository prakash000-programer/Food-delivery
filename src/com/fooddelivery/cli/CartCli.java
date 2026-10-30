package com.fooddelivery.cli;

import com.fooddelivery.models.Cart;
import com.fooddelivery.models.MenuItem;
import com.fooddelivery.services.CartService;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class CartCli {
    private final CartService cartService;
    private final Scanner scanner;
    private final PrintStream out;

    public CartCli(CartService cartService, Scanner scanner) {
        this(cartService, scanner, System.out);
    }
    public CartCli(CartService cartService, Scanner scanner, PrintStream out) {
        if (cartService == null || scanner == null || out == null) {
            throw new IllegalArgumentException("Service, scanner and output are required.");
        }
        this.cartService = cartService;
        this.scanner = scanner;
        this.out = out;
    }

    // Pass the selected restaurant ID and only that restaurant's menu.
    public void showMenu(String customerId, String restaurantId, List<MenuItem> menuItems) {
        if (menuItems == null) { throw new IllegalArgumentException("Menu items are required."); }
        while (true) {
            out.println("\n1. View cart\n2. Add item\n3. Remove item\n4. View total\n5. Clear cart\n6. Back");
            out.print("Choose: ");
            if (!scanner.hasNextLine()) { return; }
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": displayCart(customerId); break;
                    case "2":
                        out.print("Menu item ID: ");
                        if (!scanner.hasNextLine()) { return; }
                        String itemId = scanner.nextLine().trim();
                        MenuItem selected = findItem(menuItems, itemId);
                        out.print("Quantity: ");
                        if (!scanner.hasNextLine()) { return; }
                        int quantity = Integer.parseInt(scanner.nextLine().trim());
                        cartService.addItem(customerId, restaurantId, selected, quantity);
                        out.println("Item added.");
                        break;
                    case "3":
                        out.print("Menu item ID: ");
                        if (!scanner.hasNextLine()) { return; }
                        cartService.removeItem(customerId, scanner.nextLine().trim());
                        out.println("Item removed if it was in the cart.");
                        break;
                    case "4": out.printf("Items total: Rs.%.2f%n", cartService.calculateTotal(customerId)); break;
                    case "5": cartService.clearCart(customerId); out.println("Cart cleared."); break;
                    case "6": return;
                    default: out.println("Choose an option from 1 to 6.");
                }
            } catch (NumberFormatException e) {
                out.println("Enter a whole number for quantity.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                out.println(e.getMessage());
            }
        }
    }

    public void displayCart(String customerId) {
        Cart cart = cartService.getCart(customerId);
        if (cart.isEmpty()) { out.println("Cart is empty."); return; }
        out.println("Cart: " + cart.getCartId() + " | Restaurant: " + cart.getRestaurantId());
        for (MenuItem item : cart.getItems()) {
            int quantity = cart.getQuantity(item.getItemId());
            out.printf("%s | %s x %d @ Rs.%.2f = Rs.%.2f%n",
                    item.getItemId(), item.getName(), quantity, item.getPrice(), item.getPrice() * quantity);
        }
        out.printf("Items total: Rs.%.2f%n", cart.calculateTotal());
    }

    private MenuItem findItem(List<MenuItem> menuItems, String itemId) {
        for (MenuItem item : menuItems) {
            if (item != null && itemId.equals(item.getItemId())) { return item; }
        }
        throw new IllegalArgumentException("Menu item not found.");
    }
}
