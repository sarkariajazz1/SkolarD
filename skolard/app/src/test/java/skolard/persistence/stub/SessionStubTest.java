package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.utils.PasswordUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SessionStubTest {

    private SessionStub sessionStub;

    @BeforeEach
    void setUp() {
        sessionStub = new SessionStub();
    }

    @Test
    void testAddSession() {
        Tutor tutor = new Tutor("Test Tutor", "test@skolard.ca", PasswordUtil.hash("test123"), "Test Subject", Map.of("TEST 1000", 5.0));
        Student student = new Student("Test Student", "student@skolard.ca", PasswordUtil.hash("student123"));
        Session session = new Session(-1, tutor, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "TEST 1000");

        Session added = sessionStub.addSession(session);
        assertNotNull(added);
        assertEquals("Test Tutor", added.getTutor().getName());
        assertTrue(sessionStub.getAllSessions().contains(added));
    }

    @Test
    void testGetSessionById() {
        List<Session> sessions = sessionStub.getAllSessions();
        Session session = sessionStub.getSessionById(sessions.get(0).getSessionId());
        assertNotNull(session);
        assertEquals(sessions.get(0).getSessionId(), session.getSessionId());
    }

    @Test
    void testGetSessionsByTutorEmail() {
        List<Session> sessions = sessionStub.getSessionsByTutorEmail("amrit@skolard.ca");
        assertFalse(sessions.isEmpty());
        for (Session s : sessions) {
            assertEquals("amrit@skolard.ca", s.getTutor().getEmail());
        }
    }

    @Test
    void testGetSessionsByStudentEmail() {
        List<Session> sessions = sessionStub.getSessionsByStudentEmail("simran@skolard.ca");
        assertFalse(sessions.isEmpty());
        for (Session s : sessions) {
            assertNotNull(s.getStudent());
            assertEquals("simran@skolard.ca", s.getStudent().getEmail());
        }
    }

    @Test
    void testRemoveSession() {
        Session session = sessionStub.getAllSessions().get(0);
        sessionStub.removeSession(session.getSessionId());
        assertNull(sessionStub.getSessionById(session.getSessionId()));
    }

    @Test
    void testUpdateSession() {
        Session session = sessionStub.getAllSessions().get(0);
        Session updated = new Session(session.getSessionId(), session.getTutor(), session.getStudent(),
                session.getStartDateTime().plusDays(1), session.getEndDateTime().plusDays(1), session.getCourseName());

        sessionStub.updateSession(updated);
        Session fetched = sessionStub.getSessionById(session.getSessionId());
        assertEquals(updated.getStartDateTime(), fetched.getStartDateTime());
    }

    @Test
    void testAddNullSessionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> sessionStub.addSession(null));
    }

    @Test
    void testAddDuplicateSessionThrowsException() {
        Session session = sessionStub.getAllSessions().get(0);
        assertThrows(IllegalArgumentException.class, () -> sessionStub.addSession(session));
    }

    @Test
    void testRemoveNonExistentSessionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> sessionStub.removeSession(9999));
    }

    @Test
    void testUpdateNonExistentSessionDoesNothing() {
        Tutor tutor = new Tutor("Ghost Tutor", "ghost@skolard.ca", PasswordUtil.hash("ghost123"), "Ghost Subject", Map.of());
        Student student = new Student("Ghost Student", "ghost@student.ca", PasswordUtil.hash("ghost123"));
        Session ghostSession = new Session(9999, tutor, student,
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), "GHOST 0000");

        sessionStub.updateSession(ghostSession);
        assertNull(sessionStub.getSessionById(9999));
    }

    @Test
    void testGetSessionsWithNullStudent() {
        List<Session> allSessions = sessionStub.getAllSessions();
        boolean hasNullStudent = allSessions.stream().anyMatch(s -> s.getStudent() == null);
        assertTrue(hasNullStudent);

        List<Session> sessions = sessionStub.getSessionsByStudentEmail("nonexistent@student.ca");
        assertTrue(sessions.isEmpty());
    }

    @Test
    void testHydrateTutorSessions() {
        Tutor tutor = new Tutor("Amrit Singh", "amrit@skolard.ca", PasswordUtil.hash("amrit123"), "CS & Math Tutor", Map.of());
        sessionStub.hydrateTutorSessions(tutor);
        assertNotNull(tutor.getPastSessions());
        assertNotNull(tutor.getUpcomingSessions());
    }

    @Test
    void testHydrateStudentSessions() {
        Student student = new Student("Simran Dhillon", "simran@skolard.ca", PasswordUtil.hash("simran123"));
        sessionStub.hydrateStudentSessions(student);
        assertNotNull(student.getPastSessions());
        assertNotNull(student.getUpcomingSessions());
    }
}


