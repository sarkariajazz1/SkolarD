package skolard.logic.profile;

import skolard.objects.User;

/**
 * Interface for rendering user profile views.
 */
public interface ProfileFormatter {

    /**
     * Returns a basic profile view (name and email).
     * @param user the user to format
     * @return formatted profile string
     */
    String basic(User user);

    /**
     * Returns a full profile view (includes all additional info).
     * @param user the user to format
     * @return formatted full profile string
     */
    String full(User user);
}
