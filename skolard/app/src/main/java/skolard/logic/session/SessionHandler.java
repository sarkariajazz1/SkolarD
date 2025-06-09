package skolard.logic.session;

import java.time.LocalDateTime;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

/**
 * Facade that delegates session operations to specialized handlers.
 */
public class SessionHandler {
    private final SessionManagement managementHandler;
    private final SessionBooking bookingHandler;
    private final SessionAccess accessHandler;

    /**
     * Constructs the SessionHandler with a shared persistence layer.
     *
     * @param sessionPersistence the persistence interface used across all handlers
     */
    public SessionHandler(SessionPersistence sessionPersistence) {
        this.managementHandler = new SessionManagement(sessionPersistence);
        this.bookingHandler = new SessionBooking(sessionPersistence);
        this.accessHandler = new SessionAccess(sessionPersistence);
    }

    /** Delegates to SessionManagement to create a new session */
    public void createSession(Tutor tutor, LocalDateTime start, LocalDateTime end, String courseName) {
        managementHandler.createSession(tutor, start, end, courseName);
    }

    /** Delegates to SessionManagement to delete a session */
    public void deleteSession(Tutor tutor, Session session) {
        managementHandler.deleteSession(tutor, session);
    }

    /** Delegates to SessionBooking to book a session */
    public void bookASession(Student student, int sessionID) {
        bookingHandler.bookASession(student, sessionID);
    }

    /** Delegates to SessionBooking to unbook a session */
    public void unbookASession(Student student, int sessionID) {
        bookingHandler.unbookASession(student, sessionID);
    }

    /** Delegates to SessionAccess to hydrate the student's session list */
    public void setStudentSessionLists(Student student) {
        accessHandler.setStudentSessionLists(student);
    }

    /** Delegates to SessionAccess to hydrate the tutor's session list */
    public void setTutorSessionLists(Tutor tutor) {
        accessHandler.setTutorSessionLists(tutor);
    }

    /** Delegates to SessionAccess to retrieve all sessions for a tutor */
    public List<Session> getSessionsByTutor(Tutor tutor) {
        return accessHandler.getSessionsByTutor(tutor);
    }

    /** Delegates to SessionAccess to retrieve a session by ID */
    public Session getSessionByID(int id) {
        return accessHandler.getSessionByID(id);
    }
}