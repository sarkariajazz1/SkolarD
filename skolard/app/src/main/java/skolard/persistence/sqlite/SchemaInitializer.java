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
     * Creates the necessary tables for the application schema if they don't already exist.
     * 
     * @param connection active database connection to execute schema creation
     */
    public static void initializeSchema(Connection connection) {
        // Table for storing course identifiers and names
        String createCourseTable = "CREATE TABLE IF NOT EXISTS courses (" +
                "id TEXT PRIMARY KEY NOT NULL," +              // Course ID (e.g., COMP1010)
                "name TEXT NOT NULL" +                         // Course name (e.g., Intro to CS)
                ");";

        // Table for storing tutor profiles including their email, name, and bio
        String createTutorTable = "CREATE TABLE IF NOT EXISTS tutor (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +    // Tutor's email (primary key)
                "name TEXT NOT NULL," +                        // Tutor's full name
                "bio TEXT NOT NULL," +                          // Short bio or background
                "password TEXT NOT NULL" +
                ");";

        // Table for storing student profiles with their email and name
        String createStudentTable = "CREATE TABLE IF NOT EXISTS student (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +    // Student's email (primary key)
                "name TEXT NOT NULL," +                         // Student's full name
                "password TEXT NOT NULL" +         
                ");";

        // Table for storing tutoring sessions between students and tutors
        String createSessionTable = "CREATE TABLE IF NOT EXISTS session (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +      // Auto-incremented session ID
                "tutorEmail TEXT NOT NULL," +                  // Email of the tutor
                "studentEmail TEXT NOT NULL," +                // Email of the student
                "startTime TEXT NOT NULL," +                   // Session start time (ISO 8601 string)
                "endTime TEXT NOT NULL," +                     // Session end time
                "courseID TEXT NOT NULL," +                    // Course associated with the session
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)," +   // FK to tutor
                "FOREIGN KEY(studentEmail) REFERENCES student(email)," + // FK to student
                "FOREIGN KEY(courseID) REFERENCES courses(id)" +       // FK to course
                ");";

        // Table for associating tutors with courses they've taken and grades (many-to-many)
        String createTutorCourseTable = "CREATE TABLE IF NOT EXISTS tutorCourse (" +
                "tutorEmail TEXT NOT NULL," +                  // Email of the tutor
                "grade REAL," +                                // Grade achieved in the course
                "courseID TEXT NOT NULL," +                    // Course ID
                "PRIMARY KEY(tutorEmail, courseID)," +         // Composite key to avoid duplicates
                "FOREIGN KEY(courseID) REFERENCES courses(id)," +
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)" +
                ");";

        // Table for storing messages between students and tutors
        String createMessageTable = "CREATE TABLE IF NOT EXISTS messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +      // Auto-incremented message ID
                "timeSent TEXT NOT NULL," +                    // Timestamp in ISO 8601 format
                "senderEmail TEXT NOT NULL," +                 // Email of sender (student)
                "receiverEmail TEXT NOT NULL," +               // Email of receiver (tutor)
                "message TEXT NOT NULL," +                     // The message content
                "FOREIGN KEY(senderEmail) REFERENCES student(email)," +
                "FOREIGN KEY(receiverEmail) REFERENCES tutor(email)" +
                ");";

        // Execute all table creation statements
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCourseTable);
            stmt.execute(createTutorTable);
            stmt.execute(createStudentTable);
            stmt.execute(createSessionTable);
            stmt.execute(createTutorCourseTable);
            stmt.execute(createMessageTable);
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage()); // Logs any SQL issue
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
}
