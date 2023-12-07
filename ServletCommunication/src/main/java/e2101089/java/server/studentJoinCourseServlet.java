package e2101089.java.server;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/studentJoinCourseServlet")
public class studentJoinCourseServlet extends HttpServlet {
    private Connection conn;
    private PreparedStatement ps;

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

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	String studentIdParam = req.getParameter("studentId");// Assuming studentId is passed as a parameter
    	String courseIdParam = req.getParameter("courseId"); // Assuming studentId is passed as a parameter
   	 	System.out.println(studentIdParam);
    	System.out.println(courseIdParam);
        try {
        	int studentId = Integer.parseInt(studentIdParam); 
        	int courseId = Integer.parseInt(courseIdParam);  
        	PreparedStatement ps = conn.prepareStatement("INSERT INTO Student_Course (student_id, course_id) VALUES(?, ?)");
        	ps.setInt(1,studentId);
        	ps.setInt(2,courseId);
        	int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                // Assuming student_id and course_id are integers 
            	res.setStatus(HttpServletResponse.SC_FOUND);
                res.getWriter().println("success to join course");
            	
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.getWriter().println("fail to join course");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void destroy() {
        try {
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
