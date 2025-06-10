package skolard.persistence;

import org.junit.jupiter.api.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EnvironmentInitializerTest {

    @BeforeEach
    @AfterEach
    public void resetConnectionManager() throws Exception {
        ConnectionManager.close(); // Close current connection if open

        // Use reflection to nullify the static `connection` field
        var field = ConnectionManager.class.getDeclaredField("connection");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    public void testSetupEnvironmentWithoutSeed() throws Exception {
        Connection conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, false);
        assertNotNull(conn);
        assertFalse(conn.isClosed());
    }

    @Test
    public void testSetupEnvironmentWithSeed() throws Exception {
        Connection conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, true);
        assertNotNull(conn);
        assertFalse(conn.isClosed());
    }

    @Test
    public void testSetupEnvironmentFallbackToStubOnFailure() {
        // Intentionally pass a bad enum to simulate fallback
        PersistenceType badType = null;

        // Should not throw but fallback
        assertDoesNotThrow(() -> {
            PersistenceFactory.initialize(badType, false);
            assertNotNull(PersistenceRegistry.getMessagePersistence()); // Should fallback to stub
        });
    }
}
