package skolard.presentation.auth;

import static org.mockito.Mockito.*;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.Mockito;

import skolard.logic.auth.LoginHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.presentation.SkolardApp;

public class SignUpAcceptanceTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private ProfileHandler mockProfileHandler;
    private LoginHandler mockLoginHandler;
    private SkolardApp mockParentApp;

    @Override
    protected void onSetUp() {
        mockProfileHandler = Mockito.mock(ProfileHandler.class);
        mockLoginHandler = Mockito.mock(LoginHandler.class);
        mockParentApp = Mockito.mock(SkolardApp.class);

        SignUpView signUpView = GuiActionRunner.execute(() -> {
            SignUpView view = new SignUpView(mockProfileHandler, mockLoginHandler, mockParentApp);
            view.setVisible(false); // Don't show the window
            return view;
        });

        window = new FrameFixture(robot(), signUpView);
        window.show(); // This makes components available for testing without actually displaying
    }

    @Test
    public void testStudentSignUpSuccess() throws Exception {
        // Fill in the form
        window.textBox("nameField").setText("John Student");
        window.textBox("emailField").setText("john@uofm.ca");
        window.textBox("passwordField").setText("password123");
        window.textBox("confirmPasswordField").setText("password123");

        // Mock successful student creation - addStudent returns void
        Student mockStudent = new Student("John Student", "john@uofm.ca", "hashedPassword");
        doNothing().when(mockProfileHandler).addStudent(anyString(), anyString(), anyString());
        when(mockProfileHandler.getStudent("john@uofm.ca")).thenReturn(mockStudent);

        // Click sign up as student
        window.button("signUpStudentBtn").click();

        // Verify success message appears
        window.optionPane()
                .requireInformationMessage()
                .requireMessage("Student account created successfully!")
                .okButton().click();

        // Verify the handler methods were called with correct parameters
        verify(mockProfileHandler).addStudent("John Student", "john@uofm.ca", anyString());
        verify(mockProfileHandler).getStudent("john@uofm.ca");
    }

    @Test
    public void testTutorSignUpSuccess() throws Exception {
        window.textBox("nameField").setText("Jane Tutor");
        window.textBox("emailField").setText("jane@uofm.ca");
        window.textBox("passwordField").setText("password123");
        window.textBox("confirmPasswordField").setText("password123");

        // Mock successful tutor creation - use the constructor that takes name, email, bio
        Tutor mockTutor = new Tutor("Jane Tutor", "jane@uofm.ca", "Edit your bio...");
        doNothing().when(mockProfileHandler).addTutor(anyString(), anyString(), anyString());
        when(mockProfileHandler.getTutor("jane@uofm.ca")).thenReturn(mockTutor);

        window.button("signUpTutorBtn").click();

        window.optionPane()
                .requireInformationMessage()
                .requireMessage("Tutor account created successfully!")
                .okButton().click();

        verify(mockProfileHandler).addTutor("Jane Tutor", "jane@uofm.ca", anyString());
        verify(mockProfileHandler).getTutor("jane@uofm.ca");
    }

    @Test
    public void testPasswordMismatchError() throws Exception {
        window.textBox("nameField").setText("Test User");
        window.textBox("emailField").setText("test@uofm.ca");
        window.textBox("passwordField").setText("password123");
        window.textBox("confirmPasswordField").setText("differentpassword");

        window.button("signUpStudentBtn").click();

        window.optionPane()
                .requireErrorMessage()
                .requireMessage("Passwords do not match!")
                .okButton().click();

        // Verify no accounts were created
        verify(mockProfileHandler, never()).addStudent(anyString(), anyString(), anyString());
        verify(mockProfileHandler, never()).addTutor(anyString(), anyString(), anyString());
    }

    @Test
    public void testEmptyFieldsValidation() throws Exception {
        // Leave fields empty
        window.button("signUpStudentBtn").click();

        window.optionPane()
                .requireErrorMessage()
                .requireMessage("Please fill in all fields")
                .okButton().click();

        verify(mockProfileHandler, never()).addStudent(anyString(), anyString(), anyString());
    }

    @Test
    public void testShortPasswordValidation() throws Exception {
        window.textBox("nameField").setText("Test User");
        window.textBox("emailField").setText("test@uofm.ca");
        window.textBox("passwordField").setText("123");
        window.textBox("confirmPasswordField").setText("123");

        window.button("signUpStudentBtn").click();

        window.optionPane()
                .requireErrorMessage()
                .requireMessage("Password must be at least 6 characters")
                .okButton().click();

        verify(mockProfileHandler, never()).addStudent(anyString(), anyString(), anyString());
    }

    @Test
    public void testInvalidEmailValidation() throws Exception {
        window.textBox("nameField").setText("Test User");
        window.textBox("emailField").setText("invalidemail");
        window.textBox("passwordField").setText("password123");
        window.textBox("confirmPasswordField").setText("password123");

        window.button("signUpStudentBtn").click();

        window.optionPane()
                .requireErrorMessage()
                .requireMessage("Please enter a valid email address")
                .okButton().click();

        verify(mockProfileHandler, never()).addStudent(anyString(), anyString(), anyString());
    }

    @Override
    protected void onTearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }
}