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
                "id TEXT PRIMARY KEY NOT NULL," +
                "name TEXT NOT NULL" +
                ");";

        // Table for storing tutor profiles
        String createTutorTable = "CREATE TABLE IF NOT EXISTS tutor (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +
                "name TEXT NOT NULL," +
                "bio TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ");";

        // Table for storing student profiles
        String createStudentTable = "CREATE TABLE IF NOT EXISTS student (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +
                "name TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ");";

        // Table for tutoring sessions
        String createSessionTable = "CREATE TABLE IF NOT EXISTS session (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tutorEmail TEXT NOT NULL," +
                "studentEmail TEXT NOT NULL," +
                "startTime TEXT NOT NULL," +
                "endTime TEXT NOT NULL," +
                "courseID TEXT NOT NULL," +
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)," +
                "FOREIGN KEY(studentEmail) REFERENCES student(email)," +
                "FOREIGN KEY(courseID) REFERENCES courses(id)" +
                ");";

        // Table for tutors' course history
        String createTutorCourseTable = "CREATE TABLE IF NOT EXISTS tutorCourse (" +
                "tutorEmail TEXT NOT NULL," +
                "grade REAL," +
                "courseID TEXT NOT NULL," +
                "PRIMARY KEY(tutorEmail, courseID)," +
                "FOREIGN KEY(courseID) REFERENCES courses(id)," +
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)" +
                ");";

        // Table for chat messages
        String createMessageTable = "CREATE TABLE IF NOT EXISTS messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "timeSent TEXT NOT NULL," +
                "studentEmail TEXT NOT NULL," +
                "tutorEmail TEXT NOT NULL," +
                "senderEmail TEXT NOT NULL," +
                "message TEXT NOT NULL," +
                "FOREIGN KEY(studentEmail) REFERENCES student(email)," +
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)" +
                ");";

        // Table for student credit card info
        String createCardTable = "CREATE TABLE IF NOT EXISTS card (" +
                "accountEmail TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "cardNumber TEXT NOT NULL," +
                "expiry TEXT NOT NULL," +
                "PRIMARY KEY(accountEmail, cardNumber, expiry)," +
                "FOREIGN KEY(accountEmail) REFERENCES student(email)" +
                ");";

        // Table for support tickets
        String createSupportTicketTable = "CREATE TABLE IF NOT EXISTS support_ticket (" +
                "ticket_id TEXT PRIMARY KEY," +
                "requester_email TEXT NOT NULL," +
                "requester_role TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "created_at TEXT NOT NULL," +
                "closed_at TEXT," +
                "is_handled INTEGER NOT NULL," +
                "FOREIGN KEY(requester_email) REFERENCES student(email) ON DELETE SET NULL" +
                ");";

        // Table for support staff accounts
        String createSupportUserTable = "CREATE TABLE IF NOT EXISTS support (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +
                "name TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCourseTable);
            stmt.execute(createTutorTable);
            stmt.execute(createStudentTable);
            stmt.execute(createSessionTable);
            stmt.execute(createTutorCourseTable);
            stmt.execute(createMessageTable);
            stmt.execute(createCardTable);
            stmt.execute(createSupportTicketTable);
            stmt.execute(createSupportUserTable); // ✅ Added support user table
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
}
