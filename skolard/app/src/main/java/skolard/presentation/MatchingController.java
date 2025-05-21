package skolard.presentation;

import java.util.List;
import skolard.logic.MatchingHandler;
import skolard.logic.TutorList;
import skolard.objects.Session;

public class MatchingController {
    private final MatchingHandler handler;

    public MatchingController() {
        this.handler = new MatchingHandler(new TutorList());
    }

    /** Returns all sessions matching a given course code */
    public List<Session> getMatches(String courseCode) {
        return handler.getAvailableSessions(courseCode);
    }
}
