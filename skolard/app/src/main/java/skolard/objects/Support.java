package skolard.objects;

/**
 * Represents a support staff user in the SkolarD system.
 * Interacts with tickets through the SupportHandler, not local state.
 */
public class Support extends User {

    public Support(String name, String email) {
        super(name, email);
    }

    @Override
    public String toString() {
        return "Support Staff: " + name + " (" + email + ")";
    }
}


