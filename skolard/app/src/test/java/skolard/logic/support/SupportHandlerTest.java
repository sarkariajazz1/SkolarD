package skolard.logic.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.SupportTicket;
import skolard.persistence.SupportPersistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupportHandlerTest {

    private SupportPersistence mockPersistence;
    private SupportHandler supportHandler;

    @BeforeEach
    public void setup() {
        mockPersistence = mock(SupportPersistence.class);
        supportHandler = new SupportHandler(mockPersistence);
    }

    @Test
    public void testConstructor_NullPersistence_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new SupportHandler(null));
    }

    @Test
    public void testSubmitTicket_Valid() {
        SupportTicket ticket = mock(SupportTicket.class);

        supportHandler.submitTicket(ticket);

        verify(mockPersistence).addTicket(ticket);
    }

    @Test
    public void testSubmitTicket_Null_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supportHandler.submitTicket(null));
    }

    @Test
    public void testGetActiveTickets() {
        List<SupportTicket> expected = List.of(mock(SupportTicket.class));
        when(mockPersistence.getActiveTickets()).thenReturn(expected);

        List<SupportTicket> actual = supportHandler.getActiveTickets();

        assertEquals(expected, actual);
        verify(mockPersistence).getActiveTickets();
    }

    @Test
    public void testGetHandledTickets() {
        List<SupportTicket> expected = List.of(mock(SupportTicket.class));
        when(mockPersistence.getHandledTickets()).thenReturn(expected);

        List<SupportTicket> actual = supportHandler.getHandledTickets();

        assertEquals(expected, actual);
        verify(mockPersistence).getHandledTickets();
    }

    @Test
    public void testCloseTicket_Success() {
        SupportTicket ticket = mock(SupportTicket.class);
        when(ticket.isHandled()).thenReturn(false);

        supportHandler.closeTicket(ticket);

        verify(ticket).closeTicket();
        verify(mockPersistence).updateTicket(ticket);
    }

    @Test
    public void testCloseTicket_AlreadyHandled_ThrowsException() {
        SupportTicket ticket = mock(SupportTicket.class);
        when(ticket.isHandled()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> supportHandler.closeTicket(ticket));
    }

    @Test
    public void testCloseTicket_Null_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supportHandler.closeTicket(null));
    }

    @Test
    public void testGetTicketById() {
        SupportTicket ticket = mock(SupportTicket.class);
        when(mockPersistence.getTicketById(7)).thenReturn(ticket);

        SupportTicket result = supportHandler.getTicketById(7);

        assertEquals(ticket, result);
        verify(mockPersistence).getTicketById(7);
    }
}
