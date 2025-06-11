package skolard.logic.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class SessionHandlerTest {

    private SessionHandler sessionHandler;
    private SessionPersistence mockPersistence;

    @BeforeEach
    public void setup() {
        mockPersistence = mock(SessionPersistence.class);
        sessionHandler = new SessionHandler(mockPersistence);
    }

    @Test
    public void testCreateSession() {
        Tutor tutor = mock(Tutor.class);
        when(tutor.getEmail()).thenReturn("tutor@example.com");

        sessionHandler.createSession(tutor,
            LocalDateTime.of(2025, 6, 10, 13, 0),
            LocalDateTime.of(2025, 6, 10, 14, 30),
            "MATH 101");

        verify(mockPersistence).addSession(any(Session.class));
    }

    @Test
    public void testBookAndUnbookSession() {
        Student student = mock(Student.class);
        Session session = mock(Session.class);
        when(mockPersistence.getSessionById(1)).thenReturn(session);
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
        verify(mockPersistence).hydrateStudentSessions(student);
    }

    @Test
    public void testSetTutorSessionLists() {
        Tutor tutor = mock(Tutor.class);
        sessionHandler.setTutorSessionLists(tutor);
        verify(mockPersistence).hydrateTutorSessions(tutor);
    }

    @Test
    public void testGetSessionsByTutor() {
        Tutor tutor = mock(Tutor.class);
        when(tutor.getEmail()).thenReturn("tutor@example.com");

        sessionHandler.getSessionsByTutor(tutor);
        verify(mockPersistence).getSessionsByTutorEmail("tutor@example.com");
    }

    @Test
    public void testGetSessionByID() {
        Session session = mock(Session.class);
        when(mockPersistence.getSessionById(99)).thenReturn(session);

        sessionHandler.getSessionByID(99);
        verify(mockPersistence).getSessionById(99);
    }
}
