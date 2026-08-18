import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.RegistrationManager;

import java.io.IOException;

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String first = request.getParameter("firstName");
        String last = request.getParameter("lastName");
        String airport = request.getParameter("airport");
        String password = request.getParameter("password");

        RegistrationManager manager = new RegistrationManager();

        try {
            // Save to Database
            manager.registerCustomer(email, first, last, airport, password);

            // Create the Session immediately (Auto-Login)
            HttpSession session = request.getSession();
            session.setAttribute("userEmail", email);

            // Go straight to Dashboard
            response.sendRedirect("dashboard.jsp");

        } catch (Exception e) {
            response.getWriter().println("Registration Error: " + e.getMessage());
        }
    }
}