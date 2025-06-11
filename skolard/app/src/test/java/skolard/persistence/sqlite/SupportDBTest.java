package skolard.persistence.sqlite;

import org.junit.jupiter.api.*;

import skolard.objects.Student;
import skolard.objects.SupportTicket;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.StudentPersistence;
import skolard.persistence.SupportPersistence;
import skolard.persistence.TutorPersistence;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SupportDBTest {

    private Connection connection;
    private SupportDB supportDB;
    private StudentPersistence mockStudentDB;
    private TutorPersistence mockTutorDB;

    private final User mockStudent = new skolard.objects.Student("Student", "student@skolard.ca", "pass");
    private final User mockTutor = new Tutor("Tutor", "tutor@skolard.ca", "bio", "pass", null);

    @BeforeAll
    void initDb() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SchemaInitializer.initializeSchema(connection);
    }

    @BeforeEach
    void init() throws Exception {
        mockStudentDB = mock(StudentPersistence.class);
        mockTutorDB = mock(TutorPersistence.class);
        supportDB = new SupportDB(connection, mockStudentDB, mockTutorDB);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM support_ticket");
        }

        when(mockStudentDB.getStudentByEmail("student@skolard.ca")).thenReturn((Student) mockStudent);
        when(mockTutorDB.getTutorByEmail("tutor@skolard.ca")).thenReturn((Tutor) mockTutor);
    }

    @Test
    void testAddAndGetTicket_student() {
        SupportTicket ticket = new SupportTicket(mockStudent, "Issue A", "Details A");
        supportDB.addTicket(ticket);

        List<SupportTicket> all = supportDB.getAllTickets();
        assertEquals(1, all.size());
        assertEquals("Issue A", all.get(0).getTitle());
    }

    @Test
    void testAddAndGetTicket_tutor() {
        SupportTicket ticket = new SupportTicket(mockTutor, "Issue B", "Details B");
        supportDB.addTicket(ticket);

        List<SupportTicket> all = supportDB.getAllTickets();
        assertEquals(1, all.size());
        assertEquals("Issue B", all.get(0).getTitle());
    }

    @Test
    void testGetActiveAndHandledTickets() {
        SupportTicket active = new SupportTicket(mockStudent, "Active Ticket", "Pending...");
        supportDB.addTicket(active);

        SupportTicket handled = new SupportTicket(mockStudent, "Closed Ticket", "Was fixed");
        handled.closeTicket();
        supportDB.addTicket(handled);

        List<SupportTicket> activeList = supportDB.getActiveTickets();
        List<SupportTicket> handledList = supportDB.getHandledTickets();

        assertEquals(1, activeList.size());
        assertEquals(1, handledList.size());
    }

    @Test
    void testUpdateTicket() {
        SupportTicket ticket = new SupportTicket(mockTutor, "Slow loading", "Page hangs");
        supportDB.addTicket(ticket);

        SupportTicket fetched = supportDB.getAllTickets().get(0);
        assertFalse(fetched.isHandled());

        fetched.closeTicket();
        supportDB.updateTicket(fetched);

        SupportTicket updated = supportDB.getAllTickets().get(0);
        assertTrue(updated.isHandled());
        assertNotNull(updated.getClosedAt());
    }

    @Test
    void testDeleteTicketById() {
        SupportTicket ticket = new SupportTicket(mockStudent, "Delete me", "Just testing");
        supportDB.addTicket(ticket);

        int id = supportDB.getAllTickets().get(0).getTicketId();
        supportDB.deleteTicketById(id);

        assertTrue(supportDB.getAllTickets().isEmpty());
    }

    @Test
    void testGetTicketById_valid() {
        SupportTicket ticket = new SupportTicket(mockTutor, "Find me", "With ID");
        supportDB.addTicket(ticket);

        int id = supportDB.getAllTickets().get(0).getTicketId();
        SupportTicket fetched = supportDB.getTicketById(id);
        assertNotNull(fetched);
        assertEquals("Find me", fetched.getTitle());
    }

    @Test
    void testGetTicketById_invalid() {
        SupportTicket fetched = supportDB.getTicketById(-12345);
        assertNull(fetched);
    }

    @Test
    void testGetTicketsByQuery_roleFallback() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("""
            INSERT INTO support_ticket (requester_email, requester_role, title, description, created_at, closed_at, is_handled)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """)) {
            stmt.setString(1, "ghost@skolard.ca");
            stmt.setString(2, "alien");
            stmt.setString(3, "Bad role");
            stmt.setString(4, "Invalid role test");
            stmt.setString(5, LocalDateTime.now().toString());
            stmt.setString(6, null);
            stmt.setInt(7, 0);
            stmt.executeUpdate();
        }

        List<SupportTicket> result = supportDB.getAllTickets();
        assertTrue(result.isEmpty()); // due to unknown requester_role
    }
}
