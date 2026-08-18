package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    // Connection parameters
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "Yj102719!";

    //Establishes a connection to the PostgreSQL database.Uses Class.forName to ensure the driver is loaded for Tomcat.

    public static Connection getConnection() throws SQLException {
        try {
            // This tells Tomcat specifically to look for the driver in WEB-INF/lib
            Class.forName("org.postgresql.Driver");

            // Returns the connection using your defined URL, USER, and PASS
            return DriverManager.getConnection(URL, USER, PASS);

        } catch (ClassNotFoundException e) {
            // This happens if the .jar file is missing or in the wrong folder
            System.out.println("Driver not found! Check your WEB-INF/lib folder.");
            e.printStackTrace();
            return null;
        }
    }
}