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

@WebServlet(name = "SignUpServlet", urlPatterns = {"/SignUpServlet"})
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Database connection parameters
        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres"; // Update with your database name if different
        String dbUser = "postgres"; // Replace with your database username
        String dbPassword = "password"; // Replace with your actual password
        
        // Load the PostgreSQL JDBC driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error loading JDBC driver: " + e.getMessage());
            return; // Stop further execution if driver isn't found
        }
        
        // Retrieve form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = "Employee"; // Default role assigned on sign-up

        // Validate input fields
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            response.getWriter().println("Username and password are required.");
            return;
        }

        // Connect to the database and insert user data
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();

            // Redirect to login page after successful registration
            response.sendRedirect("login.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("duplicate key value")) {
                response.getWriter().println("Username already exists. Please choose a different one.");
            } else {
                response.getWriter().println("Error during sign-up: " + e.getMessage());
            }
        }
    }
}
