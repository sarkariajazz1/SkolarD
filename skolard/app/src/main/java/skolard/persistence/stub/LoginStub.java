package skolard.persistence.stub;

import java.util.HashMap;
import java.util.Map;

import skolard.persistence.LoginPersistence;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;
import skolard.utils.PasswordUtil;

/**
 * In-memory stub for login authentication.
 * Matches hashed passwords for predefined student, tutor, and support test accounts.
 */
public class LoginStub implements LoginPersistence {

    private final StudentPersistence sp;
    private final TutorPersistence tp;

    private final Map<String, String> supportCredentials = new HashMap<>(); // ✅ Support user

    public LoginStub(StudentPersistence sp, TutorPersistence tp) {
        this.sp = sp;
        this.tp = tp;

        // Support
        supportCredentials.put("support@skolard.ca", hash("admin123")); // ✅ Add support login
    }

    @Override
    public boolean authenticateStudent(String email, String password) {
        return sp.authenticate(email, hash(password)) != null;
    }

    @Override
    public boolean authenticateTutor(String email, String password) {
        return tp.authenticate(email, hash(password)) != null;
    }

    @Override
    public boolean authenticateSupport(String email, String password) {
        return supportCredentials.containsKey(email)
                && supportCredentials.get(email).equals(hash(password));
    }

    private String hash(String password) {
        return PasswordUtil.hash(password);
    }
}
