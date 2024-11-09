package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Database connection parameters
        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "password";

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

        // Validate input fields
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            response.getWriter().println("Username and password are required.");
            return;
        }

        // Connect to the database and check credentials
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT id, role FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Retrieve the user_id and role from the result set
                int userId = rs.getInt("id");
                String role = rs.getString("role");

                // Store user information in session
                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId); // Store user ID
                session.setAttribute("username", username);
                session.setAttribute("role", role);

                // Redirect based on user role
                switch (role) {
                    case "Employee":
                        response.sendRedirect("requestAccess.jsp");
                        break;
                    case "Manager":
                        response.sendRedirect("pendingRequests.jsp");
                        break;
                    case "Admin":
                        response.sendRedirect("createSoftware.jsp");
                        break;
                    default:
                        response.getWriter().println("Invalid role.");
                        break;
                }
            } else {
                // Redirect to login page with an error message if authentication fails
                response.sendRedirect("login.jsp?error=Invalid credentials");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error during login: " + e.getMessage());
        }
    }
}
