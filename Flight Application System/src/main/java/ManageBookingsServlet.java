import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.BookingManager;
import model.BookingRecord;

@WebServlet({"/ManageBookings", "/CancelBooking"})
public class ManageBookingsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Verify user is logged in
        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String userEmail = (String) session.getAttribute("userEmail");
        BookingManager bookingManager = new BookingManager();
        String path = request.getServletPath();

        try {
            // Handle the "Cancel" action
            if ("/CancelBooking".equals(path)) {
                String idParam = request.getParameter("id");
                if (idParam != null) {
                    int bookingId = Integer.parseInt(idParam);
                    // Call the void method from model.BookingManager
                    bookingManager.cancelBooking(bookingId);
                }
                // Redirect back to the view to show updated status
                response.sendRedirect("ManageBookings");
                return;
            }

            // Handle the "View" action
            // Fetches the List of BookingRecord objects for the JSP table
            List<BookingRecord> bookings = bookingManager.getCustomerBookings(userEmail);

            // Pass the data to the JSP
            request.setAttribute("userBookings", bookings);
            request.getRequestDispatcher("myBookings.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=service_unavailable");
        }
    }
}