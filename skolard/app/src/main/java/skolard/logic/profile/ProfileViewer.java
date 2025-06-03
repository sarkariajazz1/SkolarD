package skolard.logic.profile;

import skolard.objects.User;

public class ProfileViewer {
    private final ProfileFormatter formatter;

    public ProfileViewer(ProfileFormatter formatter) {
        this.formatter = formatter;
    }

    public String viewBasicProfile(User user) {
        return (user == null) ? "" : formatter.basic(user);
    }

    public String viewFullProfile(User user) {
        return (user == null) ? "" : formatter.full(user);
    }
}
