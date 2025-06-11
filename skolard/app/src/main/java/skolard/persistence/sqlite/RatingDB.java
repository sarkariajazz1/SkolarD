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

    /**
     * Constructor accepting a database connection.
     *
     * @param conn an open SQLite connection
     */
    public RatingDB(Connection conn) {
        this.conn = conn;
    }

    /**
     * Saves a rating submitted by a student for a tutor's session.
     *
     * @param tutorEmail   the email of the tutor being rated
     * @param sessionId    the ID of the session being rated
     * @param studentEmail the email of the student providing the rating
     * @param courseName   the course name associated with the session
     * @param rating       the numeric rating value
     */
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

    /**
     * Retrieves all feedback (ratings) for a given tutor.
     *
     * @param tutorId the tutor's email identifier
     * @return list of Feedback objects for the tutor
     */
    @Override
    public List<Feedback> getAllFeedbackForTutor(String tutorId) {
        List<Feedback> feedbacks = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM ratings WHERE tutorEmail = ?")) {
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

    /**
     * Extracts a Feedback object from the current row in a ResultSet.
     *
     * @param rs the ResultSet positioned at a valid row
     * @return Feedback object populated with values from the ResultSet
     * @throws SQLException if database access error occurs
     */
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
