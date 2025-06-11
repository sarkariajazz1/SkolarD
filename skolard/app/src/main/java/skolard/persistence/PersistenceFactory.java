package skolard.persistence;

import java.sql.Connection;

/**
 * Factory class to initialize the persistence layer based on the given persistence type.
 * Supports initializing either stub implementations or SQLite database connection.
 */
public class PersistenceFactory {

    /**
     * Initializes the persistence environment based on the specified type.
     * If initialization fails, falls back to using stub implementations.
     *
     * @param type the type of persistence to use (e.g., STUB, PROD, TEST)
     * @param seed whether to seed the database with initial data
     */
    public static void initialize(PersistenceType type, boolean seed) {
        try {
            // If the persistence type is STUB, initialize stub implementations
            if (type == PersistenceType.STUB) {
                PersistenceProvider.initializeStubs();
                return;
            }

            // Otherwise, setup the database environment and initialize SQLite persistence
            Connection conn = EnvironmentInitializer.setupEnvironment(type, seed);
            PersistenceProvider.initializeSqlite(conn);

        } catch (Exception e) {
            // If any exception occurs, fallback to stub implementations
            System.err.println("Falling back to stubs due to: " + e.getMessage());
            PersistenceProvider.initializeStubs();
        }
    }
}
