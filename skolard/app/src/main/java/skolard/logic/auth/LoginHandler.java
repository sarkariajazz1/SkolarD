package skolard.logic.auth;

import skolard.objects.LoginCredentials;
import skolard.persistence.LoginPersistence;
import skolard.persistence.PersistenceRegistry;

/**
 * Handles login logic for authenticating students, tutors, and support users.
 */
public class LoginHandler {
    private final LoginPersistence loginDB;

    /**
     * Default constructor using production persistence implementation.
     */
    public LoginHandler() {
        this(PersistenceRegistry.getLoginPersistence());
    }

    /**
     * Constructor for injecting a custom LoginPersistence (useful for testing).
     */
    public LoginHandler(LoginPersistence loginDB) {
        this.loginDB = loginDB;
    }

    /**
     * Attempts to authenticate the user based on their credentials.
     *
     * @param credentials the login credentials containing email, password, and role
     * @return true if login is successful, false otherwise
     */
    public boolean login(LoginCredentials credentials) {
        if (credentials == null || credentials.getRole() == null) {
            return false;
        }

        String role = credentials.getRole().toLowerCase();
        String email = credentials.getEmail();
        String password = credentials.getPlainPassword();

        return switch (role) {
            case "student" -> loginDB.authenticateStudent(email, password);
            case "tutor" -> loginDB.authenticateTutor(email, password);
            case "support" -> loginDB.authenticateSupport(email, password);
            default -> {
                System.err.println("Unknown role: " + role);
                yield false;
            }
        };
    }

    /**
     * Stores login credentials via persistence layer.
     */
    public void registerCredentials(String email, String password, String role) {
        LoginCredentials credentials = new LoginCredentials(email, password, role);
        loginDB.addLoginCredentials(credentials);
    }
}
