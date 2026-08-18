<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Retrieve the booking ID passed from the ConfirmBookingServlet
    String bookingId = request.getParameter("id");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Booking Confirmed | Flight System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        body { background-color: #f8f9fa; }
        .success-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            max-width: 600px;
            margin: 80px auto;
            padding: 40px;
        }
        .check-icon {
            font-size: 50px;
            color: #198754;
            background: #e9f7ef;
            width: 80px;
            height: 80px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            margin: 0 auto 30px;
        }
        .booking-ref-box {
            background: #f1f7ff;
            border-radius: 12px;
            padding: 20px;
            margin: 30px 0;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="success-card text-center">
        <div class="check-icon">
            <i class="bi bi-check-lg"></i>
        </div>

        <h1 class="fw-bold h2">Reservation Complete!</h1>
        <p class="text-muted">Your flight has been successfully booked. Please keep your booking ID for your records.</p>

        <div class="booking-ref-box">
            <div class="text-uppercase small fw-bold text-muted mb-1">Booking Reference</div>
            <div class="display-5 fw-bold text-primary">#<%= bookingId %></div>
        </div>

        <div class="d-grid gap-2">
            <a href="searchFlights.jsp" class="btn btn-primary btn-lg fw-bold py-3">Book Another Flight</a>

            <a href="ManageBookings" class="btn btn-outline-secondary btn-lg py-3">View My Bookings</a>
        </div>

        <p class="mt-4 text-muted small">A confirmation email has been sent to your registered address.</p>
    </div>
</div>

</body>
</html>