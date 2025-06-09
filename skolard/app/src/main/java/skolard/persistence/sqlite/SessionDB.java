package skolard.persistence.sqlite;

<<<<<<< HEAD
=======
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

>>>>>>> dev
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

<<<<<<< HEAD
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

=======
>>>>>>> dev
/**
 * SQLite-based implementation of SessionPersistence.
 * Handles CRUD operations for tutoring sessions in the database.
 */
public class SessionDB implements SessionPersistence {

    private final Connection connection;
    private final StudentPersistence studentPersistence;
    private final TutorPersistence tutorPersistence;

    /**
     * Constructor that takes in a database connection and references to
     * student and tutor persistence to resolve foreign key relations.
     */
    public SessionDB(Connection connection, StudentPersistence studentPersistence, TutorPersistence tutorPersistence) {
        this.connection = connection;
        this.studentPersistence = studentPersistence;
        this.tutorPersistence = tutorPersistence;
    }

    /**
     * Inserts a new session into the database.
     */
    @Override
<<<<<<< HEAD
    public void addSession(Session session) {
        String sql = "INSERT INTO session (id, tutorEmail, studentEmail, startTime, endTime, courseID) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, session.getSessionId());
            stmt.setString(2, session.getTutor().getEmail());
            stmt.setString(3, session.getStudent().getEmail());
            stmt.setString(4, session.getStartDateTime().toString());
            stmt.setString(5, session.getEndDateTime().toString());
            stmt.setString(6, session.getCourseName());
            stmt.executeUpdate();
        } catch (SQLException e) {
=======
    public Session addSession(Session session) {
        String sql = "INSERT INTO session (tutorEmail, studentEmail, startTime, endTime, courseID) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, session.getTutor().getEmail());
            stmt.setString(2, null); // unbooked session
            stmt.setString(3, session.getStartDateTime().toString());
            stmt.setString(4, session.getEndDateTime().toString());
            stmt.setString(5, session.getCourseName());
            stmt.executeUpdate();

            // Retrieve the auto-generated message ID from the database
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new Session(id, session.getTutor(), session.getStudent(),
                        session.getStartDateTime(), session.getEndDateTime(), session.getCourseName());
                } else {
                    throw new RuntimeException("Failed to retrieve generated session ID.");
                }
            }
        } catch (SQLException e) {
            //e.printStackTrace();
>>>>>>> dev
            throw new RuntimeException("Error adding session", e);
        }
    }

<<<<<<< HEAD
=======

>>>>>>> dev
    /**
     * Retrieves a specific session by its ID.
     */
    @Override
    public Session getSessionById(int sessionId) {
        String sql = "SELECT * FROM session WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return fromResultSet(rs); // Convert row to Session object
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving session", e);
        }

        return null; // Not found
    }

    /**
     * Retrieves all sessions from the database.
     */
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

    /**
     * Retrieves all sessions for a specific tutor based on their email.
     */
    @Override
    public List<Session> getSessionsByTutorEmail(String tutorEmail) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE tutorEmail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tutorEmail);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessions.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving sessions by tutor", e);
        }

        return sessions;
    }

    /**
     * Retrieves all sessions for a specific student based on their email.
     */
    @Override
    public List<Session> getSessionsByStudentEmail(String studentEmail) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session WHERE studentEmail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentEmail);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessions.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving sessions by student", e);
        }

        return sessions;
    }

    /**
     * Deletes a session from the database based on its ID.
     */
    @Override
    public void removeSession(int sessionId) {
        String sql = "DELETE FROM session WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting session", e);
        }
    }

    /**
     * Update the information of an existing session.
     *
     * @param updatedSession updated session object
     */
    @Override
    public void updateSession(Session updatedSession) {
        String sql = "UPDATE session SET tutorEmail = ?, studentEmail = ?, " +
            "startTime = ?, endTime = ?, courseID = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, updatedSession.getTutor().getEmail());
<<<<<<< HEAD
            stmt.setString(2, updatedSession.getStudent().getEmail());
=======

            if (updatedSession.getStudent() != null) {
                stmt.setString(2, updatedSession.getStudent().getEmail());
            } else {
                stmt.setNull(2, java.sql.Types.VARCHAR);
            }

>>>>>>> dev
            stmt.setString(3, updatedSession.getStartDateTime().toString());
            stmt.setString(4, updatedSession.getEndDateTime().toString());
            stmt.setString(5, updatedSession.getCourseName());
            stmt.setInt(6, updatedSession.getSessionId());
            stmt.executeUpdate();
        } catch (SQLException e) {
<<<<<<< HEAD
            throw new RuntimeException("Error adding session", e);
        }
    }
=======
            throw new RuntimeException("Error updating session", e);
        }
    }
    @Override
    public void hydrateTutorSessions(Tutor tutor) {
    List<Session> sessions = getSessionsByTutorEmail(tutor.getEmail());

    List<Session> upcoming = new ArrayList<>();
    List<Session> past = new ArrayList<>();

    for (Session session : sessions) {
        if (session.getEndDateTime().isBefore(LocalDateTime.now())) {
            past.add(session);
        } else {
            upcoming.add(session);
        }
    }

    tutor.setPastSessions(past);
    tutor.setUpcomingSessions(upcoming);
    }
    @Override
    public void hydrateStudentSessions(Student student) {
    List<Session> all = getSessionsByStudentEmail(student.getEmail());
    List<Session> past = new ArrayList<>();
    List<Session> upcoming = new ArrayList<>();

    for (Session s : all) {
        if (s.getEndDateTime().isBefore(LocalDateTime.now())) {
            past.add(s);
        } else {
            upcoming.add(s);
        }
    }

    student.setPastSessions(past);
    student.setUpcomingSessions(upcoming);
    }
>>>>>>> dev

    /**
     * Helper method that converts a ResultSet row into a Session object.
     * Resolves tutor and student using their respective persistence layers.
     */
    private Session fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tutorEmail = rs.getString("tutorEmail");
        String studentEmail = rs.getString("studentEmail");
        String startTime = rs.getString("startTime");
        String endTime = rs.getString("endTime");
        String courseId = rs.getString("courseID");

        Tutor tutor = tutorPersistence.getTutorByEmail(tutorEmail);
<<<<<<< HEAD
        Student student = studentPersistence.getStudentByEmail(studentEmail);
=======
        Student student = null;
        if (studentEmail != null) {
            student = studentPersistence.getStudentByEmail(studentEmail);
        }
>>>>>>> dev

        return new Session(
            id,
            tutor,
            student,
            LocalDateTime.parse(startTime),
            LocalDateTime.parse(endTime),
            courseId
        );
    }
<<<<<<< HEAD
=======

>>>>>>> dev
}
