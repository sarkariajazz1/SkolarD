package skolard.logic.session;

import skolard.objects.Session;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;


/**
 * Handles creation and deletion of tutor sessions for tutors.
 *
 */
public class SessionManagement{
    private final SessionPersistence sessionPersistence;

    /**
     * Constructor that initializes the session persistence layer.
     *
     * @param sessionPersistence the persistence layer to handle session data
     */
    public SessionManagement(SessionPersistence sessionPersistence) {
        this.sessionPersistence = sessionPersistence;
    }

    /**
     * Creates a new session for a tutor if there are no scheduling conflicts
     * with their existing sessions.
     *
     * @param tutor the tutor creating the session
     * @param start the desired start time of the session
     * @param end the desired end time of the session
     * @param courseName the course name associated with the session
     */
    public void createSession(Tutor tutor, LocalDateTime start, LocalDateTime end, String courseName) {
        long durationInHours = Duration.between(start,end).toHours();

        if(durationInHours > 3 || durationInHours <= 0){
            throw new IllegalArgumentException("Session must be between 1 and 3 hours long");
        }

        List<Session> tutorSessions = sessionPersistence.getSessionsByTutorEmail(tutor.getEmail());

        boolean hasConflict = tutorSessions.stream().anyMatch(session -> {
            LocalDateTime tutorStart = session.getStartDateTime();
            LocalDateTime tutorEnd = session.getEndDateTime();
            return start.isBefore(tutorEnd) && end.isAfter(tutorStart);
        });

        if (hasConflict) {
            throw new IllegalArgumentException("Session conflicts with existing sessions");
        }

        sessionPersistence.addSession(new Session(-1, tutor, null, start, end, courseName));
    }

    /**
     * Deletes a session from the system if it belongs to the given tutor.
     *
     * @param tutor the tutor requesting the deletion
     * @param session the session to be deleted
     */
    public void deleteSession(Tutor tutor, Session session) {
        List<Session> tutorSessions = sessionPersistence.getSessionsByTutorEmail(tutor.getEmail());
        boolean found = tutorSessions.stream().anyMatch(s -> s.getSessionId() == session.getSessionId());
        
        if (found) {
            sessionPersistence.removeSession(session.getSessionId());
        } else {
            throw new IllegalArgumentException("Session not found in database, cannot remove");
        }
    }
}
