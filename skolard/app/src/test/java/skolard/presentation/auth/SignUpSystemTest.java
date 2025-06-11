package skolard.presentation.auth;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.Mockito;

import skolard.logic.auth.LoginHandler;
import skolard.logic.profile.DefaultProfileFormatter;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.session.SessionHandler;
import skolard.objects.LoginCredentials;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.PersistenceType;
import skolard.presentation.SkolardApp;

public class SignUpSystemTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private ProfileHandler realProfileHandler;
    private LoginHandler realLoginHandler;

    @Override
    protected void onSetUp() throws Exception {
        PersistenceFactory.initialize(PersistenceType.TEST, false);

        SessionHandler sessionHandler = new SessionHandler(
            PersistenceRegistry.getSessionPersistence(),
            PersistenceRegistry.getRatingRequestPersistence()
        );

        realProfileHandler = new ProfileHandler(
            PersistenceRegistry.getStudentPersistence(),
            PersistenceRegistry.getTutorPersistence(),
            new DefaultProfileFormatter(),
            sessionHandler
        );

        realLoginHandler = new LoginHandler(PersistenceRegistry.getLoginPersistence());
        relaunchSignupView();
    }

    private void relaunchSignupView() {
        if (window != null) {
            window.cleanUp();
        }
        SkolardApp mockParentApp = Mockito.mock(SkolardApp.class);
        SignUpView view = GuiActionRunner.execute(() -> new SignUpView(realProfileHandler, realLoginHandler, mockParentApp));
        window = new FrameFixture(robot(), view);
        window.show();
    }

    @Test
    public void testStudentSignUpActuallyPersistsToDatabase() {
        window.textBox("nameField").setText("Alice Johnson");
        window.textBox("emailField").setText("alice@uofm.ca");
        window.textBox("passwordField").setText("securepass123");
        window.textBox("confirmPasswordField").setText("securepass123");

        window.button("signUpStudentBtn").click();
        window.optionPane().requireInformationMessage().okButton().click();
        robot().waitForIdle();

        Student savedStudent = realProfileHandler.getStudent("alice@uofm.ca");
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getName()).isEqualTo("Alice Johnson");

        LoginCredentials creds = new LoginCredentials("alice@uofm.ca", "securepass123", "student");
        assertThat(realLoginHandler.login(creds)).isTrue();
    }

    @Test
    public void testTutorSignUpActuallyPersistsToDatabase() {
        window.textBox("nameField").setText("Dr. Bob Smith");
        window.textBox("emailField").setText("bob@uofm.ca");
        window.textBox("passwordField").setText("tutorpass456");
        window.textBox("confirmPasswordField").setText("tutorpass456");

        window.button("signUpTutorBtn").click();
        window.optionPane().requireInformationMessage().okButton().click();
        robot().waitForIdle();

        Tutor savedTutor = realProfileHandler.getTutor("bob@uofm.ca");
        assertThat(savedTutor).isNotNull();
        assertThat(savedTutor.getName()).isEqualTo("Dr. Bob Smith");

        LoginCredentials creds = new LoginCredentials("bob@uofm.ca", "tutorpass456", "tutor");
        assertThat(realLoginHandler.login(creds)).isTrue();
    }

@Test
public void testDuplicateEmailPrevention() {
    window.textBox("nameField").setText("First Student");
    window.textBox("emailField").setText("duplicate@uofm.ca");
    window.textBox("passwordField").setText("password1");
    window.textBox("confirmPasswordField").setText("password1");
    window.button("signUpStudentBtn").click();
    window.optionPane().requireInformationMessage().okButton().click();

    window.cleanUp();
    relaunchSignupView();

    window.textBox("nameField").setText("Second Student");
    window.textBox("emailField").setText("duplicate@uofm.ca");
    window.textBox("passwordField").setText("password2");
    window.textBox("confirmPasswordField").setText("password2");
    window.button("signUpStudentBtn").click();

    // Changed from requireWarningMessage() to requireErrorMessage()
    window.optionPane().requireErrorMessage().okButton().click();
    robot().waitForIdle();

    Student student = realProfileHandler.getStudent("duplicate@uofm.ca");
    assertThat(student.getName()).isEqualTo("First Student");
}


    @Test
    public void testPasswordMismatchShowsValidationError() {
        window.textBox("nameField").setText("Charlie");
        window.textBox("emailField").setText("charlie@uofm.ca");
        window.textBox("passwordField").setText("pass1234");
        window.textBox("confirmPasswordField").setText("pass4321");

        window.button("signUpStudentBtn").click();
        window.optionPane()
                .requireErrorMessage()
                .requireMessage("Passwords do not match!")
                .okButton().click();
    }

    @Test
    public void testInvalidEmailFormatShowsError() {
        window.textBox("nameField").setText("Dana");
        window.textBox("emailField").setText("bad-email");
        window.textBox("passwordField").setText("password");
        window.textBox("confirmPasswordField").setText("password");

        window.button("signUpStudentBtn").click();
        window.optionPane()
                .requireErrorMessage()
                .requireMessage("Please enter a valid email address")
                .okButton().click();
    }

    @Test
    public void testEmptyFieldsBlockSubmission() {
        window.textBox("nameField").setText("");
        window.textBox("emailField").setText("");
        window.textBox("passwordField").setText("");
        window.textBox("confirmPasswordField").setText("");

        window.button("signUpStudentBtn").click();
        window.optionPane()
                .requireErrorMessage()
                .requireMessage("Please fill in all fields")
                .okButton().click();
    }

    @Test
    public void testPasswordTooShortBlocksSignUp() {
        window.textBox("nameField").setText("Eva");
        window.textBox("emailField").setText("eva@uofm.ca");
        window.textBox("passwordField").setText("123");
        window.textBox("confirmPasswordField").setText("123");

        window.button("signUpStudentBtn").click();
        window.optionPane()
                .requireErrorMessage()
                .requireMessage("Password must be at least 6 characters")
                .okButton().click();
    }

    @Test
    public void testUnexpectedExceptionDuringStudentSignup() {
        ProfileHandler mockHandler = Mockito.mock(ProfileHandler.class);
        SkolardApp mockApp = Mockito.mock(SkolardApp.class);
        LoginHandler mockLogin = Mockito.mock(LoginHandler.class);

        SignUpView view = GuiActionRunner.execute(() ->
            new SignUpView(mockHandler, mockLogin, mockApp)
        );
        window = new FrameFixture(robot(), view);
        window.show();

        window.textBox("nameField").setText("Failsafe User");
        window.textBox("emailField").setText("fail@uofm.ca");
        window.textBox("passwordField").setText("secret123");
        window.textBox("confirmPasswordField").setText("secret123");

        Mockito.doThrow(new RuntimeException("Unexpected failure"))
            .when(mockHandler).addStudent(Mockito.any(), Mockito.any(), Mockito.any());

        window.button("signUpStudentBtn").click();

        window.optionPane()
                .requireErrorMessage()
                .requireMessage("Error creating account: Unexpected failure")
                .okButton().click();
    }

    @Override
    protected void onTearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }
}
