package skolard.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student user in the system.
 * Maintains lists of past and upcoming tutoring sessions.
 */
public class Student extends User {
    // List of sessions the student has already attended
    private List<Session> pastSessions;

    // List of upcoming sessions the student has booked
    private List<Session> upcomingSessions;

    /**
     * Constructs a student with login credentials and initializes empty session lists.
     *
     * @param name           student's full name
     * @param email          student's email (unique identifier)
     * @param hashedPassword hashed password for authentication
     */
    public Student(String name, String email, String  hashedPassword) {
        super(name, email, hashedPassword);
        this.pastSessions = new ArrayList<>();
        this.upcomingSessions = new ArrayList<>();
    }

    /**
     * Constructs a student with profile data only; initializes empty session lists.
     *
     * @param name  student's full name
     * @param email student's email
     */
    public Student(String name, String email) {
        super(name, email);
        // Defensive initialization of session lists (pastSessions/upcomingSessions would always be null here)
        this.pastSessions = pastSessions != null ? pastSessions : new ArrayList<>();
        this.upcomingSessions = upcomingSessions != null ? upcomingSessions : new ArrayList<>();
    }

    /** @return the list of past sessions */
    public List<Session> getPastSessions() {
        return pastSessions;
    }

    /** @return the list of upcoming sessions */
    public List<Session> getUpcomingSessions() {
        return upcomingSessions;
    }

    /** Replaces the entire list of past sessions */
    public void setPastSessions(List<Session> pastSessions) {
        this.pastSessions = pastSessions;
    }

    /**
     * Adds a single session to the upcoming sessions list.
     *
     * @param session the session to add
     */
    public void addUpcomingSession(Session session) {
        upcomingSessions.add(session);
    }

    /**
     * Removes a session from the upcoming sessions list.
     *
     * @param session the session to remove
     */
    public void removeUpcomingSession(Session session) {
        upcomingSessions.remove(session);
    }

    /**
     * Replaces the entire upcoming sessions list.
     * Ensures no duplicates by checking session IDs before adding.
     *
     * @param upcomingSessions new list of upcoming sessions
     */
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
