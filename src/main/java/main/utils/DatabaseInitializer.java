package main.utils;

import java.sql.*;

public class DatabaseInitializer {

    private static final String DATABASE_URL = "jdbc:sqlite:restaurant.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "full_name TEXT NOT NULL, "
                    + "username TEXT NOT NULL UNIQUE, "
                    + "email TEXT NOT NULL UNIQUE, "
                    + "password TEXT NOT NULL,"
                    + "is_admin INTEGER DEFAULT 0"
                    + ");";
            stmt.execute(createUsersTable);

            String createMenuTable = "CREATE TABLE IF NOT EXISTS menu_items ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "price REAL NOT NULL, "
                    + "image_path TEXT NOT NULL, "
                    + "category TEXT NOT NULL, "
                    + "availability INTEGER DEFAULT 1 "
                    + ");";
            stmt.execute(createMenuTable);

            String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "user_id INTEGER NOT NULL, "
                    + "items TEXT NOT NULL, "
                    + "total_price REAL NOT NULL, "
                    + "order_status DEFAULT 'Pending', "
                    + "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY(user_id) REFERENCES users(id)"
                    + ");";
            stmt.execute(createOrdersTable);

            if (!tableHasData(conn, "users")) {
                String insertUser = "INSERT INTO users (full_name, username, email, password, is_admin) VALUES "
                        + "('Admin User', 'admin', 'admin@example.com', 'admin', TRUE);";
                stmt.execute(insertUser);
            }

            if (!tableHasData(conn, "menu_items")) {
                String insertMenuItems = "INSERT INTO menu_items (name, price, image_path, category) VALUES "
                        + "('Burger', 5.99, 'images/burger.png', 'Meals'), "
                        + "('Pizza', 8.99, 'images/pizza.png', 'Meals'), "
                        + "('Pasta', 7.49, 'images/pasta.png', 'Meals'), "
                        + "('Coke', 1.99, 'images/coke.png', 'Drinks'), "
                        + "('Orange Juice', 2.49, 'images/orange_juice.png', 'Drinks'), "
                        + "('Ice Cream', 3.99, 'images/ice_cream.png', 'Desserts');";
                stmt.execute(insertMenuItems);
            }

            System.out.println(" Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println(" Error initializing database: " + e.getMessage());
        }
    }

    private static boolean tableHasData(Connection conn, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.getInt(1) > 0;
        }
    }
}
