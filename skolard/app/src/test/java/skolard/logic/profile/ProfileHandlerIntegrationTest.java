package skolard.logic.profile;

import org.junit.jupiter.api.*;
import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.*;

import java.sql.Connection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileHandlerIntegrationTest {

    private Connection conn;
    private ProfileHandler profileHandler;

    private StudentPersistence studentPersistence;
    private TutorPersistence tutorPersistence;
    private SessionHandler sessionHandler;

    @BeforeAll
    void setup() throws Exception {
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, false);
        PersistenceProvider.initializeSqlite(conn);

        studentPersistence = PersistenceRegistry.getStudentPersistence();
        tutorPersistence = PersistenceRegistry.getTutorPersistence();
        SessionPersistence sessionPersistence = PersistenceRegistry.getSessionPersistence();
        RatingRequestPersistence ratingRequestPersistence = PersistenceRegistry.getRatingRequestPersistence();

        sessionHandler = new SessionHandler(sessionPersistence, ratingRequestPersistence);
        profileHandler = new ProfileHandler(studentPersistence, tutorPersistence, sessionHandler);

        // Add necessary users
        profileHandler.addStudent("Alice", "alice@example.com", "hashed123");
        profileHandler.addTutor("Bob", "bob@example.com", "hashed456");
    }

    @BeforeEach
    void resetData() {
        tutorPersistence.deleteTutorByEmail("bob@example.com");
        studentPersistence.deleteStudentByEmail("alice@example.com");

        profileHandler.addStudent("Alice", "alice@example.com", "hashed123");
        profileHandler.addTutor("Bob", "bob@example.com", "hashed456");
    }


    @Test
    void testAddAndRetrieveStudent() {
        Student student = profileHandler.getStudent("alice@example.com");
        assertNotNull(student);
        assertEquals("Alice", student.getName());
    }

    @Test
    void testAddAndRetrieveTutor() {
        Tutor tutor = profileHandler.getTutor("bob@example.com");
        assertNotNull(tutor);
        assertEquals("Bob", tutor.getName());
        assertEquals("Edit your bio...", tutor.getBio());
    }

    @Test
    void testUpdateTutorBio() {
        Tutor tutor = profileHandler.getTutor("bob@example.com");
        profileHandler.updateBio(tutor, "I love math!");

        Tutor updated = profileHandler.getTutor("bob@example.com");
        assertEquals("I love math!", updated.getBio());
    }

    @Test
    void testAddAndRemoveCourse() {
        Tutor tutor = profileHandler.getTutor("bob@example.com");

        profileHandler.addCourse(tutor, "MATH101", 4.5);
        Tutor updated = profileHandler.getTutor("bob@example.com");

        Map<String, Double> courses = updated.getCoursesWithGrades();
        assertTrue(courses.containsKey("MATH101"));
        assertEquals(4.5, courses.get("MATH101"));

        profileHandler.removeCourse(updated, "MATH101");
        Tutor cleaned = profileHandler.getTutor("bob@example.com");
        assertFalse(cleaned.getCoursesWithGrades().containsKey("MATH101"));
    }


    @Test
    void testViewBasicAndFullProfiles() {
        Student student = profileHandler.getStudent("alice@example.com");
        String basicStudent = profileHandler.viewBasicProfile(student);
        String fullStudent = profileHandler.viewFullProfile(student);

        assertTrue(basicStudent.contains("Alice"));
        assertTrue(fullStudent.contains("Upcoming Sessions"));

        Tutor tutor = profileHandler.getTutor("bob@example.com");
        String basicTutor = profileHandler.viewBasicProfile(tutor);
        String fullTutor = profileHandler.viewFullProfile(tutor);

        assertTrue(basicTutor.contains("Bob"));
        assertTrue(fullTutor.contains("Bio:"));
    }

    @AfterAll
    void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}
