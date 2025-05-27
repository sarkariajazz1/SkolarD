package skolard.logic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

public class SessionHandlerTest {

    private SessionHandler sessionHandler;
    private SessionPersistence sessionPersistence;
    private Tutor mockTutor;
    private Student mockStudent;

    @Before
    public void setUp() {
        sessionHandler = new SessionHandler(sessionPersistence);
        mockTutor = new Tutor("John Doe","john@myumanitoba.ca", "Comp Sci graduate");
        mockStudent = new Student("Jane Doe", "jane@myumanitoba.ca");
    }

    @Test
    public void testCreateSessionWithTutor() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        String courseName = "Physics";

        sessionHandler.createSession(mockTutor, start, end, courseName);

        assertEquals(1, sessionPersistence.getAllSessions().size());
        Session session = sessionPersistence.getAllSessions().get(0);
        assertEquals(mockTutor, session.getTutor());
        assertEquals(start, session.getStartDateTime());
        assertEquals(end, session.getEndDateTime());
        assertEquals(courseName, session.getCourseName());
    }

    @Test
    public void testBookSessionWithStudent() {
        Session session = new Session(1, mockTutor, null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Math");

        sessionPersistence.addSession(session);

        sessionHandler.bookASession(mockStudent, 1);

        assertEquals(mockStudent, sessionPersistence.getSessionById(1).getStudent());
    }

    @Test
    public void testCreateSessionWithNonTutor() {
        sessionHandler.createSession(mockStudent, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Biology");

        assertTrue(sessionPersistence.getAllSessions().isEmpty());
    }

    @Test
    public void testBookSessionWithNonStudent() {
        Session session = new Session(1, mockTutor, null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Math");
        sessionPersistence.addSession(session);

        sessionHandler.bookASession(mockTutor, 1);

        assertNull(session.getStudent());
    }

}
