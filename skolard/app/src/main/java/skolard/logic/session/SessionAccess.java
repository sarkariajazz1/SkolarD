package skolard.logic.session;

import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

/**
 * Handles refreshing upcoming/past sessions for tutor and students and retrieving specific sessions.
 *
 */
public class SessionAccess{
    private final SessionPersistence sessionPersistence;

    /**
     * Constructor that initializes the session persistence layer.
     *
     * @param sessionPersistence the persistence layer to handle session data
     */
    public SessionAccess(SessionPersistence sessionPersistence) {
        this.sessionPersistence = sessionPersistence;
    }

    /**
     * Populates the student's session lists (e.g., booked sessions)
     * by fetching data from the persistence layer.
     *
     * @param student the student whose sessions will be loaded
     */
    public void setStudentSessionLists(Student student) {
        sessionPersistence.hydrateStudentSessions(student);
    }

    /**
     * Populates the tutor's session lists (e.g., created sessions)
     * from the persistence layer.
     *
     * @param tutor the tutor whose sessions will be loaded
     */
    public void setTutorSessionLists(Tutor tutor) {
        sessionPersistence.hydrateTutorSessions(tutor);
    }

    /**
     * Retrieves all sessions associated with a given tutor.
     *
     * @param tutor the tutor whose sessions are being queried
     * @return a list of the tutor's sessions
     */
    public List<Session> getSessionsByTutor(Tutor tutor) {
        return sessionPersistence.getSessionsByTutorEmail(tutor.getEmail());
    }

    /**
     * Retrieves a specific session by its unique session ID.
     *
     * @param id the session ID
     * @return the corresponding Session object
     */
    public Session getSessionByID(int id){
        return sessionPersistence.getSessionById(id);
    }
}
