package skolard.logic.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.SessionPersistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionBookingTest {

    private SessionPersistence mockPersistence;
    private SessionBooking booking;

    @BeforeEach
    public void setup() {
        mockPersistence = mock(SessionPersistence.class);
        booking = new SessionBooking(mockPersistence);
    }

    @Test
    public void testBookASession_Success() {
        Student student = mock(Student.class);
        Session session = mock(Session.class);

        when(session.isBooked()).thenReturn(false);
        when(mockPersistence.getSessionById(1)).thenReturn(session);

        booking.bookASession(student, 1);

        verify(session).bookSession(student);
        verify(mockPersistence).updateSession(session);
    }

    @Test
    public void testBookASession_AlreadyBookedBySameStudent() {
        Student student = mock(Student.class);
        Session session = mock(Session.class);

        when(session.isBooked()).thenReturn(true);
        when(session.getStudent()).thenReturn(student);
        when(mockPersistence.getSessionById(1)).thenReturn(session);

        assertThrows(IllegalArgumentException.class, () -> booking.bookASession(student, 1));
    }

    @Test
    public void testBookASession_AlreadyBookedByDifferentStudent() {
        Student student1 = mock(Student.class);
        Student student2 = mock(Student.class);
        Session session = mock(Session.class);

        when(session.isBooked()).thenReturn(true);
        when(session.getStudent()).thenReturn(student1);
        when(mockPersistence.getSessionById(1)).thenReturn(session);

        assertThrows(IllegalArgumentException.class, () -> booking.bookASession(student2, 1));
    }

    @Test
    public void testUnbookASession_Success() {
        Student student = mock(Student.class);
        Session session = mock(Session.class);

        when(session.isBooked()).thenReturn(true);
        when(mockPersistence.getSessionById(1)).thenReturn(session);

        booking.unbookASession(student, 1);

        verify(session).unbookSession(student);
        verify(mockPersistence).updateSession(session);
    }

    @Test
    public void testUnbookASession_NotBooked() {
        Student student = mock(Student.class);
        Session session = mock(Session.class);

        when(session.isBooked()).thenReturn(false);
        when(mockPersistence.getSessionById(1)).thenReturn(session);

        assertThrows(IllegalArgumentException.class, () -> booking.unbookASession(student, 1));
    }
}
