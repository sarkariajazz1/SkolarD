package skolard.persistence;

import org.junit.jupiter.api.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EnvironmentInitializerTest {

    @BeforeEach
    @AfterEach
    public void resetConnectionManager() throws Exception {
        ConnectionManager.close(); // Close current connection if open

        // Use reflection to clear the static connection field
        var field = ConnectionManager.class.getDeclaredField("connection");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    public void testSetupEnvironmentWithoutSeed() throws Exception {
        Connection conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, false);
        assertNotNull(conn, "Connection should not be null");
        assertFalse(conn.isClosed(), "Connection should be open");
    }

    @Test
    public void testSetupEnvironmentFallbackToStubOnNull() {
        assertDoesNotThrow(() -> {
            // Simulate a null PersistenceType, should fallback
            PersistenceFactory.initialize(null, false);

            // Test that fallback registry still gives non-null persistence
            assertNotNull(PersistenceRegistry.getMessagePersistence());
            assertNotNull(PersistenceRegistry.getStudentPersistence());
            assertNotNull(PersistenceRegistry.getTutorPersistence());
        });
    }
}
