package skolard.objects;

/**
 * Lightweight wrapper for login input.
 * Contains the user's email, plain-text password, and selected role (e.g., "student" or "tutor").
 */
public class LoginCredentials {
    // User's email used for identification
    private final String email;

    // Plain-text password entered during login (should be hashed before storage or verification)
    private final String plainPassword;

    // Role of the user attempting to log in (e.g., "student", "tutor")
    private final String role;

    /**
     * Constructs a LoginCredentials object with email, password, and role.
     *
     * @param email         the user's email
     * @param plainPassword the user's plain-text password
     * @param role          the role selected during login
     */
    public LoginCredentials(String email, String plainPassword, String role) {
        this.email = email;
        this.plainPassword = plainPassword;
        this.role = role;
    }

    /**
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the plain-text password entered
     */
    public String getPlainPassword() {
        return plainPassword;
    }

    /**
     * @return the selected login role
     */
    public String getRole() {
        return role;
    }
}
