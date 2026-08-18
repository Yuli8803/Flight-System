package model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {

    // Fetches all bookings for a customer, joining ticket + flight to get display info
    public List<BookingRecord> getCustomerBookings(String email) throws SQLException {
        List<BookingRecord> bookings = new ArrayList<>();

        // JOIN ticket and flight so we can populate flightInfo, travelDate, selectedClass
        // ticket PK: (booking_id, airline_code, flight_number, flight_date)
        String sql = "SELECT b.booking_id, b.customer_email, " +
                "t.selected_class, t.flight_date, " +
                "f.departure_airport_code, f.arrival_airport_code " +
                "FROM booking b " +
                "JOIN ticket t ON b.booking_id = t.booking_id " +
                "JOIN flight f ON t.airline_code = f.airline_code " +
                "  AND t.flight_number = f.flight_number " +
                "  AND t.flight_date = f.flight_date " +
                "WHERE b.customer_email = ? " +
                "ORDER BY b.booking_id";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BookingRecord br = new BookingRecord();
                br.setBookingId(rs.getInt("booking_id"));
                br.setCustomerEmail(rs.getString("customer_email"));
                br.setSelectedClass(rs.getString("selected_class"));
                br.setTravelDate(rs.getDate("flight_date").toString());

                // Build a readable flight info string e.g. "ORD → JFK"
                String dep = rs.getString("departure_airport_code");
                String arr = rs.getString("arrival_airport_code");
                br.setFlightInfo(dep + " \u2192 " + arr);

                bookings.add(br);
            }
        }
        return bookings;
    }

    // Cancels (deletes) a booking — cascades to ticket via ON DELETE CASCADE
    public void cancelBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM booking WHERE booking_id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        }
    }

    // Returns card numbers previously used by this customer
    public List<String> getUserCards(String email) throws SQLException {
        List<String> cards = new ArrayList<>();
        String sql = "SELECT DISTINCT creditcard_number FROM booking WHERE customer_email = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cards.add(rs.getString("creditcard_number"));
            }
        }
        return cards;
    }

    // Creates the base booking row and returns the new booking_id
    // booking_id is a plain INTEGER (not SERIAL), so we generate it manually
    public int createBaseBooking(String email, String cardNumber) throws SQLException {
        // Get the next available ID
        String maxSql = "SELECT COALESCE(MAX(booking_id), 0) + 1 FROM booking";
        int newId = -1;
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement maxStmt = conn.prepareStatement(maxSql)) {
            ResultSet rs = maxStmt.executeQuery();
            if (rs.next()) newId = rs.getInt(1);
        }
        if (newId == -1) throw new SQLException("Could not generate booking_id");

        // Insert with the generated ID
        String sql = "INSERT INTO booking (booking_id, customer_email, creditcard_number) VALUES (?, ?, ?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newId);
            stmt.setString(2, email);
            stmt.setString(3, cardNumber);
            stmt.executeUpdate();
        }
        return newId;
    }

    // Inserts a ticket row
    public void addFlightToBooking(int bookingId, String airlineCode,
                                   int flightNumber, Date flightDate,
                                   String selectedClass) throws SQLException {
        String sql = "INSERT INTO ticket (booking_id, airline_code, flight_number, flight_date, selected_class) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.setString(2, airlineCode);
            stmt.setInt(3, flightNumber);
            stmt.setDate(4, flightDate);
            stmt.setString(5, selectedClass);
            stmt.executeUpdate();
        }
    }
}