package model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentManager {

    public boolean deleteCreditCard(String cardNum) throws SQLException {
        String sql = "DELETE FROM creditcard WHERE card_number = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cardNum);
            pstmt.executeUpdate();
            System.out.println("Credit card " + cardNum + " deleted successfully.");
            return true;

        } catch (SQLException e) {
            // State 23503 is the standard SQL code for Foreign Key Violation
            if ("23503".equals(e.getSQLState())) {
                System.out.println("Cannot delete card: " + cardNum + " is linked to an existing booking.");
                return false;
            } else {
                // If it's a different error, throw it so the Servlet can catch it
                throw e;
            }
        }
    }

    // Helper to fetch cards for the dropdown.

    public List<String> getCardsByEmail(String email) {
        List<String> cards = new ArrayList<>();
        String sql = "SELECT card_number FROM creditcard WHERE customer_email = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                cards.add(rs.getString("card_number"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching cards: " + e.getMessage());
        }
        return cards;
    }
}