import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;

@WebServlet("/userProfiles")  // Servlet URL pattern
public class UserProfileServlet extends HttpServlet {

    // JDBC URL, username, and password for MySQL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/event";  // Replace with your DB name
    private static final String DB_USER = "root";  // Replace with your DB username
    private static final String DB_PASSWORD = "password";  // Replace with your DB password

    // JDBC connection object
    private Connection connection;

    @Override
    public void init() throws ServletException {
        // Initialize the JDBC connection
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Database connection error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the response content type to JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Get the username parameter from the request
        String username = request.getParameter("username");
        
        if (username != null && !username.trim().isEmpty()) {
            // SQL query to get the user details by username
            String query = "SELECT id, user_id, username, email, password, confirm_password, role FROM users WHERE username = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("id");
                    String userId = rs.getString("user_id");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String confirmPassword = rs.getString("confirm_password");
                    String role = rs.getString("role");

                    // Create JSON response with user details
                    String jsonResponse = String.format(
                            "{\"user_id\": \"%s\", \"username\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"confirm_password\": \"%s\", \"role\": \"%s\"}",
                            userId, username, email, password, confirmPassword, role);

                    out.write(jsonResponse);
                } else {
                    out.write("{\"error\": \"User not found\"}");
                }
            } catch (SQLException e) {
                out.write("{\"error\": \"Database error\"}");
            }
        } else {
            out.write("{\"error\": \"Username is required\"}");
        }

        out.close();
    }

    @Override
    public void destroy() {
        // Close the database connection when the servlet is destroyed
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
