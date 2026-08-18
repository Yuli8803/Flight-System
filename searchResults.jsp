<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.Duration" %>
<%@ page import="java.time.LocalTime" %>
<%
    // Determine if we are showing Outbound or Return results
    String mode = (String) request.getAttribute("mode");
    if (mode == null) mode = "outbound";

    List results;
    String title;
    String step;

    if ("return".equals(mode)) {
        results = (List) session.getAttribute("returnResults");
        title = "Select Your Return Flight";
        step = "return";
    } else {
        results = (List) request.getAttribute("outboundResults");
        title = "Select Your Outbound Flight";
        step = "outbound";
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Flight Results | FlightConnect</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .flight-card { transition: transform 0.2s; border-radius: 15px; }
        .flight-card:hover { transform: scale(1.01); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
        .price-text { font-size: 1.3rem; font-weight: bold; color: #0d6efd; }
        .class-label { font-size: 0.75rem; text-transform: uppercase; letter-spacing: 1px; color: #6c757d; }
        .duration-line { border-top: 2px solid #dee2e6; position: relative; margin: 10px 0; }
        .duration-text { font-size: 0.8rem; color: #6c757d; }
    </style>
</head>
<body class="bg-light">

<div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold text-primary mb-0"><%= title %></h2>
        <span class="badge bg-primary rounded-pill"><%= results != null ? results.size() : 0 %> Connections Found</span>
    </div>

    <% if (results == null || results.isEmpty()) { %>
        <div class="alert alert-warning shadow-sm">
            <i class="bi bi-exclamation-triangle me-2"></i>
            No flight connections found for the selected criteria.[cite: 1]
            <a href="searchFlights.jsp" class="alert-link">Try a different search.</a>
        </div>
    <% } else { %>
        <div class="row g-4">
            <%
                for (Object connection : results) {
                    // Reflection to access FlightConnection methods
                    int id = (Integer) connection.getClass().getMethod("getId").invoke(connection);
                    double econPrice = (Double) connection.getClass().getMethod("getTotalEconomyPrice").invoke(connection);
                    double firstPrice = (Double) connection.getClass().getMethod("getTotalFirstClassPrice").invoke(connection);

                    // Capacity checks
                    boolean hasEcon = (Boolean) connection.getClass().getMethod("hasEconomySeats").invoke(connection);
                    boolean hasFirst = (Boolean) connection.getClass().getMethod("hasFirstClassSeats").invoke(connection);

                    List flights = (List) connection.getClass().getMethod("getFlights").invoke(connection);
                    Object firstF = flights.get(0);
                    Object lastF = flights.get(flights.size() - 1);

                    String depTimeStr = String.valueOf(firstF.getClass().getMethod("getDepartureTime").invoke(firstF));
                    String arrTimeStr = String.valueOf(lastF.getClass().getMethod("getArrivalTime").invoke(lastF));

                    // Requirement 4.3: Calculate Total Length
                    LocalTime t1 = LocalTime.parse(depTimeStr);
                    LocalTime t2 = LocalTime.parse(arrTimeStr);
                    Duration duration = Duration.between(t1, t2);
                    if (duration.isNegative()) duration = duration.plusDays(1); // Handle overnight flights
                    String totalDuration = duration.toHours() + "h " + (duration.toMinutes() % 60) + "m";
            %>
                <div class="col-12">
                    <div class="card flight-card border-0 shadow-sm p-4">
                        <div class="row align-items-center">
                            <!-- Times & Codes -->
                            <div class="col-md-3 text-center">
                                <div class="h3 mb-0 fw-bold"><%= depTimeStr %></div>
                                <div class="fw-bold text-secondary"><%= firstF.getClass().getMethod("getDepartureAirportCode").invoke(firstF) %></div>
                            </div>

                            <!-- Duration & Stops -->
                            <div class="col-md-3 text-center">
                                <div class="duration-text"><%= totalDuration %></div>
                                <div class="duration-line">
                                    <i class="bi bi-airplane-fill text-primary" style="position:absolute; top:-10px; left:45%;"></i>
                                </div>
                                <span class="badge bg-light text-dark border">
                                    <%= (flights.size() == 1) ? "Direct" : (flights.size() - 1) + " Stop" %>
                                </span>
                            </div>

                            <div class="col-md-3 text-center">
                                <div class="h3 mb-0 fw-bold"><%= arrTimeStr %></div>
                                <div class="fw-bold text-secondary"><%= lastF.getClass().getMethod("getArrivalAirportCode").invoke(lastF) %></div>
                            </div>

                            <!-- Pricing Section (Economy & First Class) -->
                            <div class="col-md-3 border-start ps-4">
                                <% if (hasEcon) { %>
                                    <div class="mb-3">
                                        <div class="class-label">Economy</div>
                                        <div class="price-text mb-1">$<%= String.format("%.2f", econPrice) %></div>
                                        <a href="FlightServlet?action=viewDetails&id=<%= id %>&class=Economy&step=<%= step %>"
                                           class="btn btn-outline-primary btn-sm w-100 fw-bold">
                                            Details & Select
                                        </a>
                                    </div>
                                <% } %>

                                <% if (hasFirst) { %>
                                    <div>
                                        <div class="class-label">First Class</div>
                                        <div class="price-text mb-1 text-purple" style="color: #6f42c1;">$<%= String.format("%.2f", firstPrice) %></div>
                                        <a href="FlightServlet?action=viewDetails&id=<%= id %>&class=First&step=<%= step %>"
                                           class="btn btn-outline-dark btn-sm w-100 fw-bold">
                                            Details & Select
                                        </a>
                                    </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
            <% } %>
        </div>
    <% } %>
</div>

</body>
</html>