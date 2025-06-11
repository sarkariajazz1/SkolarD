package skolard.logic.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.RatingRequestPersistence;
import skolard.persistence.SessionPersistence;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SessionHandlerTest {

    private SessionHandler sessionHandler;
    private SessionPersistence mockSessionPersistence;
    private RatingRequestPersistence mockRatingRequestPersistence;

    @BeforeEach
    public void setup() {
        mockSessionPersistence = mock(SessionPersistence.class);
        mockRatingRequestPersistence = mock(RatingRequestPersistence.class);
        sessionHandler = new SessionHandler(mockSessionPersistence, mockRatingRequestPersistence);
    }

    @Test
    public void testCreateSession() {
        Tutor tutor = mock(Tutor.class);
        when(tutor.getEmail()).thenReturn("tutor@example.com");

        sessionHandler.createSession(tutor,
            LocalDateTime.of(2025, 6, 10, 13, 0),
            LocalDateTime.of(2025, 6, 10, 14, 30),
            "MATH 101");

        verify(mockSessionPersistence).addSession(any(Session.class));
    }

    @Test
    public void testDeleteSession() {
        Tutor tutor = mock(Tutor.class);
        Session session = mock(Session.class);

        when(tutor.getEmail()).thenReturn("tutor@example.com");
        when(session.getSessionId()).thenReturn(42);
        when(mockSessionPersistence.getSessionsByTutorEmail("tutor@example.com"))
            .thenReturn(List.of(session));

        sessionHandler.deleteSession(tutor, session);

        verify(mockSessionPersistence).removeSession(42);
    }

    @Test
    public void testDeleteSession_NotFound() {
        Tutor tutor = mock(Tutor.class);
        Session session = mock(Session.class);

        when(tutor.getEmail()).thenReturn("tutor@example.com");
        when(session.getSessionId()).thenReturn(42);
        when(mockSessionPersistence.getSessionsByTutorEmail("tutor@example.com"))
            .thenReturn(List.of()); // session not found

        assertThrows(IllegalArgumentException.class, () -> sessionHandler.deleteSession(tutor, session));
    }


    @Test
    public void testBookAndUnbookSession() {
        Student student = mock(Student.class);
        Session session = mock(Session.class);
        when(mockSessionPersistence.getSessionById(1)).thenReturn(session);
        when(session.isBooked()).thenReturn(false).thenReturn(true);
        when(session.getStudent()).thenReturn(student);

        sessionHandler.bookASession(student, 1);
        verify(session).bookSession(student);

        sessionHandler.unbookASession(student, 1);
        verify(session).unbookSession(student);
    }

    @Test
    public void testSetStudentSessionLists() {
        Student student = mock(Student.class);
        sessionHandler.setStudentSessionLists(student);
        verify(mockSessionPersistence).hydrateStudentSessions(student);
    }

    @Test
    public void testSetTutorSessionLists() {
        Tutor tutor = mock(Tutor.class);
        sessionHandler.setTutorSessionLists(tutor);
        verify(mockSessionPersistence).hydrateTutorSessions(tutor);
    }

    @Test
    public void testGetSessionsByTutor() {
        Tutor tutor = mock(Tutor.class);
        when(tutor.getEmail()).thenReturn("tutor@example.com");

        sessionHandler.getSessionsByTutor(tutor);
        verify(mockSessionPersistence).getSessionsByTutorEmail("tutor@example.com");
    }

    @Test
    public void testGetSessionByID() {
        Session session = mock(Session.class);
        when(mockSessionPersistence.getSessionById(99)).thenReturn(session);

        sessionHandler.getSessionByID(99);
        verify(mockSessionPersistence).getSessionById(99);
    }
}

