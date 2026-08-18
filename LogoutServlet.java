import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the current session
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Destroy the session (clears the email and all data)
            session.invalidate();
        }

        // Redirect to the login page (index.jsp)
        response.sendRedirect("index.jsp");
    }
}