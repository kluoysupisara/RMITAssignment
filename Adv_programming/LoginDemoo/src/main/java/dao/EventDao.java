package dao;

import model.Event;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EventDao {
    private final String TABLE_NAME = "events";

    public EventDao() {
    }

    public void setup() throws SQLException {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                    + "( id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "eventName TEXT,"
                    + "venue TEXT,"
                    + "day TEXT,"
                    + "price REAL,"
                    + "sold INT,"
                    + "Total INT);";

            stmt.executeUpdate(sql);
            System.out.println("Table '" + TABLE_NAME + "' checked/created successfully.");
            // ✅ Only insert data if table is empty
            if (isTableEmpty(conn)) {
                insertEventFromFile(conn);
            } else {
                System.out.println("Table is not empty. Skipping file import.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isTableEmpty(Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            return rs.next() && rs.getInt(1) == 0;
        }
    }
    public void insertEventFromFile(Connection conn) throws SQLException, IOException {
        String filePath = "src/events.dat";
        try {
            //Read file event.dat to insert into table
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            String sql = "INSERT INTO " + TABLE_NAME
                    + " (eventName, venue, day, price, sold, total) "
                    + "VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split(";");
                pstmt.setString(1, values[0].trim());
                pstmt.setString(2, values[1].trim());
                pstmt.setString(3, values[2].trim());
                pstmt.setString(4, values[3].trim());
                pstmt.setInt(5, Integer.parseInt(values[4].trim()));
                pstmt.setInt(6, Integer.parseInt(values[5].trim()));

                pstmt.executeUpdate();
                System.out.println(pstmt.toString());
            }
            System.out.println("Setup Data imported successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<String> getValidDays(){
        DayOfWeek today = LocalDate.now().getDayOfWeek(); // MONDAY, etc.
        String[] allDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        int index = today.getValue() - 1; // Convert MONDAY=1 to index=0
        return Arrays.asList(Arrays.copyOfRange(allDays, index, allDays.length)); // [Friday, Sat, Sun]
    }

    public List<Event> getUpComingEvents() throws SQLException {
        List<Event> events = new ArrayList<>();


        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ) {


            List<String> validDays = getValidDays();
            String inClause = validDays.stream()
                    .map(day -> "'" + day.trim() + "'")
                    .collect(Collectors.joining(", "));

            String query = "SELECT * FROM " + TABLE_NAME + " WHERE day IN (" + inClause +" )";
            System.out.println("query: " + query);

            ResultSet rs = stmt.executeQuery(query);
            //System.out.println("rs size: "+ rs.toString());;

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
                events.add(event);
            }

        } catch (Exception e) {
            System.err.println("DB Error: " + e.getMessage());
        }

        return events;
    }
    public void updateSoldTickets(int eventId, int newSoldCount) {
        String sql = "UPDATE " + TABLE_NAME + " SET sold = ? WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, newSoldCount);
            stmt.setInt(2, eventId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating sold tickets: " + e.getMessage());
        }
    }
    public Event getEventById(int eventId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String eventName = rs.getString("eventName");
                String venue = rs.getString("venue");
                String day = rs.getString("day");
                double price = rs.getDouble("price");
                int sold = rs.getInt("sold");
                int total = rs.getInt("total");
                int available = total - sold;

                return new Event(id, eventName, venue, day, price, sold, total, available);
            }
        } catch (SQLException e) {
            System.out.println("Error getting event by ID: " + e.getMessage());
        }
        return null;
    }

    public void setEventEnabled(int eventId, boolean enabled) {
        String sql = "UPDATE " + TABLE_NAME +" SET enabled = ? WHERE id = ?";
        try (Connection con = Database.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, enabled ? 1 : 0);
            stmt.setInt(2, eventId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY event_name, venue, day";

        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("event_name");
                String venue = rs.getString("venue");
                String day = rs.getString("day");
                double price = rs.getDouble("price");
                int soldTickets = rs.getInt("sold_tickets");
                int totalTickets = rs.getInt("total_tickets");
                boolean enabled = rs.getBoolean("enabled");

                Event event = new Event(id, name, venue, day, price, soldTickets, totalTickets, enabled);
                events.add(event);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all events: " + e.getMessage());
        }

        return events;
    }



}

