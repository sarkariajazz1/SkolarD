package skolard.persistence.stub;

import skolard.objects.Session;
import skolard.persistence.SessionPersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory (stub) implementation of SessionPersistence.
 * This class stores sessions in a list and simulates a database for testing or development purposes.
 */
public class SessionStub implements SessionPersistence {

    // Internal list acting as our fake database
    private final List<Session> sessions = new ArrayList<>();

    /**
     * Add a new session to the in-memory list.
     * Ensures no duplicate session IDs are inserted.
     *
     * @param session the Session to add
     */
    @Override
    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        if (getSessionById(session.getSessionId()) != null) {
            throw new IllegalArgumentException("Session ID already exists: " + session.getSessionId());
        }
        sessions.add(session);
    }

    /**
     * Find a session by its unique session ID.
     *
     * @param sessionId the ID to search for
     * @return the Session object if found, or null
     */
    @Override
    public Session getSessionById(String sessionId) {
        return sessions.stream()
                .filter(s -> s.getSessionId().equals(sessionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Return all sessions in the system.
     * Returns an unmodifiable view to prevent external changes.
     *
     * @return list of all sessions
     */
    @Override
    public List<Session> getAllSessions() {
        return Collections.unmodifiableList(sessions);
    }

    /**
     * Retrieve all sessions associated with a specific tutor ID.
     *
     * @param tutorId the tutor's unique ID
     * @return list of sessions where tutor matches
     */
    @Override
    public List<Session> getSessionsByTutorId(String tutorId) {
        return sessions.stream()
                .filter(s -> s.getTutor() != null && s.getTutor().getId().equals(tutorId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all sessions associated with a specific student ID.
     *
     * @param studentId the student's unique ID
     * @return list of sessions where student matches
     */
    @Override
    public List<Session> getSessionsByStudentId(String studentId) {
        return sessions.stream()
                .filter(s -> s.getStudent() != null && s.getStudent().getId().equals(studentId))
                .collect(Collectors.toList());
    }

    /**
     * Remove a session from the system using its ID.
     * If the session ID does not exist, nothing is removed.
     *
     * @param sessionId the ID of the session to remove
     */
    @Override
    public void removeSession(String sessionId) {
        sessions.removeIf(s -> s.getSessionId().equals(sessionId));
    }
}
