package skolard.logic;

import skolard.objects.LoginCredentials;
import skolard.persistence.LoginPersistence;
import skolard.persistence.PersistenceRegistry;

/**
 * Handles login logic for authenticating students and tutors.
 */
public class LoginHandler {
    private final LoginPersistence loginDB;
    public LoginHandler() {
        this.loginDB = PersistenceRegistry.getLoginPersistence();

    }
    
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
        String role = credentials.getRole();
        String email = credentials.getEmail();
        String password = credentials.getPlainPassword();

        switch (role.toLowerCase()) {
            case "student":
                return loginDB.authenticateStudent(email, password);
            case "tutor":
                return loginDB.authenticateTutor(email, password);
            case "support":
                // For now, support accounts may be hardcoded or simply treated as admin
                // This is just an example logic; adjust as needed
                return email.equals("support@skolard.com") && password.equals("admin123");
            default:
                System.err.println("Unknown role: " + role);
                return false;
        }
    }
}
