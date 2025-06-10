// package skolard.presentation.auth;

// import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
// import org.assertj.swing.fixture.FrameFixture;
// import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
// import org.junit.jupiter.api.Test;
// import skolard.logic.auth.LoginHandler;
// import skolard.logic.profile.ProfileHandler;
// import skolard.presentation.SkolardApp;

// import static org.mockito.Mockito.*;

// public class SignUpViewTest extends AssertJSwingJUnitTestCase {
//     private FrameFixture window;
//     private ProfileHandler mockProfileHandler;
//     private LoginHandler mockLoginHandler;
//     private SkolardApp mockApp;

//     @Override
//     protected void onSetUp() {
//         mockProfileHandler = mock(ProfileHandler.class);
//         mockLoginHandler = mock(LoginHandler.class);
//         mockApp = mock(SkolardApp.class);

//         FailOnThreadViolationRepaintManager.install();
//         SignUpView frame = new SignUpView(mockProfileHandler, mockLoginHandler, mockApp);
//         window = new FrameFixture(robot(), frame);
//         window.show();
//     }

//     @Test
//     public void testEmptyForm_ShowsError() {
//         window.button("signUpStudentBtn").click();
//         window.label("statusLabel").requireText("Please fill in all fields");
//     }

//     @Test
//     public void testPasswordMismatch_ShowsError() {
//         window.textBox("nameField").enterText("Test");
//         window.textBox("emailField").enterText("test@example.com");
//         window.textBox("passwordField").enterText("123456");
//         window.textBox("confirmPasswordField").enterText("654321");

//         window.button("signUpStudentBtn").click();
//         window.label("statusLabel").requireText("Passwords do not match");
//     }

//     // More tests:
//     // - Invalid email format
//     // - Password too short
//     // - Successful tutor/student registration
// }
