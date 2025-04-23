package main.controllers;

import main.models.MenuItem;
import main.utils.DatabaseHelper;
import main.utils.SessionManager;
import java.util.Map;

public class OrderController {

    public static boolean placeOrder(Map<MenuItem, Integer> cartItems) {
        int userId = SessionManager.getLoggedInUserId();
        if (userId == -1) {
            System.err.println("Error: No logged-in user found.");
            return false;
        }

        if (cartItems.isEmpty()) {
            System.err.println("Error: Cart is empty, cannot place order.");
            return false;
        }

        String itemsList = cartItemsToString(cartItems);
        double totalPrice = calculateTotal(cartItems);

        System.out.println("Placing order for User ID: " + userId);
        System.out.println("Items: " + itemsList);
        System.out.println("Total Price: " + totalPrice);

        return DatabaseHelper.saveOrder(userId, itemsList, totalPrice);
    }

    private static String cartItemsToString(Map<MenuItem, Integer> cartItems) {
        StringBuilder items = new StringBuilder();
        for (Map.Entry<MenuItem, Integer> entry : cartItems.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            items.append(item.getName()).append(" x").append(quantity).append(", ");
        }
        return items.length() > 0 ? items.substring(0, items.length() - 2) : "";
    }

    private static double calculateTotal(Map<MenuItem, Integer> cartItems) {
        double total = 0;
        for (Map.Entry<MenuItem, Integer> entry : cartItems.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            total += item.getPrice() * quantity;
        }
        return total;
    }
}