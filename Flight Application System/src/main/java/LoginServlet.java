import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.DataBaseConnection;

import java.io.IOException;
import java.sql.*;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    // This method handles the data sent from your login form
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Connect to PostgreSQL using your model.DataBaseConnection class
            Connection conn = DataBaseConnection.getConnection();

            // Search for the user with matching email and password
            String sql = "SELECT * FROM customer WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // If found, create a session and go to dashboard
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", email);
                response.sendRedirect("dashboard.jsp");
            } else {
                // If not found, show an error message
                response.setContentType("text/html");
                response.getWriter().println("<h3 style='color:red;'>Login Failed: Incorrect email or password.</h3>");
                response.getWriter().println("<a href='index.jsp'>Try Again</a>");
            }

            conn.close();
        } catch (Exception e) {
            // Print errors to console for debugging
            e.printStackTrace();
            response.getWriter().println("Database Error: " + e.getMessage());
        }
    }
}