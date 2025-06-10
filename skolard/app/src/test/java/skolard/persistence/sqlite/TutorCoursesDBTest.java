package skolard.persistence.sqlite;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TutorCoursesDBTest {

    private static Connection connection;
    private TutorCoursesDB tutorCoursesDB;

    @BeforeAll
    static void init() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SchemaInitializer.initializeSchema(connection);
    }

    @BeforeEach
    void setup() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM tutorCourse");
        }
        tutorCoursesDB = new TutorCoursesDB(connection);
    }

    @Test
    void testAddAndGetCourses() {
        Map<String, Double> courses = tutorCoursesDB.addCourse("t1@skolard.ca", "COMP3010", 4.5);
        assertTrue(courses.containsKey("COMP3010"));
        assertEquals(4.5, courses.get("COMP3010"));
    }

    @Test
    void testDeleteCourse() {
        tutorCoursesDB.addCourse("t2@skolard.ca", "COMP2150", 3.7);
        tutorCoursesDB.deleteTutorCourse("t2@skolard.ca", "COMP2150");

        Map<String, Double> courses = tutorCoursesDB.getTutorCourses("t2@skolard.ca");
        assertTrue(courses.isEmpty());
    }

    @Test
    void testDeleteAllCourses() {
        tutorCoursesDB.addCourse("t3@skolard.ca", "COMP1010", 4.0);
        tutorCoursesDB.addCourse("t3@skolard.ca", "COMP1020", 3.5);
        tutorCoursesDB.deleteAllTutorCourses("t3@skolard.ca");

        Map<String, Double> courses = tutorCoursesDB.getTutorCourses("t3@skolard.ca");
        assertTrue(courses.isEmpty());
    }
}
