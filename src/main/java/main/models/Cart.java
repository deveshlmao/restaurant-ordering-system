package main.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static final List<MenuItem> cartItems = new ArrayList<>();

    public static void addItem(MenuItem item) {
        cartItems.add(item);
    }

    public static void removeItem(MenuItem item) {
        cartItems.remove(item);
    }

    public static List<MenuItem> getCartItems() {
        return cartItems;
    }
}
