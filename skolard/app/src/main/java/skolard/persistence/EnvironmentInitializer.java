package skolard.persistence;

import java.sql.Connection;
import java.util.List;

import skolard.persistence.sqlite.SchemaInitializer;

public class EnvironmentInitializer {

    public static Connection setupEnvironment(PersistenceType type, boolean seed) throws Exception {
        String dbPath = type == PersistenceType.PROD ? "skolard.db" : "test.db";
        ConnectionManager.initialize(dbPath);
        Connection conn = ConnectionManager.get();

        SchemaInitializer.initializeSchema(conn);

        if (seed) {
            DatabaseSeeder.seed(conn, List.of(
                "/seed_students.sql",
                "/seed_tutors.sql",
                "/seed_tutorCourse.sql",
                "/seed_sessions.sql",
                "/seed_card.sql",
                "/seed_support_user.sql",
                "/seed_support.sql",
                "/seed_messages.sql"//,
                   // "/seed_ratings.sql"
            ));
        }

        return conn;
    }
}
