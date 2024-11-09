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


public class SoftwareServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String accessLevel = request.getParameter("access_level");

        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "password";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("createSoftware.jsp?message=Error loading JDBC driver.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String sql = "INSERT INTO software (name, description, access_levels) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, description);
                stmt.setString(3, accessLevel);
                stmt.executeUpdate();
                response.sendRedirect("createSoftware.jsp?message=Software created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getSQLState().equals("23505")) { // Duplicate entry error for unique constraint
                response.sendRedirect("createSoftware.jsp?message=Error: Software name already exists.");
            } else {
                response.sendRedirect("createSoftware.jsp?message=Error during software creation.");
            }
        }
    }
}
