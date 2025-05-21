package skolard.persistence;

import skolard.objects.Session;

import java.util.List;

/**
 * Interface that defines the required operations for managing Session data.
 * This abstraction allows for both real and stub database implementations.
 */
public interface SessionPersistence {

    /**
     * Add a new tutoring session to the system.
     *
     * @param session the Session object to be added
     */
    void addSession(Session session);

    /**
     * Find and return a session by its unique session ID.
     *
     * @param sessionId the ID of the session
     * @return the Session object if found, otherwise null
     */
    Session getSessionById(String sessionId);

    /**
     * Retrieve all sessions currently stored in the system.
     *
     * @return an unmodifiable list of all sessions
     */
    List<Session> getAllSessions();

    /**
     * Get all sessions scheduled by a specific tutor.
     *
     * @param tutorId the unique ID of the tutor
     * @return a list of sessions where the tutor matches the given ID
     */
    List<Session> getSessionsByTutorId(String tutorId);

    /**
     * Get all sessions booked by a specific student.
     *
     * @param studentId the unique ID of the student
     * @return a list of sessions where the student matches the given ID
     */
    List<Session> getSessionsByStudentId(String studentId);

    /**
     * Remove a session from the system using its ID.
     *
     * @param sessionId the ID of the session to remove
     */
    void removeSession(String sessionId);
}
