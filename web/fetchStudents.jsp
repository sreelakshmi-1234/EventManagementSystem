<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<table class="table table-hover table-striped table-bordered">
    <thead class="table-dark">
        <tr>
            <th>Name</th>
            <th>User ID</th>
            <th>Course</th>
            <th>Email</th>
            <th>Contact</th>
        </tr>
    </thead>
    <tbody>
        <%
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/event", "root", "password");
                String sql = "SELECT name, user_id, course, email, contact FROM students";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getString("name") + "</td>");
                    out.println("<td>" + rs.getString("user_id") + "</td>");
                    out.println("<td>" + rs.getString("course") + "</td>");
                    out.println("<td>" + rs.getString("email") + "</td>");
                    out.println("<td>" + rs.getString("contact") + "</td>");
                    out.println("</tr>");
                }
                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                out.println("<tr><td colspan='5'>Error: " + e.getMessage() + "</td></tr>");
            }
        %>
    </tbody>
</table>
