package skolard.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Manages a single shared connection to the SQLite database.
 * Ensures that only one connection is established and reused across the application.
 */
public class ConnectionManager {
    // Static reference to the database connection (singleton-style)
    private static Connection connection;

    /**
     * Initializes the database connection using the given file path.
     * Loads the SQLite JDBC driver and creates a connection if not already initialized.
     *
     * @param dbPath the path to the SQLite database file (e.g., "skolard.db")
     * @throws Exception if the JDBC driver cannot be loaded or connection fails
     */
    public static void initialize(String dbPath) throws Exception {
        // Avoid reinitializing if a connection already exists
        if (connection != null) return;

        // Load the SQLite JDBC driver class into memory
        Class.forName("org.sqlite.JDBC");

        // Establish a connection to the specified SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    /**
     * Returns the active database connection.
     *
     * @return the shared SQLite connection instance
     */
    public static Connection get() {
        return connection;
    }

    /**
     * Closes the database connection if it has been initialized.
     * Silently ignores any exceptions during close.
     */
    public static void close() {
        try {
            if (connection != null) connection.close();
        } catch (Exception ignored) {}
    }
}
