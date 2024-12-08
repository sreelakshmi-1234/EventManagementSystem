import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/EventRegisterServlet")
public class EventRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String userId = request.getParameter("user_id");
        String course = request.getParameter("course");
        String email = request.getParameter("email");
        String contact = request.getParameter("contact");

        String dbURL = "jdbc:mysql://localhost:3306/event";
        String dbUser = "root";
        String dbPassword = "password";

        try {
            // Explicitly load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
                 PreparedStatement stmt = con.prepareStatement(
                         "INSERT INTO students (name, user_id, course, email, contact) VALUES (?, ?, ?, ?, ?)")) {

                stmt.setString(1, name);
                stmt.setString(2, userId);
                stmt.setString(3, course);
                stmt.setString(4, email);
                stmt.setString(5, contact);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    // Retrieve the updated students list
                    List<Student> students = getStudents(con);
                    request.setAttribute("students", students);
                    // Forward to JSP for displaying as cards
                    request.getRequestDispatcher("student_dashboard.jsp").forward(request, response);
                } else {
                    response.sendRedirect("failure.html");
                }
            }
        } catch (ClassNotFoundException e) {
            response.getWriter().println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            response.getWriter().println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Student> getStudents(Connection con) throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT name, user_id, course, email, contact FROM students";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Student student = new Student(
                        rs.getString("name"),
                        rs.getString("user_id"),
                        rs.getString("course"),
                        rs.getString("email"),
                        rs.getString("contact")
                );
                students.add(student);
            }
        }
        return students;
    }

    public static class Student {
        private String name;
        private String userId;
        private String course;
        private String email;
        private String contact;

        public Student(String name, String userId, String course, String email, String contact) {
            this.name = name;
            this.userId = userId;
            this.course = course;
            this.email = email;
            this.contact = contact;
        }

        public String getName() { return name; }
        public String getUserId() { return userId; }
        public String getCourse() { return course; }
        public String getEmail() { return email; }
        public String getContact() { return contact; }
    }
}
