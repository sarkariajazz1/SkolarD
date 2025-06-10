package skolard.persistence;

import java.sql.Connection;

public class PersistenceFactory {

    public static void initialize(PersistenceType type, boolean seed) {
        try {
            if (type == PersistenceType.STUB) {
                PersistenceProvider.initializeStubs();
                return;
            }

            Connection conn = EnvironmentInitializer.setupEnvironment(type, seed);
            PersistenceProvider.initializeSqlite(conn);

        } catch (Exception e) {
            System.err.println("Falling back to stubs due to: " + e.getMessage());
            PersistenceProvider.initializeStubs();
        }
    }
}
