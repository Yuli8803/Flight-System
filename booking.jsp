<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%
    // Retrieve the selected flights from the session (set by BookFlightServlet)
    Object outbound = session.getAttribute("selectedOutbound");
    Object returnFlight = session.getAttribute("selectedReturn");

    // We use reflection to get data safely from the Connection objects
    List outboundFlights = (List) outbound.getClass().getMethod("getFlights").invoke(outbound);
    double outboundPrice = (Double) outbound.getClass().getMethod("getTotalEconomyPrice").invoke(outbound);
    double outboundFirstPrice = (Double) outbound.getClass().getMethod("getTotalFirstClassPrice").invoke(outbound);

    double totalEcon = outboundPrice;
    double totalFirst = outboundFirstPrice;

    // If a return flight exists, add its price to the total
    if (returnFlight != null) {
        totalEcon += (Double) returnFlight.getClass().getMethod("getTotalEconomyPrice").invoke(returnFlight);
        totalFirst += (Double) returnFlight.getClass().getMethod("getTotalFirstClassPrice").invoke(returnFlight);
    }

    List<String> userCards = (List<String>) request.getAttribute("userCards");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Finalize Reservation | Flight System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .summary-box { background-color: #f8f9fa; border-left: 5px solid #0d6efd; }
        .card-option { cursor: pointer; transition: 0.2s; }
        .card-option:hover { border-color: #0d6efd; }
    </style>
</head>
<body class="bg-light">

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-9">
            <div class="card shadow-lg border-0 rounded-4">
                <div class="card-header bg-primary text-white py-3">
                    <h4 class="mb-0"><i class="bi bi-credit-card-2-back me-2"></i>Finalize Your Booking</h4>
                </div>

                <div class="card-body p-4">
                    <h5 class="fw-bold mb-3 text-secondary">Trip Summary</h5>

                    <div class="p-3 mb-3 summary-box rounded">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="badge bg-primary mb-2">Outbound</span>
                                <h6 class="mb-0">Connection with <%= outboundFlights.size() %> flight(s)</h6>
                            </div>
                            <i class="bi bi-airplane text-primary fs-3"></i>
                        </div>
                    </div>

                    <% if (returnFlight != null) {
                        List returnLegs = (List) returnFlight.getClass().getMethod("getFlights").invoke(returnFlight);
                    %>
                        <div class="p-3 mb-3 summary-box rounded" style="border-left-color: #0dcaf0;">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <span class="badge bg-info text-dark mb-2">Return</span>
                                    <h6 class="mb-0">Connection with <%= returnLegs.size() %> flight(s)</h6>
                                </div>
                                <i class="bi bi-airplane-fill text-info fs-3" style="transform: rotate(180deg);"></i>
                            </div>
                        </div>
                    <% } %>

                    <hr class="my-4">

                    <form action="ConfirmBooking" method="POST">
                        <div class="mb-4">
                            <label class="form-label fw-bold">Choose Travel Class for Entire Trip:</label>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <input type="radio" class="btn-check" name="travelClass" id="econ" value="Economy" checked>
                                    <label class="btn btn-outline-primary w-100 p-3 text-start h-100" for="econ">
                                        <span class="d-block fw-bold">Economy Class</span>
                                        <span class="fs-4">$<%= String.format("%.2f", totalEcon) %></span>
                                    </label>
                                </div>
                                <div class="col-md-6">
                                    <input type="radio" class="btn-check" name="travelClass" id="first" value="First">
                                    <label class="btn btn-outline-primary w-100 p-3 text-start h-100" for="first">
                                        <span class="d-block fw-bold">First Class</span>
                                        <span class="fs-4">$<%= String.format("%.2f", totalFirst) %></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="mb-4">
                            <label class="form-label fw-bold">Select Saved Payment Method:</label>
                            <select class="form-select form-select-lg" name="cardNumber" required>
                                <option value="" disabled selected>-- Select a Card --</option>
                                <%
                                    if (userCards != null && !userCards.isEmpty()) {
                                        for (String card : userCards) {
                                            String lastFour = card.substring(card.length() - 4);
                                %>
                                    <option value="<%= card %>">Visa/Mastercard ending in <%= lastFour %></option>
                                <%
                                        }
                                    } else {
                                %>
                                    <option value="" disabled>No cards found. Update in Profile.</option>
                                <% } %>
                            </select>
                        </div>

                        <div class="d-grid gap-2 mt-5">
                            <button type="submit" class="btn btn-success btn-lg fw-bold py-3 shadow-sm">
                                <i class="bi bi-lock-fill me-2"></i>Confirm Booking
                            </button>
                            <a href="javascript:history.back()" class="btn btn-link text-muted">Go Back</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>