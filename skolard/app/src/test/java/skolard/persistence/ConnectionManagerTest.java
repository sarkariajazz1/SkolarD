package skolard.persistence;

import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionManagerTest {

    @BeforeEach
    public void setup() throws Exception {
        ConnectionManager.initialize(":memory:");
    }

    @AfterEach
    public void cleanup() {
        ConnectionManager.close();
    }

    @Test
    public void testInitializeAndGetConnection() throws Exception {
        Connection conn = ConnectionManager.get();
        assertNotNull(conn);
        assertFalse(conn.isClosed());
    }

    @Test
    public void testCloseConnection() throws Exception {
        ConnectionManager.close();
        assertTrue(ConnectionManager.get().isClosed());
    }
}
