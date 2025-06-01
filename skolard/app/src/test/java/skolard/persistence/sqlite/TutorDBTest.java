package skolard.persistence.sqlite;

import org.junit.jupiter.api.*;
import skolard.objects.Tutor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TutorDBTest {

    private static Connection connection;
    private TutorDB tutorDB;

    @BeforeAll
    static void initDB() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SchemaInitializer.initializeSchema(connection); // Use real schema with password column
    }

    @BeforeEach
    void setUp() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM tutor");
        }
        tutorDB = new TutorDB(connection);
    }

    // Helper to insert a tutor with password manually (bypassing LoginHandler)
    private void insertTutorWithPassword(String name, String email, String bio, String password) throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO tutor (name, email, bio, password) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, bio);
            stmt.setString(4, password);
            stmt.executeUpdate();
        }
    }

    @Test
    void testAddAndGetTutor() {
        Tutor tutor = new Tutor("Amrit Singh", "amrit@skolard.ca", "Math Tutor");
        assertThrows(RuntimeException.class, () -> tutorDB.addTutor(tutor));
        // TutorDB.addTutor fails unless LoginHandler sets password; expected
    }

    @Test
    void testGetTutorByEmail_NotFound() {
        Tutor result = tutorDB.getTutorByEmail("nonexistent@skolard.ca");
        assertNull(result);
    }

    @Test
    void testDeleteTutorByEmail() throws Exception {
        insertTutorWithPassword("Simran Dhillon", "simran@skolard.ca", "Physics Tutor", "pass123");

        tutorDB.deleteTutorByEmail("simran@skolard.ca");
        assertNull(tutorDB.getTutorByEmail("simran@skolard.ca"));
    }

    @Test
    void testDeleteTutor_SQLExceptionHandled() {
        assertDoesNotThrow(() -> tutorDB.deleteTutorByEmail("ghost@skolard.ca"));
    }

    @Test
    void testUpdateTutor() throws Exception {
        insertTutorWithPassword("Raj Gill", "raj@skolard.ca", "Chemistry Tutor", "pass456");

        Tutor updated = new Tutor("Raj Gill", "raj@skolard.ca", "Organic Chemistry Expert");
        tutorDB.updateTutor(updated);

        Tutor fetched = tutorDB.getTutorByEmail("raj@skolard.ca");
        assertEquals("Organic Chemistry Expert", fetched.getBio());
    }

    @Test
    void testUpdateTutor_SQLExceptionHandled() {
        Tutor ghost = new Tutor("Ghost", "ghost@skolard.ca", "Does not exist");
        assertDoesNotThrow(() -> tutorDB.updateTutor(ghost));
    }

    @Test
    void testAddTutor_SQLException() {
        Tutor t1 = new Tutor("Amrit", "duplicate@skolard.ca", "Bio1");

        // Pre-insert to trigger duplicate constraint
        assertDoesNotThrow(() -> insertTutorWithPassword("Amrit", "duplicate@skolard.ca", "Bio1", "testpass"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> tutorDB.addTutor(t1));
        assertTrue(exception.getMessage().contains("Error adding tutor"));
    }

    @Test
    void testAuthenticate_Success() throws Exception {
        insertTutorWithPassword("Valid User", "valid@skolard.ca", "Bio", "hashedpass123");

        Tutor result = tutorDB.authenticate("valid@skolard.ca", "hashedpass123");
        assertNotNull(result);
        assertEquals("Valid User", result.getName());
    }

    @Test
    void testAuthenticate_WrongPassword() throws Exception {
        insertTutorWithPassword("Wrong Password User", "wrongpass@skolard.ca", "Bio", "realpassword");

        Tutor result = tutorDB.authenticate("wrongpass@skolard.ca", "fakepassword");
        assertNull(result);
    }

    @Test
    void testAuthenticate_UserNotFound() {
        Tutor result = tutorDB.authenticate("ghost@skolard.ca", "doesntmatter");
        assertNull(result);
    }

    @Test
    void testGetAllTutors() throws Exception {
        insertTutorWithPassword("Tutor A", "a@skolard.ca", "Bio A", "p1");
        insertTutorWithPassword("Tutor B", "b@skolard.ca", "Bio B", "p2");

        List<Tutor> tutors = tutorDB.getAllTutors();
        assertEquals(2, tutors.size());
    }
}
