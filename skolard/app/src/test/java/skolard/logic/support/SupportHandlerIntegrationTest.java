package skolard.logic.support;

import org.junit.jupiter.api.*;
import skolard.objects.*;
import skolard.persistence.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SupportHandlerIntegrationTest {

    private Connection conn;
    private SupportHandler supportHandler;
    private SupportPersistence supportPersistence;
    private StudentPersistence studentPersistence;

    @BeforeEach
    void setup() throws Exception {
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, false);
        PersistenceProvider.initializeSqlite(conn);

        supportPersistence = PersistenceRegistry.getSupportPersistence();
        studentPersistence = PersistenceRegistry.getStudentPersistence();
        supportHandler = new SupportHandler(supportPersistence);
    }

    private Student createTestStudent() {
        String email = "student_" + UUID.randomUUID() + "@example.com";
        Student student = new Student("Test Student", email, "hashedPassword");
        studentPersistence.addStudent(student);
        return student;
    }

    private SupportTicket createAndPersistTicket(User requester, String title, String description) {
        SupportTicket ticket = new SupportTicket(requester, title, description);
        supportPersistence.addTicket(ticket);
        return supportPersistence.getActiveTickets().stream()
                .filter(t -> t.getRequester().getEmail().equals(requester.getEmail()) && t.getTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Ticket not found after insert"));
    }

    @Test
    void testSubmitTicket_success() {
        Student student = createTestStudent();
        SupportTicket ticket = new SupportTicket(student, "Login Issue", "Can't log in");

        supportHandler.submitTicket(ticket);

        List<SupportTicket> active = supportHandler.getActiveTickets();
        boolean found = active.stream().anyMatch(t ->
                t.getRequester().getEmail().equals(student.getEmail()) &&
                t.getTitle().equals("Login Issue"));

        assertTrue(found, "Submitted ticket should appear in active tickets.");
    }

    @Test
    void testGetActiveTickets_onlyUnresolved() {
        Student student = createTestStudent();
        SupportTicket ticket = createAndPersistTicket(student, "App Crash", "App crashes on launch");

        List<SupportTicket> active = supportHandler.getActiveTickets();
        boolean found = active.stream().anyMatch(t -> t.getTicketId() == ticket.getTicketId());

        assertTrue(found, "Unresolved ticket should be in active list.");
    }


    @Test
    void testCloseTicket_movesToHandled() {
        Student student = createTestStudent();
        SupportTicket ticket = createAndPersistTicket(student, "Bug Report", "Found a bug in dashboard");

        supportHandler.closeTicket(ticket);

        List<SupportTicket> active = supportHandler.getActiveTickets();
        List<SupportTicket> handled = supportHandler.getHandledTickets();

        assertFalse(active.contains(ticket), "Closed ticket should not be in active list.");
        assertTrue(handled.stream().anyMatch(t -> t.getTicketId() == ticket.getTicketId()), "Closed ticket should appear in handled list.");
    }

    @Test
    void testGetTicketById_returnsCorrectTicket() {
        Student student = createTestStudent();
        SupportTicket ticket = createAndPersistTicket(student, "Feature Request", "Please add dark mode");

        SupportTicket fetched = supportHandler.getTicketById(ticket.getTicketId());

        assertNotNull(fetched);
        assertEquals(ticket.getTicketId(), fetched.getTicketId());
        assertEquals(ticket.getTitle(), fetched.getTitle());
    }

    @Test
    void testSubmitTicket_null_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> supportHandler.submitTicket(null));
    }

    @Test
    void testCloseTicket_nullOrHandled_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> supportHandler.closeTicket(null));

        Student student = createTestStudent();
        SupportTicket ticket = new SupportTicket(1, student, "Closed", "Already handled",
                LocalDateTime.now(), LocalDateTime.now(), true);

        assertThrows(IllegalArgumentException.class, () -> supportHandler.closeTicket(ticket));
    }

    @AfterEach
    void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}

