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
            PreparedStatement ps = conn.prepareStatement("SELECT password FROM Students WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString("password");
                // Use a secure method to compare hashed passwords (e.g., BCrypt)
                // Replace this comparison with your actual password comparison method
                if (isPasswordCorrect(password, hashedPasswordFromDB)) {
                    // Authentication successful, create a session and redirect
                    request.getSession().setAttribute("email", email);
                    response.sendRedirect("/students"); // Redirect to students' page
                    return;
                } else {
                    // Incorrect password
                    response.sendRedirect("/login.html?error=incorrect_password");
                    return;
                }
            } else {
                // Email not found in the database
                response.sendRedirect("/login.html?error=email_not_found");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Authentication failed due to an unknown reason
        response.sendRedirect("/login.html?error=authentication_failed");
    }

    // Implement a secure method to compare hashed passwords
    private boolean isPasswordCorrect(String enteredPassword, String hashedPasswordFromDB) {
        // Implement your password comparison logic here (e.g., using a library like BCrypt)
        // Return true if passwords match; otherwise, return false
        // Example: return BCrypt.checkpw(enteredPassword, hashedPasswordFromDB);
        // Replace this with your actual password comparison logic
        return enteredPassword.equals(hashedPasswordFromDB);
    }
}