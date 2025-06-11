package skolard.objects;

/**
 * Represents a support staff user in the SkolarD system.
 * Support users interact with support tickets via SupportHandler,
 * and do not maintain local ticket state.
 */
public class Support extends User {

    /**
     * Constructs a Support user with the given name and email.
     *
     * @param name  support staff's full name
     * @param email support staff's email address
     */
    public Support(String name, String email) {
        super(name, email);
    }

    /**
     * Returns a string representation of the support user.
     *
     * @return formatted string with name and email
     */
    @Override
    public String toString() {
        return "Support Staff: " + name + " (" + email + ")";
    }
}