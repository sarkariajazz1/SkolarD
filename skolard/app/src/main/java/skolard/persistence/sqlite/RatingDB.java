package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.Feedback;
import skolard.persistence.RatingPersistence;

public class RatingDB implements RatingPersistence {
    private final Connection conn;

    public RatingDB(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void saveRating(String tutorEmail, int sessionId, String studentEmail, String courseName, int rating) {

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO ratings (tutorEmail, sessionId, studentEmail, courseName, rating) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, tutorEmail); // tutorEmail
            ps.setInt(2, sessionId); // sessionId as int
            ps.setString(3, studentEmail); // studentEmail
            ps.setString(4, courseName); // courseName
            ps.setInt(5, rating); // rating
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

    private Feedback extractFeedback(ResultSet rs) throws SQLException {
        return new Feedback(
                rs.getInt("sessionId"),
                rs.getString("courseName"),
                rs.getString("tutorEmail"),
                rs.getString("studentEmail"),
                rs.getInt("rating")
        );
    }
}