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
import jakarta.servlet.http.HttpSession;


public class RequestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Get user ID and form data
        int userId = (int) session.getAttribute("user_id"); // Assume user_id is stored in session
        String softwareName = request.getParameter("software_id");
        String accessType = request.getParameter("access_type");
        String reason = request.getParameter("reason");

        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "password";

        // Insert request into the requests table
        String sql = "INSERT INTO requests (user_id, software_id, access_type, reason, status) VALUES (?, (SELECT id FROM software WHERE name = ?), ?, ?, 'Pending')";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, softwareName); // Use software name to find software ID
            stmt.setString(3, accessType);
            stmt.setString(4, reason);
            stmt.executeUpdate();
            response.sendRedirect("requestAccess.jsp?message=Request+submitted+successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("requestAccess.jsp?error=Error+submitting+request");
        }
    }
}
