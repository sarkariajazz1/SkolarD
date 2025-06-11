package skolard.logic.auth;

import org.junit.jupiter.api.*;
import skolard.objects.LoginCredentials;
import skolard.persistence.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for LoginHandler using a real SQLite database.
 */
public class LoginHandlerIntegrationTest {

    private Connection conn;
    private LoginHandler loginHandler;

    @BeforeEach
    void initDatabase() throws Exception {
        // Set up the TEST database and seed it with sample users
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, false);
        PersistenceProvider.initializeSqlite(conn);

        // Use the default login handler tied to LoginDB
        loginHandler = new LoginHandler();
    }

    @AfterEach
    void closeDatabase() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @Test
    void invalidStudentPassword_shouldFailLogin() {
        LoginCredentials credentials = new LoginCredentials("alice@example.com", "wrongpass", "student");
        boolean result = loginHandler.login(credentials);
        assertFalse(result, "Student login with wrong password should fail.");
    }

    @Test
    void nonExistentTutorEmail_shouldFailLogin() {
        LoginCredentials credentials = new LoginCredentials("nobody@nowhere.com", "password", "tutor");
        boolean result = loginHandler.login(credentials);
        assertFalse(result, "Login should fail for non-existent tutor.");
    }

    @Test
    void nullCredentials_shouldFailLogin() {
        boolean result = loginHandler.login(null);
        assertFalse(result, "Login should fail when credentials are null.");
    }

    @Test
    void nullRoleInCredentials_shouldFailLogin() {
        LoginCredentials credentials = new LoginCredentials("test@example.com", "password", null);
        boolean result = loginHandler.login(credentials);
        assertFalse(result, "Login should fail when role is null.");
    }

    @Test
    void unsupportedRole_shouldFailLogin() {
        LoginCredentials credentials = new LoginCredentials("admin@example.com", "password", "admin");
        boolean result = loginHandler.login(credentials);
        assertFalse(result, "Login should fail with unsupported role 'admin'.");
    }
}
