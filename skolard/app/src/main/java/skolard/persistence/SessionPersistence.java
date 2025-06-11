package skolard.persistence;

import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;

/**
 * Interface that defines the required operations for managing Session data.
 * This abstraction allows you to use either a real database or a stub implementation.
 */
public interface SessionPersistence {

    /**
     * Adds a new session to the persistence layer.
     * @param session the Session object to add
     * @return the added Session, possibly with an assigned ID
     */
    Session addSession(Session session);

    /**
     * Retrieves a session by its unique ID.
     * @param sessionId the ID of the session
     * @return the Session object if found, or null if not found
     */
    Session getSessionById(int sessionId);

    /**
     * Retrieves all sessions.
     * @return a list of all Session objects
     */
    List<Session> getAllSessions();

    /**
     * Retrieves all sessions associated with a specific tutor's email.
     * @param tutorEmail the email of the tutor
     * @return a list of Session objects for that tutor
     */
    List<Session> getSessionsByTutorEmail(String tutorEmail);

    /**
     * Retrieves all sessions associated with a specific student's email.
     * @param studentEmail the email of the student
     * @return a list of Session objects for that student
     */
    List<Session> getSessionsByStudentEmail(String studentEmail);

    /**
     * Removes a session by its unique ID.
     * @param sessionId the ID of the session to remove
     */
    void removeSession(int sessionId);

    /**
     * Updates the details of an existing session.
     * @param updatedSession the Session object containing updated details
     */
    void updateSession(Session updatedSession);

    /**
     * Loads (hydrates) the tutor's session lists (e.g., upcoming, past) from persistence.
     * This allows the Tutor object to have its session data populated.
     * @param tutor the Tutor object to hydrate
     */
    void hydrateTutorSessions(Tutor tutor);

    /**
     * Loads (hydrates) the student's session lists (e.g., upcoming, past) from persistence.
     * This allows the Student object to have its session data populated.
     * @param student the Student object to hydrate
     */
    void hydrateStudentSessions(Student student);
}
