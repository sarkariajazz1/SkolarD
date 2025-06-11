package skolard.persistence.sqlite;

import org.junit.jupiter.api.*;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.stub.StudentStub;
import skolard.persistence.stub.TutorStub;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionDBTest {

    private static Connection connection;
    private SessionDB sessionDB;
    private Tutor tutor;
    private Student student;

    @BeforeAll
    static void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE session (id INTEGER PRIMARY KEY, tutorEmail TEXT, studentEmail TEXT, startTime TEXT, endTime TEXT, courseID TEXT);");
        }
    }

    @BeforeEach
    void init() {
        sessionDB = new SessionDB(connection, new StudentStub(), new TutorStub());
        tutor = new Tutor("Test Tutor", "tutor@skolard.ca", "Bio");
        student = new Student("Test Student", "student@skolard.ca");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM session");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddAndRetrieveSession() {
        Session session = new Session(1, tutor, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP1010");
        sessionDB.addSession(session);

        Session result = sessionDB.getSessionById(1);
        assertNotNull(result);
        assertEquals("COMP1010", result.getCourseName());
    }

    @Test
    void testAddUnbookedSession() {
        Session session = new Session(-1, tutor, null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP3010");
        Session added = sessionDB.addSession(session);

        assertNotNull(added);
        assertNull(added.getStudent());
        assertEquals("COMP3010", added.getCourseName());
    }

    @Test
    void testUpdateSession() {
        Session original = new Session(-1, tutor, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP1010");
        original = sessionDB.addSession(original);

        Session updated = new Session(original.getSessionId(), tutor, student, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "MATH2020");
        sessionDB.updateSession(updated);

        Session fetched = sessionDB.getSessionById(updated.getSessionId());
        assertEquals("MATH2020", fetched.getCourseName());
    }

    @Test
    void testUnbookSession() {
        Session session = new Session(-1, tutor, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP3010");
        session = sessionDB.addSession(session);

        session = new Session(session.getSessionId(), tutor, null, session.getStartDateTime(), session.getEndDateTime(), session.getCourseName());
        sessionDB.updateSession(session);

        Session updated = sessionDB.getSessionById(session.getSessionId());
        assertNull(updated.getStudent());
    }

    @Test
    void testRemoveSession() {
        Session session = new Session(3, tutor, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP1010");
        sessionDB.addSession(session);

        sessionDB.removeSession(3);
        assertNull(sessionDB.getSessionById(3));
    }

    @Test
    void testGetSessionByIdNotFound() {
        assertNull(sessionDB.getSessionById(999));
    }

    @Test
    void testGetAllSessions() {
        Session s1 = new Session(10, tutor, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "BIO1110");
        Session s2 = new Session(11, tutor, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "MATH2030");
        sessionDB.addSession(s1);
        sessionDB.addSession(s2);

        List<Session> sessions = sessionDB.getAllSessions();
        assertEquals(2, sessions.size());
    }

    @Test
    void testGetSessionsByTutorEmail() {
        Session s1 = new Session(20, tutor, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "PHYS1000");
        sessionDB.addSession(s1);

        List<Session> sessions = sessionDB.getSessionsByTutorEmail("tutor@skolard.ca");
        assertEquals(1, sessions.size());
    }

    @Test
    void testGetSessionsByStudentEmail() {
        Session s1 = new Session(-1, tutor, null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "CHEM1110");
        s1 = sessionDB.addSession(s1);
        s1.bookSession(student);
        sessionDB.updateSession(s1);

        List<Session> sessions = sessionDB.getSessionsByStudentEmail("student@skolard.ca");
        assertEquals(1, sessions.size());
    }

    @Test
    void testGetSessionsByTutorEmailNoneFound() {
        List<Session> sessions = sessionDB.getSessionsByTutorEmail("notfound@skolard.ca");
        assertTrue(sessions.isEmpty());
    }

    @Test
    void testGetSessionsByStudentEmailNoneFound() {
        List<Session> sessions = sessionDB.getSessionsByStudentEmail("missing@student.ca");
        assertTrue(sessions.isEmpty());
    }

    @Test
    void testHydrateTutorSessions() {
        Session past = new Session(-1, tutor, student, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), "HIST1000");
        Session future = new Session(-1, tutor, student, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "HIST2000");
        sessionDB.addSession(past);
        sessionDB.addSession(future);

        sessionDB.hydrateTutorSessions(tutor);

        assertEquals(1, tutor.getPastSessions().size());
        assertEquals(1, tutor.getUpcomingSessions().size());
    }

    @Test
    void testHydrateStudentSessions() {
        LocalDateTime now = LocalDateTime.now();

        Session past = new Session(-1, tutor, null, now.minusDays(2), now.minusDays(1), "ENG1000");
        Session future = new Session(-1, tutor, null, now.plusDays(1), now.plusDays(2), "ENG2000");

        past = sessionDB.addSession(past);
        future = sessionDB.addSession(future);

        // Book the sessions for the student
        past.bookSession(student);
        future.bookSession(student);
        sessionDB.updateSession(past);
        sessionDB.updateSession(future);

        sessionDB.hydrateStudentSessions(student);

        assertEquals(1, student.getPastSessions().size());
        assertEquals(1, student.getUpcomingSessions().size());
    }



    @Test
    void testAddSessionWithNullTutorThrows() {
        Session session = new Session(-1, null, student, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP4040");
        assertThrows(RuntimeException.class, () -> sessionDB.addSession(session));
    }
}
