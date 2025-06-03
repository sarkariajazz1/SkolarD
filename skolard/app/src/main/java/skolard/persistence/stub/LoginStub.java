package skolard.persistence.stub;

import java.util.HashMap;
import java.util.Map;

import skolard.persistence.LoginPersistence;

/**
 * In-memory stub for login authentication.
 * Matches hashed passwords for predefined student, tutor, and support test accounts.
 */
public class LoginStub implements LoginPersistence {

    private final Map<String, String> studentCredentials = new HashMap<>();
    private final Map<String, String> tutorCredentials = new HashMap<>();
    private final Map<String, String> supportCredentials = new HashMap<>(); // ✅ Support users

    public LoginStub() {
        // Students
        studentCredentials.put("test@student.com", hash("pass123"));
        studentCredentials.put("raj@skolard.ca", hash("raj123"));
        studentCredentials.put("simran@skolard.ca", hash("simran123"));

        // Tutors
        tutorCredentials.put("test@tutor.com", hash("pass123"));
        tutorCredentials.put("amrit@skolard.ca", hash("amrit123"));
        tutorCredentials.put("sukhdeep@skolard.ca", hash("sukhdeep123"));

        // Support
        supportCredentials.put("support@skolard.ca", hash("admin123")); // ✅ Add support login
    }

    @Override
    public boolean authenticateStudent(String email, String password) {
        return studentCredentials.containsKey(email)
                && studentCredentials.get(email).equals(hash(password));
    }

    @Override
    public boolean authenticateTutor(String email, String password) {
        return tutorCredentials.containsKey(email)
                && tutorCredentials.get(email).equals(hash(password));
    }

    @Override
    public boolean authenticateSupport(String email, String password) {
        return supportCredentials.containsKey(email)
                && supportCredentials.get(email).equals(hash(password));
    }

    private String hash(String password) {
        return Integer.toHexString(password.hashCode());
    }
}
