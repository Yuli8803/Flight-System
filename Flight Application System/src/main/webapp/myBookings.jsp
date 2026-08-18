<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.BookingRecord" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Bookings | FlightConnect</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .table-container { background: white; border-radius: 15px; overflow: hidden; }
        .thead-custom { background-color: #f8f9fa; color: #6c757d; text-transform: uppercase; font-size: 0.85rem; letter-spacing: 0.5px; }
        .booking-id { font-family: 'Courier New', Courier, monospace; font-weight: bold; color: #0d6efd; }
        .badge-economy { background-color: #e0f2f1; color: #00796b; }
        .badge-first { background-color: #fff8e1; color: #f57f17; }
    </style>
</head>
<body class="bg-light">

<div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold mb-1">My Bookings</h2>
            <p class="text-muted small mb-0">Showing current and past reservations</p>
        </div>
        <a href="dashboard.jsp" class="btn btn-sm btn-outline-secondary rounded-pill px-3">
            <i class="bi bi-house-door me-1"></i> Dashboard
        </a>
    </div>

    <div class="table-container shadow-sm border">
        <table class="table table-hover align-middle mb-0">
            <thead class="thead-custom border-bottom">
                <tr>
                    <th class="ps-4 py-3">Booking ID</th>
                    <th>Flight Info</th>
                    <th>Travel Date</th>
                    <th>Class</th>
                    <th class="text-center">Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<BookingRecord> bookings = (List<BookingRecord>) request.getAttribute("userBookings");

                    if (bookings != null && !bookings.isEmpty()) {
                        for (BookingRecord b : bookings) {
                            String classClass = "Economy".equalsIgnoreCase(b.getSelectedClass()) ? "badge-economy" : "badge-first";
                %>
                    <tr>
                        <td class="ps-4">
                            <span class="booking-id">#<%= b.getBookingId() %></span>
                        </td>
                        <td>
                            <div class="fw-bold text-dark"><%= b.getFlightInfo() %></div>
                        </td>
                        <td>
                            <span class="text-muted"><%= b.getTravelDate() %></span>
                        </td>
                        <td>
                            <span class="badge <%= classClass %> rounded-pill px-3 py-2">
                                <%= b.getSelectedClass() %>
                            </span>
                        </td>
                        <td class="text-center">
                            <a href="CancelBooking?id=<%= b.getBookingId() %>"
                               class="btn btn-light btn-sm text-danger border border-danger-subtle"
                               onclick="return confirm('Release these seats? This action cannot be undone.')">
                               <i class="bi bi-trash3"></i> Cancel
                            </a>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="5" class="text-center py-5">
                            <div class="py-4">
                                <i class="bi bi-journal-x fs-1 text-light-emphasis"></i>
                                <p class="mt-3 text-muted">You haven't booked any flights yet.</p>
                                <a href="searchFlights.jsp" class="btn btn-primary btn-sm rounded-pill px-4">Find a Flight</a>
                            </div>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <div class="mt-5 text-center small text-muted">
        Need help? Contact <a href="mailto:support@flightconnect.com" class="text-decoration-none">support@flightconnect.com</a>
    </div>
</div>

</body>
</html>