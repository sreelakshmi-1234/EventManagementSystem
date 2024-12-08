import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form data from request
        String userId = request.getParameter("user_id");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password"); // Only used for client-side validation
        String role = request.getParameter("role");

        // Check if password or confirmPassword is null or empty
        if (password == null || confirmPassword == null || password.isEmpty() || confirmPassword.isEmpty()) {
            response.getWriter().println("Password fields cannot be empty. Please try again.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            response.getWriter().println("Passwords do not match. Please try again.");
            return;
        }

        // Database connection
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection to the database
            String url = "jdbc:mysql://localhost:3306/event"; // Database URL
            String dbUsername = "root";  // Database username
            String dbPassword = "password";  // Database password

            con = DriverManager.getConnection(url, dbUsername, dbPassword);

            // SQL statement to insert the new user, excluding confirm_password
            String sql = "INSERT INTO users (user_id, username, email, password, role) VALUES (?, ?, ?, ?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password); // Store password only
            stmt.setString(5, role);

            // Execute update
            int rows = stmt.executeUpdate();
            System.out.println("Rows affected: " + rows);

            if (rows > 0) {
                response.sendRedirect("sucess.html"); // Redirect to success page
            } else {
                response.sendRedirect("failure.html"); // Redirect to failure page
            }
        } catch (ClassNotFoundException e) {
            // Handle exception if JDBC driver is not found
            e.printStackTrace();
            response.getWriter().println("Database driver not found.");
        } catch (SQLException e) {
            // Handle database connection error
            e.printStackTrace();  // Print the stack trace for debugging
            response.getWriter().println("Database connection error: " + e.getMessage()); // Display actual error message
        } finally {
            // Close resources if explicitly required
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
