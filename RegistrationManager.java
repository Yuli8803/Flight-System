package model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationManager {

    //Saves a new user's information into the PostgreSQL customer table. Takes email, names, airport code, and password as inputs.

    public void registerCustomer(String email, String first, String last, String airport, String password) {

        // Defines the SQL command to insert a new row into the customer table
        // We use 5 question marks as placeholders for the 5 pieces of data
        String sql = "INSERT INTO customer (email, first_name, last_name, home_airport_code, password) VALUES (?, ?, ?, ?, ?)";

        // Opens a connection to the database and prepares the SQL statement
        // The try-with-resources block ensures the connection closes automatically
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Maps the Java strings to the SQL placeholders in the correct order
            pstmt.setString(1, email);
            pstmt.setString(2, first);
            pstmt.setString(3, last);
            pstmt.setString(4, airport);
            pstmt.setString(5, password);

            // Executes the command to actually save the data into the database
            pstmt.executeUpdate();
            System.out.println("Registration successful for: " + email);

        } catch (SQLException e) {
            // Checks if the error is specifically a "Duplicate Key" error
            // This happens if the email is already registered in the database
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Registration failed: Email " + email + " already exists.");
            } else {
                // Catches other database errors like connection issues or syntax errors
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }
}