package skolard.logic.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SessionAccessTest {

    private SessionPersistence mockPersistence;
    private SessionAccess access;

    @BeforeEach
    public void setup() {
        mockPersistence = mock(SessionPersistence.class);
        access = new SessionAccess(mockPersistence);
    }

    @Test
    public void testSetStudentSessionLists() {
        Student student = mock(Student.class);
        access.setStudentSessionLists(student);
        verify(mockPersistence).hydrateStudentSessions(student);
    }

    @Test
    public void testSetTutorSessionLists() {
        Tutor tutor = mock(Tutor.class);
        access.setTutorSessionLists(tutor);
        verify(mockPersistence).hydrateTutorSessions(tutor);
    }

    @Test
    public void testGetSessionsByTutor() {
        Tutor tutor = mock(Tutor.class);
        when(tutor.getEmail()).thenReturn("tutor@test.com");

        Session session = mock(Session.class);
        when(mockPersistence.getSessionsByTutorEmail("tutor@test.com"))
            .thenReturn(List.of(session));

        List<Session> result = access.getSessionsByTutor(tutor);

        assertEquals(1, result.size());
        assertSame(session, result.get(0));
    }

    @Test
    public void testGetSessionByID() {
        Session session = mock(Session.class);
        when(mockPersistence.getSessionById(5)).thenReturn(session);

        Session result = access.getSessionByID(5);
        assertSame(session, result);
    }
}
