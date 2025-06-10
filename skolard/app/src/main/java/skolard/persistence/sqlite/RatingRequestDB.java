package skolard.persistence.sqlite;

import java.sql.Statement;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.RatingRequest;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.RatingRequestPersistence;
import skolard.persistence.SessionPersistence;
import skolard.persistence.StudentPersistence;

public class RatingRequestDB implements RatingRequestPersistence {
    private final Connection connection;
    private final StudentPersistence studentPersistence;
    private final SessionPersistence sessionPersistence;

    public RatingRequestDB(Connection conn, StudentPersistence studentPersistence, SessionPersistence sessionPersistence) {
        this.connection = conn;
        this.studentPersistence = studentPersistence;
        this.sessionPersistence = sessionPersistence;
    }

    @Override
    public RatingRequest addRequest(RatingRequest request) {
    String sql = "INSERT INTO ratingRequests (sessionId, studentEmail, completed, skipped, createdAt)" +
        " VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, request.getSession().getSessionId());
            stmt.setString(2, request.getStudent().getEmail());
            stmt.setInt(3, request.isCompleted() ? 1 : 0);
            stmt.setInt(4, request.isSkipped() ? 1 : 0);
            stmt.setString(5, request.getCreatedAt().toString());
            stmt.executeUpdate();

            // Retrieve the auto-generated message ID from the database
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new RatingRequest(id, request.getSession(), request.getStudent(),
                        request.getCreatedAt(), request.isCompleted(), request.isSkipped());
                } else {
                    throw new RuntimeException("Failed to retrieve generated request ID.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding rating request", e);
        }
    }

    @Override
    public void updateRequest(RatingRequest request) {
        String sql = "UPDATE ratingRequests SET completed = ?, skipped = ?" +
            "WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, request.isCompleted() ? 1 : 0);
            stmt.setInt(2, request.isSkipped() ? 1 : 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating rating request", e);
        }
    }

    @Override
    public List<RatingRequest> getAllRequests() {
        List<RatingRequest> ratingRequests = new ArrayList<>();
        String sql = "SELECT * FROM ratingRequests";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ratingRequests.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all rating requests", e);
        }

        return ratingRequests;
    }

    @Override
    public List<RatingRequest> getPendingRequestsForStudent(String studentEmail) {
        List<RatingRequest> ratingRequests = new ArrayList<>();
        String sql = "SELECT * FROM ratingRequests WHERE studentEmail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentEmail);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ratingRequests.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving rating requests", e);
        }

        return ratingRequests;
    }

    private RatingRequest fromResultSet(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        int sessionId = rs.getInt("sessionId");
        String studentEmail = rs.getString("studentEmail");
        LocalDateTime time = LocalDateTime.parse(rs.getString("createdAt"));
        boolean completed = rs.getInt("completed") == 1;
        boolean skipped = rs.getInt("skipped") == 1;

        Session session = sessionPersistence.getSessionById(sessionId);
        Student student = studentPersistence.getStudentByEmail(studentEmail);

        return new RatingRequest(id,
            session,
            student,
            time,
            completed,
            skipped);
    }
}
