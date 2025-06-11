package skolard.logic.profile;

import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;

/**
 * ProfileViewer is responsible for formatting and returning profile information
 * for display. It also ensures session data is refreshed for Students and Tutors
 * before rendering full profiles.
 */
public class ProfileViewer {

    // Formatter used to generate readable profile output (basic or full)
    private final ProfileFormatter formatter;

    // Reference to the SessionHandler for updating session-related user info
    private final SessionHandler sessionHandler;

    /**
     * Constructor for dependency injection.
     * Allows custom formatters and session handlers to be used.
     *
     * @param formatter profile formatting implementation
     * @param sessionHandler session manager for refreshing user session data
     */
    public ProfileViewer(ProfileFormatter formatter, SessionHandler sessionHandler) {
        this.formatter = formatter;
        this.sessionHandler = sessionHandler;
    }

    /**
     * Returns a basic profile string (e.g., name and email) for the given user.
     *
     * @param user the User object (can be a Student or Tutor)
     * @return formatted basic profile string or an empty string if user is null
     */
    public String viewBasicProfile(User user) {
        return (user == null) ? "" : formatter.basic(user);
    }

    /**
     * Returns a full profile string for the user, with session data refreshed first.
     * Ensures the Student or Tutor has up-to-date session information before formatting.
     *
     * @param user the User object (expected to be Student or Tutor)
     * @return formatted full profile string or an empty string if user is null
     */
    public String viewFullProfile(User user) {
        if (user == null) return "";

        // Refresh session data for students
        if (user instanceof Student student) {
            sessionHandler.setStudentSessionLists(student);
        }
        // Refresh session data for tutors
        else if (user instanceof Tutor tutor) {
            sessionHandler.setTutorSessionLists(tutor);
        }

        // Return the fully formatted profile
        return formatter.full(user);
    }
}
