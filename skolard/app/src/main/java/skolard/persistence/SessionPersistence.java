package skolard.persistence;

import java.util.List;

import skolard.objects.Feedback;
import skolard.objects.Session;

/**
 * Interface that defines the required operations for managing Session data.
 * This abstraction allows you to use either a real database or a stub implementation.
 */
public interface SessionPersistence {

    void addSession(Session session);

    Session getSessionById(int sessionId);

    List<Session> getAllSessions();

    List<Session> getSessionsByTutorEmail(String tutorEmail);

    List<Session> getSessionsByStudentEmail(String studentEmail);

    void removeSession(int sessionId);

    void updateSession(Session updatedSession);

    /**
     * Persist a feedback record associated with a completed session.
     * Should be linked to courseName in the database via foreign key.
     *
     * @param feedback Feedback object containing rating and comment.
     */
    void saveFeedback(Feedback feedback);

    /**
     * Retrieve all feedback entries related to a specific course.
     *
     * @param courseName the course name (e.g., "COMP2150")
     * @return list of feedback records linked to that course
     */
    List<Feedback> getFeedbackByCourse(String courseName);
}
