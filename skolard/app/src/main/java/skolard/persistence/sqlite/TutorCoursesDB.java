package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TutorCoursesDB {

    private final Connection connection;

    public TutorCoursesDB(Connection connection) {
        this.connection = connection;
    }

    public Map<String, Double> addCourse(String email, String course, Double grade) {
        String sql = "INSERT INTO tutorCourse (tutorEmail, courseID, grade) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, course);
            stmt.setDouble(3, grade);
            stmt.executeUpdate();
            return getTutorCourses(email);

        } catch (SQLException e) {
            throw new RuntimeException("Error adding course for tutor", e);
        }
    }

    public Map<String, Double> getTutorCourses(String email) {
        Map<String, Double> courses = new HashMap<>();
        String sql = "SELECT courseID, grade FROM tutorCourse WHERE tutorEmail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                courses.put(rs.getString("courseID"), 
                    (Double)rs.getDouble("grade"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tutor courses", e);
        }

        return courses;
    }

    
    public void deleteTutorCourse(String email, String course) {
        String sql = "DELETE FROM tutor WHERE email = ? AND courseID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, course);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting course", e);
        }
    }

    public void deleteAllTutorCourses(String email) {
        String sql = "DELETE FROM tutorCourse WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all the tutors courses", e);
        }
    }
}
