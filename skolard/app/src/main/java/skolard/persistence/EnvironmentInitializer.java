package skolard.persistence;

import skolard.persistence.sqlite.SchemaInitializer;
import java.sql.Connection;
import java.util.List;


public class EnvironmentInitializer {

    public static Connection setupEnvironment(PersistenceType type, boolean seed) throws Exception {
        String dbPath = type == PersistenceType.PROD ? "skolard.db" : "test.db";
        ConnectionManager.initialize(dbPath);
        Connection conn = ConnectionManager.get();

        // Initialize schema
        SchemaInitializer.initializeSchema(conn);

        // Optionally seed the database
        if (seed) {
            DatabaseSeeder.seed(conn, List.of(
                "/seed_students.sql",
                "/seed_tutors.sql",
                "/seed_sessions.sql",
                "/seed_messages.sql"
            ));
        }

        return conn;
    }
}
