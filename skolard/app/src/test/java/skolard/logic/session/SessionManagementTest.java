package skolard.logic.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Session;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionManagementTest {

    private SessionPersistence mockPersistence;
    private SessionManagement management;
    private Tutor tutor;

    @BeforeEach
    public void setup() {
        mockPersistence = mock(SessionPersistence.class);
        management = new SessionManagement(mockPersistence);
        tutor = mock(Tutor.class);
        when(tutor.getEmail()).thenReturn("tutor@example.com");
    }

    @Test
    public void testCreateSession_Success() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 10, 12, 0);
        when(mockPersistence.getSessionsByTutorEmail(any())).thenReturn(List.of());

        management.createSession(tutor, start, end, "COMP 3010");

        verify(mockPersistence).addSession(any(Session.class));
    }

    @Test
    public void testCreateSession_TooLongDuration() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 10, 15, 0); // 5 hours

        assertThrows(IllegalArgumentException.class, () ->
            management.createSession(tutor, start, end, "COMP 3010"));
    }

    @Test
    public void testCreateSession_Conflict() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 10, 12, 0);

        Session existing = mock(Session.class);
        when(existing.getStartDateTime()).thenReturn(start.minusMinutes(30));
        when(existing.getEndDateTime()).thenReturn(end.plusMinutes(30));
        when(mockPersistence.getSessionsByTutorEmail(any())).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class, () ->
            management.createSession(tutor, start, end, "COMP 3010"));
    }

    @Test
    public void testDeleteSession_Success() {
        Session session = new Session(42, tutor, null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP 3010");
        when(mockPersistence.getSessionsByTutorEmail(any()))
            .thenReturn(List.of(session));

        management.deleteSession(tutor, session);

        verify(mockPersistence).removeSession(42);
    }

    @Test
    public void testDeleteSession_NotFound() {
        Session session = new Session(99, tutor, null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP 3010");
        when(mockPersistence.getSessionsByTutorEmail(any()))
            .thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () ->
            management.deleteSession(tutor, session));
    }
}
