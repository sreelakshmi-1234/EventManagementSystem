import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/AddEventServlet")
public class AddEventServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = "";
        
        // Get the event details from the request
        String eventName = request.getParameter("eventName");
        String eventDate = request.getParameter("eventDate");
        String eventTime = request.getParameter("eventTime");

        // Check if all required fields are provided
        if (eventName != null && !eventName.isEmpty() && eventDate != null && !eventDate.isEmpty() && eventTime != null && !eventTime.isEmpty()) {
            // Connect to the database and insert the new event
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/event", "root", "password")) {
                // SQL query to insert event into the database
                String query = "INSERT INTO events (event_name, date_of_event, event_time) VALUES (?, ?, ?)";
                
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    // Set the parameters for the query
                    stmt.setString(1, eventName);
                    stmt.setString(2, eventDate);
                    stmt.setString(3, eventTime);

                    // Execute the query
                    int rowsAffected = stmt.executeUpdate();

                    // Check if the event was successfully added
                    if (rowsAffected > 0) {
                        message = "Event added successfully!";
                    } else {
                        message = "Error: Event could not be added.";
                    }
                }
            } catch (SQLException e) {
                // Handle any database errors
                message = "Database error: " + e.getMessage();
            }
        } else {
            // Handle the case where fields are missing
            message = "All fields are required.";
        }

        // Set the message as an attribute to be accessed in the JSP page
        request.setAttribute("message", message);
        
        // Redirect back to FetchEvents.jsp to show the success or error message
        response.sendRedirect("FetchEvents.jsp");
    }
}
