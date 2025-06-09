package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.utils.PasswordUtil;

import static org.junit.jupiter.api.Assertions.*;

class LoginStubTest {

    private LoginStub loginStub;
    private StudentStub studentStub;
    private TutorStub tutorStub;

    @BeforeEach
    void setUp() {
        studentStub = new StudentStub();
        tutorStub = new TutorStub();

        // Add test students
        studentStub.addStudent(new Student("Test Student", "test@student.com", PasswordUtil.hash("pass123")));
        studentStub.addStudent(new Student("Raj", "raj@skolard.ca", PasswordUtil.hash("raj123")));
        studentStub.addStudent(new Student("Simran", "simran@skolard.ca", PasswordUtil.hash("simran123")));

        // Add test tutors
        tutorStub.addTutor(new Tutor("Test Tutor", "test@tutor.com", PasswordUtil.hash("pass123"), null, null));
        tutorStub.addTutor(new Tutor("Amrit", "amrit@skolard.ca", PasswordUtil.hash("amrit123"), null, null));
        tutorStub.addTutor(new Tutor("Sukhdeep", "sukhdeep@skolard.ca", PasswordUtil.hash("sukhdeep123"), null, null));

        loginStub = new LoginStub(studentStub, tutorStub);
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

