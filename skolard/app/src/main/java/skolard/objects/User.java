package skolard.objects;

/**
 * Abstract base class representing a general user in SkolarD.
 * Can be extended into Student or Tutor.
 */
public abstract class User {
    protected String id;      // Unique identifier
    protected String name;    // Display name
    protected String email;   // Contact email

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
