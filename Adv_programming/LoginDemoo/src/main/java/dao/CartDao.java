package dao;

import model.CartItems;
import model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
    private final String TABLE_NAME = "cartItems";

    public void createTable() {
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                    + "( id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_name  TEXT NOT NULL,"
                    + "event_id INTEGER NOT NULL,"
                    + "quantity INTEGER NOT NULL,"
                    + "FOREIGN KEY (user_id) REFERENCES users(id),"
                    + "FOREIGN KEY (event_id) REFERENCES events(id),"
                    + "UNIQUE(user_name, event_id)))");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public List<CartItems> loadCartItems(String userName) {
        List<CartItems> cartItems = new ArrayList<>();
        String sql = """
                    SELECT c.quantity, e.*
                    FROM  ?
                    JOIN users u ON c.user_name = u.user_name
                    JOIN events e ON c.event_id = e.id
                    WHERE u.username = ?
                """;
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, TABLE_NAME);
            stmt.setString(2, userName);

            //execute Query to get data from database
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int total = rs.getInt("total");
                int sold = rs.getInt("sold");
                int available = total - sold;
                String day = rs.getString("day");
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("eventName"),
                        rs.getString("venue"),
                        day,
                        rs.getDouble("price"),
                        sold,
                        total,
                        available
                );

                int quantity = rs.getInt("quantity");
                cartItems.add(new CartItems(event, quantity));
            }
            return cartItems;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        };
        return cartItems;
    }
    public void saveOrUpdateItem(String username, int eventId, int quantity) {
        String sql = """
            INSERT INTO cart (user_id, event_id, quantity)
            VALUES ((SELECT id FROM users WHERE username = ?), ?, ?)
            ON CONFLICT(user_id, event_id) DO UPDATE SET quantity = excluded.quantity
        """;

        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setInt(2, eventId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(String username, int eventId) {
        String sql = """
            DELETE FROM cart
            WHERE user_id = (SELECT id FROM users WHERE username = ?)
              AND event_id = ?
        """;

        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setInt(2, eventId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCart(String username) {
        String sql = """
            DELETE FROM cart
            WHERE user_id = (SELECT id FROM users WHERE username = ?)
        """;

        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
