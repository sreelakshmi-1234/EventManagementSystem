import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class UpdateEventServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jdbcURL = "jdbc:mysql://localhost:3306/event";
        String jdbcUsername = "root";
        String jdbcPassword = "password";

        // Log incoming values
        System.out.println("Update Event Servlet triggered.");

        int eventId = Integer.parseInt(request.getParameter("eventId"));
        String eventName = request.getParameter("eventName");
        String eventDate = request.getParameter("eventDate");
        String eventTime = request.getParameter("eventTime");

        System.out.println("Received Event ID: " + eventId);
        System.out.println("Received Event Name: " + eventName);
        System.out.println("Received Event Date: " + eventDate);
        System.out.println("Received Event Time: " + eventTime);

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)) {
            String sql = "UPDATE events SET event_name = ?, date_of_event = ?, event_time = ? WHERE event_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, eventName);
            statement.setDate(2, Date.valueOf(eventDate));
            statement.setTime(3, Time.valueOf(eventTime));
            statement.setInt(4, eventId);

            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows Updated: " + rowsUpdated);

            if (rowsUpdated > 0) {
                response.sendRedirect("FetchEvents.jsp?status=updated");
            } else {
                response.sendRedirect("FetchEvents.jsp?status=error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("FetchEvents.jsp?status=error");
        }
    }
}
