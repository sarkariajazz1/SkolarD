package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

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
     * Note: Sets studentEmail as null initially (unbooked session).
     * Returns a Session object with the generated session ID.
     */
    @Override
    public Session addSession(Session session) {
        String sql = "INSERT INTO session (tutorEmail, studentEmail, startTime, endTime, courseID) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set tutor's email
            stmt.setString(1, session.getTutor().getEmail());
            // Set studentEmail to null for unbooked session
            stmt.setString(2, null);
            // Store start and end time as strings
            stmt.setString(3, session.getStartDateTime().toString());
            stmt.setString(4, session.getEndDateTime().toString());
            // Set course ID/name
            stmt.setString(5, session.getCourseName());
            stmt.executeUpdate();

            // Retrieve the auto-generated session ID
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    // Return new Session object with generated ID and original data
                    return new Session(id, session.getTutor(), session.getStudent(),
                        session.getStartDateTime(), session.getEndDateTime(), session.getCourseName());
                } else {
                    throw new RuntimeException("Failed to retrieve generated session ID.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding session", e);
        }
    }

    /**
     * Retrieves a session by its unique ID.
     * Returns the Session object if found, else returns null.
     */
    @Override
    public Session getSessionById(int sessionId) {
        String sql = "SELECT * FROM session WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return fromResultSet(rs); // Convert database row into Session object
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving session", e);
        }

        return null; // Session not found
    }

    /**
     * Retrieves all sessions in the database.
     * Returns a list of Session objects.
     */
    @Override
    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM session";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterate over each row and convert to Session
            while (rs.next()) {
                sessions.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all sessions", e);
        }

        return sessions;
    }

    /**
     * Retrieves all sessions associated with a specific tutor by their email.
     * Returns a list of sessions hosted by the tutor.
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
     * Retrieves all sessions booked by a specific student by their email.
     * Returns a list of sessions booked by the student.
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
     * Deletes a session from the database by its ID.
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
     * Updates an existing session in the database.
     * Supports updating tutor, student, start/end times, and course ID.
     */
    @Override
    public void updateSession(Session updatedSession) {
        String sql = "UPDATE session SET tutorEmail = ?, studentEmail = ?, " +
            "startTime = ?, endTime = ?, courseID = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set tutor's email
            stmt.setString(1, updatedSession.getTutor().getEmail());

            // Set student email if present, otherwise set NULL
            if (updatedSession.getStudent() != null) {
                stmt.setString(2, updatedSession.getStudent().getEmail());
            } else {
                stmt.setNull(2, java.sql.Types.VARCHAR);
            }

            // Set start and end time as strings
            stmt.setString(3, updatedSession.getStartDateTime().toString());
            stmt.setString(4, updatedSession.getEndDateTime().toString());

            // Set course ID/name
            stmt.setString(5, updatedSession.getCourseName());

            // Set session ID to locate record
            stmt.setInt(6, updatedSession.getSessionId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating session", e);
        }
    }

    /**
     * Loads (hydrates) all sessions associated with a tutor into their upcoming and past session lists.
     */
    @Override
    public void hydrateTutorSessions(Tutor tutor) {
        List<Session> sessions = getSessionsByTutorEmail(tutor.getEmail());

        List<Session> upcoming = new ArrayList<>();
        List<Session> past = new ArrayList<>();

        // Categorize sessions based on whether they have ended already
        for (Session session : sessions) {
            if (session.getEndDateTime().isBefore(LocalDateTime.now())) {
                past.add(session);
            } else {
                upcoming.add(session);
            }
        }

        // Set the tutor's past and upcoming session lists
        tutor.setPastSessions(past);
        tutor.setUpcomingSessions(upcoming);
    }

    /**
     * Loads (hydrates) all sessions associated with a student into their upcoming and past session lists.
     */
    @Override
    public void hydrateStudentSessions(Student student) {
        List<Session> all = getSessionsByStudentEmail(student.getEmail());

        List<Session> past = new ArrayList<>();
        List<Session> upcoming = new ArrayList<>();

        // Categorize sessions as past or upcoming based on current time
        for (Session s : all) {
            if (s.getEndDateTime().isBefore(LocalDateTime.now())) {
                past.add(s);
            } else {
                upcoming.add(s);
            }
        }

        // Set the student's past and upcoming session lists
        student.setPastSessions(past);
        student.setUpcomingSessions(upcoming);
    }

    /**
     * Converts a ResultSet row into a Session object.
     * Uses tutorPersistence and studentPersistence to resolve tutor and student objects by email.
     */
    private Session fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tutorEmail = rs.getString("tutorEmail");
        String studentEmail = rs.getString("studentEmail");
        String startTime = rs.getString("startTime");
        String endTime = rs.getString("endTime");
        String courseId = rs.getString("courseID");

        // Retrieve tutor object using tutor persistence
        Tutor tutor = tutorPersistence.getTutorByEmail(tutorEmail);

        // Retrieve student object if studentEmail is not null
        Student student = null;
        if (studentEmail != null) {
            student = studentPersistence.getStudentByEmail(studentEmail);
        }

        // Create and return a new Session object populated with the retrieved data
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