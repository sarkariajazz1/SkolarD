package skolard.objects;

/**
 * Abstract base class representing a general user in SkolarD.
 * Extended by Student and Tutor.
 */
public abstract class User {
    protected String name;    
    protected String email;   
    protected final String hashedPassword; // Hashed password for authentication

    // Constructor for login/registration
    public User(String name, String email, String hashedPassword) { 
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword != null ? hashedPassword : "";
    }

    // Constructor for profile-only (no password known)
    public User(String name, String email) {
        this(name, email, ""); // Use default empty hash
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
