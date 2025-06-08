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

    public Student(String name, String email, String  hashedPassword) {
        super(name, email, hashedPassword);
        this.pastSessions = new ArrayList<>();
        this.upcomingSessions = new ArrayList<>();
    }
    public Student(String name, String email) {
        super(name, email);
        this.pastSessions = pastSessions != null ? pastSessions : new ArrayList<>();
        this.upcomingSessions = upcomingSessions != null ? upcomingSessions : new ArrayList<>();
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

    public void removeUpcomingSession(Session session) {
        upcomingSessions.remove(session);
    }


    // Replaces all upcoming sessions
    public void setUpcomingSessions(List<Session> upcomingSessions) {
        this.upcomingSessions = new ArrayList<>(upcomingSessions);
        for (Session s : upcomingSessions) {
            boolean exists = this.upcomingSessions.stream()
                .anyMatch(existing -> existing.getSessionId() == s.getSessionId());

            if (!exists) {
                this.upcomingSessions.add(s);
            }
        }
    }
}
