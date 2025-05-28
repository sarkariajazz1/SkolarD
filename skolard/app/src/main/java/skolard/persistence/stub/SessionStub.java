package skolard.persistence.stub;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Stub implementation of SessionPersistence that stores session data in-memory using a HashMap.
 * This is useful for testing and development without needing a real database.
 */
public class SessionStub implements SessionPersistence {

    private Map<Integer, Session> sessions;       // Stores sessions by their unique ID
    private static int sessionCounter = 0;       // Used to generate unique session IDs

    public SessionStub() {
        confirmCreation();       // Ensure map is initialized
        addSampleSessions();     // Populate with fake data
    }

    // Initialize the internal session map if not already done
    private void confirmCreation() {
        if (sessions == null) {
            sessions = new HashMap<>();
        }
    }

    // Generate and add some fake sessions to simulate real data
    private void addSampleSessions() {
        Tutor tutor1 = new Tutor("Amrit Singh", "amrit@skolard.ca",
                "CS & Math Tutor", new ArrayList<>(List.of("COMP 1010", "MATH 1500")),
                Map.of("COMP 1010", 4.5, "MATH 1500", 4.0));

        Tutor tutor2 = new Tutor("Sukhdeep Kaur", "sukhdeep@skolard.ca",
                "Physics tutor", new ArrayList<>(List.of("PHYS 1050")),
                Map.of("PHYS 1050", 4.0));

        Student student1 = new Student("Raj Gill", "raj@skolard.ca");
        Student student2 = new Student("Simran Dhillon", "simran@skolard.ca");

        // Create sessions
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

        // Add to in-memory DB
        addSession(s1);
        addSession(s2);
        addSession(s3);
    }

    // Generate a unique session ID
    private int generateSessionId() {
        return sessionCounter++;
    }

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

    @Override
    public Session getSessionById(int sessionId) {
        confirmCreation();
        return sessions.get(sessionId);
    }

    @Override
    public List<Session> getAllSessions() {
        confirmCreation();
        return new ArrayList<>(sessions.values());
    }

    @Override
    public List<Session> getSessionsByTutorEmail(String tutorEmail) {
        confirmCreation();
        return sessions.values().stream()
                .filter(s -> s.getTutor().getEmail().equals(tutorEmail))
                .toList();
    }

    @Override
    public List<Session> getSessionsByStudentEmail(String studentEmail) {
        confirmCreation();
        return sessions.values().stream()
                .filter(s -> s.getStudent() != null && s.getStudent().getEmail().equals(studentEmail))
                .toList();
    }

    @Override
    public void removeSession(int sessionId) {
        confirmCreation();
        if (!sessions.containsKey(sessionId)) {
            throw new IllegalArgumentException("Cannot remove non-existent session ID: " + sessionId);
        }
        sessions.remove(sessionId);
    }

    @Override
    public void updateSession(Session updatedSession) {
        confirmCreation();
        if(sessions.containsKey(updatedSession.getSessionId())) {
            sessions.replace(updatedSession.getSessionId(), updatedSession);
        }
    }

    // Optional helper for clearing data
    public void deleteAllSessions() {
        this.sessions = new HashMap<>();
    }

    // Optional helper for resetting the stub entirely
    public void close() {
        this.sessions = null;
    }
}


