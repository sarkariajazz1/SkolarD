package skolard.objects;

/**
 * Abstract base class representing a general user in SkolarD.
 * Can be extended into Student or Tutor.
 */
public abstract class User {
    protected String name;    // Display name
    protected String email;   // Contact email

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
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
