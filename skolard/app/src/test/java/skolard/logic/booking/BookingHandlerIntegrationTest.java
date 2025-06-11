package skolard.logic.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import skolard.logic.booking.BookingHandler.SessionFilter;
import skolard.objects.Session;
import skolard.objects.Tutor;
import skolard.persistence.EnvironmentInitializer;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceProvider;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.PersistenceType;
import skolard.persistence.SessionPersistence;
import skolard.persistence.TutorPersistence;

import java.sql.Connection;

public class BookingHandlerIntegrationTest {
    private Connection conn;
    private BookingHandler bookingHandler;
    private SessionPersistence sessionPersistence;
    private TutorPersistence tutorPersistence;
    private LocalDateTime futureTime;

    @BeforeEach
    void setUp() throws Exception{
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, false);
        PersistenceProvider.initializeSqlite(conn);

        sessionPersistence = PersistenceRegistry.getSessionPersistence();
        tutorPersistence = PersistenceRegistry.getTutorPersistence();
        bookingHandler = new BookingHandler(sessionPersistence);

        Tutor tutor = new Tutor("David", "david@example.com", "4th year Comp Sci student");
        tutorPersistence.addTutor(tutor);

        futureTime = LocalDateTime.now().plusDays(1);

        sessionPersistence.addSession(new Session(-1, tutor, null, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "COMP1010"));
        sessionPersistence.addSession(new Session(-1, tutor, null, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1), "COMP1010")); // Past
        sessionPersistence.addSession(new Session(-1, tutor, null, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1), "COMP1020"));
    }

    @Test
    void testGetAvailableSessions_basicFiltering() {
        List<Session> result = bookingHandler.getAvailableSessions("COMP1010", "student@example.com");
        assertEquals(1, result.size());
        assertEquals("COMP1010", result.get(0).getCourseName());
    }

    @Test
    void testGetAvailableSessions_filteredByTime() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        List<Session> result = bookingHandler.getAvailableSessions(
                BookingHandler.SessionFilter.TIME,
                "COMP1010",
                start,
                end,
                "student@example.com"
        );

        // Should return the upcoming one only (not past, not outside time range)
        assertEquals(1, result.size());
    }

    @Test
    void testGetAvailableSessionsFilteredByRate() {
        // Tutors with different grades for COMP101
        Tutor tutor1 = new Tutor("Alice", "alice@example.com", "pass", "Math Tutor", Map.of("COMP101", 3.5));
        Tutor tutor2 = new Tutor("Bob", "bob@example.com", "pass", "CS Tutor", Map.of("COMP101", 3.0));

        tutorPersistence.addTutor(tutor1);
        tutorPersistence.addTutor(tutor2);

        // Sessions
        sessionPersistence.addSession(new Session(-1, tutor1,null,futureTime, futureTime.plusHours(1),"COMP101"));
        sessionPersistence.addSession(new Session(-1, tutor2, null, futureTime, futureTime.plusHours(1),"COMP101"));

        List<Session> sessions = bookingHandler.getAvailableSessions(
            SessionFilter.RATE, "COMP101", null, null, "student@example.com");

        assertEquals(2, sessions.size());
        assertEquals("alice@example.com", sessions.get(0).getTutor().getEmail());
        assertEquals("bob@example.com", sessions.get(1).getTutor().getEmail());
    }

    @Test
    void testGetAvailableSessionsFilteredByTutor() {
        // Tutors with different average ratings
        Map<String, Double> grades1 = Map.of("COMP101", 4.0);
        Map<String, Double> grades2 = Map.of("COMP101", 3.0, "COMP202", 3.5);

        Tutor tutor1 = new Tutor("Alice", "alice@example.com", "pass", "Math Tutor", grades1);
        Tutor tutor2 = new Tutor("Bob", "bob@example.com", "pass", "CS Tutor", grades2);

        tutorPersistence.addTutor(tutor1);
        tutorPersistence.addTutor(tutor2);

        // Sessions
        sessionPersistence.addSession(new Session(-1, tutor1, null, futureTime, futureTime.plusHours(1),"COMP101"));
        sessionPersistence.addSession(new Session(-1, tutor2, null, futureTime, futureTime.plusHours(1),"COMP101"));

        List<Session> sessions = bookingHandler.getAvailableSessions(
            SessionFilter.TUTOR, "COMP101", null, null, "student@example.com");

        assertEquals(2, sessions.size());
        assertEquals("alice@example.com", sessions.get(0).getTutor().getEmail());
        assertEquals("bob@example.com", sessions.get(1).getTutor().getEmail());
    }


    @Test
    void testGetAvailableSessions_excludesTutorSessionsForSelfBooking() {
        List<Session> result = bookingHandler.getAvailableSessions("COMP1010", "david@example.com");
        assertTrue(result.isEmpty());
    }

    @AfterEach
    void cleanup() throws Exception {
        PersistenceFactory.reset();
    }
}
