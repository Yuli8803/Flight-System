import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/ConfirmBookingServlet")
public class ConfirmBookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        FlightConnection outbound = (FlightConnection) session.getAttribute("selectedOutbound");
        FlightConnection returnConn = (FlightConnection) session.getAttribute("selectedReturn");
        String userEmail = (String) session.getAttribute("userEmail");
        String cardNumber = request.getParameter("cardNumber");
        String outClass = (String) session.getAttribute("outboundClass");
        String retClass = (String) session.getAttribute("returnClass");

        if (userEmail == null || outbound == null || cardNumber == null) {
            response.sendRedirect("searchFlights.jsp?error=missing_data");
            return;
        }

        BookingManager bm = new BookingManager();
        try {
            int bookingId = bm.createBaseBooking(userEmail, cardNumber);

            // Get the first leg of the outbound connection
            Flight outFlight = outbound.getFlights().get(0);
            bm.addFlightToBooking(
                    bookingId,
                    outFlight.getAirlineCode(),
                    outFlight.getFlightNumber(),
                    outFlight.getFlightDate(),
                    outClass
            );

            // Handle return flight if it exists
            if (returnConn != null && !returnConn.getFlights().isEmpty()) {
                Flight retFlight = returnConn.getFlights().get(0);
                bm.addFlightToBooking(
                        bookingId,
                        retFlight.getAirlineCode(),
                        retFlight.getFlightNumber(),
                        retFlight.getFlightDate(),
                        retClass
                );
            }

            // Clean up session and go to confirmation
            session.removeAttribute("selectedOutbound");
            session.removeAttribute("selectedReturn");
            session.removeAttribute("outboundClass");
            session.removeAttribute("returnClass");
            response.sendRedirect("confirmation.jsp?id=" + bookingId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("searchFlights.jsp?error=booking_failed");
        }
    }
}