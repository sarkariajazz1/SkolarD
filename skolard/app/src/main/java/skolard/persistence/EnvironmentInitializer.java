package skolard.persistence;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import skolard.persistence.sqlite.SchemaInitializer;

public class EnvironmentInitializer {

    public static Connection setupEnvironment(PersistenceType type, boolean seed) throws Exception {
        String dbPath = type == PersistenceType.PROD ? "skolard.db" : "test.db";
        ConnectionManager.initialize(dbPath);
        Connection conn = ConnectionManager.get();

        SchemaInitializer.initializeSchema(conn);

        if (type == PersistenceType.TEST) {
           resetTestDB(conn, dbPath);
        }

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

    public static void resetTestDB(Connection conn, String dbPath) {
        if(dbPath.equals("test.db")) {
            
            String[] sqlStatements = {
                "PRAGMA foreign_keys = OFF;",
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
                // Reset AUTOINCREMENT counters
                "DELETE FROM sqlite_sequence WHERE name='ratings';",
                "DELETE FROM sqlite_sequence WHERE name='ratingRequests';",
                "DELETE FROM sqlite_sequence WHERE name='faq';",
                "DELETE FROM sqlite_sequence WHERE name='support_ticket';",
                "DELETE FROM sqlite_sequence WHERE name='messages';",
                "DELETE FROM sqlite_sequence WHERE name='session';",
                "PRAGMA foreign_keys = ON;"
            };

            try (Statement stmt = conn.createStatement()) {
                conn.setAutoCommit(false); // Start transaction

                for (String sql : sqlStatements) {
                    System.out.println("Executing: " + sql); // Optional: for debugging
                    stmt.execute(sql);
                }

                conn.commit(); // Commit transaction
                System.out.println("Database reset successfully.");
            } catch (SQLException e) {
                try {
                    conn.rollback(); // Roll back if any statement fails
                    System.err.println("Database reset failed. Rolled back. Error: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            } finally {
                try {
                    conn.setAutoCommit(true); // Restore default behavior
                } catch (SQLException e) {
                    System.err.println("Failed to reset auto-commit: " + e.getMessage());
                }
            }
        }
    }

}
