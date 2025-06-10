package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.SupportTicket;
import skolard.objects.Student;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupportStubTest {

    private SupportStub supportStub;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        supportStub = new SupportStub();
        testStudent = new Student("student@skolard.ca", "Test Student");
    }

    @Test
    void testAddAndGetTicketById() {
        SupportTicket ticket = new SupportTicket(999, testStudent, "Login Issue", "Can't log in",
                LocalDateTime.now(), null, false);
        SupportTicket added = supportStub.addTicket(ticket);

        SupportTicket retrieved = supportStub.getTicketById(added.getTicketId());
        assertNotNull(retrieved);
        assertEquals("Login Issue", retrieved.getTitle());
        assertEquals(added.getTicketId(), retrieved.getTicketId());
    }

    @Test
    void testUpdateTicket() {
        SupportTicket original = new SupportTicket(999, testStudent, "Bug", "App crashes",
                LocalDateTime.now(), null, false);
        SupportTicket added = supportStub.addTicket(original);

        SupportTicket updated = new SupportTicket(
                added.getTicketId(),
                testStudent,
                "Bug Fixed",
                "App no longer crashes",
                added.getCreatedAt(),
                LocalDateTime.now(),
                true
        );

        supportStub.updateTicket(updated);
        SupportTicket result = supportStub.getTicketById(added.getTicketId());

        assertEquals("Bug Fixed", result.getTitle());
        assertTrue(result.isHandled());
    }

    @Test
    void testDeleteTicketById() {
        SupportTicket ticket = new SupportTicket(999, testStudent, "Delete Me", "Please delete",
                LocalDateTime.now(), null, false);
        SupportTicket added = supportStub.addTicket(ticket);

        supportStub.deleteTicketById(added.getTicketId());
        assertNull(supportStub.getTicketById(added.getTicketId()));
    }

    @Test
    void testGetAllTickets() {
        supportStub.addTicket(new SupportTicket(0, testStudent, "A", "desc", LocalDateTime.now(), null, false));
        supportStub.addTicket(new SupportTicket(0, testStudent, "B", "desc", LocalDateTime.now(), null, false));

        List<SupportTicket> all = supportStub.getAllTickets();
        assertEquals(2, all.size());
    }

    @Test
    void testGetActiveTickets() {
        supportStub.addTicket(new SupportTicket(0, testStudent, "Active", "desc", LocalDateTime.now(), null, false));
        supportStub.addTicket(new SupportTicket(0, testStudent, "Handled", "desc", LocalDateTime.now(), LocalDateTime.now(), true));

        List<SupportTicket> active = supportStub.getActiveTickets();
        assertEquals(1, active.size());
        assertFalse(active.get(0).isHandled());
    }

    @Test
    void testGetHandledTickets() {
        supportStub.addTicket(new SupportTicket(0, testStudent, "Active", "desc", LocalDateTime.now(), null, false));
        supportStub.addTicket(new SupportTicket(0, testStudent, "Handled", "desc", LocalDateTime.now(), LocalDateTime.now(), true));

        List<SupportTicket> handled = supportStub.getHandledTickets();
        assertEquals(1, handled.size());
        assertTrue(handled.get(0).isHandled());
    }
}
