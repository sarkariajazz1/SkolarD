package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.Feedback;
import skolard.persistence.RatingPersistence;

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
                    tutorName TEXT NOT NULL,
                    sessionId INTEGER NOT NULL,
                    courseName TEXT NOT NULL,
                    studentName TEXT NOT NULL,
                    rating INTEGER NOT NULL
                )
            """);
        } catch (SQLException e) {
            System.err.println("Failed to create ratings table: " + e.getMessage());
        }
    }

    @Override
    public void saveRating(String tutorId, String sessionId, int tutorRating, int courseRating, String studentId) {
        // Assume: tutorId = tutorName, sessionId = int sessionId, courseRating (or tutorRating) = rating
        // Choose which rating you want to store, here using courseRating
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO ratings (tutorName, sessionId, courseName, studentName, rating) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, tutorId); // tutorName
            ps.setInt(2, Integer.parseInt(sessionId)); // sessionId as int
            ps.setString(3, ""); // courseName is required, adapt to real value if available
            ps.setString(4, studentId); // studentName
            ps.setInt(5, courseRating); // rating
            ps.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Failed to save rating: " + e.getMessage());
        }
    }

    @Override
    public List<Feedback> getAllFeedbackForTutor(String tutorId) {
        List<Feedback> feedbacks = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM ratings WHERE tutorName = ?")) {
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
                "SELECT * FROM ratings WHERE courseName = ?")) {
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
                rs.getInt("sessionId"),
                rs.getString("courseName"),
                rs.getString("tutorName"),
                rs.getString("studentName"),
                rs.getInt("courseRating"),
                rs.getInt("tutorRating") // Assuming rating is the courseRating
        );
    }
}