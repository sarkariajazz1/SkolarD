package skolard.objects;

/**
 * Abstract base class representing a general user in SkolarD.
 * Can be extended into Student or Tutor.
 */
public abstract class User {
    protected String name;    // Display name
    protected String email;   // Contact email
    protected String hashedPassword; // Password hash (not used in this class)

    public User(String name, String email, String hashedPassword) { 
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword; // Placeholder, should be overridden in subclasses
    }

    public String getName() {
        return name;
    }
    public String getHashedPassword(){
        return hashedPassword; // Placeholder, should be overridden in subclasses
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
