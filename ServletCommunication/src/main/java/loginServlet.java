 import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {

    private Connection conn;

    @Override
    public void init() throws ServletException {
        // Establish database connection in the init method
        try {
            Class.forName("org.mariadb.jdbc.Driver"); // Replace with your database driver
            conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101089_java_demo","e2101089","yeBvxVDHGWV");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Create a prepared statement to query the database for the hashed password
            PreparedStatement ps = conn.prepareStatement("SELECT password FROM Students WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString("password");
                // Compare hashed password from database with entered password
                if (hashedPasswordFromDB.equals(password)) {
                    // Authentication successful, create a session and redirect
                    request.getSession().setAttribute("email", email);
                    response.sendRedirect("/students"); // Redirect to students' page
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Incorrect credentials, redirect back to login page with an error parameter
        response.sendRedirect("/login.html?error=1");
    }

    @Override
    public void destroy() {
        // Close database connection in the destroy method
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
