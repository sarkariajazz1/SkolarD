package skolard.persistence.sqlite;

import skolard.objects.Feedback;
import skolard.persistence.RatingPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDB implements RatingPersistence {
    private final Connection conn;

    public RatingDB(Connection conn) {
        this.conn = conn;
        createTableIfNeeded();
    }

    private void createTableIfNeeded() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS ratings (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    tutorId TEXT NOT NULL,
                    sessionId TEXT NOT NULL,
                    tutorRating INTEGER NOT NULL,
                    courseRating INTEGER NOT NULL,
                    studentId TEXT NOT NULL
                )
            """);
        } catch (SQLException e) {
            System.err.println("Failed to create ratings table: " + e.getMessage());
        }
    }

    @Override
    public void saveRating(String tutorId, String sessionId, int tutorRating, int courseRating, String studentId) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO ratings (tutorId, sessionId, tutorRating, courseRating, studentId) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, tutorId);
            ps.setString(2, sessionId);
            ps.setInt(3, tutorRating);
            ps.setInt(4, courseRating);
            ps.setString(5, studentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save rating: " + e.getMessage());
        }
    }

    @Override
    public List<Feedback> getAllFeedbackForTutor(String tutorId) {
        List<Feedback> feedbacks = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM ratings WHERE tutorId = ?")) {
            ps.setString(1, tutorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                feedbacks.add(extractFeedback(rs));
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve tutor feedback: " + e.getMessage());
        }
        return feedbacks;
    }

    @Override
    public List<Feedback> getAllFeedbackForCourse(String courseName) {
        List<Feedback> feedbacks = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM ratings WHERE sessionId = ?")) { // Adjust to courseName if schema supports it
            ps.setString(1, courseName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                feedbacks.add(extractFeedback(rs));
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve course feedback: " + e.getMessage());
        }
        return feedbacks;
    }

    private Feedback extractFeedback(ResultSet rs) throws SQLException {
        return new Feedback(
            rs.getString("tutorId"),
            rs.getString("sessionId"),
            rs.getInt("tutorRating"),
            rs.getInt("courseRating"),
            rs.getString("studentId")
        );
    }
}