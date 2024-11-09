package servlets;

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


public class ApprovalServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Database connection parameters
        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "password";

        // Retrieve request parameters
        int requestId = Integer.parseInt(request.getParameter("request_id"));
        String action = request.getParameter("action");

        // Determine the new status based on the action
        String newStatus;
        if ("Approve".equals(action)) {
            newStatus = "Approved";
        } else if ("Reject".equals(action)) {
            newStatus = "Rejected";
        } else {
            response.sendRedirect("pendingRequests.jsp?error=Invalid+action");
            return;
        }

        // Update the status of the request in the database
        String sql = "UPDATE requests SET status = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();

            // Redirect back to the pendingRequests.jsp with a success message
            response.sendRedirect("pendingRequests.jsp?message=Request+has+been+" + newStatus.toLowerCase());
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("pendingRequests.jsp?error=Error+processing+request");
        }
    }
}
