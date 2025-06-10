// package skolard.presentation.auth;

// import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
// import org.assertj.swing.fixture.FrameFixture;
// import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import skolard.logic.auth.LoginHandler;
// import skolard.logic.profile.ProfileHandler;
// import skolard.presentation.SkolardApp;

// import static org.mockito.Mockito.*;

// public class LoginViewTest extends AssertJSwingJUnitTestCase {
//     private FrameFixture window;
//     private LoginHandler mockLoginHandler;
//     private ProfileHandler mockProfileHandler;
//     private SkolardApp mockApp;

//     @Override
//     protected void onSetUp() {
//         mockLoginHandler = mock(LoginHandler.class);
//         mockProfileHandler = mock(ProfileHandler.class);
//         mockApp = mock(SkolardApp.class);

//         FailOnThreadViolationRepaintManager.install();
//         LoginView frame = new LoginView(mockProfileHandler, mockLoginHandler, mockApp);
//         window = new FrameFixture(robot(), frame);
//         window.show(); // shows the frame to test
//     }

//     @Test
//     public void testEmptyCredentials_ShowsError() {
//         window.button("loginStudentBtn").click();
//         window.label("statusLabel").requireText("Please enter both email and password");
//     }

//     // Add more tests like:
//     // - Valid login and profile found
//     // - Valid login but profile is null
//     // - Invalid login shows error
// }
