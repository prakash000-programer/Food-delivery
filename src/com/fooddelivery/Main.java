package com.fooddelivery;

import com.fooddelivery.cli.CartCli;
import com.fooddelivery.cli.CouponCli;
import com.fooddelivery.cli.DeliveryPartnerCLI;
import com.fooddelivery.cli.MenuItemCli;
import com.fooddelivery.cli.OrderCli;
import com.fooddelivery.cli.OrderItemCli;
import com.fooddelivery.cli.PaymentCli;
import com.fooddelivery.cli.RatingCLI;
import com.fooddelivery.cli.RestaurantCli;
import com.fooddelivery.cli.UserCli;
import com.fooddelivery.models.MenuItem;
import com.fooddelivery.repository.CartRepository;
import com.fooddelivery.repository.CouponRepository;
import com.fooddelivery.repository.DeliveryPartnerRepository;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.OrderItemRepository;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.PaymentRepository;
import com.fooddelivery.repository.RatingRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.services.CartService;
import com.fooddelivery.services.CouponService;
import com.fooddelivery.services.DeliveryPartnerService;
import com.fooddelivery.services.MenuItemService;
import com.fooddelivery.services.OrderItemService;
import com.fooddelivery.services.OrderService;
import com.fooddelivery.services.PaymentService;
import com.fooddelivery.services.RatingService;
import com.fooddelivery.services.RestaurantService;
import com.fooddelivery.services.UserService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 1. Initialize Repositories
        UserRepository userRepository = new UserRepository();
        RestaurantRepository restaurantRepository = new RestaurantRepository();
        MenuItemRepository menuItemRepository = new MenuItemRepository();
        CartRepository cartRepository = new CartRepository();
        OrderItemRepository orderItemRepository = new OrderItemRepository();
        OrderRepository orderRepository = new OrderRepository();
        CouponRepository couponRepository = new CouponRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        DeliveryPartnerRepository partnerRepository = new DeliveryPartnerRepository();
        RatingRepository ratingRepository = new RatingRepository();

        // 2. Initialize Services
        UserService userService = new UserService(userRepository);
        OrderService orderService = new OrderService(orderRepository);
        RestaurantService restaurantService = new RestaurantService(
                restaurantRepository,
                menuItemRepository,
                restaurantId -> orderRepository.findAll().stream()
                        .anyMatch(o -> restaurantId.equals(o.getRestaurantId())
                                && !"DELIVERED".equalsIgnoreCase(o.getStatus())
                                && !"CANCELLED".equalsIgnoreCase(o.getStatus()))
        );
        MenuItemService menuItemService = new MenuItemService(menuItemRepository, restaurantRepository);
        CartService cartService = new CartService(cartRepository);
        OrderItemService orderItemService = new OrderItemService(orderItemRepository);
        CouponService couponService = new CouponService(couponRepository);
        PaymentService paymentService = new PaymentService(paymentRepository);
        DeliveryPartnerService partnerService = new DeliveryPartnerService(partnerRepository);
        RatingService ratingService = new RatingService(ratingRepository);

        // 3. Initialize CLIs
        Scanner scanner = new Scanner(System.in);
        UserCli userCli = new UserCli(userService, scanner);
        RestaurantCli restaurantCli = new RestaurantCli(restaurantService, scanner);
        MenuItemCli menuItemCli = new MenuItemCli(menuItemService, scanner);
        CartCli cartCli = new CartCli(cartService, scanner);
        OrderItemCli orderItemCli = new OrderItemCli(orderItemService);
        OrderCli orderCli = new OrderCli(orderService, scanner);
        CouponCli couponCli = new CouponCli(couponService, scanner);
        PaymentCli paymentCli = new PaymentCli(paymentService, scanner);
        DeliveryPartnerCLI deliveryPartnerCli = new DeliveryPartnerCLI(partnerService, scanner);
        RatingCLI ratingCli = new RatingCLI(ratingService, scanner);

        // 4. Main Menu Loop
        boolean running = true;
        while (running) {
            System.out.println("\n==========================================");
            System.out.println("      FOOD DELIVERY MANAGEMENT SYSTEM     ");
            System.out.println("==========================================");
            System.out.println("1.  User Management");
            System.out.println("2.  Restaurant Management");
            System.out.println("3.  Menu Item Management");
            System.out.println("4.  Cart Operations");
            System.out.println("5.  Order Management");
            System.out.println("6.  Order Items Inspection");
            System.out.println("7.  Coupon Management");
            System.out.println("8.  Payment Processing");
            System.out.println("9.  Delivery Partner Management");
            System.out.println("10. Rating & Reviews");
            System.out.println("0.  Exit");
            System.out.print("Select an option: ");

            if (!scanner.hasNextLine()) {
                break;
            }
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    userCli.showMenu();
                    break;
                case "2": {
                    System.out.print("Enter your User ID: ");
                    String actorId = scanner.nextLine().trim();
                    System.out.print("Enter your Role (ADMIN / RESTAURANT_OWNER / CUSTOMER): ");
                    String role = scanner.nextLine().trim().toUpperCase();
                    restaurantCli.showMenu(actorId, role);
                    break;
                }
                case "3": {
                    System.out.print("Enter your User ID: ");
                    String actorId = scanner.nextLine().trim();
                    System.out.print("Enter your Role (ADMIN / RESTAURANT_OWNER): ");
                    String role = scanner.nextLine().trim().toUpperCase();
                    System.out.print("Enter Restaurant ID: ");
                    String restaurantId = scanner.nextLine().trim();
                    menuItemCli.showMenu(actorId, role, restaurantId);
                    break;
                }
                case "4": {
                    System.out.print("Enter Customer ID: ");
                    String customerId = scanner.nextLine().trim();
                    System.out.print("Enter Restaurant ID: ");
                    String restaurantId = scanner.nextLine().trim();
                    try {
                        List<MenuItem> menuItems = menuItemService.getMenuItems(restaurantId);
                        cartCli.showMenu(customerId, restaurantId, menuItems);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                }
                case "5":
                    orderCli.showMenu();
                    break;
                case "6": {
                    System.out.print("Enter Order ID to inspect items: ");
                    String orderId = scanner.nextLine().trim();
                    orderItemCli.displayOrderItems(orderId);
                    break;
                }
                case "7":
                    couponCli.start();
                    break;
                case "8":
                    paymentCli.start();
                    break;
                case "9":
                    deliveryPartnerCli.start();
                    break;
                case "10":
                    ratingCli.start();
                    break;
                case "0":
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose between 0 and 10.");
            }
        }
        scanner.close();
    }
}
