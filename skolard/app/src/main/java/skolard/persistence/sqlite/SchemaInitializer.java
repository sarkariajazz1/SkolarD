package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is responsible for initializing the SQLite database schema
 * for the SkolarD application. It creates all required tables if they do not exist.
 */
public class SchemaInitializer {

    /**
     * Initializes the database schema by executing SQL CREATE TABLE statements.
     * 
     * @param connection The active database connection used to execute schema statements.
     */
    public static void intializeSchema(Connection connection) {
        // SQL statement to create the 'courses' table
        // Stores course identifiers and names
        String createCourseTable = "CREATE TABLE IF NOT EXISTS courses ("
                                        + "id TEXT PRIMARY KEY NOT NULL,"       // Unique course ID (e.g., COMP1010)
                                        + "name TEXT NOT NULL"                  // Name of the course
                                        + ");";

        // SQL statement to create the 'tutor' table
        // Stores tutor profiles including email, name, and bio
        String createTutorTable = "CREATE TABLE IF NOT EXISTS tutor ("
                                        + "email TEXT PRIMARY KEY NOT NULL UNIQUE," // Tutor's unique email
                                        + "name TEXT NOT NULL,"                     // Full name
                                        + "bio TEXT NOT NULL"                       // Brief biography or description
                                        + ");";

        // SQL statement to create the 'student' table
        // Stores student profiles with email and name
        String createStudentTable = "CREATE TABLE IF NOT EXISTS student ("
                                        + "email TEXT PRIMARY KEY NOT NULL UNIQUE," // Student's unique email
                                        + "name TEXT NOT NULL"                      // Full name
                                        + ");";

        // SQL statement to create the 'session' table
        // Represents a tutoring session between a tutor and student
        String createSessionTable = "CREATE TABLE IF NOT EXISTS session ("
                                        + "id INTEGER PRIMARY KEY NOT NULL AUTOINCREMENT UNIQUE," // Unique session ID
                                        + "tutorEmail TEXT NOT NULL,"                            // Tutor's email (FK)
                                        + "studentEmail TEXT NOT NULL,"                          // Student's email (FK)
                                        + "startTime TEXT NOT NULL,"                             // Session start time (as string)
                                        + "endTime TEXT NOT NULL,"                               // Session end time (as string)
                                        + "courseID TEXT NOT NULL,"                              // ID of the course for this session (FK)
                                        + "FOREIGN KEY(tutorEmail) REFERENCES tutor(email),"     // FK to tutor
                                        + "FOREIGN KEY(studentEmail) REFERENCES student(email)," // FK to student
                                        + "FOREIGN KEY(courseID) REFERENCES courses(id)"         // FK to courses
                                        + ");";

        // SQL statement to create the 'tutorCourse' table
        // Represents a many-to-many relationship between tutors and courses, with optional grade info
        String createTutorCourseTable = "CREATE TABLE IF NOT EXISTS tutorCourse ("
                                        + "tutorEmail TEXT NOT NULL,"                            // Tutor's email (FK)
                                        + "grade TEXT,"                                          // Grade or proficiency level (optional)
                                        + "courseID TEXT NOT NULL,"                              // Course ID (FK)
                                        + "PRIMARY KEY(tutorEmail, courseID),"                   // Composite key to prevent duplicates
                                        + "FOREIGN KEY(courseID) REFERENCES courses(id),"        // FK to courses
                                        + "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)"      // FK to tutor
                                        + ");";

        // Try-with-resources to ensure the statement is closed automatically
        try (Statement stmt = connection.createStatement()) {
            // Execute each CREATE TABLE statement in order
            stmt.execute(createCourseTable);
            stmt.execute(createTutorTable);
            stmt.execute(createStudentTable);
            stmt.execute(createSessionTable);
            stmt.execute(createTutorCourseTable);
        } catch (SQLException e) {
            // Wrap and rethrow as runtime exception to signal schema setup failure
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
}
