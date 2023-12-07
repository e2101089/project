package e2101089.java.server;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
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
            conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101089_java_demo", "e2101089", "HACAzsRBjXq");
            System.out.println(conn);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 String studentIdParam = request.getParameter("studentId"); // Assuming studentId is passed as a parameter
    	 System.out.println(studentIdParam);
    	    try {
    	        int studentId = Integer.parseInt(studentIdParam); // Convert the parameter to an integer

    	        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Students WHERE student_id = ?");
    	        ps.setInt(1, studentId);
    	        ResultSet rs = ps.executeQuery();

    	        if (rs.next()) {
    	            // Retrieve data for the student with the given ID
    	            int id = rs.getInt("student_id");
    	            String name = rs.getString("name");
    	            String email = rs.getString("email");
                    RequestDispatcher reqDis = request.getRequestDispatcher("StudentList");;                    // Display student details in the servlet's response
                    request.setAttribute("student_id", studentId);
                    request.setAttribute("name", name);
                    request.setAttribute("email", email);
                    reqDis.forward(request, response);
                
                    // Display other details as needed
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().println("Student details not found");
                }
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error retrieving student details");
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