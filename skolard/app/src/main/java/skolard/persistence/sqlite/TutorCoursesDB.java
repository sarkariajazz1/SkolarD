package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles database operations related to tutors' courses and their grades.
 * Supports adding, retrieving, and deleting course-grade records for tutors.
 */
public class TutorCoursesDB {

    private final Connection connection;

    /**
     * Constructs the TutorCoursesDB with a database connection.
     * 
     * @param connection the SQLite database connection
     */
    public TutorCoursesDB(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a course with a grade for the tutor identified by email.
     * After adding, returns the updated map of all courses and grades for the tutor.
     * 
     * @param email the tutor's email
     * @param course the course identifier
     * @param grade the grade for the course
     * @return a map of course IDs to grades for the tutor
     */
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

    /**
     * Retrieves all courses and their corresponding grades for a tutor.
     * 
     * @param email the tutor's email
     * @return a map from course IDs to grades
     */
    public Map<String, Double> getTutorCourses(String email) {
        Map<String, Double> courses = new HashMap<>();
        String sql = "SELECT courseID, grade FROM tutorCourse WHERE tutorEmail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                courses.put(rs.getString("courseID"), 
                    rs.getDouble("grade"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tutor courses", e);
        }

        return courses;
    }

    /**
     * Deletes a specific course from the tutor's courses.
     * 
     * @param email the tutor's email
     * @param course the course ID to delete
     */
    public void deleteTutorCourse(String email, String course) {
        String sql = "DELETE FROM tutorCourse WHERE tutorEmail = ? AND courseID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, course);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting course", e);
        }
    }

    /**
     * Deletes all courses associated with a tutor.
     * 
     * @param email the tutor's email
     */
    public void deleteAllTutorCourses(String email) {
        String sql = "DELETE FROM tutorCourse WHERE tutorEmail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all the tutors courses", e);
        }
    }
}

