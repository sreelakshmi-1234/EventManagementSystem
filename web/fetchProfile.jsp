<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.sql.*" %>
<%
    String username = (String) session.getAttribute("username");  // Retrieve logged-in user's username from session
    String jsonResponse = "{}";  // Default empty JSON response

    if (username != null) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Establish connection to MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database_name", "your_db_user", "your_db_password");

            // Prepare SQL query to fetch user details based on the logged-in username
            String sql = "SELECT user_id, username, email, contact FROM event.users WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            // Process the result and generate JSON response
            if (rs.next()) {
                String userId = rs.getString("user_id");
                String userName = rs.getString("username");
                String email = rs.getString("email");
                String contact = rs.getString("contact");

                jsonResponse = "{";
                jsonResponse += "\"userId\": \"" + (userId != null ? userId : "") + "\",";
                jsonResponse += "\"username\": \"" + (userName != null ? userName : "") + "\",";
                jsonResponse += "\"email\": \"" + (email != null ? email : "") + "\",";
                jsonResponse += "\"contact\": \"" + (contact != null ? contact : "") + "\"";
                jsonResponse += "}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Return the JSON response
    response.setContentType("application/json");
    response.getWriter().write(jsonResponse);
%>
