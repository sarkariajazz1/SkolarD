package skolard.objects;

/**
 * Abstract base class representing a general user in SkolarD.
 * Extended by more specific user types like Student and Tutor.
 */
public abstract class User {
    // Full name of the user
    protected String name;

    // Email address used for login and identification
    protected String email;

    // Hashed password used for secure authentication
    protected final String hashedPassword;

    /**
     * Constructs a User with full credentials, typically used during login or registration.
     *
     * @param name            the user's name
     * @param email           the user's email
     * @param hashedPassword  the user's hashed password
     */
    public User(String name, String email, String hashedPassword) { 
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword != null ? hashedPassword : "";
    }

    /**
     * Constructs a User with name and email only.
     * Useful when password is not needed (e.g., profile views).
     *
     * @param name   the user's name
     * @param email  the user's email
     */
    public User(String name, String email) {
        this(name, email, ""); // Default to empty hash
    }

    /**
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the user's hashed password
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Updates the user's name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Updates the user's email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}