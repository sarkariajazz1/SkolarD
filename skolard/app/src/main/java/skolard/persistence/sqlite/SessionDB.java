package skolard.persistence.sqlite;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionDB implements SessionPersistence {

    private final Connection connection;
    private final StudentPersistence studentPersistence;
    private final TutorPersistence tutorPersistence;

    public SessionDB(Connection connection, StudentPersistence studentPersistence, TutorPersistence tutorPersistence) {
        this.connection = connection;
        this.studentPersistence = studentPersistence;
        this.tutorPersistence = tutorPersistence;
    }

    @Override
    public void addSession(Session session) {
        String sql = "INSERT INTO session (id, tutorEmail, studentEmail, startTime, endTime, courseID) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, session.getSessionId());
            stmt.setString(2, session.getTutor().getEmail());
            stmt.setString(3, session.getStudent().getEmail());
            stmt.setString(4, session.getStartDateTime().toString());
            stmt.setString(5, session.getEndDateTime().toString());
            stmt.setString(6, session.getCourseName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding session", e);
        }
    }

    @Override
    public Session getSessionById(String sessionId) {
        String sql = "SELECT * FROM session WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sessionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return fromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving session", e);
        }

        return null;
    }

    @Override
    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sessions.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all sessions", e);
        }

        return sessions;
    }

    @Override
    public List<Session> getSessionsByTutorId(String tutorId) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE tutorEmail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tutorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessions.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving sessions by tutor", e);
        }

        return sessions;
    }

    @Override
    public List<Session> getSessionsByStudentId(String studentId) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE studentEmail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessions.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving sessions by student", e);
        }

        return sessions;
    }

    @Override
    public void removeSession(String sessionId) {
        String sql = "DELETE FROM session WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sessionId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting session", e);
        }
    }

    // Helper method to construct a Session object from a ResultSet
    private Session fromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String tutorEmail = rs.getString("tutorEmail");
        String studentEmail = rs.getString("studentEmail");
        String startTime = rs.getString("startTime");
        String endTime = rs.getString("endTime");
        String courseId = rs.getString("courseID");

        Tutor tutor = tutorPersistence.getTutorByEmail(tutorEmail);
        Student student = studentPersistence.getStudentByEmail(studentEmail);

        return new Session(
            id,
            tutor,
            student,
            LocalDateTime.parse(startTime),
            LocalDateTime.parse(endTime),
            courseId
        );
    }
}
