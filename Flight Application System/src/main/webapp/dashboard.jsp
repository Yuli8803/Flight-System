<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //Security Check
    // If the session is empty, redirect them to the login page immediately.
    if (session.getAttribute("userEmail") == null) {
        response.sendRedirect("index.jsp");
        return; // Stop processing the rest of the page
    }

    // Cache Control
    // This forces the browser to check with the server every time (prevents 'back' button hacking)
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<html>
<head>
    <title>Flight System | Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        body { background-color: #f4f7f6; }
        .feature-card { transition: transform 0.2s; border: none; border-radius: 15px; }
        .feature-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
        .icon-circle { width: 60px; height: 60px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-bottom: 20px; }
    </style>
</head>
<body>

<nav class="navbar navbar-dark bg-primary shadow-sm mb-5">
    <div class="container">
        <span class="navbar-brand fw-bold"><i class="bi bi-airplane-engines"></i> Flight System</span>
        <div class="d-flex align-items-center">
            <span class="navbar-text me-3 text-white">Hello, <strong>${sessionScope.userEmail}</strong></span>
            <a href="logoutServlet" class="btn btn-sm btn-outline-light">Logout</a>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row mb-4">
        <div class="col">
            <h2 class="fw-bold">User Dashboard</h2>
            <p class="text-muted">Welcome to your flight management portal. What would you like to do today?</p>
        </div>
    </div>

    <div class="row g-4">
        <div class="col-md-4">
            <div class="card h-100 p-4 feature-card shadow-sm">
                <div class="icon-circle bg-success text-white"><i class="bi bi-search fs-3"></i></div>
                <h4>Find Flights</h4>
                <p class="text-muted">Search for flight connections, compare prices, and book your next trip.</p>
                <div class="mt-auto">
                    <a href="searchFlights.jsp" class="btn btn-success w-100">Search & Book</a>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card h-100 p-4 feature-card shadow-sm">
                <div class="icon-circle bg-warning text-white"><i class="bi bi-ticket-perforated fs-3"></i></div>
                <h4>My Bookings</h4>
                <p class="text-muted">Browse your current flight reservations, view details, or cancel bookings.</p>
                <div class="mt-auto">
                    <a href="ManageBookings" class="btn btn-warning w-100 text-white">Manage Bookings</a>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card h-100 p-4 feature-card shadow-sm">
                <div class="icon-circle bg-primary text-white"><i class="bi bi-credit-card-2-back fs-3"></i></div>
                <h4>Profile & Payment</h4>
                <p class="text-muted">Modify your saved addresses and register or delete your credit cards.</p>
                <div class="mt-auto">
                    <a href="ManageProfile" class="btn btn-primary w-100">Account Settings</a>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>