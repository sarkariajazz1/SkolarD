package skolard.objects;

/**
 * Lightweight wrapper for login input (email, plain-text password, and role).
 */
public class LoginCredentials {
    private final String email;
    private final String plainPassword;
    private final String role;

    public LoginCredentials(String email, String plainPassword, String role) {
        this.email = email;
        this.plainPassword = plainPassword;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public String getRole() {
        return role;
    }
}
