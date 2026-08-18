<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%
    List myAddresses = (List) request.getAttribute("myAddresses");
    List myCards = (List) request.getAttribute("myCards");
    if (myAddresses == null && session.getAttribute("userEmail") != null) {
        response.sendRedirect("ManageProfile");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Profile | Flight System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4 shadow-sm">
    <div class="container"><a class="navbar-brand fw-bold" href="dashboard.jsp"><i class="bi bi-arrow-left"></i> Back to Dashboard</a></div>
</nav>

<div class="container">
    <h2 class="mb-4 fw-bold">Manage Addresses & Payments</h2>

    <%-- Notifications --%>
    <% if (request.getParameter("success") != null) { %>
        <div class="alert alert-success alert-dismissible fade show">Action successful! <button class="btn-close" data-bs-dismiss="alert"></button></div>
    <% } %>
    <% if ("AddressInUse".equals(request.getParameter("error"))) { %>
        <div class="alert alert-danger alert-dismissible fade show">Cannot delete address: It is linked to a credit card. <button class="btn-close" data-bs-dismiss="alert"></button></div>
    <% } %>

    <div class="row">
        <div class="col-md-6">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white"><strong>Your Addresses</strong></div>
                <div class="card-body">
                    <ul class="list-group mb-3">
                    <% if (myAddresses != null) { for (Object obj : myAddresses) {
                        int id = (Integer) obj.getClass().getMethod("getAddressId").invoke(obj);
                        String street = String.valueOf(obj.getClass().getMethod("getStreet").invoke(obj));
                        String city = String.valueOf(obj.getClass().getMethod("getCity").invoke(obj));
                        String state = String.valueOf(obj.getClass().getMethod("getState").invoke(obj));
                        String zip = String.valueOf(obj.getClass().getMethod("getZipCode").invoke(obj));
                        String country = String.valueOf(obj.getClass().getMethod("getCountry").invoke(obj));
                    %>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <strong><%= street %></strong><br>
                                <small class="text-muted"><%= city %>, <%= state %> <%= zip %></small>
                            </div>
                            <div>
                                <%-- Pass ALL fields into the edit function --%>
                                <button class="btn btn-sm btn-outline-primary me-1"
                                        onclick="editAddress('<%= id %>', '<%= street %>', '<%= city %>', '<%= state %>', '<%= zip %>', '<%= country %>')">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <form action="ManageProfile" method="POST" style="display:inline;">
                                    <input type="hidden" name="action" value="deleteAddress">
                                    <input type="hidden" name="addressId" value="<%= id %>">
                                    <button type="submit" class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
                                </form>
                            </div>
                        </li>
                    <% } } %>
                    </ul>
                    <hr>
                    <h6 id="addrFormTitle">Add New Address</h6>
                    <form action="ManageProfile" method="POST" id="addressForm">
                        <input type="hidden" name="action" id="addrAction" value="addAddress">
                        <input type="hidden" name="addressId" id="editAddrId">

                        <input type="text" name="street" id="editStreet" class="form-control mb-2" placeholder="Street" required>
                        <div class="row g-2 mb-2">
                            <div class="col-6"><input type="text" name="city" id="editCity" class="form-control" placeholder="City" required></div>
                            <div class="col-3"><input type="text" name="state" id="editState" class="form-control" placeholder="ST" required></div>
                            <div class="col-3"><input type="text" name="zipCode" id="editZip" class="form-control" placeholder="Zip" required></div>
                        </div>
                        <input type="text" name="country" id="editCountry" class="form-control mb-3" placeholder="Country" required>

                        <button type="submit" class="btn btn-primary w-100" id="addrBtn">Save Address</button>
                        <button type="button" class="btn btn-link w-100 mt-1 d-none" id="cancelAddr" onclick="resetAddrForm()">Cancel Edit</button>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white"><strong>Saved Cards</strong></div>
                <div class="card-body">
                    <ul class="list-group mb-3">
                    <% if (myCards != null) { for (Object obj : myCards) { Object[] card = (Object[]) obj; %>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>****<%= card[0].toString().substring(12) %> <small class="text-muted">(<%= card[3] %>)</small></div>
                            <form action="ManageProfile" method="POST" style="display:inline;">
                                <input type="hidden" name="action" value="deleteCard">
                                <input type="hidden" name="cardNumber" value="<%= card[0] %>">
                                <button type="submit" class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
                            </form>
                        </li>
                    <% } } %>
                    </ul>
                    <hr>
                    <h6>Add New Credit Card</h6>
                    <form action="ManageProfile" method="POST">
                        <input type="hidden" name="action" value="addCard">
                        <input type="text" name="cardNumber" class="form-control mb-2" placeholder="Card Number" maxlength="16" required>
                        <input type="text" name="cardHolder" class="form-control mb-2" placeholder="Cardholder Name" required>
                        <input type="text" name="expDate" class="form-control mb-2" placeholder="MM/YY" maxlength="5" required>
                        <select name="billingAddressId" class="form-select mb-3" required>
                            <option value="">-- Select Billing Address --</option>
                            <% if (myAddresses != null) { for (Object obj : myAddresses) {
                                int id = (Integer) obj.getClass().getMethod("getAddressId").invoke(obj);
                                String street = String.valueOf(obj.getClass().getMethod("getStreet").invoke(obj));
                            %>
                                <option value="<%= id %>"><%= street %></option>
                            <% } } %>
                        </select>
                        <button type="submit" class="btn btn-success w-100">Register Card</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function editAddress(id, street, city, state, zip, country) {
        document.getElementById('addrFormTitle').innerText = "Modify Address";
        document.getElementById('addrAction').value = "modifyAddress";
        document.getElementById('editAddrId').value = id;

        // Fill all fields
        document.getElementById('editStreet').value = street;
        document.getElementById('editCity').value = city;
        document.getElementById('editState').value = state;
        document.getElementById('editZip').value = zip;
        document.getElementById('editCountry').value = country;

        document.getElementById('addrBtn').innerText = "Update Address";
        document.getElementById('addrBtn').className = "btn btn-warning w-100";
        document.getElementById('cancelAddr').classList.remove('d-none');

        // Scroll to form smoothly
        document.getElementById('addressForm').scrollIntoView({ behavior: 'smooth' });
    }

    function resetAddrForm() {
        document.getElementById('addrFormTitle').innerText = "Add New Address";
        document.getElementById('addrAction').value = "addAddress";
        document.getElementById('addressForm').reset();
        document.getElementById('addrBtn').innerText = "Save Address";
        document.getElementById('addrBtn').className = "btn btn-primary w-100";
        document.getElementById('cancelAddr').classList.add('d-none');
    }
</script>
</body>
</html>