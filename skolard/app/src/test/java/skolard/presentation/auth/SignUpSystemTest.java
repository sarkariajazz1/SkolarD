package skolard.presentation.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.core.ComponentMatcher;
import org.assertj.swing.core.NameMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

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
        // Initialize with stub persistence for testing
        PersistenceFactory.initialize(PersistenceType.STUB, false);

        // Create real handlers with actual persistence - need SessionHandler for ProfileHandler
        SessionHandler sessionHandler = new SessionHandler(PersistenceRegistry.getSessionPersistence());
        realProfileHandler = new ProfileHandler(
                PersistenceRegistry.getStudentPersistence(),
                PersistenceRegistry.getTutorPersistence(),
                new DefaultProfileFormatter(),
                sessionHandler
        );
        realLoginHandler = new LoginHandler(
                PersistenceRegistry.getLoginPersistence()
        );

        SkolardApp mockParentApp = org.mockito.Mockito.mock(SkolardApp.class);

        SignUpView signUpView = GuiActionRunner.execute(() -> {
            SignUpView view = new SignUpView(realProfileHandler, realLoginHandler, mockParentApp);
            view.setVisible(false); // Don't show the window
            return view;
        });

        window = new FrameFixture(robot(), signUpView);
        window.show(); // This makes components available for testing without actually displaying
    }

    @Test
    public void testStudentSignUpActuallyPersistsToDatabase() throws Exception {
        // Fill registration form
        window.textBox("nameField").setText("Alice Johnson");
        window.textBox("emailField").setText("alice@uofm.ca");
        window.textBox("passwordField").setText("securepass123");
        window.textBox("confirmPasswordField").setText("securepass123");

        // Submit registration
        window.button("signUpStudentBtn").click();

        // Handle success dialog
        window.optionPane()
                .requireInformationMessage()
                .okButton().click();

        robot().waitForIdle();

        // Verify student was actually saved to database
        Student savedStudent = realProfileHandler.getStudent("alice@uofm.ca");
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getName()).isEqualTo("Alice Johnson");
        assertThat(savedStudent.getEmail()).isEqualTo("alice@uofm.ca");

        // Verify login credentials using the login method that exists
        LoginCredentials creds = new LoginCredentials("alice@uofm.ca", "securepass123", "student");
        boolean canLogin = realLoginHandler.login(creds);
        assertThat(canLogin).isTrue();
    }

    @Test
    public void testTutorSignUpActuallyPersistsToDatabase() throws Exception {
        window.textBox("nameField").setText("Dr. Bob Smith");
        window.textBox("emailField").setText("bob@uofm.ca");
        window.textBox("passwordField").setText("tutorpass456");
        window.textBox("confirmPasswordField").setText("tutorpass456");

        window.button("signUpTutorBtn").click();

        window.optionPane()
                .requireInformationMessage()
                .okButton().click();

        robot().waitForIdle();

        // Verify tutor was actually saved
        Tutor savedTutor = realProfileHandler.getTutor("bob@uofm.ca");
        assertThat(savedTutor).isNotNull();
        assertThat(savedTutor.getName()).isEqualTo("Dr. Bob Smith");
        assertThat(savedTutor.getEmail()).isEqualTo("bob@uofm.ca");

        // Verify login credentials using the login method that exists
        LoginCredentials creds = new LoginCredentials("bob@uofm.ca", "tutorpass456", "tutor");
        boolean canLogin = realLoginHandler.login(creds);
        assertThat(canLogin).isTrue();
    }

    @Test
    public void testDuplicateEmailPrevention() throws Exception {
        // Create first student
        window.textBox("nameField").setText("First Student");
        window.textBox("emailField").setText("duplicate@uofm.ca");
        window.textBox("passwordField").setText("password1");
        window.textBox("confirmPasswordField").setText("password1");
        window.button("signUpStudentBtn").click();
        window.optionPane().okButton().click();

        // Clear form and try to create another with same email
        window.textBox("nameField").setText("");
        window.textBox("emailField").setText("");
        window.textBox("passwordField").setText("");
        window.textBox("confirmPasswordField").setText("");

        window.textBox("nameField").setText("Second Student");
        window.textBox("emailField").setText("duplicate@uofm.ca");
        window.textBox("passwordField").setText("password2");
        window.textBox("confirmPasswordField").setText("password2");
        window.button("signUpStudentBtn").click();

        // Should show error for duplicate email
        window.optionPane()
                .requireErrorMessage()
                .okButton().click();

        robot().waitForIdle();

        // Verify only first student exists
        Student student = realProfileHandler.getStudent("duplicate@uofm.ca");
        assertThat(student.getName()).isEqualTo("First Student");
    }

    @Override
    protected void onTearDown() {
        if (window != null) {
            window.cleanUp();
        }
        PersistenceFactory.reset();
    }
}