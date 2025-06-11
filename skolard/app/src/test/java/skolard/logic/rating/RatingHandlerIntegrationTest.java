package skolard.logic.rating;

import org.junit.jupiter.api.*;
import skolard.objects.Feedback;
import skolard.objects.RatingRequest;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.*;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatingHandlerIntegrationTest {

    private Connection conn;
    private RatingHandler ratingHandler;
    private RatingRequestPersistence ratingRequestPersistence;
    private RatingPersistence ratingPersistence;
    private StudentPersistence studentPersistence;
    private SessionPersistence sessionPersistence;

    @BeforeAll
    void setup() throws Exception {
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, true);
        PersistenceProvider.initializeSqlite(conn);

        studentPersistence = PersistenceRegistry.getStudentPersistence();
        sessionPersistence = PersistenceRegistry.getSessionPersistence();
        ratingRequestPersistence = PersistenceRegistry.getRatingRequestPersistence();
        ratingPersistence = PersistenceRegistry.getRatingPersistence();

        ratingHandler = new RatingHandler(ratingRequestPersistence, ratingPersistence);
    }

    @Test
    void testCreateRatingRequest() {
        Student student = studentPersistence.getStudentByEmail("alice@example.com");
        Session session = sessionPersistence.getSessionById(6); // Assume booked session

        ratingHandler.createRatingRequest(session, student);

        List<RatingRequest> requests = ratingRequestPersistence.getPendingRequestsForStudent(student.getEmail());
        boolean found = requests.stream().anyMatch(r -> r.getSession().getSessionId() == session.getSessionId());

        assertTrue(found, "Rating request should be created and pending.");
    }

    @Test
    void testProcessRatingSubmission() {
        Student student = studentPersistence.getStudentByEmail("alice@example.com");
        Session session = sessionPersistence.getSessionById(15); // Booked session

        assertNotNull(session.getStudent(), "Session should be booked.");

        ratingHandler.createRatingRequest(session, student);
        RatingRequest request = ratingRequestPersistence.getPendingRequestsForStudent(student.getEmail())
                .stream().filter(r -> r.getSession().getSessionId() == session.getSessionId()).findFirst().orElse(null);

        assertNotNull(request, "Rating request should exist for the session.");

        ratingHandler.processRatingSubmission(request, 4);

        List<Feedback> feedbackList = ratingPersistence.getAllFeedbackForTutor(session.getTutor().getEmail());

        boolean match = feedbackList.stream().anyMatch(f ->
            f.getSessionId() == session.getSessionId() &&
            f.getRating() == 4 &&
            f.getStudentEmail().equals(student.getEmail())
        );

        assertTrue(match, "Feedback should be saved after rating submission.");
    }




    @Test
    void testProcessRatingSkip() {
        Student student = studentPersistence.getStudentByEmail("alice@example.com");
        Session session = sessionPersistence.getSessionById(8); // Assume booked session

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
        String tutorEmail = "bob@skolard.ca";
        List<Feedback> feedback = ratingHandler.getTutorFeedback(tutorEmail);
        assertNotNull(feedback);
    }

    @AfterAll
    void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}
