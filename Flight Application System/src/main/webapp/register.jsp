<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Flight System | Register</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .register-container { max-width: 450px; margin-top: 50px; }
    </style>
</head>
<body>

<div class="container d-flex justify-content-center">
    <div class="register-container w-100">
        <div class="card shadow-sm border-0">
            <div class="card-body p-5">
                <h2 class="text-center mb-4 fw-bold text-primary">Create Account</h2>

                <form action="registerServlet" method="POST">
                    <div class="mb-3">
                        <label class="form-label">Email Address</label>
                        <input type="email" name="email" class="form-control" required>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">First Name</label>
                            <input type="text" name="firstName" class="form-control" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Last Name</label>
                            <input type="text" name="lastName" class="form-control" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Home Airport Code</label>
                        <input type="text" name="airport" class="form-control" placeholder="e.g. ORD" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Password</label>
                        <input type="password" name="password" class="form-control" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100 py-2 mb-3">Register Account</button>
                </form>

                <hr>

                <div class="text-center">
                    <p class="small text-muted">Already have an account? <a href="index.jsp" class="text-decoration-none">Login here</a></p>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>