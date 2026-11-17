/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeteriaapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton that holds the customer's cart items in memory.
 * Works the same way as Session.java — one shared instance across all controllers.
 *
 * @author DELL
 */
public class Cart {

    private static Cart instance;

    private final List<CartItem> items = new ArrayList<>();

    private Cart() {}

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addItem(int menuItemId, String name, double price) {
        for (CartItem item : items) {
            if (item.getMenuItemId() == menuItemId) {
                item.incrementQuantity();
                return;
            }
        }
        items.add(new CartItem(menuItemId, name, price, 1));
    }

    public void removeItem(int menuItemId) {
        items.removeIf(item -> item.getMenuItemId() == menuItemId);
    }

    public void incrementQuantity(int menuItemId) {
        for (CartItem item : items) {
            if (item.getMenuItemId() == menuItemId) {
                item.incrementQuantity();
                return;
            }
        }
    }

    public void decrementQuantity(int menuItemId) {
        for (CartItem item : items) {
            if (item.getMenuItemId() == menuItemId) {
                item.decrementQuantity();
                if (item.getQuantity() <= 0) {
                    removeItem(menuItemId);
                }
                return;
            }
        }
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public static class CartItem {

        private final int menuItemId;
        private final String name;
        private final double price;
        private int quantity;

        public CartItem(int menuItemId, String name, double price, int quantity) {
            this.menuItemId = menuItemId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public int getMenuItemId() {
            return menuItemId;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getSubtotal() {
            return price * quantity;
        }

        public void incrementQuantity() {
            quantity++;
        }

        public void decrementQuantity() {
            quantity--;
        }
    }
}