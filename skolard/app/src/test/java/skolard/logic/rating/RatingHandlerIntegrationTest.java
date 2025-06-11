package skolard.logic.rating;

import org.junit.jupiter.api.*;
import skolard.objects.*;
import skolard.persistence.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RatingHandlerIntegrationTest {

    private Connection conn;
    private RatingHandler ratingHandler;
    private RatingRequestPersistence ratingRequestPersistence;
    private RatingPersistence ratingPersistence;
    private StudentPersistence studentPersistence;
    private TutorPersistence tutorPersistence;
    private SessionPersistence sessionPersistence;

    @BeforeEach
    void setup() throws Exception {
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, false);
        PersistenceProvider.initializeSqlite(conn);

        studentPersistence = PersistenceRegistry.getStudentPersistence();
        tutorPersistence = PersistenceRegistry.getTutorPersistence();
        sessionPersistence = PersistenceRegistry.getSessionPersistence();
        ratingRequestPersistence = PersistenceRegistry.getRatingRequestPersistence();
        ratingPersistence = PersistenceRegistry.getRatingPersistence();

        ratingHandler = new RatingHandler(ratingRequestPersistence, ratingPersistence);
    }

    private Student createTestStudent() {
        String email = "student_" + UUID.randomUUID() + "@example.com";
        Student student = new Student("Test Student", email, "hashedPassword");
        studentPersistence.addStudent(student);
        return student;
    }

    private Tutor createTestTutor() {
        String email = "tutor_" + UUID.randomUUID() + "@example.com";
        Map<String, Double> courses = new HashMap<>();
        courses.put("Math", 4.0);
        Tutor tutor = new Tutor("Test Tutor", email, "hashedPassword", "Bio", courses);
        tutorPersistence.addTutor(tutor);
        return tutor;
    }

    private Session createAndBookSession(Student student, Tutor tutor) {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);
        Session session = new Session(0, tutor, null, start, end, "Math");
        sessionPersistence.addSession(session);
        Session saved = sessionPersistence.getSessionsByTutorEmail(tutor.getEmail()).stream()
                .filter(s -> s.getStartDateTime().equals(start))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Session not found after insert"));

        saved.bookSession(student);
        sessionPersistence.updateSession(saved);
        return saved;
    }

    @Test
    void testCreateRatingRequest() {
        Student student = createTestStudent();
        Tutor tutor = createTestTutor();
        Session session = createAndBookSession(student, tutor);

        ratingHandler.createRatingRequest(session, student);

        List<RatingRequest> requests = ratingRequestPersistence.getPendingRequestsForStudent(student.getEmail());
        boolean found = requests.stream().anyMatch(r -> r.getSession().getSessionId() == session.getSessionId());

        assertTrue(found, "Rating request should be created and pending.");
    }

    @Test
    void testProcessRatingSubmission() {
        Student student = createTestStudent();
        Tutor tutor = createTestTutor();
        Session session = createAndBookSession(student, tutor);

        ratingHandler.createRatingRequest(session, student);
        RatingRequest request = ratingRequestPersistence.getPendingRequestsForStudent(student.getEmail())
                .stream().filter(r -> r.getSession().getSessionId() == session.getSessionId()).findFirst().orElse(null);

        assertNotNull(request, "Rating request should exist for the session.");

        ratingHandler.processRatingSubmission(request, 4);

        List<Feedback> feedbackList = ratingPersistence.getAllFeedbackForTutor(tutor.getEmail());

        boolean match = feedbackList.stream().anyMatch(f ->
            f.getSessionId() == session.getSessionId() &&
            f.getRating() == 4 &&
            f.getStudentEmail().equals(student.getEmail())
        );

        assertTrue(match, "Feedback should be saved after rating submission.");
    }

    @Test
    void testProcessRatingSkip() {
        Student student = createTestStudent();
        Tutor tutor = createTestTutor();
        Session session = createAndBookSession(student, tutor);

        ratingHandler.createRatingRequest(session, student);
        RatingRequest request = ratingRequestPersistence.getPendingRequestsForStudent(student.getEmail())
                .stream().filter(r -> r.getSession().getSessionId() == session.getSessionId()).findFirst().orElse(null);

        assertNotNull(request);

        ratingHandler.processRatingSkip(request);

        List<RatingRequest> pending = ratingRequestPersistence.getPendingRequestsForStudent(student.getEmail());
        boolean stillPending = pending.stream().anyMatch(r -> r.getSession().getSessionId() == session.getSessionId());

        assertFalse(stillPending, "Rating request should be skipped and no longer pending.");
    }

    @Test
    void testGetAllRequests() {
        List<RatingRequest> all = ratingHandler.getAllRequests();
        assertNotNull(all);
        assertTrue(all.size() >= 0); // Just ensure it runs and returns a list
    }

    @Test
    void testGetTutorFeedback() {
        Tutor tutor = createTestTutor();
        List<Feedback> feedback = ratingHandler.getTutorFeedback(tutor.getEmail());
        assertNotNull(feedback);
    }

    @AfterEach
    void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}
