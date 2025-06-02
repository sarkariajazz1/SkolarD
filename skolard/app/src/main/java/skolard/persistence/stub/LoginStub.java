package skolard.persistence.stub;

import java.util.HashMap;
import java.util.Map;

import skolard.persistence.LoginPersistence;

/**
 * In-memory stub for login authentication.
 * Matches hashed passwords for predefined student and tutor test accounts.
 */
public class LoginStub implements LoginPersistence {

    // Simulated user credentials (email -> hashed password)
    private final Map<String, String> studentCredentials = new HashMap<>();
    private final Map<String, String> tutorCredentials = new HashMap<>();

    public LoginStub() {
        // Add hardcoded student
        studentCredentials.put("test@student.com", hash("pass123"));
        studentCredentials.put("raj@skolard.ca", hash("raj123"));
        studentCredentials.put("simran@skolard.ca", hash("simran123"));

        // Add hardcoded tutor
        tutorCredentials.put("test@tutor.com", hash("pass123"));
        tutorCredentials.put("amrit@skolard.ca", hash("amrit123"));
        tutorCredentials.put("sukhdeep@skolard.ca", hash("sukhdeep123"));
    }

    @Override
    public boolean authenticateStudent(String email, String password) {
        return studentCredentials.containsKey(email) &&
                studentCredentials.get(email).equals(hash(password));
    }

    @Override
    public boolean authenticateTutor(String email, String password) {
        return tutorCredentials.containsKey(email) &&
                tutorCredentials.get(email).equals(hash(password));
    }

    /**
     * Simulated password hashing.
     * @param password plaintext
     * @return a hashed string (simple hex hash)
     */
    private String hash(String password) {
        return Integer.toHexString(password.hashCode());
    }
}
