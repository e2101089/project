import jakarta.servlet.http.HttpServlet;
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

@WebServlet("/students/*")
public class StudentServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101089_java_demo", "e2101089", "yeBvxVDHGWV");
            System.out.println(conn);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            try {
                String pathInfo = request.getPathInfo();
                if (pathInfo != null) {
                    String[] pathParts = pathInfo.split("/");
                    if (pathParts.length == 2) {
                        Long studentId = Long.parseLong(pathParts[1]);
                        // Logic to fetch student details by ID
                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM students WHERE id = ?");
                        ps.setLong(1, studentId);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            // Retrieve student details and return as JSON or HTML response
                            response.getWriter().println("Student details for ID: " + studentId);
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().println("Student not found");
                        }
                        ps.close();
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().println("Invalid URL");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("Invalid URL");
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error retrieving student details");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Unauthorized access");
        }
        
    }

    // Similar logic for doPost and doDelete methods...

    @Override
    public void destroy() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}