package skolard.persistence;

import java.sql.Connection;
import java.util.List;

import skolard.persistence.sqlite.MessageDB;
import skolard.persistence.sqlite.SchemaInitializer;
import skolard.persistence.sqlite.StudentDB;
import skolard.persistence.sqlite.TutorDB;
import skolard.persistence.sqlite.SessionDB;
import skolard.persistence.stub.StubFactory;

public class PersistenceFactory {

    // Static instances of each persistence interface
    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;
    private static SessionPersistence sessionPersistence;
    private static MessagePersistence messagePersistence;

    /**
     * Initializes the appropriate persistence layer based on the specified type.
     * If type is PROD or TEST, initializes SQLite database; otherwise, falls back to stub mode.
     * 
     * @param type the type of persistence (PROD, TEST, or STUB)
     * @param seed whether to seed the database with initial data
     */
    public static void initialize(PersistenceType type, boolean seed) {
        // Prevent re-initialization if persistence has already been set up
        if (studentPersistence != null || tutorPersistence != null || sessionPersistence != null) return;

        switch (type) {
            case PROD, TEST -> {
                // Choose database file name based on environment
                String dbPath = type == PersistenceType.PROD ? "skolard.db" : "test.db";
                try {
                    // Initialize SQLite connection
                    ConnectionManager.initialize(dbPath);
                    Connection conn = ConnectionManager.get();

                    // Create all required tables if they do not exist
                    SchemaInitializer.initializeSchema(conn);

                    // Optionally populate database with initial records
                    if (seed) {
                        DatabaseSeeder.seed(conn, List.of(
                            "/seed_students.sql",
                            "/seed_tutors.sql",
                            "/seed_sessions.sql",
                            "/seed_messages.sql"
                        ));
                    }

                    // Instantiate real persistence implementations
                    studentPersistence = new StudentDB(conn);
                    tutorPersistence = new TutorDB(conn);
                    sessionPersistence = new SessionDB(conn, studentPersistence, tutorPersistence);
                    messagePersistence = new MessageDB(conn);

                } catch (Exception e) {
                    // Fallback to stub persistence if any error occurs
                    fallbackToStubs(e);
                }
            }
            case STUB -> fallbackToStubs(null); // Explicit fallback to stub mode
        }
    }

    /**
     * Initializes stub-based persistence as a fallback or default.
     * 
     * @param e the exception that triggered the fallback, or null if intentional
     */
    private static void fallbackToStubs(Exception e) {
        studentPersistence = StubFactory.createStudentPersistence();
        tutorPersistence = StubFactory.createTutorPersistence();
        sessionPersistence = StubFactory.createSessionPersistence();
        if (e != null) {
            System.err.println("Falling back to stubs due to: " + e.getMessage());
        }
    }

    // Accessor for student persistence
    public static StudentPersistence getStudentPersistence() {
        return studentPersistence;
    }

    // Accessor for tutor persistence
    public static TutorPersistence getTutorPersistence() {
        return tutorPersistence;
    }

    // Accessor for session persistence
    public static SessionPersistence getSessionPersistence() {
        return sessionPersistence;
    }

    // Accessor for message persistence
    public static MessagePersistence getMessagePersistence() {
        return messagePersistence;
    }

    /**
     * Resets the persistence layer and closes the active database connection.
     * This is typically used when shutting down or reinitializing the system.
     */
    public static void reset() {
        ConnectionManager.close();
        studentPersistence = null;
        tutorPersistence = null;
        sessionPersistence = null;
        messagePersistence = null;
    }
}
