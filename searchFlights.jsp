<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Find Flights | FlightConnect</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">

<nav class="navbar navbar-dark bg-primary mb-4 shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="dashboard.jsp">Flight System</a>
    </div>
</nav>

<div class="container">
    <div class="card shadow border-0 p-4 mb-4">
        <h3 class="fw-bold mb-4">Search Flight Connections</h3>

        <form action="FlightServlet" method="GET">
            <div class="row g-3">
                <div class="col-md-3">
                    <label class="form-label fw-bold">From</label>
                    <input type="text" name="dep" class="form-control" placeholder="e.g. ORD" required maxlength="3">
                </div>
                <div class="col-md-3">
                    <label class="form-label fw-bold">To </label>
                    <input type="text" name="dest" class="form-control" placeholder="e.g. JFK" required maxlength="3">
                </div>
                <div class="col-md-3">
                    <label class="form-label fw-bold">Departure Date</label>
                    <input type="date" name="depDate" class="form-control" required>
                </div>
                <div class="col-md-3">
                    <label class="form-label fw-bold">Return Date (Optional)</label>
                    <input type="date" name="retDate" class="form-control">
                </div>

                <div class="col-md-2">
                    <label class="form-label">Max Connections</label>
                    <select name="maxConnections" class="form-select">
                        <option value="0">Direct Only</option>
                        <option value="1">Up to 1 Stop</option>
                        <option value="2">Up to 2 Stops</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Max Price ($)</label>
                    <input type="number" name="maxPrice" class="form-control" placeholder="No limit">
                </div>
                <div class="col-md-2">
                    <label class="form-label">Max Length (Hrs)</label>
                    <input type="number" name="maxLength" class="form-control" placeholder="Any">
                </div>
                <div class="col-md-3">
                    <label class="form-label">Sort Results By</label>
                    <select name="sortBy" class="form-select">
                        <option value="price">Price (Low to High)</option>
                        <option value="duration">Trip Duration</option>
                    </select>
                </div>

                <div class="col-md-3 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100 fw-bold py-2">
                        <i class="bi bi-search"></i> Search Flights
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>