package skolard.persistence;

/**
 * Interface defining authentication methods for different user roles.
 */
public interface LoginPersistence {
    /**
     * Authenticate a student with the given email and password.
     * @param email student's email
     * @param password student's password
     * @return true if authentication succeeds, false otherwise
     */
    boolean authenticateStudent(String email, String password);

    /**
     * Authenticate a tutor with the given email and password.
     * @param email tutor's email
     * @param password tutor's password
     * @return true if authentication succeeds, false otherwise
     */
    boolean authenticateTutor(String email, String password);

    /**
     * Authenticate a support user with the given email and password.
     * @param email support user's email
     * @param password support user's password
     * @return true if authentication succeeds, false otherwise
     */
    boolean authenticateSupport(String email, String password);
}