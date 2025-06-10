package skolard.logic.auth;

import skolard.objects.LoginCredentials;
import skolard.persistence.LoginPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginHandlerTest {

    private LoginPersistence mockLoginDB;
    private LoginHandler loginHandler;

    @BeforeEach
    void setUp() {
        mockLoginDB = mock(LoginPersistence.class);
        loginHandler = new LoginHandler(mockLoginDB);
    }

    @Test
    void testValidStudentLogin() {
        LoginCredentials creds = new LoginCredentials("student@skolard.ca", "pass123", "student");
        when(mockLoginDB.authenticateStudent("student@skolard.ca", "pass123")).thenReturn(true);

        assertTrue(loginHandler.login(creds));
    }

    @Test
    void testValidTutorLogin() {
        LoginCredentials creds = new LoginCredentials("tutor@skolard.ca", "abc123", "tutor");
        when(mockLoginDB.authenticateTutor("tutor@skolard.ca", "abc123")).thenReturn(true);

        assertTrue(loginHandler.login(creds));
    }

    @Test
    void testValidSupportLogin() {
        LoginCredentials creds = new LoginCredentials("support@skolard.ca", "admin", "support");
        when(mockLoginDB.authenticateSupport("support@skolard.ca", "admin")).thenReturn(true);

        assertTrue(loginHandler.login(creds));
    }

    @Test
    void testInvalidLogin_UnknownRole() {
        LoginCredentials creds = new LoginCredentials("ghost@skolard.ca", "123", "alien");
        assertFalse(loginHandler.login(creds));
    }

    @Test
    void testInvalidLogin_NullCredentials() {
        assertFalse(loginHandler.login(null));
    }

    @Test
    void testInvalidLogin_NullRole() {
        LoginCredentials creds = new LoginCredentials("user@skolard.ca", "123", null);
        assertFalse(loginHandler.login(creds));
    }

    @Test
    void testLoginReturnsFalseIfAuthenticationFails() {
        LoginCredentials creds = new LoginCredentials("tutor@skolard.ca", "wrong", "tutor");
        when(mockLoginDB.authenticateTutor("tutor@skolard.ca", "wrong")).thenReturn(false);

        assertFalse(loginHandler.login(creds));
    }

    @Test
    void testDefaultConstructorDoesNotThrow() {
        assertDoesNotThrow(() -> {
            LoginHandler handler = new LoginHandler();
            assertNotNull(handler);
        });
    }

}
