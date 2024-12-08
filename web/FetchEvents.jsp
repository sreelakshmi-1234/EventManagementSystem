<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.sql.*" %>
<%@ page session="true" %>
<%
    // Database credentials
    String jdbcURL = "jdbc:mysql://localhost:3306/event";
    String jdbcUsername = "root";
    String jdbcPassword = "password";

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    // Get the status from request (if available)
    String status = request.getParameter("status");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Event List</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container my-4">
    <!-- Back to Dashboard Button -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <a href="admin_dashboard.html" class="btn btn-secondary">Back to Dashboard</a>
        <!-- Add Event Button -->
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addEventModal">Add Event</button>
    </div>

    <!-- Display Success/Error Messages -->
    <%
        if (status != null) {
            String message = "";
            String alertType = "";

            switch (status) {
                case "success":
                    message = "Event added successfully!";
                    alertType = "success";
                    break;
                case "error":
                    message = "Failed to update the event. Please try again.";
                    alertType = "danger";
                    break;
                case "deleted":
                    message = "Event deleted successfully!";
                    alertType = "success";
                    break;
                case "updated":
                    message = "Event updated successfully!";
                    alertType = "success";
                    break;
                default:
                    message = "An unknown status was received.";
                    alertType = "warning";
                    break;
            }
    %>
        <div class="alert alert-<%= alertType %> alert-dismissible fade show" role="alert">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

    <!-- Events Table -->
    <%
        try {
            // Establish database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            String sql = "SELECT event_id, event_name, date_of_event, event_time FROM events";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
    %>
    <table class="table table-hover table-striped table-bordered">
        <thead class="table-dark">
            <tr>
                <th>Event ID</th>
                <th>Event Name</th>
                <th>Date</th>
                <th>Time</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% 
                while (resultSet.next()) { 
                    int eventId = resultSet.getInt("event_id");
                    String eventName = resultSet.getString("event_name");
                    java.sql.Date eventDate = resultSet.getDate("date_of_event");
                    java.sql.Time eventTime = resultSet.getTime("event_time");
            %>
            <tr>
                <td><%= eventId %></td>
                <td><%= eventName %></td>
                <td><%= eventDate %></td>
                <td><%= eventTime %></td>
                <td>
                    <!-- Update Button -->
                    <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#updateEventModal<%= eventId %>">
                        Update
                    </button>
                    <!-- Delete Button -->
                    <a href="DeleteEventServlet?eventId=<%= eventId %>" class="btn btn-danger btn-sm">Delete</a>
                </td>
            </tr>

            <!-- Modal for Updating Event -->
            <div class="modal fade" id="updateEventModal<%= eventId %>" tabindex="-1" aria-labelledby="updateEventModalLabel<%= eventId %>" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form action="UpdateEventServlet" method="post">
                            <div class="modal-header">
                                <h5 class="modal-title" id="updateEventModalLabel<%= eventId %>">Update Event</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <!-- Hidden Input for Event ID -->
                                <input type="hidden" name="eventId" value="<%= eventId %>">
                                <!-- Event Name -->
                                <div class="mb-3">
                                    <label for="eventName<%= eventId %>" class="form-label">Event Name</label>
                                    <input type="text" class="form-control" id="eventName<%= eventId %>" name="eventName" value="<%= eventName %>" required>
                                </div>
                                <!-- Event Date -->
                                <div class="mb-3">
                                    <label for="eventDate<%= eventId %>" class="form-label">Date</label>
                                    <input type="date" class="form-control" id="eventDate<%= eventId %>" name="eventDate" value="<%= eventDate %>" required>
                                </div>
                                <!-- Event Time -->
                                <div class="mb-3">
                                    <label for="eventTime<%= eventId %>" class="form-label">Time</label>
                                    <input type="time" class="form-control" id="eventTime<%= eventId %>" name="eventTime" value="<%= eventTime %>" required>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">Update Event</button>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <% } %>
        </tbody>
    </table>
    <%
        } catch (Exception e) {
            out.println("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
        } finally {
            // Clean up resources
            if (resultSet != null) try { resultSet.close(); } catch (SQLException ignored) {}
            if (statement != null) try { statement.close(); } catch (SQLException ignored) {}
            if (connection != null) try { connection.close(); } catch (SQLException ignored) {}
        }
    %>
</div>

<!-- Modal for Adding Event -->
<div class="modal fade" id="addEventModal" tabindex="-1" aria-labelledby="addEventModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="AddEventsServlet" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="addEventModalLabel">Add New Event</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <!-- Event Name -->
                    <div class="mb-3">
                        <label for="addEventName" class="form-label">Event Name</label>
                        <input type="text" class="form-control" id="addEventName" name="eventName" required>
                    </div>
                    <!-- Event Date -->
                    <div class="mb-3">
                        <label for="addEventDate" class="form-label">Date</label>
                        <input type="date" class="form-control" id="addEventDate" name="eventDate" required>
                    </div>
                    <!-- Event Time -->
                    <div class="mb-3">
                        <label for="addEventTime" class="form-label">Time</label>
                        <input type="time" class="form-control" id="addEventTime" name="eventTime" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Add Event</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
