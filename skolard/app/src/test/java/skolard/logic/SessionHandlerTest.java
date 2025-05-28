package skolard.logic;

import skolard.objects.Tutor;
import skolard.objects.Student;
import skolard.objects.Session;
import skolard.persistence.SessionPersistence;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

public class SessionHandlerTest {

    private SessionPersistence sessionPersistence;
    private SessionHandler sessionHandler;

    @Before
    public void setup() {
        PersistenceFactory.initialize(PersistenceType.STUB, false);
        sessionPersistence = PersistenceFactory.getSessionPersistence();
        sessionHandler = new SessionHandler(sessionPersistence);
        
    }


    // ─── createSession for Tutor ──────────────────────────────────────────────

    @Test
    public void testCreateSession_NoConflicts() {
        Tutor tutor = new Tutor("Tutor","tutor@myumanitoba.ca", "A tutor");
        LocalDateTime start = LocalDateTime.of(2025, 5, 30, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 5, 30, 11, 0);

        sessionHandler.createSession(tutor, start, end, "Math 101");

        assertEquals(1, sessionPersistence.getAllSessions().size());
        Session session = sessionPersistence.getAllSessions().get(0);
        assertEquals(tutor, session.getTutor());
        assertEquals("Math 101", session.getCourseName());
    }

    @Test
    public void testCreateSession_WithConflict() {
        Tutor tutor = new Tutor("Tutor","tutor@myumanitoba.ca", "A tutor");

        // Add an existing session
        sessionPersistence.addSession(new Session(1, tutor, null,
                LocalDateTime.of(2025, 5, 30, 10, 0),
                LocalDateTime.of(2025, 5, 30, 11, 0),
                "Physics"));

        LocalDateTime newStart = LocalDateTime.of(2025, 5, 30, 10, 30);
        LocalDateTime newEnd = LocalDateTime.of(2025, 5, 30, 11, 30);

        assertThrows(IllegalArgumentException.class, () ->
            sessionHandler.createSession(tutor, newStart, newEnd, "Chemistry")
        );
    }


    // ─── bookASession for student ──────────────────────────────────────────────

    @Test
    public void testBookASession_NotBooked() {
        Tutor tutor = new Tutor("Tutor","tutor@myumanitoba.ca", "A tutor");
        Student student = new Student("student","student@myumanitoba.ca");

        Session session = new Session(1, tutor, null,
                LocalDateTime.of(2025, 6, 1, 14, 0),
                LocalDateTime.of(2025, 6, 1, 15, 0),
                "Biology");

        sessionPersistence.addSession(session);

        sessionHandler.bookASession(student, 1);

        assertTrue(session.isBooked());
        assertEquals(student, session.getStudent());
    }

    @Test
    public void testBookASession_AlreadyBooked() {
        Tutor tutor = new Tutor("Tutor","tutor@myumanitoba.ca", "A tutor");
        Student student = new Student("student","student@myumanitoba.ca");

        Session session = new Session(1, tutor, student,
                LocalDateTime.of(2025, 6, 1, 14, 0),
                LocalDateTime.of(2025, 6, 1, 15, 0),
                "Biology");

        sessionPersistence.addSession(session);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            sessionHandler.bookASession(student, 1)
        );

        assertEquals("Session is already booked", ex.getMessage());
    }

}
