package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class ProfileManager {

    // CREATES

    public void addAddress(String customerEmail, String street, String city, String state, String country, String zipCode) throws SQLException {
        String sql = "INSERT INTO address (address_id, customer_email, street, city, state, country, zip_code) " +
                "VALUES ((SELECT COALESCE(MAX(address_id), 0) + 1 FROM address), ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerEmail);
            stmt.setString(2, street);
            stmt.setString(3, city);
            stmt.setString(4, state);
            stmt.setString(5, country);
            stmt.setString(6, zipCode);
            stmt.executeUpdate();
        }
    }

    public void addCreditCard(String cardNumber, String email, String holderName, String expDate, int billingAddressId) throws SQLException {
        String sql = "INSERT INTO creditcard (card_number, customer_email, cardholder_name, expiration_date, billing_address_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardNumber);
            stmt.setString(2, email);
            stmt.setString(3, holderName);
            stmt.setString(4, expDate);
            stmt.setInt(5, billingAddressId);
            stmt.executeUpdate();
        }
    }

    // READ

    public List<Address> getAddressesByEmail(String email) throws SQLException {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM address WHERE customer_email = ? ORDER BY address_id DESC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Address addr = new Address();
                addr.setAddressId(rs.getInt("address_id"));
                addr.setStreet(rs.getString("street"));
                addr.setCity(rs.getString("city"));
                addr.setState(rs.getString("state"));
                addr.setZipCode(rs.getString("zip_code"));
                addr.setCountry(rs.getString("country"));
                list.add(addr);
            }
        }
        return list;
    }

    public List<Object[]> getCardsByEmail(String email) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        // JOIN allows us to get the street name associated with the billing ID
        String sql = "SELECT c.card_number, c.cardholder_name, c.expiration_date, a.street " +
                "FROM creditcard c JOIN address a ON c.billing_address_id = a.address_id " +
                "WHERE c.customer_email = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("card_number"),
                        rs.getString("cardholder_name"),
                        rs.getString("expiration_date"),
                        rs.getString("street")
                });
            }
        }
        return list;
    }

    // DELETE ACTIONS

    public void deleteCreditCard(String cardNumber, String email) throws SQLException {
        String sql = "DELETE FROM creditcard WHERE card_number = ? AND customer_email = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardNumber);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    public boolean deleteAddress(int addressId, String email) throws SQLException {
        //  Check if this address is being used by ANY credit card
        String checkSql = "SELECT COUNT(*) FROM creditcard WHERE billing_address_id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, addressId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // model.Address is in use! We cannot delete it yet.
                return false;
            }

            // If not in use, proceed with deletion
            String deleteSql = "DELETE FROM address WHERE address_id = ? AND customer_email = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, addressId);
                deleteStmt.setString(2, email);
                deleteStmt.executeUpdate();
                return true;
            }
        }
    }
    // UPDATE

    public void updateAddress(int addressId, String street, String city, String state, String country, String zipCode) throws SQLException {
        String sql = "UPDATE address SET street=?, city=?, state=?, country=?, zip_code=? WHERE address_id=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, street);
            stmt.setString(2, city);
            stmt.setString(3, state);
            stmt.setString(4, country);
            stmt.setString(5, zipCode);
            stmt.setInt(6, addressId);
            stmt.executeUpdate();
        }
    }

    public void updateCreditCard(String cardNumber, String holderName, String expDate, int billingAddressId) throws SQLException {
        String sql = "UPDATE creditcard SET cardholder_name=?, expiration_date=?, billing_address_id=? WHERE card_number=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, holderName);
            stmt.setString(2, expDate);
            stmt.setInt(3, billingAddressId);
            stmt.setString(4, cardNumber);
            stmt.executeUpdate();
        }
    }
}