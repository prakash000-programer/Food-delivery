package com.fooddelivery.cli;

import com.fooddelivery.models.OrderItem;
import com.fooddelivery.services.OrderItemService;
import java.io.PrintStream;
import java.util.List;

public class OrderItemCli {
    private final OrderItemService orderItemService;
    private final PrintStream out;

    public OrderItemCli(OrderItemService orderItemService) {
        this(orderItemService, System.out);
    }
    public OrderItemCli(OrderItemService orderItemService, PrintStream out) {
        if (orderItemService == null || out == null) {
            throw new IllegalArgumentException("Service and output are required.");
        }
        this.orderItemService = orderItemService;
        this.out = out;
    }

    // The order CLI passes its selected order ID here.
    public void displayOrderItems(String orderId) {
        List<OrderItem> items = orderItemService.getOrderItems(orderId);
        if (items.isEmpty()) { out.println("No items found for this order."); return; }
        out.println("Items for order " + orderId + ":");
        for (OrderItem item : items) { out.println(item); }
        out.printf("Items total: Rs.%.2f%n", orderItemService.calculateTotal(orderId));
    }
}
