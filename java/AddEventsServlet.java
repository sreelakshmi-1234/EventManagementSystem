import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AddEventServlet")
public class AddEventsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String eventName = request.getParameter("eventName");
        String eventDate = request.getParameter("eventDate");
        String eventTime = request.getParameter("eventTime");

        String jdbcURL = "jdbc:mysql://localhost:3306/event";
        String jdbcUsername = "root";
        String jdbcPassword = "password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            String sql = "INSERT INTO events (event_name, date_of_event, event_time) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, eventName);
            statement.setString(2, eventDate);
            statement.setString(3, eventTime);

            statement.executeUpdate();
            connection.close();

            response.sendRedirect("FetchEvents.jsp?status=success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FetchEvents.jsp?status=error");
        }
    }
}
