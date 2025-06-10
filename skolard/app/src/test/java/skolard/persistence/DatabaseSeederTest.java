package skolard.persistence;

import org.junit.jupiter.api.*;
import skolard.persistence.sqlite.SchemaInitializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseSeederTest {
    
    private Connection conn;

    @BeforeEach
    public void setup() throws Exception {
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        SchemaInitializer.initializeSchema(conn);
    }

    @Test
    public void testSeedValidFile() {
        assertDoesNotThrow(() ->
            DatabaseSeeder.seed(conn, List.of("/seed_faqs.sql"))
        );
    }

    @Test
    public void testSeedInvalidFile() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            DatabaseSeeder.seed(conn, List.of("/invalid_file.sql"))
        );
        assertTrue(ex.getMessage().contains("Seed file not found"));
    }
}
