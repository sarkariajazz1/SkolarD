package skolard.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionTest {

    private Session session;
    private Tutor mockTutor;
    private Student mockStudent;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    public void setup() {
        mockTutor = mock(Tutor.class);
        mockStudent = mock(Student.class);
        start = LocalDateTime.now();
        end = start.plusHours(2);

        session = new Session(101, mockTutor, null, start, end, "Math 101");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(101, session.getSessionId());
        assertEquals(mockTutor, session.getTutor());
        assertNull(session.getStudent());
        assertEquals(start, session.getStartDateTime());
        assertEquals(end, session.getEndDateTime());
        assertEquals("Math 101", session.getCourseName());
    }

    @Test
    public void testSetters() {
        Student newStudent = mock(Student.class);
        Tutor newTutor = mock(Tutor.class);
        LocalDateTime newStart = start.plusDays(1);
        LocalDateTime newEnd = end.plusDays(1);

        session.setSessionId(202);
        session.setStudent(newStudent);
        session.setTutor(newTutor);
        session.setStartDateTime(newStart);
        session.setEndDateTime(newEnd);
        session.setCourseName("Physics");

        assertEquals(202, session.getSessionId());
        assertEquals(newStudent, session.getStudent());
        assertEquals(newTutor, session.getTutor());
        assertEquals(newStart, session.getStartDateTime());
        assertEquals(newEnd, session.getEndDateTime());
        assertEquals("Physics", session.getCourseName());
    }

    @Test
    public void testIsBookedFalseInitially() {
        assertFalse(session.isBooked());
    }

    @Test
    public void testBookSessionSuccess() {
        when(mockStudent.getName()).thenReturn("Alice");

        session.bookSession(mockStudent);

        assertTrue(session.isBooked());
        assertEquals(mockStudent, session.getStudent());
        verify(mockTutor).addUpcomingSession(session);
        verify(mockStudent).addUpcomingSession(session);
    }

    @Test
    public void testBookSessionAlreadyBookedThrowsException() {
        session.setStudent(mockStudent);
        assertThrows(IllegalStateException.class, () -> session.bookSession(mockStudent));
    }

    @Test
    public void testUnbookSessionSuccess() {
        when(mockStudent.getEmail()).thenReturn("alice@example.com");
        when(mockStudent.getName()).thenReturn("Alice");

        session.setStudent(mockStudent);
        session.unbookSession(mockStudent);

        assertFalse(session.isBooked());
        assertNull(session.getStudent());
        verify(mockStudent).removeUpcomingSession(session);
    }

    @Test
    public void testUnbookSession_NotBooked_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> session.unbookSession(mockStudent));
    }

    @Test
    public void testUnbookSession_DifferentStudent_ThrowsException() {
        Student differentStudent = mock(Student.class);
        when(mockStudent.getEmail()).thenReturn("alice@example.com");
        when(differentStudent.getEmail()).thenReturn("bob@example.com");

        session.setStudent(mockStudent);
        assertThrows(IllegalArgumentException.class, () -> session.unbookSession(differentStudent));
    }
}
