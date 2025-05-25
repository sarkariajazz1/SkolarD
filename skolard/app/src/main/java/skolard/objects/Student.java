package skolard.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student user in the system.
 * Stores past and upcoming tutoring sessions.
 */
public class Student extends User {
    private List<Session> pastSessions;
    private List<Session> upcomingSessions;

    public Student(String name, String email) {
        super(name, email);
        this.pastSessions = new ArrayList<Session>();
        this.upcomingSessions = new ArrayList<Session>();
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

    // Adds a single session to the upcoming list
    public void addUpcomingSession(Session session) {
        upcomingSessions.add(session);
    }

    // Unused method: could be removed or renamed properly
    public void setUpcomingSession(Session session) { }

    // Replaces all upcoming sessions
    public void setUpcomingSessions(List<Session> upcomingSessions) {
        this.upcomingSessions.addAll(upcomingSessions);
    }
}
