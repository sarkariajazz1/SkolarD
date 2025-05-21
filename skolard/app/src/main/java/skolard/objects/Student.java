package skolard.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student user who can book and attend sessions.
 */
public class Student extends User {
    private List<Session> pastSessions;       // List of sessions the student has already completed
    private List<Session> upcomingSessions;   // List of future sessions the student has booked

    public Student(String id, String name, String email) {
        super(id, name, email);
        this.pastSessions = new ArrayList<Session>();      // Initialize past sessions
        this.upcomingSessions = new ArrayList<Session>();  // Initialize upcoming sessions
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

    // Currently does nothing; consider removing or replacing
    public void setUpcomingSession(Session session) { }

    // Adds multiple sessions to the upcoming session list
    public void setUpcomingSessions(List<Session> upcomingSessions) {
        this.upcomingSessions.addAll(upcomingSessions);
    }
}
