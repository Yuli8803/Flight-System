import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.FlightConnection;
import model.FlightManager;
import model.PaymentManager;
import java.io.IOException;
import java.util.List;

@WebServlet("/FlightServlet")
public class FlightServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("viewDetails".equals(action)) {
            handleViewDetails(request, response, session);
        } else {
            handleSearch(request, response, session);
        }
    }

    private void handleViewDetails(HttpServletRequest request, HttpServletResponse response,
                                   HttpSession session) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String travelClass = request.getParameter("class");
        String step = request.getParameter("step");

        FlightManager fm = new FlightManager();
        try {
            List<FlightConnection> searchResults =
                    (List<FlightConnection>) session.getAttribute("searchResults");

            if (searchResults == null) {
                response.sendRedirect("searchFlights.jsp");
                return;
            }

            int connectionId = Integer.parseInt(idStr);
            FlightConnection connection = fm.getConnectionById(searchResults, connectionId);

            if ("outbound".equals(step)) {
                session.setAttribute("selectedOutbound", connection);
                session.setAttribute("outboundClass", travelClass);

                // Check if this is a round trip (return results exist in session)
                List returnResults = (List) session.getAttribute("returnResults");
                boolean hasReturn = (returnResults != null && !returnResults.isEmpty());

                if (hasReturn) {
                    // Round trip switch searchResults in session to return flights
                    // so handleViewDetails can look up the selected return flight by ID
                    session.setAttribute("searchResults", returnResults);
                    request.setAttribute("mode", "return");
                    request.getRequestDispatcher("searchResults.jsp").forward(request, response);
                } else {
                    // One-way  show the detail/review page before booking
                    prepareConnectionDetails(request, session);
                    request.getRequestDispatcher("connectionDetails.jsp").forward(request, response);
                }

            } else if ("return".equals(step)) {
                session.setAttribute("selectedReturn", connection);
                session.setAttribute("returnClass", travelClass);

                // Both flights selected — show the detail/review page before booking
                prepareConnectionDetails(request, session);
                request.getRequestDispatcher("connectionDetails.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("searchFlights.jsp?error=selection");
        }
    }

    // Loads saved cards and calculates grand total so connectionDetails.jsp has everything it needs
    private void prepareConnectionDetails(HttpServletRequest request, HttpSession session)
            throws Exception {
        FlightConnection outbound = (FlightConnection) session.getAttribute("selectedOutbound");
        FlightConnection returnConn = (FlightConnection) session.getAttribute("selectedReturn");
        String outClass = (String) session.getAttribute("outboundClass");
        String retClass = (String) session.getAttribute("returnClass");
        String userEmail = (String) session.getAttribute("userEmail");

        double total = "First".equalsIgnoreCase(outClass)
                ? outbound.getTotalFirstClassPrice()
                : outbound.getTotalEconomyPrice();

        if (returnConn != null) {
            total += "First".equalsIgnoreCase(retClass)
                    ? returnConn.getTotalFirstClassPrice()
                    : returnConn.getTotalEconomyPrice();
        }

        session.setAttribute("grandTotal", total);

        // Use PaymentManager to fetch cards saved to the customer's account
        PaymentManager pm = new PaymentManager();
        List<String> savedCards = pm.getCardsByEmail(userEmail);
        request.setAttribute("savedCards", savedCards);
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response,
                              HttpSession session) throws ServletException, IOException {
        String dep = request.getParameter("dep");
        String dest = request.getParameter("dest");
        String depDate = request.getParameter("depDate");
        String retDate = request.getParameter("retDate");
        String sortBy = request.getParameter("sortBy");
        if (sortBy == null || sortBy.isEmpty()) sortBy = "price";

        String maxConnStr = request.getParameter("maxConnections");
        int maxConns = (maxConnStr != null && !maxConnStr.isEmpty())
                ? Integer.parseInt(maxConnStr) : 0;

        String maxPriceStr = request.getParameter("maxPrice");
        Double maxPrice = (maxPriceStr != null && !maxPriceStr.isEmpty())
                ? Double.parseDouble(maxPriceStr) : 99999.0;

        String maxLengthStr = request.getParameter("maxLength");
        Integer maxLength = (maxLengthStr != null && !maxLengthStr.isEmpty())
                ? Integer.parseInt(maxLengthStr) : 24;

        FlightManager fm = new FlightManager();
        try {
            // Search outbound flights
            List<FlightConnection> outboundResults = fm.searchFlights(
                    dep, dest, depDate, maxConns, maxPrice, maxLength, null, sortBy);

            session.setAttribute("searchResults", outboundResults);
            request.setAttribute("outboundResults", outboundResults);

            // If a return date was provided, search return flights separately
            if (retDate != null && !retDate.isEmpty()) {
                List<FlightConnection> returnResults = fm.searchFlights(
                        dest, dep, retDate, maxConns, maxPrice, maxLength, null, sortBy);
                session.setAttribute("returnResults", returnResults);
            } else {
                session.removeAttribute("returnResults");
            }

            request.getRequestDispatcher("searchResults.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("searchFlights.jsp?error=search_failed");
        }
    }
}