package skolard.objects;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<Session> pastSessions    = new ArrayList<>();
    private List<Session> upcomingSessions = new ArrayList<>();

    public Student(String id, String name, String email) {
        super(id, name, email);
        this.pastSessions = new ArrayList();
        this.upcomingSessions = new ArrayList();
    }

    public List<Session> getPastSessions() {
        return pastSessions;
    }

    public List<Session> getUpcomingSessions() {
        return upcomingSessions;
    }

    public void setPastSessions(List<Session> pastSessions) {
        this.pastSessions = pastSessions;
    }

    // rename this to "addUpcomingSession" to match what it actually does
    public void addUpcomingSession(Session session) {
        upcomingSessions.add(session);
    }

    public void setUpcomingSession(Session session) {

    }

        public void setUpcomingSessions(List<Session> upcomingSessions) {
        this.upcomingSessions.addAll(upcomingSessions);
    }
}
