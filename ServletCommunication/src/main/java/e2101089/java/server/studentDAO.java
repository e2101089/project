package e2101089.java.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class studentDAO {
    private String jdbcURL =  "jdbc:mariadb://mariadb.vamk.fi/e2101089_java_demo";
    private String jdbcUserName = "e2101089";
    private String jdbcPassword = "HACAzsRBjXq";

    // Constructors
    public studentDAO(String url, String userName, String password) {
        this.jdbcURL = url;
        this.jdbcUserName = userName;
        this.jdbcPassword = password;
    }

    public studentDAO() {
    }

    private static final String SELECT_ALL_STUDENTS_QUERY = "SELECT * FROM Students";
    private static final String SELECT_STUDENT_BY_ID = "SELECT * FROM Students WHERE student_id=?";
   

    protected Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public List<Student> selectAllStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM Students");) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int student_id = rs.getInt(1);
                String name = rs.getString(3);
                String email = rs.getString(4);

                students.add(new Student(student_id, email, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public Student selectStudentByID(int student_id) {
        Student student = null;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_STUDENT_BY_ID);) {
            ps.setInt(1, student_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                
                String name = rs.getString("Name");
                String email = rs.getString("email");

                student = new Student(student_id, name, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }
}