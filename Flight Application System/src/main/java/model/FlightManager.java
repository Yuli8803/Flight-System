package model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightManager {

    public List<FlightConnection> searchFlights(String dep, String dest, String date,
                                                int maxConns, Double maxPrice, Integer maxLength,
                                                String retDate, String sortBy) throws SQLException {

        List<FlightConnection> allResults = new ArrayList<>();

        if (dep == null || dest == null || date == null || date.isEmpty()) {
            return allResults;
        }

        dep = dep.toUpperCase();
        dest = dest.toUpperCase();

        System.out.println("Searching: " + dep + " -> " + dest + " on " + date);

        allResults.addAll(getDirectFlights(dep, dest, date, maxPrice, maxLength));

        if (maxConns >= 1) {
            allResults.addAll(getOneStopConnections(dep, dest, date, maxPrice, maxLength));
        }

        if ("price".equals(sortBy)) {
            allResults.sort((a, b) -> Double.compare(a.getTotalEconomyPrice(), b.getTotalEconomyPrice()));
        } else if ("duration".equals(sortBy)) {
            allResults.sort((a, b) -> Long.compare(a.getTotalDurationMinutes(), b.getTotalDurationMinutes()));
        }

        return allResults;
    }

    private List<FlightConnection> getDirectFlights(String dep, String dest, String date,
                                                    Double maxPrice, Integer maxLength) throws SQLException {

        List<FlightConnection> connections = new ArrayList<>();

        // JOIN price table here so we get prices in one query per flight
        // Capacity comes directly from the flight table (first_class_capacity, economy_class_capacity)
        String sql = "SELECT f.airline_code, f.flight_number, f.flight_date, " +
                "f.departure_airport_code, f.arrival_airport_code, " +
                "f.departure_time, f.arrival_time, " +
                "f.first_class_capacity, f.economy_class_capacity, " +
                "MAX(CASE WHEN p.travel_class = 'Economy' THEN p.price_amount END) AS economy_price, " +
                "MAX(CASE WHEN p.travel_class = 'First'   THEN p.price_amount END) AS first_price " +
                "FROM flight f " +
                "LEFT JOIN price p ON f.airline_code = p.airline_code " +
                "  AND f.flight_number = p.flight_number " +
                "  AND f.flight_date = p.flight_date " +
                "WHERE f.departure_airport_code = ? " +
                "  AND f.arrival_airport_code = ? " +
                "  AND f.flight_date = ? " +
                "GROUP BY f.airline_code, f.flight_number, f.flight_date, " +
                "f.departure_airport_code, f.arrival_airport_code, " +
                "f.departure_time, f.arrival_time, " +
                "f.first_class_capacity, f.economy_class_capacity";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dep);
            stmt.setString(2, dest);
            stmt.setDate(3, java.sql.Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Flight f = mapRowToFlight(rs);
                FlightConnection fc = new FlightConnection();
                fc.addFlight(f);

                if (fc.hasEconomySeats() && isWithinFilters(fc, maxPrice, maxLength)) {
                    connections.add(fc);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format: " + date);
        }
        return connections;
    }

    // Maps a ResultSet row directly
    private Flight mapRowToFlight(ResultSet rs) throws SQLException {
        Flight f = new Flight();
        f.setAirlineCode(rs.getString("airline_code"));
        f.setFlightNumber(rs.getInt("flight_number"));
        f.setFlightDate(rs.getDate("flight_date"));
        f.setDepartureAirportCode(rs.getString("departure_airport_code"));
        f.setArrivalAirportCode(rs.getString("arrival_airport_code"));
        f.setDepartureTime(rs.getTime("departure_time"));
        f.setArrivalTime(rs.getTime("arrival_time"));

        // Capacity comes from flight table directly
        int econCap = rs.getInt("economy_class_capacity");
        int firstCap = rs.getInt("first_class_capacity");
        f.setEconomyClassCapacity(econCap);
        f.setFirstClassCapacity(firstCap);

        f.setEconomyClassPrice(rs.getDouble("economy_price"));
        f.setFirstClassPrice(rs.getDouble("first_price"));


        // This ensures fully booked flights are excluded from search results
        setAvailableSeats(f, econCap, firstCap);

        return f;
    }

    // Queries the ticket table to count how many seats are already booked per class
    private void setAvailableSeats(Flight f, int econCap, int firstCap) throws SQLException {
        String sql = "SELECT selected_class, COUNT(*) AS booked " +
                "FROM ticket " +
                "WHERE airline_code = ? AND flight_number = ? AND flight_date = ? " +
                "GROUP BY selected_class";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getAirlineCode());
            stmt.setInt(2, f.getFlightNumber());
            stmt.setDate(3, f.getFlightDate());

            ResultSet rs = stmt.executeQuery();
            int bookedEcon = 0;
            int bookedFirst = 0;
            while (rs.next()) {
                String cls = rs.getString("selected_class");
                int count = rs.getInt("booked");
                if ("Economy".equalsIgnoreCase(cls)) bookedEcon = count;
                else if ("First".equalsIgnoreCase(cls))  bookedFirst = count;
            }
            f.setAvailableEconomySeats(econCap - bookedEcon);
            f.setAvailableFirstClassSeats(firstCap - bookedFirst);
        }
    }

    private boolean isWithinFilters(FlightConnection fc, Double maxPrice, Integer maxLength) {
        if (maxPrice != null && maxPrice > 0 && fc.getTotalEconomyPrice() > maxPrice) return false;
        if (maxLength != null && maxLength > 0 && fc.getTotalDurationMinutes() > (maxLength * 60)) return false;
        return true;
    }

    private List<FlightConnection> getOneStopConnections(String dep, String dest, String date,
                                                         Double maxPrice, Integer maxLength) throws SQLException {
        return new ArrayList<>();
    }

    public FlightConnection getConnectionById(List<FlightConnection> results, int id) {
        if (results == null) return null;
        return results.stream()
                .filter(fc -> fc.getId() == id)
                .findFirst()
                .orElse(null);
    }
}