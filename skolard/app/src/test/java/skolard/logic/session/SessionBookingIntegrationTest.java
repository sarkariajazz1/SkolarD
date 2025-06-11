package skolard.logic.session;

import org.junit.jupiter.api.*;
import skolard.objects.*;
import skolard.persistence.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionBookingIntegrationTest {

    private Connection conn;
    private SessionHandler sessionHandler;
    private RatingRequestPersistence ratingRequestPersistence;
    private StudentPersistence studentPersistence;
    private TutorPersistence tutorPersistence;
    private SessionPersistence sessionPersistence;

    @BeforeAll
    void setup() throws Exception {
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, false);
        PersistenceProvider.initializeSqlite(conn);

        studentPersistence = PersistenceRegistry.getStudentPersistence();
        tutorPersistence = PersistenceRegistry.getTutorPersistence();
        sessionPersistence = PersistenceRegistry.getSessionPersistence();
        ratingRequestPersistence = PersistenceRegistry.getRatingRequestPersistence();

        sessionHandler = new SessionHandler(sessionPersistence, ratingRequestPersistence);
    }

    private Student createTestStudent() {
        String email = "student_" + UUID.randomUUID() + "@example.com";
        String name = "Test Student";
        String password = "hashedPassword"; // Replace with actual hashing if needed
        Student student = new Student(name, email, password);
        studentPersistence.addStudent(student);
        return student;
    }

    private Tutor createTestTutor() {
        String email = "tutor_" + UUID.randomUUID() + "@example.com";
        String name = "Test Tutor";
        String password = "hashedPassword";
        String bio = "Experienced in Math and Physics.";
        Map<String, Double> courses = new HashMap<>();
        courses.put("Math", 4.0);
        courses.put("Physics", 3.8);

        Tutor tutor = new Tutor(name, email, password, bio, courses);
        tutorPersistence.addTutor(tutor);
        return tutor;
    }

    private Session createTestSession(Tutor tutor) {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);
        Session session = new Session(0, tutor, null, start, end, "Math");
        sessionPersistence.addSession(session);
        return sessionPersistence.getSessionsByTutorEmail(tutor.getEmail()).stream()
                .filter(s -> s.getStartDateTime().equals(start))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Session not found after insert"));
    }

    @Test
    void testBookSession_success() {
        Student student = createTestStudent();
        Tutor tutor = createTestTutor();
        Session session = createTestSession(tutor);

        assertNull(session.getStudent());

        sessionHandler.bookASession(student, session.getSessionId());

        Session updated = sessionPersistence.getSessionById(session.getSessionId());
        assertNotNull(updated.getStudent());
        assertEquals(student.getEmail(), updated.getStudent().getEmail());
    }

    @Test
    void testUnbookSession_success() {
        Student student = createTestStudent();
        Tutor tutor = createTestTutor();
        Session session = createTestSession(tutor);

        sessionHandler.bookASession(student, session.getSessionId());
        sessionHandler.unbookASession(student, session.getSessionId());

        Session updated = sessionPersistence.getSessionById(session.getSessionId());
        assertNull(updated.getStudent());
    }

    @Test
    void testUnbookingSkipsRatingRequest() {
        Student student = createTestStudent();
        Tutor tutor = createTestTutor();
        Session session = createTestSession(tutor);

        sessionHandler.bookASession(student, session.getSessionId());
        sessionHandler.unbookASession(student, session.getSessionId());

        List<RatingRequest> pending = ratingRequestPersistence.getPendingRequestsForStudent(student.getEmail());
        boolean match = pending.stream().noneMatch(r -> r.getSession().getSessionId() == session.getSessionId());

        assertTrue(match, "Rating request should be skipped when session is unbooked.");
    }

    @AfterAll
    void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}
