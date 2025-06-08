package skolard.logic.profile;

import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;

public class ProfileViewer {
    private final ProfileFormatter formatter;
    private final SessionHandler sessionHandler;

    public ProfileViewer(ProfileFormatter formatter, SessionHandler sessionHandler) {
        this.formatter = formatter;
        this.sessionHandler = sessionHandler;
    }

    public String viewBasicProfile(User user) {
        return (user == null) ? "" : formatter.basic(user);
    }

    public String viewFullProfile(User user) {
        if (user == null) return "";

        // Automatically refresh session lists if applicable
        if (user instanceof Student student) {
            sessionHandler.setStudentSessionLists(student);
        } else if (user instanceof Tutor tutor) {
            sessionHandler.setTutorSessionLists(tutor);
        }

        return formatter.full(user);
    }
}
