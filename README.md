# Food Delivery Management System

A modular, console-based Food Delivery Management System implemented in Java following Clean Architecture and Domain-Driven design patterns (layered into Models, Repositories, Services, and CLI presentation modules).

---

## 📌 Project Overview

This application simulates a complete food ordering and delivery workflow, connecting Customers, Restaurant Owners, Delivery Partners, and Admins. It includes end-to-end features such as user management, restaurant and catalog listing, shopping cart operations, checkout and order lifecycle management, coupon discounts, payment strategy execution, delivery partner onboarding, and customer ratings/reviews.

---

## 🏗️ Architecture & Package Structure

```text
src/
└── com/
    └── fooddelivery/
        ├── Main.java                          # Application entry point & service wiring
        ├── cli/                               # Interactive CLI console presentation layer
        │   ├── CartCli.java
        │   ├── CouponCli.java
        │   ├── DeliveryPartnerCLI.java
        │   ├── MenuItemCli.java
        │   ├── OrderCli.java
        │   ├── OrderItemCli.java
        │   ├── PaymentCli.java
        │   ├── RatingCLI.java
        │   ├── RestaurantCli.java
        │   └── UserCli.java
        ├── models/                            # Domain entities & business invariants
        │   ├── Cart.java
        │   ├── Coupon.java
        │   ├── DeliveryPartner.java
        │   ├── MenuItem.java
        │   ├── Order.java
        │   ├── OrderItem.java
        │   ├── Payment.java
        │   ├── Rating.java
        │   ├── Restaurant.java
        │   └── User.java
        ├── payment/                           # Strategy pattern implementations for payments
        │   ├── PaymentMethod.java             # Payment interface
        │   ├── CardPayment.java               # Credit / Debit card processing
        │   ├── CashPayment.java               # Cash on delivery processing
        │   └── UpiPayment.java                # UPI digital payments
        ├── repository/                        # In-memory storage layer
        │   ├── CartRepository.java
        │   ├── CouponRepository.java
        │   ├── DeliveryPartnerRepository.java
        │   ├── MenuItemRepository.java
        │   ├── OrderItemRepository.java
        │   ├── OrderRepository.java
        │   ├── PaymentRepository.java
        │   ├── RatingRepository.java
        │   ├── RestaurantRepository.java
        │   └── UserRepository.java
        └── services/                          # Business logic & authorization services
            ├── CartService.java
            ├── CouponService.java
            ├── DeliveryPartnerService.java
            ├── MenuItemService.java
            ├── OrderItemService.java
            ├── OrderService.java
            ├── PaymentService.java
            ├── RatingService.java
            ├── RestaurantService.java
            └── UserService.java
```

---

## 👥 Module Ownership & Roles

| Contributor | Assigned Modules |
| :--- | :--- |
| **Prakash** | User Management, Order Lifecycle & Core Integration |
| **Akash** | Coupons, Discount Calculations & Payment Strategies |
| **Dharanidaran** | Cart Operations & OrderItems |
| **Priyanka** | Restaurants, Operating Windows & Menu Items |
| **Joel** | Delivery Partners & Rating/Review System |

---

## 🚀 Key Features

### 1. User Management
- Register users with roles: `CUSTOMER`, `RESTAURANT_OWNER`, `DELIVERY_PARTNER`, `ADMIN`.
- User authentication and profile updates.

### 2. Restaurant & Menu Management
- Create, activate, and manage restaurants with operating hours (`openingTime`, `closingTime`) and minimum order amounts.
- Role-based authorization (`ADMIN` and owner permissions).
- Categorized menu items (`Veg`, `Non-Veg`) with price updates and availability toggles.

### 3. Cart & Order Item Operations
- Multi-item carts tied to specific restaurants.
- Quantity boundary enforcement (1 to 10 per item).
- Automatic clearing or pruning of unavailable menu items prior to checkout.
- Itemized breakdown of orders with prices and quantities.

### 4. Order Lifecycle Management
- Calculate totals including delivery fees, taxes (GST), and coupon discounts.
- State machine order transitions: `CREATED` ➔ `CONFIRMED` ➔ `OUT_FOR_DELIVERY` ➔ `DELIVERED` (or `CANCELLED`).
- Cancellation rules guarding against cancelling delivered orders.

### 5. Discounts & Coupons
- Percentage-based and flat amount coupon validation.
- Expiration date checking and minimum order threshold validation.

### 6. Payment Processing (Strategy Pattern)
- Pluggable payment strategies via `PaymentMethod` interface:
  - **Card Payment** (`CardPayment`)
  - **UPI Payment** (`UpiPayment`)
  - **Cash on Delivery** (`CashPayment`)
- Support for payment processing and refund tracking.

### 7. Delivery Partners
- Onboard partners with vehicle details and phone numbers.
- Availability tracking (`AVAILABLE`, `ON_DELIVERY`, `OFFLINE`).

### 8. Rating & Review System
- Star ratings (1 to 5) with optional textual feedback.
- Ratings tracked per user, restaurant, and delivery partner.
- Aggregated average score calculation for partners and restaurants.

---

## 🛠️ How to Build and Run

### Prerequisites
- **Java Development Kit (JDK) 11 or higher** installed and configured on your `PATH`.

### Compile
Open PowerShell or your terminal in the root directory:

```powershell
# Compile all Java source files into the bin directory
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

*On Linux/macOS or bash:*
```bash
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
```

### Run
Launch the application through `Main`:

```powershell
java -cp bin com.fooddelivery.Main
```

---

## 📋 Interactive CLI Navigation

When you run `Main`, you are presented with the central management console:

```text
==========================================
      FOOD DELIVERY MANAGEMENT SYSTEM     
==========================================
1.  User Management
2.  Restaurant Management
3.  Menu Item Management
4.  Cart Operations
5.  Order Management
6.  Order Items Inspection
7.  Coupon Management
8.  Payment Processing
9.  Delivery Partner Management
10. Rating & Reviews
0.  Exit
```
Select an option to enter any sub-menu and navigate back anytime using option `0` or `Back`.
