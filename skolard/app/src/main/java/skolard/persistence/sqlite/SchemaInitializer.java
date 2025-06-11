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

        // Table for storing tutor profiles, with unique email as primary key
        String createTutorTable = "CREATE TABLE IF NOT EXISTS tutor (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +
                "name TEXT NOT NULL," +
                "bio TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ");";

        // Table for storing student profiles, email as unique identifier and primary key
        String createStudentTable = "CREATE TABLE IF NOT EXISTS student (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +
                "name TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ");";

        // Table for tutoring sessions
        // Includes foreign keys referencing tutor and student emails
        // studentEmail can be NULL to represent unbooked sessions
        String createSessionTable = "CREATE TABLE IF NOT EXISTS session (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +  // Auto-incrementing session ID
                "tutorEmail TEXT NOT NULL," +               // Tutor who hosts session
                "studentEmail TEXT," +                       // Student who booked session (nullable)
                "startTime TEXT NOT NULL," +                 // Session start time (stored as text)
                "endTime TEXT NOT NULL," +                   // Session end time
                "courseID TEXT NOT NULL," +                  // Associated course ID
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email) ON DELETE CASCADE," +
                "FOREIGN KEY(studentEmail) REFERENCES student(email) ON DELETE SET NULL" +
                ");";

        // Table for storing tutors' course history with grades
        // Composite primary key on tutorEmail and courseID ensures uniqueness per tutor-course pair
        String createTutorCourseTable = "CREATE TABLE IF NOT EXISTS tutorCourse (" +
                "tutorEmail TEXT NOT NULL," +
                "courseID TEXT NOT NULL," +
                "grade REAL NOT NULL," +                      // Grade the tutor has for this course
                "PRIMARY KEY(tutorEmail, courseID)," +
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email) ON DELETE CASCADE" +
                ");";

        // Table to store chat messages between students and tutors
        // Contains sender info and references to student and tutor emails
        String createMessageTable = "CREATE TABLE IF NOT EXISTS messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +   // Unique message ID
                "timeSent TEXT NOT NULL," +                   // Timestamp of message
                "studentEmail TEXT NOT NULL," +               // Student involved in chat
                "tutorEmail TEXT NOT NULL," +                 // Tutor involved in chat
                "senderEmail TEXT NOT NULL," +                // Actual sender of the message
                "message TEXT NOT NULL," +                     // Message content
                "FOREIGN KEY(studentEmail) REFERENCES student(email)," +
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)" +
                ");";

        // Table for storing student credit card info
        // Composite primary key includes accountEmail, cardNumber, and expiry date to ensure uniqueness
        String createCardTable = "CREATE TABLE IF NOT EXISTS card (" +
                "accountEmail TEXT NOT NULL," +
                "name TEXT NOT NULL," +                        // Cardholder name
                "cardNumber TEXT NOT NULL," +                  // Card number (should be stored securely in practice)
                "expiry TEXT NOT NULL," +                       // Expiry date
                "PRIMARY KEY(accountEmail, cardNumber, expiry)," +
                "FOREIGN KEY(accountEmail) REFERENCES student(email) ON DELETE CASCADE" +
                ");";

        // Table for support tickets submitted by users
        // Tracks ticket status and timestamps
        String createSupportTicketTable = "CREATE TABLE IF NOT EXISTS support_ticket (" +
                "ticket_id INTEGER PRIMARY KEY AUTOINCREMENT," + // Unique ticket ID
                "requester_email TEXT NOT NULL," +                // Email of the user who made the request
                "requester_role TEXT NOT NULL," +                 // Role of requester (student/tutor/support)
                "title TEXT NOT NULL," +                           // Title of the support ticket
                "description TEXT NOT NULL," +                     // Detailed description
                "created_at TEXT NOT NULL," +                      // Creation timestamp
                "closed_at TEXT," +                                // Closing timestamp, nullable
                "is_handled INTEGER NOT NULL," +                   // Ticket handled flag (0 or 1)
                "FOREIGN KEY(requester_email) REFERENCES student(email) ON DELETE SET NULL" + // Foreign key constraint
                ");";

        // Table for support staff accounts
        // Stores credentials for support users
        String createSupportUserTable = "CREATE TABLE IF NOT EXISTS support (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +
                "name TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ");";

        // Table to track rating requests sent to students for sessions
        // Tracks completion and skip status for each request
        String createRatingRequestTable = "CREATE TABLE IF NOT EXISTS ratingRequests (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +         // Unique rating request ID
                "sessionId INTEGER NOT NULL," +                     // Session related to the request
                "studentEmail TEXT NOT NULL," +                     // Student who received the request
                "completed INTEGER NOT NULL," +                      // 0 or 1 indicating if rating was completed
                "skipped INTEGER NOT NULL," +                        // 0 or 1 indicating if request was skipped
                "createdAt TEXT NOT NULL," +                         // Timestamp of request creation
                "FOREIGN KEY(sessionId) REFERENCES session(id) ON DELETE CASCADE," +
                "FOREIGN KEY(studentEmail) REFERENCES student(email) ON DELETE CASCADE" +
                ");";

        // Table for storing ratings given by students to tutors for sessions
        String createRatingsTable = "CREATE TABLE IF NOT EXISTS ratings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +          // Unique rating ID
                "tutorEmail TEXT NOT NULL," +                       // Tutor being rated
                "sessionId INTEGER NOT NULL," +                     // Session the rating is for
                "studentEmail TEXT NOT NULL," +                     // Student who rated
                "courseName TEXT NOT NULL," +                        // Course associated with rating
                "rating INTEGER NOT NULL," +                         // Numeric rating value
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)," +
                "FOREIGN KEY(studentEmail) REFERENCES student(email)," +
                "FOREIGN KEY(sessionId) REFERENCES session(id)" +
                ");";

        // Table for frequently asked questions (FAQ)
        // Stores unique questions and their answers
        String createFAQTable = "CREATE TABLE IF NOT EXISTS faq (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "question TEXT NOT NULL UNIQUE," +
                "answer TEXT NOT NULL" +
                ");";


        try (Statement stmt = connection.createStatement()) {
            // Execute each table creation SQL statement
            stmt.execute(createTutorTable);
            stmt.execute(createStudentTable);
            stmt.execute(createSessionTable);
            stmt.execute(createTutorCourseTable);
            stmt.execute(createMessageTable);
            stmt.execute(createCardTable);
            stmt.execute(createSupportTicketTable);
            stmt.execute(createSupportUserTable);
            stmt.execute(createRatingRequestTable);
            stmt.execute(createRatingsTable);
            stmt.execute(createFAQTable);
        } catch (SQLException e) {
            // Log and rethrow exceptions if any table creation fails
            System.err.println("SQL Error: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
}
