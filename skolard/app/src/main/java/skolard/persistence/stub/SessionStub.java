package skolard.persistence.stub;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Stub implementation of SessionPersistence that stores session data in memory.
 * This allows development and testing without requiring a real database backend.
 */
public class SessionStub implements SessionPersistence {

    private Map<String, Session> sessions;     // Stores all sessions keyed by session ID
    private static int sessionCounter = 1;     // Counter for generating unique session IDs

    /**
     * Constructor initializes the session map and loads sample data.
     */
    public SessionStub() {
        confirmCreation();
        addSampleSessions();
    }

    /**
     * Ensures the internal session map is created.
     */
    private void confirmCreation() {
        if (sessions == null) {
            sessions = new HashMap<>();
        }
    }

    /**
     * Populates the stub with pre-defined example sessions.
     * This includes booked and unbooked sessions with mock data.
     */
    private void addSampleSessions() {
        Tutor tutor1 = new Tutor("tutor1", "Amrit Singh", "amrit@skolard.ca",
                "CS & Math Tutor", new ArrayList<>(List.of("COMP 1010", "MATH 1500")),
                Map.of("COMP 1010", "A+", "MATH 1500", "A"));

        Tutor tutor2 = new Tutor("tutor2", "Sukhdeep Kaur", "sukhdeep@skolard.ca",
                "Physics tutor", new ArrayList<>(List.of("PHYS 1050")),
                Map.of("PHYS 1050", "A"));

        Student student1 = new Student("student1", "Raj Gill", "raj@skolard.ca");
        Student student2 = new Student("student2", "Simran Dhillon", "simran@skolard.ca");

        Session s1 = new Session(generateSessionId(), tutor1, null,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1),
                "COMP 1010");

        Session s2 = new Session(generateSessionId(), tutor2, student2,
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                "PHYS 1050");
        s2.bookSession(student2);

        Session s3 = new Session(generateSessionId(), tutor1, student1,
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusHours(1),
                "MATH 1500");
        s3.bookSession(student1);

        addSession(s1);
        addSession(s2);
        addSession(s3);
    }

    /**
     * Generates a new session ID (e.g., "S1", "S2", etc.).
     *
     * @return A unique session ID
     */
    private String generateSessionId() {
        return "S" + sessionCounter++;
    }

    /**
     * Adds a session to the in-memory store.
     *
     * @param session The session to add
     */
    @Override
    public void addSession(Session session) {
        confirmCreation();
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        if (sessions.containsKey(session.getSessionId())) {
            throw new IllegalArgumentException("Session already exists with ID: " + session.getSessionId());
        }
        sessions.put(session.getSessionId(), session);
    }

    /**
     * Retrieves a session by its ID.
     *
     * @param sessionId The session's unique ID
     * @return The matching session, or null
     */
    @Override
    public Session getSessionById(String sessionId) {
        confirmCreation();
        return sessions.get(sessionId);
    }

    /**
     * Gets a list of all sessions currently stored.
     *
     * @return List of all sessions
     */
    @Override
    public List<Session> getAllSessions() {
        confirmCreation();
        return new ArrayList<>(sessions.values());
    }

    /**
     * Retrieves sessions taught by a specific tutor.
     *
     * @param tutorId The tutor's unique ID
     * @return List of sessions associated with that tutor
     */
    @Override
    public List<Session> getSessionsByTutorId(String tutorId) {
        confirmCreation();
        return sessions.values().stream()
                .filter(s -> s.getTutor().getId().equals(tutorId))
                .toList();
    }

    /**
     * Retrieves sessions booked by a specific student.
     *
     * @param studentId The student's unique ID
     * @return List of sessions associated with that student
     */
    @Override
    public List<Session> getSessionsByStudentId(String studentId) {
        confirmCreation();
        return sessions.values().stream()
                .filter(s -> s.getStudent() != null && s.getStudent().getId().equals(studentId))
                .toList();
    }

    /**
     * Removes a session from the in-memory store.
     *
     * @param sessionId The ID of the session to remove
     */
    @Override
    public void removeSession(String sessionId) {
        confirmCreation();
        if (!sessions.containsKey(sessionId)) {
            throw new IllegalArgumentException("Cannot remove non-existent session ID: " + sessionId);
        }
        sessions.remove(sessionId);
    }

    /**
     * Clears all sessions from memory (optional).
     */
    public void deleteAllSessions() {
        this.sessions = new HashMap<>();
    }

    /**
     * Destroys the internal session store (optional helper).
     */
    public void close() {
        this.sessions = null;
    }
}
