import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.sql.*;

@WebServlet("/UnifiedLoginServlet")
public class UnifiedLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String dbUrl = "jdbc:mysql://localhost:3306/event";
        String dbUser = "root";
        String dbPass = "password"; // Replace with your database password

        response.setContentType("text/html");

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Query to check both tables for matching username, password, and role
            String query = """
                    SELECT role FROM (
                        SELECT username, password, role FROM users
                        UNION
                        SELECT username, password, role FROM admin
                    ) AS combined
                    WHERE username = ? AND password = ?;
                    """;

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // Set query parameters
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");

                    // Redirect based on role
                    if ("admin".equalsIgnoreCase(role)) {
                        response.sendRedirect("admin_dashboard.html"); // Redirect to admin dashboard
                    } else if ("student".equalsIgnoreCase(role)) {
                        response.sendRedirect("Events.html"); // Redirect to events page
                    } else {
                        response.getWriter().println("<script>alert('Invalid role specified.'); window.location='login.html';</script>");
                    }
                } else {
                    // Invalid credentials
                    response.getWriter().println("<script>alert('Invalid credentials.'); window.location='login.html';</script>");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Handle database connection errors
            response.getWriter().println("<script>alert('Database connection error: " + e.getMessage() + "'); window.location='login.html';</script>");
        }
    }
}
