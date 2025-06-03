package dao;

import model.CartItems;
import model.Event;
import model.Order;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDao {
    private static final String ORDER_TABLE = "orders";
    private static final String ORDER_ITEMS_TABLE = "order_items";

    public void createTables() {
        try (Connection con = Database.getConnection(); Statement stmt = con.createStatement()) {
            // Create orders table
            String createOrders = "CREATE TABLE IF NOT EXISTS " + ORDER_TABLE + " ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "order_number TEXT NOT NULL,"
                    + "user_name TEXT NOT NULL,"
                    + "order_date TEXT NOT NULL,"
                    + "total_price REAL NOT NULL"
                    + ");";
            stmt.executeUpdate(createOrders);

            // Create order_items table
            String createOrderItems = "CREATE TABLE IF NOT EXISTS " + ORDER_ITEMS_TABLE + " ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "order_id INTEGER NOT NULL,"
                    + "event_id INTEGER NOT NULL,"
                    + "quantity INTEGER NOT NULL,"
                    + "FOREIGN KEY(order_id) REFERENCES " + ORDER_TABLE + "(id)"
                    + ");";
            stmt.executeUpdate(createOrderItems);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String generateNextOrderNumberForUser(String username) {
        String sql = "SELECT order_number FROM orders WHERE user_name = ? ORDER BY id DESC LIMIT 1";

        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String lastNumber = rs.getString("order_number");
                int last = Integer.parseInt(lastNumber);
                int next = last + 1;
                return String.format("%04d", next);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "0001"; // First order for this user
    }

    public int saveOrder(String orderNumber, String username, LocalDateTime orderDate, double totalPrice) {
        String insertOrder = "INSERT INTO " + ORDER_TABLE + " (order_number, user_name, order_date, total_price) VALUES (?, ?, ?, ?)";
        String insertOrderItem = "INSERT INTO " + ORDER_ITEMS_TABLE + " (order_id, event_id, quantity) VALUES (?, ?, ?)";

        try (Connection con = Database.getConnection()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // More readable
            String formattedDate = orderDate.format(formatter);

            // Insert order
            PreparedStatement orderStmt = con.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setString(1, orderNumber);
            orderStmt.setString(2, username);
            orderStmt.setString(3, formattedDate);
            orderStmt.setDouble(4, totalPrice);
            orderStmt.executeUpdate();

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);
                return orderId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // failed
    }
    public void saveOrderItems(Order order) {
        String insertOrderItem = "INSERT INTO " + ORDER_ITEMS_TABLE +
                " (order_id, event_id, quantity) VALUES (?, ?, ?)";

        try (Connection con = Database.getConnection()) {
            for (CartItems item : order.getItems()) {
                try (PreparedStatement stmt = con.prepareStatement(insertOrderItem)) {
                    stmt.setInt(1, order.getOrderId());          // use the generated order ID
                    stmt.setInt(2, item.getEvent().getId());     // event ID
                    stmt.setInt(3, item.getQuantity());          // quantity
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving order items: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        String fetchOrders = "SELECT * FROM " + ORDER_TABLE + " ORDER BY order_date DESC";
        String fetchItems = "SELECT * FROM " + ORDER_ITEMS_TABLE + " WHERE order_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement orderStmt = con.prepareStatement(fetchOrders)) {

            ResultSet orderRs = orderStmt.executeQuery();

            while (orderRs.next()) {
                int orderId = orderRs.getInt("id");
                String orderNumber = orderRs.getString("order_number");
                String username = orderRs.getString("user_name");
                String orderDateStr = orderRs.getString("order_date");
                LocalDateTime orderDate = LocalDateTime.parse(orderDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                double totalPrice = orderRs.getDouble("total_price");

                // Fetch order items
                List<CartItems> items = new ArrayList<>();
                try (PreparedStatement itemStmt = con.prepareStatement(fetchItems)) {
                    itemStmt.setInt(1, orderId);
                    ResultSet itemRs = itemStmt.executeQuery();

                    while (itemRs.next()) {
                        int eventId = itemRs.getInt("event_id");
                        int quantity = itemRs.getInt("quantity");

                        Event event = new EventDao().getEventById(eventId); // Load full event info
                        items.add(new CartItems(event, quantity));
                    }
                }

                Order order = new Order(orderId, orderNumber, username, orderDate, items, totalPrice);
                orders.add(order);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all orders: " + e.getMessage());
        }

        return orders;
    }

    public List<Order> getOrdersByUser(String userName) {
        List<Order> orders = new ArrayList<>();
        String fetchOrders = "SELECT * FROM " + ORDER_TABLE + " WHERE user_name = ? ORDER BY order_date DESC";
        String fetchItems = "SELECT * FROM " + ORDER_ITEMS_TABLE + " WHERE order_id = ?";

        try (Connection con = Database.getConnection(); PreparedStatement orderStmt = con.prepareStatement(fetchOrders)) {
            orderStmt.setString(1, userName);
            ResultSet orderRs = orderStmt.executeQuery();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while (orderRs.next()) {
                int orderId = orderRs.getInt("id");
                String orderNumber = orderRs.getString("order_number");
                String dateString = orderRs.getString("order_date");
                LocalDateTime orderDate = LocalDateTime.parse(dateString, formatter);
                double totalPrice = orderRs.getDouble("total_price");

                List<CartItems> items = new ArrayList<>();
                PreparedStatement itemStmt = con.prepareStatement(fetchItems);
                itemStmt.setInt(1, orderId);
                ResultSet itemRs = itemStmt.executeQuery();
                while (itemRs.next()) {
                    int eventId = itemRs.getInt("event_id");
                    int qty = itemRs.getInt("quantity");

                    Event event = new EventDao().getEventById(eventId);
                    items.add(new CartItems(event, qty));
                }

                Order order = new Order(orderId, orderNumber, userName, orderDate, items, totalPrice);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public void exportOrdersToFile(List<Order> orders, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Order order : orders) {
                writer.write("Order Number: " + order.getOrderNumber());
                writer.newLine();
                writer.write("Date & Time: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                writer.newLine();
                writer.write("Items:");
                writer.newLine();
                for (CartItems item : order.getItems()) {
                    writer.write("  - " + item.getEvent().getEventName() + " x" + item.getQuantity());
                    writer.newLine();
                }
                writer.write("Total Price: $" + String.format("%.2f", order.getOrderPrice()));
                writer.newLine();
                writer.write("---------------------------");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
