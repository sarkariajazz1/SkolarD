package skolard.logic.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.RatingRequest;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.RatingRequestPersistence;
import skolard.persistence.SessionPersistence;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionBookingTest {

    private SessionPersistence mockPersistence;
    private RatingRequestPersistence mockRequestPersistence;
    private SessionBooking booking;

    @BeforeEach
    public void setup() {
        mockPersistence = mock(SessionPersistence.class);
        mockRequestPersistence = mock(RatingRequestPersistence.class);
        booking = new SessionBooking(mockPersistence, mockRequestPersistence);
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
        RatingRequest request1 = mock(RatingRequest.class);
        RatingRequest request2 = mock(RatingRequest.class);
        List<RatingRequest> requests = Arrays.asList(request1, request2);

        when(session.isBooked()).thenReturn(true);
        when(mockPersistence.getSessionById(1)).thenReturn(session);
        when(mockRequestPersistence.getPendingSessionRequest(1)).thenReturn(requests);

        booking.unbookASession(student, 1);

        verify(session).unbookSession(student);
        verify(mockPersistence).updateSession(session);
        verify(mockRequestPersistence).getPendingSessionRequest(1);
        verify(request1).skip();
        verify(request2).skip();
        verify(mockRequestPersistence).updateRequest(request1);
        verify(mockRequestPersistence).updateRequest(request2);
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

