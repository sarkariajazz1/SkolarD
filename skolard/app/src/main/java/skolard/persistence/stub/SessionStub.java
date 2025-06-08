package skolard.persistence.stub;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

/**
 * Stub implementation of SessionPersistence that stores session data in-memory using a HashMap.
 * This is useful for testing and development without needing a real database.
 */
public class SessionStub implements SessionPersistence {

    private Map<Integer, Session> sessions;       // Stores sessions by their unique ID
    private static int id = 0;        // Used to generate unique session IDs

    public SessionStub() {
        sessions = new HashMap<>();      // Intialize map
        addSampleSessions();     // Populate with fake data
    }

    // Simulate password hashing (stub use only)
    private String hash(String password) {
        return Integer.toHexString(password.hashCode());
    }

    // Populate with mock tutor/student and sessions
    private void addSampleSessions() {
        Tutor tutor1 = new Tutor(
                "Amrit Singh",
                "amrit@skolard.ca",
                hash("amrit123"),
                "CS & Math Tutor",
                Map.of("COMP 1010", 4.5, "MATH 1500", 4.0)
        );

        Tutor tutor2 = new Tutor(
                "Sukhdeep Kaur",
                "sukhdeep@skolard.ca",
                hash("sukhdeep123"),
                "Physics tutor",
                Map.of("PHYS 1050", 4.0)
        );

        Student student1 = new Student("Raj Gill", "raj@skolard.ca", hash("raj123"));
        Student student2 = new Student("Simran Dhillon", "simran@skolard.ca", hash("simran123"));

        Session s1 = new Session(-1, tutor1, null,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1),
                "COMP 1010");

        Session s2 = new Session(-1, tutor2, student2,
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1),
                "PHYS 1050");

        Session s3 = new Session(-1, tutor1, student1,
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusHours(1),
                "MATH 1500");

        addSession(s1);
        addSession(s2);
        addSession(s3);
    }

    @Override
    public Session addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        if (sessions.containsKey(session.getSessionId())) {
            throw new IllegalArgumentException("Session already exists with ID: " + session.getSessionId());
        }
        Session newSession = new Session(id++, session.getTutor(), session.getStudent(),
            session.getStartDateTime(), session.getEndDateTime(), session.getCourseName());

        sessions.put(newSession.getSessionId(), newSession);

        return newSession;
    }

    @Override
    public Session getSessionById(int sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public List<Session> getAllSessions() {
        return new ArrayList<>(sessions.values());
    }

    @Override
    public List<Session> getSessionsByTutorEmail(String tutorEmail) {
        return sessions.values().stream()
                .filter(s -> s.getTutor().getEmail().equals(tutorEmail))
                .toList();
    }

    @Override
    public List<Session> getSessionsByStudentEmail(String studentEmail) {
        return sessions.values().stream()
                .filter(s -> s.getStudent() != null && s.getStudent().getEmail().equals(studentEmail))
                .toList();
    }

    @Override
    public void removeSession(int sessionId) {
        if (!sessions.containsKey(sessionId)) {
            throw new IllegalArgumentException("Cannot remove non-existent session ID: " + sessionId);
        }
        sessions.remove(sessionId);
    }

    @Override
    public void updateSession(Session updatedSession) {
        if (sessions.containsKey(updatedSession.getSessionId())) {
            sessions.replace(updatedSession.getSessionId(), updatedSession);
        }
    }

    @Override
    public void hydrateTutorSessions(Tutor tutor) {
    List<Session> all = getSessionsByTutorEmail(tutor.getEmail());
    List<Session> past = new ArrayList<>();
    List<Session> upcoming = new ArrayList<>();

    for (Session s : all) {
        if (s.getEndDateTime().isBefore(LocalDateTime.now())) {
            past.add(s);
        } else {
            upcoming.add(s);
        }
    }

    tutor.setPastSessions(past);
    tutor.setUpcomingSessions(upcoming);
    }
    @Override
    public void hydrateStudentSessions(Student student) {
        List<Session> all = getSessionsByStudentEmail(student.getEmail());
        List<Session> past = new ArrayList<>();
        List<Session> upcoming = new ArrayList<>();

        for (Session s : all) {
            if (s.getEndDateTime().isBefore(LocalDateTime.now())) {
                past.add(s);
            } else {
                upcoming.add(s);
            }
        }

        student.setPastSessions(past);
        student.setUpcomingSessions(upcoming);
    }

}
