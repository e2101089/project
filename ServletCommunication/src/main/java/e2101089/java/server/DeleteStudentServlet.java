package e2101089.java.server;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/deleteStudentServlet")
public class DeleteStudentServlet extends HttpServlet {

    private Connection connection;
    private PreparedStatement statement;

    @Override
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101089_java_demo", "e2101089", "HACAzsRBjXq");
            statement = connection.prepareStatement("DELETE FROM Students WHERE student_id = ? AND name = ? AND email = ?");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String student_id = request.getParameter("student_id");
    	String name = request.getParameter("name");
    	String email = request.getParameter("email"); // Corrected parameter name
       
        try {
			statement.setString(1, student_id);
            statement.setString(2, name);
            statement.setString(3, email);

            int result = statement.executeUpdate();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print("<b>" + result + " student deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}