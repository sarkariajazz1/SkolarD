package skolard.logic.auth;

import org.junit.jupiter.api.*;
import skolard.objects.LoginCredentials;
import skolard.persistence.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginHandlerIntegrationTest {

    private Connection conn;
    private LoginHandler loginHandler;

    @BeforeAll
    void setup() throws Exception {
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, true); // Uses seeds because of support login
        PersistenceProvider.initializeSqlite(conn);

        loginHandler = new LoginHandler(); // Uses default LoginPersistence
    }

    @Test
    void testStudentLogin_success() {
        LoginCredentials credentials = new LoginCredentials("alice@example.com", "password", "student");
        assertTrue(loginHandler.login(credentials), "Student login should succeed with correct credentials.");
    }

    @Test
    void testTutorLogin_success() {
        LoginCredentials credentials = new LoginCredentials("bob@example.com", "password", "tutor");
        assertTrue(loginHandler.login(credentials), "Tutor login should succeed with correct credentials.");
    }

    @Test
    void testSupportLogin_success() {
        LoginCredentials credentials = new LoginCredentials("support@skolard.ca", "password", "support");
        assertTrue(loginHandler.login(credentials), "Support login should succeed with correct credentials.");
    }

    @Test
    void testLogin_invalidPassword() {
        LoginCredentials credentials = new LoginCredentials("alice@example.com", "wrongpass", "student");
        assertFalse(loginHandler.login(credentials), "Login should fail with incorrect password.");
    }

    @Test
    void testLogin_unknownRole() {
        LoginCredentials credentials = new LoginCredentials("user@example.com", "pass", "alien");
        assertFalse(loginHandler.login(credentials), "Login should fail with unknown role.");
    }

    @Test
    void testLogin_nullCredentials() {
        assertFalse(loginHandler.login(null), "Login should fail with null credentials.");
    }

    @Test
    void testLogin_nullRole() {
        LoginCredentials credentials = new LoginCredentials("user@example.com", "pass", null);
        assertFalse(loginHandler.login(credentials), "Login should fail with null role.");
    }

    @AfterAll
    void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}


