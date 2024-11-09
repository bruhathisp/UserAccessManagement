package servlets;

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
import jakarta.servlet.http.HttpSession;

@WebServlet("/SoftwareListServlet")
public class SoftwareListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if the user is logged in and has the "Employee" role
        if (session == null || !"Employee".equals(session.getAttribute("role"))) {
            System.out.println("Unauthorized access or missing session.");
            request.setAttribute("errorMessage", "Unauthorized access. Please log in as an Employee.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        List<String> softwareList = new ArrayList<>();
        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "password";

        // Retrieve software data
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            System.out.println("Database connection established.");
            String query = "SELECT name FROM software";
            try (PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String softwareName = rs.getString("name");
                    softwareList.add(softwareName);
                    System.out.println("Fetched software: " + softwareName);
                }
            }
            System.out.println("Software list size: " + softwareList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error fetching software list: " + e.getMessage());
            request.getRequestDispatcher("requestAccess.jsp").forward(request, response);
            return;
        }

        // Set the software list as a request attribute and forward to requestAccess.jsp
        request.setAttribute("softwareList", softwareList);
        request.getRequestDispatcher("requestAccess.jsp").forward(request, response);
    }
}
