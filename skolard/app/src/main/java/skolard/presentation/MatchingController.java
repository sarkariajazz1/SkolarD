package skolard.presentation;

import java.util.List;
import skolard.logic.matchingHandler;
import skolard.logic.TutorList;
import skolard.objects.Session;

public class MatchingController {
    private final matchingHandler handler;

    public MatchingController() {
        this.handler = new matchingHandler(new TutorList());
    }

    /** Returns all sessions matching a given course code */
    public List<Session> getMatches(String courseCode) {
        return handler.getAvailableSessions(courseCode);
    }
}
