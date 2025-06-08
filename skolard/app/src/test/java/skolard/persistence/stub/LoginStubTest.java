package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LoginStubTest {

    private LoginStub loginStub;

    @BeforeEach
    void setUp() {
        loginStub = new LoginStub();
    }

    // Student Authentication Tests
    @Test
    void testAuthenticateStudentSuccess() {
        assertTrue(loginStub.authenticateStudent("test@student.com", "pass123"));
        assertTrue(loginStub.authenticateStudent("raj@skolard.ca", "raj123"));
        assertTrue(loginStub.authenticateStudent("simran@skolard.ca", "simran123"));
    }

    @Test
    void testAuthenticateStudentFailure() {
        assertFalse(loginStub.authenticateStudent("test@student.com", "wrongpass"));
        assertFalse(loginStub.authenticateStudent("unknown@student.com", "pass123"));
    }

    // Tutor Authentication Tests
    @Test
    void testAuthenticateTutorSuccess() {
        assertTrue(loginStub.authenticateTutor("test@tutor.com", "pass123"));
        assertTrue(loginStub.authenticateTutor("amrit@skolard.ca", "amrit123"));
        assertTrue(loginStub.authenticateTutor("sukhdeep@skolard.ca", "sukhdeep123"));
    }

    @Test
    void testAuthenticateTutorFailure() {
        assertFalse(loginStub.authenticateTutor("test@tutor.com", "wrongpass"));
        assertFalse(loginStub.authenticateTutor("unknown@tutor.com", "pass123"));
    }

    // Support Authentication Tests
    @Test
    void testAuthenticateSupportSuccess() {
        assertTrue(loginStub.authenticateSupport("support@skolard.ca", "admin123"));
    }

    @Test
    void testAuthenticateSupportFailure() {
        assertFalse(loginStub.authenticateSupport("support@skolard.ca", "wrongpass"));
        assertFalse(loginStub.authenticateSupport("unknown@skolard.ca", "admin123"));
    }
}

