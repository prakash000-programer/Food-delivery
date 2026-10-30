package com.fooddelivery.cli;

import com.fooddelivery.services.CouponService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CouponCli {
    private final CouponService service;
    private final Scanner sc;

    public CouponCli(CouponService service, Scanner sc) {
        this.service = service;
        this.sc = sc;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Coupon Menu ---");
            System.out.println("1. Add coupon");
            System.out.println("2. View coupon");
            System.out.println("3. Check discount for an order amount");
            System.out.println("4. View all coupons");
            System.out.println("5. Remove coupon");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        System.out.print("Code: ");
                        String code = sc.nextLine().trim();
                        System.out.print("Type (PERCENT/FLAT): ");
                        String type = sc.nextLine().trim();
                        System.out.print("Discount value: ");
                        double value = Double.parseDouble(sc.nextLine().trim());
                        System.out.print("Minimum order value: ");
                        double min = Double.parseDouble(sc.nextLine().trim());
                        System.out.print("Expiry date (yyyy-MM-dd): ");
                        LocalDate expiry = LocalDate.parse(sc.nextLine().trim());
                        System.out.println("Added: " + service.addCoupon(code, type, value, min, expiry));
                        break;
                    case "2":
                        System.out.print("Code: ");
                        System.out.println(service.getCoupon(sc.nextLine().trim()));
                        break;
                    case "3":
                        System.out.print("Code: ");
                        String c = sc.nextLine().trim();
                        System.out.print("Order amount: ");
                        double amt = Double.parseDouble(sc.nextLine().trim());
                        double d = service.applyCoupon(c, amt);
                        System.out.println(d > 0 ? "Discount: Rs." + d + " | Payable: Rs." + (amt - d)
                                                 : "Coupon not applicable (expired or below minimum order).");
                        break;
                    case "4":
                        if (service.getAllCoupons().isEmpty()) System.out.println("No coupons.");
                        service.getAllCoupons().forEach(System.out::println);
                        break;
                    case "5":
                        System.out.print("Code: ");
                        System.out.println(service.removeCoupon(sc.nextLine().trim()) ? "Removed." : "Coupon not found.");
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: enter a valid number.");
            } catch (DateTimeParseException e) {
                System.out.println("Error: use date format yyyy-MM-dd.");
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}