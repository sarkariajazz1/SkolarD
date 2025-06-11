package skolard.logic.session;

import org.junit.jupiter.api.*;
import skolard.logic.rating.RatingHandler;
import skolard.objects.RatingRequest;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.*;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionBookingIntegrationTest {

    private Connection conn;
    private SessionHandler sessionHandler;
    private RatingRequestPersistence ratingRequestPersistence;
    private RatingPersistence ratingPersistence;
    private StudentPersistence studentPersistence;
    private SessionPersistence sessionPersistence;
    private RatingHandler ratingHandler;

    @BeforeAll
    void setup() throws Exception {
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, true);
        PersistenceProvider.initializeSqlite(conn);

        studentPersistence = PersistenceRegistry.getStudentPersistence();
        sessionPersistence = PersistenceRegistry.getSessionPersistence();
        ratingRequestPersistence = PersistenceRegistry.getRatingRequestPersistence();
        ratingPersistence = PersistenceRegistry.getRatingPersistence();

        sessionHandler = new SessionHandler(sessionPersistence, ratingRequestPersistence);
        ratingHandler = new RatingHandler(ratingRequestPersistence, ratingPersistence);
    }

    @Test
    void testBookSession_success() {
        Student student = studentPersistence.getStudentByEmail("alice@example.com");
        Session session = sessionPersistence.getSessionById(2); // Unbooked in seed

        assertNotNull(student);
        assertNotNull(session);
        assertNull(session.getStudent());

        sessionHandler.bookASession(student, session.getSessionId());

        Session updated = sessionPersistence.getSessionById(session.getSessionId());
        assertNotNull(updated.getStudent());
        assertEquals(student.getEmail(), updated.getStudent().getEmail());
    }

    @Test
    void testBookSession_createsRatingRequest() {
        Student student = studentPersistence.getStudentByEmail("alice@example.com");
        Session session = sessionPersistence.getSessionById(3); // Use a different unbooked session

        sessionHandler.bookASession(student, session.getSessionId());

        List<RatingRequest> pending = ratingRequestPersistence.getPendingRequestsForStudent(student.getEmail());
        boolean match = pending.stream().anyMatch(r -> r.getSession().getSessionId() == session.getSessionId());

        assertTrue(match, "Rating request should be created for booked session.");
    }

    @Test
    void testUnbookSession_success() {
        Student student = studentPersistence.getStudentByEmail("alice@example.com");
        Session session = sessionPersistence.getSessionById(4);

        sessionHandler.bookASession(student, session.getSessionId());
        sessionHandler.unbookASession(student, session.getSessionId());

        Session updated = sessionPersistence.getSessionById(session.getSessionId());
        assertNull(updated.getStudent());
    }

    @Test
    void testUnbookingSkipsRatingRequest() {
        Student student = studentPersistence.getStudentByEmail("alice@example.com");
        Session session = sessionPersistence.getSessionById(5);

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
