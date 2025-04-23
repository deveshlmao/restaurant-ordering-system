package main.utils;

import main.models.OrderStatus;
import main.models.User;
import main.models.MenuItem;
import main.models.Order;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DATABASE_URL = "jdbc:sqlite:restaurant.db";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    private static LocalDateTime parseTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static boolean userExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error checking user existence: " + e.getMessage());
        }
        return false;
    }


    public static boolean registerUser(String fullName, String username, String email, String password) {
        String query = "INSERT INTO users (full_name, username, email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, username);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error registering user: " + e.getMessage());
        }
        return false;
    }


    public static User authenticateUser(String username, String password) {
        String query = "SELECT id, full_name, username, email, is_admin FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getBoolean("is_admin")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error authenticating user: " + e.getMessage());
        }
        return null;
    }


    public static boolean saveOrder(int userId, String items, double totalPrice) {
        String query = "INSERT INTO orders (user_id, items, total_price, order_status) VALUES (?, ?, ?, 'Pending')";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, items);
            pstmt.setDouble(3, totalPrice);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error saving order: " + e.getMessage());
        }
        return false;
    }

    public static List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT id, name, price, image_path, category, availability FROM menu_items";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                menuItems.add(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("image_Path"),
                        rs.getString("category"),
                        rs.getString("availability")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching menu items: " + e.getMessage());
        }
        return menuItems;
    }

    public static List<MenuItem> getMenuItemsByCategory(String category) {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT id, name, price, image_path, category, availability FROM menu_items WHERE category = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String imagePath = rs.getString("image_path");
                boolean availability = rs.getBoolean("availability");


                menuItems.add(new MenuItem(id, name, price, imagePath, category, availability));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching menu items by category: " + e.getMessage());
        }
        return menuItems;
    }




    public static MenuItem getMenuItemById(int itemId) {
        String query = "SELECT id, name, price, image_path, category FROM menu_items WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("image_path"),
                        rs.getString("category"),
                        rs.getBoolean("availability")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching menu item: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateMenuItem(MenuItem menuItem) {
        String query = "UPDATE menu_items SET name = ?, price = ?, category = ?, availability = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, menuItem.getName());
            pstmt.setDouble(2, menuItem.getPrice());
            pstmt.setString(3, menuItem.getCategory());
            pstmt.setBoolean(4, menuItem.isAvailable());
            pstmt.setInt(5, menuItem.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating menu item: " + e.getMessage());
        }
        return false;
    }

    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("items"),
                        rs.getDouble("total_price"),
                        OrderStatus.fromString(rs.getString("order_status")),
                        parseTimestamp(rs.getString("order_date"))
                );
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }



    public static boolean updateOrderStatus(int orderId, String newStatus) {
        String query = "UPDATE orders SET order_status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating order status: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteOrder(int orderId) {
        String query = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error deleting order: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteMenuItem(int itemId) {
        String query = "DELETE FROM menu_items WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, itemId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error deleting menu item: " + e.getMessage());
        }
        return false;
    }

    public static boolean addMenuItem(MenuItem item) {
        String query = "INSERT INTO menu_items (name, price, category, availability, image_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, item.getName());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setString(3, item.getCategory());
            pstmt.setBoolean(4, item.isAvailable());
            pstmt.setString(5, item.getImagePath());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error adding menu item: " + e.getMessage());
        }
        return false;
    }


    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, full_name, username, email, is_admin FROM users ORDER BY id ASC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getBoolean("is_admin")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching users: " + e.getMessage());
        }
        return users;
    }

    public static boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error deleting user: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateUsername(int userId, String newUsername) {
        String query = "UPDATE users SET username = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newUsername);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating username: " + e.getMessage());
        }
        return false;
    }



}
