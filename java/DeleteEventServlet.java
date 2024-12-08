import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class DeleteEventServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jdbcURL = "jdbc:mysql://localhost:3306/event";
        String jdbcUsername = "root";
        String jdbcPassword = "password";

        int eventId = Integer.parseInt(request.getParameter("eventId"));

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)) {
            String sql = "DELETE FROM events WHERE event_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, eventId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                response.sendRedirect("FetchEvents.jsp?status=deleted");
            } else {
                response.sendRedirect("event_list.jsp?status=error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("event_list.jsp?status=error");
        }
    }
}
