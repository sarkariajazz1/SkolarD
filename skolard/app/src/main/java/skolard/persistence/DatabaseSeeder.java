package skolard.persistence;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class responsible for executing SQL seed scripts from resource files.
 * This is typically used to populate the database with initial test or demo data.
 */
public class DatabaseSeeder {

    /**
     * Iterates over a list of SQL resource file paths and executes each one
     * using the provided database connection.
     *
     * @param connection     the active database connection
     * @param resourcePaths  a list of classpath resource paths to SQL files
     */
    public static void seed(Connection connection, List<String> resourcePaths) {
        for (String path : resourcePaths) {
            executeSqlFromResource(path, connection);
        }
    }

    /**
     * Loads a single SQL file from the classpath, splits its contents by semicolons,
     * and executes each individual SQL statement.
     *
     * @param resourcePath the path to the SQL resource file (e.g., "/seed_students.sql")
     * @param connection   the active database connection
     */
    private static void executeSqlFromResource(String resourcePath, Connection connection) {
        // Attempt to load the SQL file from the classpath
        InputStream input = DatabaseSeeder.class.getResourceAsStream(resourcePath);
        if (input == null) {
            throw new RuntimeException("Seed file not found: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            // Join the file's lines into a single SQL string
            String sql = reader.lines().collect(Collectors.joining("\n"));

            // Split the SQL string into individual statements based on semicolon delimiters
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();

                // Ignore any empty statements
                if (!trimmed.isEmpty()) {
                    try (Statement stmt = connection.createStatement()) {
                        stmt.execute(trimmed); // Execute each SQL statement individually
                    }
                }
            }
        } catch (Exception e) {
            // Wrap and rethrow any exception encountered while executing seed logic
            throw new RuntimeException("Failed to execute seed file: " + resourcePath, e);
        }
    }
}
