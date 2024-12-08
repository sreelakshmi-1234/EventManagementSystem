<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<%
    String userId = request.getParameter("userId");
    String errorMessage = null;

    try {
        // Load the JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/event", "root", "password");

        // Query to fetch user data excluding the password
        String sql = "SELECT user_id, username, email, role FROM users WHERE user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
%>
<div class="card" style="width: 18rem; margin: auto; margin-top: 20px;">
    <div class="card-header bg-primary text-white">
        User Profile
    </div>
    <ul class="list-group list-group-flush">
        <li class="list-group-item"><strong>User ID:</strong> <%= rs.getString("user_id") %></li>
        <li class="list-group-item"><strong>Username:</strong> <%= rs.getString("username") %></li>
        <li class="list-group-item"><strong>Email:</strong> <%= rs.getString("email") %></li>
        <li class="list-group-item"><strong>Role:</strong> <%= rs.getString("role") %></li>
    </ul>
</div>
<%
        } else {
            errorMessage = "User ID not found.";
        }

        // Close connections
        rs.close();
        pstmt.close();
        conn.close();
    } catch (Exception e) {
        errorMessage = "Error: " + e.getMessage();
    }

    if (errorMessage != null) {
%>
<div class="alert alert-danger" role="alert">
    <%= errorMessage %>
</div>
<%
    }
%>
