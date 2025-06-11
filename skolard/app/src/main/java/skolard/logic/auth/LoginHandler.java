package skolard.logic.auth;

import skolard.objects.LoginCredentials;
import skolard.persistence.LoginPersistence;
import skolard.persistence.PersistenceRegistry;

/**
 * Handles login logic for authenticating students, tutors, and support users.
 */
public class LoginHandler {

    // Reference to the LoginPersistence interface used to authenticate users
    private final LoginPersistence loginDB;

    /**
     * Default constructor.
     * Uses the default persistence implementation registered for production use.
     */
    public LoginHandler() {
        this(PersistenceRegistry.getLoginPersistence());
    }

    /**
     * Constructor for dependency injection.
     * Allows passing a custom LoginPersistence implementation (e.g., for testing).
     *
     * @param loginDB the injected LoginPersistence instance
     */
    public LoginHandler(LoginPersistence loginDB) {
        this.loginDB = loginDB;
    }

    /**
     * Attempts to authenticate a user based on the provided credentials.
     * Supports multiple roles: student, tutor, and support.
     *
     * @param credentials the LoginCredentials object containing email, password, and role
     * @return true if authentication is successful, false otherwise
     */
    public boolean login(LoginCredentials credentials) {
        // Ensure credentials and role are not null
        if (credentials == null || credentials.getRole() == null) {
            return false;
        }

        // Normalize role to lowercase for comparison
        String role = credentials.getRole().toLowerCase();
        String email = credentials.getEmail();
        String password = credentials.getPlainPassword();

        // Use appropriate authentication method based on role
        return switch (role) {
            case "student" -> loginDB.authenticateStudent(email, password);
            case "tutor" -> loginDB.authenticateTutor(email, password);
            case "support" -> loginDB.authenticateSupport(email, password);
            default -> {
                // Unknown role – log error and fail authentication
                System.err.println("Unknown role: " + role);
                yield false;
            }
        };
    }
}
