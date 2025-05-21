
package skolard.logic;

import org.junit.Before;
import org.junit.Test;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class MatchingHandlerTest {

    private MatchingHandler handler;
    private Tutor tutor;
    private Student student;
    private Session session1;
    private Session session2;

    @Before
    public void setUp() {
        handler = new MatchingHandler();

        tutor = new Tutor("t1", "Tutor One", "tutor1@email.com", "Bio");
        tutor.addCourseGrade("COMP1010", "4.0");

        student = new Student("s1", "Student One", "student1@email.com");

        session1 = new Session("sess1", tutor, null,
                LocalDateTime.of(2025, 5, 20, 10, 0),
                LocalDateTime.of(2025, 5, 20, 11, 0),
                "COMP1010");

        session2 = new Session("sess2", tutor, null,
                LocalDateTime.of(2025, 5, 21, 12, 0),
                LocalDateTime.of(2025, 5, 21, 13, 0),
                "COMP1010");
    }

    @Test
    public void testAddAndRetrieveAvailableSessions() {
        handler.addSession(session1);
        handler.addSession(session2);

        List<Session> result = handler.getAvailableSessions("COMP1010");

        assertEquals(2, result.size());
        assertTrue(result.contains(session1));
        assertTrue(result.contains(session2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullSessionThrowsException() {
        handler.addSession(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAvailableSessions_NullCourseName() {
        handler.getAvailableSessions(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAvailableSessions_EmptyCourseName() {
        handler.getAvailableSessions("");
    }

    @Test
    public void testGetAvailableSessions_FilterBooked() {
        session2.bookSession(student);
        handler.addSession(session1);
        handler.addSession(session2);

        List<Session> available = handler.getAvailableSessions("COMP1010");

        assertEquals(1, available.size());
        assertTrue(available.contains(session1));
        assertFalse(available.contains(session2));
    }

    @Test
    public void testBookSessionSuccessfully() {
        handler.addSession(session1);
        handler.bookSession(session1, student);

        assertTrue(session1.isBooked());
        assertEquals(student, session1.getStudent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookSession_NullArguments() {
        handler.bookSession(null, student);
    }

    @Test
    public void testClearSessions() {
        handler.addSession(session1);
        handler.addSession(session2);

        handler.clearSessions();
        List<Session> result = handler.getAvailableSessions("COMP1010");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testToStringReflectsSessions() {
        handler.addSession(session1);
        String output = handler.toString();
        assertTrue(output.contains("sess1"));
    }
}
