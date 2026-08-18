<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Flight System | Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .login-container { max-width: 400px; margin-top: 100px; }
    </style>
</head>
<body>

<div class="container d-flex justify-content-center">
    <div class="login-container w-100">
        <div class="card shadow-sm border-0">
            <div class="card-body p-5">
                <h2 class="text-center mb-4 fw-bold text-primary">Flight Portal</h2>
                <p class="text-muted text-center mb-4">Please enter your details to login</p>

                <form action="loginServlet" method="POST">
                    <div class="mb-3">
                        <label class="form-label">Email Address</label>
                        <input type="email" name="email" class="form-control" placeholder="name@example.com" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Password</label>
                        <input type="password" name="password" class="form-control" placeholder="••••••••" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100 py-2 mb-3">Login</button>
                </form>

                <hr>

                <div class="text-center">
                    <p class="small text-muted mb-2">New to our flight system?</p>
                    <a href="register.jsp" class="btn btn-outline-secondary w-100">Create an Account</a>
                </div>
            </div>
        </div>
        <p class="text-center mt-4 text-muted small">&copy; 2026 Flight Application System</p>
    </div>
</div>

</body>
</html>