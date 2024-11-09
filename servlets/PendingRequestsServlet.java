package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




@WebServlet("/PendingRequestsServlet")
public class PendingRequestsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "password");
             Statement stmt = conn.createStatement();
             PrintWriter out = response.getWriter()) {

            ResultSet rs = stmt.executeQuery("SELECT r.id, u.username, s.name, r.access_type, r.reason, r.status " +
                                             "FROM requests r " +
                                             "JOIN users u ON r.user_id = u.id " +
                                             "JOIN software s ON r.software_id = s.id " +
                                             "WHERE r.status = 'Pending'");

            out.println("<tr><th>Username</th><th>Software</th><th>Access Type</th><th>Reason</th><th>Action</th></tr>");
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("access_type") + "</td>");
                out.println("<td>" + rs.getString("reason") + "</td>");
                out.println("<td><button onclick=\"updateRequest(" + rs.getInt("id") + ", 'Approve')\">Approve</button>");
                out.println("<button onclick=\"updateRequest(" + rs.getInt("id") + ", 'Reject')\">Reject</button></td>");
                out.println("</tr>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
