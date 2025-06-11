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

    Session addSession(Session session);

    Session getSessionById(int sessionId);

    List<Session> getAllSessions();

    List<Session> getSessionsByTutorEmail(String tutorEmail);

    List<Session> getSessionsByStudentEmail(String studentEmail);

    void removeSession(int sessionId);

    void updateSession(Session updatedSession);

    void hydrateTutorSessions(Tutor tutor);
    
    void hydrateStudentSessions(Student student);

}
