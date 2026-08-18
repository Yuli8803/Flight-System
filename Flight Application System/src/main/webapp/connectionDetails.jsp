<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.FlightConnection" %>
<%@ page import="model.Flight" %>
<%@ page import="java.util.List" %>
<%
    // Retrieve data prepared by the BookingServlet
    FlightConnection outbound = (FlightConnection) session.getAttribute("selectedOutbound");
    FlightConnection returnConn = (FlightConnection) session.getAttribute("selectedReturn");
    String outClass = (String) session.getAttribute("outboundClass");
    String retClass = (String) session.getAttribute("returnClass");

    // Total price and saved cards are now passed from the Servlet
    Double total = (Double) session.getAttribute("grandTotal");
    List<String> savedCards = (List<String>) request.getAttribute("savedCards");

    if (outbound == null) {
        response.sendRedirect("searchFlights.jsp");
        return;
    }

    // Calculate individual leg prices for display headers
    double outboundPrice = "First".equalsIgnoreCase(outClass) ? outbound.getTotalFirstClassPrice() : outbound.getTotalEconomyPrice();
    double returnPrice = (returnConn != null) ? ("First".equalsIgnoreCase(retClass) ? returnConn.getTotalFirstClassPrice() : returnConn.getTotalEconomyPrice()) : 0;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Review Your Trip | FlightConnect</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .trip-section { border-radius: 15px; border: 1px solid #dee2e6; background: white; margin-bottom: 20px; }
        .leg-header { background: #f8f9fa; border-bottom: 1px solid #dee2e6; padding: 10px 20px; border-radius: 15px 15px 0 0; }
        .segment { padding: 15px 20px; border-bottom: 1px dashed #eee; }
        .segment:last-child { border-bottom: none; }
        .leg-price { font-weight: 600; color: #333; margin-right: 15px; }
        .total-display { font-size: 3rem; color: #0d6efd; line-height: 1; }
    </style>
</head>
<body class="bg-light">

<div class="container py-5">
    <h2 class="fw-bold mb-4">Review Your Trip</h2>

    <!-- OUTBOUND LEG -->
    <div class="trip-section shadow-sm">
        <div class="leg-header d-flex justify-content-between align-items-center">
            <h5 class="mb-0 text-primary"><i class="bi bi-airplane-fill me-2"></i>Outbound Flight</h5>
            <div class="d-flex align-items-center">
                <span class="leg-price">$<%= String.format("%.2f", outboundPrice) %></span>
                <span class="badge bg-primary"><%= outClass %> Class</span>
            </div>
        </div>
        <% for (Flight f : outbound.getFlights()) { %>
            <div class="segment">
                <div class="row align-items-center text-center">
                    <div class="col-md-2 fw-bold text-uppercase"><%= f.getAirlineCode() %> <%= f.getFlightNumber() %></div>
                    <div class="col-md-3">
                        <div class="h5 mb-0"><%= f.getDepartureTime() %></div>
                        <div class="text-secondary"><%= f.getDepartureAirportCode() %></div>
                    </div>
                    <div class="col-md-2"><i class="bi bi-arrow-right fs-4"></i></div>
                    <div class="col-md-3">
                        <div class="h5 mb-0"><%= f.getArrivalTime() %></div>
                        <div class="text-secondary"><%= f.getArrivalAirportCode() %></div>
                    </div>
                </div>
            </div>
        <% } %>
    </div>

    <!-- RETURN LEG (Only shows if round-trip) -->
    <% if (returnConn != null) { %>
    <div class="trip-section shadow-sm">
        <div class="leg-header d-flex justify-content-between align-items-center">
            <h5 class="mb-0 text-success"><i class="bi bi-airplane-fill me-2" style="transform: rotate(180deg);"></i>Return Flight</h5>
            <div class="d-flex align-items-center">
                <span class="leg-price">$<%= String.format("%.2f", returnPrice) %></span>
                <span class="badge bg-success"><%= retClass %> Class</span>
            </div>
        </div>
        <% for (Flight f : returnConn.getFlights()) { %>
            <div class="segment">
                <div class="row align-items-center text-center">
                    <div class="col-md-2 fw-bold text-uppercase"><%= f.getAirlineCode() %> <%= f.getFlightNumber() %></div>
                    <div class="col-md-3">
                        <div class="h5 mb-0"><%= f.getDepartureTime() %></div>
                        <div class="text-secondary"><%= f.getDepartureAirportCode() %></div>
                    </div>
                    <div class="col-md-2"><i class="bi bi-arrow-right fs-4 text-success"></i></div>
                    <div class="col-md-3">
                        <div class="h5 mb-0"><%= f.getArrivalTime() %></div>
                        <div class="text-secondary"><%= f.getArrivalAirportCode() %></div>
                    </div>
                </div>
            </div>
        <% } %>
    </div>
    <% } %>

    <!-- TOTAL & BOOKING FOOTER -->
    <div class="card border-0 shadow mt-4 p-4">
        <form action="ConfirmBookingServlet" method="POST">
            <div class="row align-items-center">
                <div class="col-md-4">
                    <div class="text-muted text-uppercase small fw-bold">Total Price (All Passengers)</div>
                    <div class="total-display fw-bold">
                        $<%= (total != null) ? String.format("%.2f", total) : "0.00" %>
                    </div>
                </div>

                <div class="col-md-4">
                    <label class="form-label fw-bold"><i class="bi bi-credit-card me-2"></i>Payment Method</label>
                    <select name="cardNumber" class="form-select" required>
                        <option value="">-- Select Saved Card --</option>
                        <% if (savedCards != null && !savedCards.isEmpty()) {
                            for (String card : savedCards) {
                                // Mask the card number for security (shows last 4 digits)
                                String lastFour = card.length() > 4 ? card.substring(card.length() - 4) : card;
                                String masked = "****" + lastFour;
                        %>
                            <option value="<%= card %>"><%= masked %></option>
                        <% } } else { %>
                            <option value="" disabled>No saved cards found</option>
                        <% } %>
                    </select>
                    <div class="mt-2">
                        <a href="ManageProfile" class="small text-decoration-none">+ Add new card in profile</a>
                    </div>
                </div>

                <div class="col-md-4 text-end">
                    <a href="searchFlights.jsp" class="btn btn-link text-decoration-none text-muted me-3">Cancel</a>
                    <button type="submit" class="btn btn-primary btn-lg px-5 fw-bold rounded-pill shadow">
                        Confirm & Book <i class="bi bi-chevron-right ms-2"></i>
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>