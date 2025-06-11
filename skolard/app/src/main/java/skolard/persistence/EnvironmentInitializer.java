package skolard.persistence;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import skolard.Config.Config;
import skolard.persistence.sqlite.SchemaInitializer;

/**
 * Class responsible for initializing the database environment.
 * Sets up connections, initializes schema, resets test DB, and optionally seeds data.
 */
public class EnvironmentInitializer {

    /**
     * Sets up the environment by initializing the connection, schema, optionally resetting test DB, and seeding data.
     * 
     * @param type the persistence type (PROD or TEST)
     * @param seed whether to seed the database with initial data
     * @return a Connection object to the database
     * @throws Exception if setup fails
     */
    public static Connection setupEnvironment(PersistenceType type, boolean seed) throws Exception {
        // Choose database path based on environment type
        String dbPath = type == PersistenceType.PROD ? Config.PROD_DB : Config.TEST_DB;
        ConnectionManager.initialize(dbPath);
        Connection conn = ConnectionManager.get();

        // Initialize database schema if not already present
        SchemaInitializer.initializeSchema(conn);

        // If running tests, reset the test database to a clean state
        if (type == PersistenceType.TEST) {
           resetTestDB(conn, dbPath);
        }

        // Optionally seed the database with initial data from SQL files
        if (seed) {
            DatabaseSeeder.seed(conn, List.of(
                "/seed_students.sql",
                "/seed_tutors.sql",
                "/seed_tutorCourse.sql",
                "/seed_sessions.sql",
                "/seed_card.sql",
                "/seed_support_user.sql",
                "/seed_support.sql",
                "/seed_messages.sql",
                "/seed_faqs.sql",
                "/seed_ratings.sql"
            ));
        }

        return conn;
    }

    /**
     * Resets the test database by deleting all data from tables and resetting auto-increment counters.
     * Disables foreign key constraints during reset to avoid conflicts.
     * 
     * @param conn the database connection
     * @param dbPath the path of the database file
     */
    public static void resetTestDB(Connection conn, String dbPath) {
        // Only reset if this is the test database
        if(dbPath.equals(Config.TEST_DB)) {

            // SQL statements to delete data and reset sequences
            String[] sqlStatements = {
                "PRAGMA foreign_keys = OFF;", // Disable FK constraints
                "DELETE FROM ratings;",
                "DELETE FROM ratingRequests;",
                "DELETE FROM faq;",
                "DELETE FROM support_ticket;",
                "DELETE FROM support;",
                "DELETE FROM card;",
                "DELETE FROM messages;",
                "DELETE FROM tutorCourse;",
                "DELETE FROM session;",
                "DELETE FROM student;",
                "DELETE FROM tutor;",
                // Reset AUTOINCREMENT counters for some tables
                "DELETE FROM sqlite_sequence WHERE name='ratings';",
                "DELETE FROM sqlite_sequence WHERE name='ratingRequests';",
                "DELETE FROM sqlite_sequence WHERE name='faq';",
                "DELETE FROM sqlite_sequence WHERE name='support_ticket';",
                "DELETE FROM sqlite_sequence WHERE name='messages';",
                "DELETE FROM sqlite_sequence WHERE name='session';",
                "PRAGMA foreign_keys = ON;"  // Re-enable FK constraints
            };

            try (Statement stmt = conn.createStatement()) {
                conn.setAutoCommit(false); // Begin transaction

                for (String sql : sqlStatements) {
                    System.out.println("Executing: " + sql); // Debug output
                    stmt.execute(sql);
                }

                conn.commit(); // Commit transaction
                System.out.println("Database reset successfully.");
            } catch (SQLException e) {
                try {
                    conn.rollback(); // Rollback if any SQL fails
                    System.err.println("Database reset failed. Rolled back. Error: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            } finally {
                try {
                    conn.setAutoCommit(true); // Restore auto-commit mode
                } catch (SQLException e) {
                    System.err.println("Failed to reset auto-commit: " + e.getMessage());
                }
            }
        }
    }

}

