package skolard.logic;

import java.util.ArrayList;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;

/**
 * The MatchingHandler class handles the logic for matching students
 * with available tutoring sessions. It allows session searching, booking,
 * and session list management based on course name.
 */
public class MatchingHandler {
    
    // Holds the list of available sessions (can be sorted by rating, time, etc.)
    private PriorityList<Session> availableSessions;

    /**
     * Default constructor.
     * Initializes the session list using TutorList (sorted by tutor rating).
     */
    public MatchingHandler(){
        // Can be instance of any other class that extends PriorityList (e.g., RatingList)
        this.availableSessions = new TutorList();
    }

    /**
     * Overloaded constructor that accepts a custom session list.
     * Useful for injecting different sorting/filtering strategies.
     *
     * @param sessionList A PriorityList implementation to manage sessions
     */
    public MatchingHandler(PriorityList<Session> sessionList){
        this.availableSessions = sessionList;
    }

    /**
     * Adds a new session to the list of available sessions.
     *
     * @param session The session to be added
     * @throws IllegalArgumentException if the session is null
     */
    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        availableSessions.addItem(session);
    }

    /**
     * Returns a list of available (unbooked) sessions for a given course.
     * The list is automatically sorted (using natural order or a comparator).
     *
     * @param courseName The name of the course to search for
     * @return A list of matching, unbooked sessions
     * @throws IllegalArgumentException if courseName is null or empty
     */
    public List<Session> getAvailableSessions(String courseName) {
        if (courseName == null || courseName.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }

        List<Session> matchingSessions = new ArrayList<>();
        for (Session session : availableSessions.getAllItems()) {
            // Match course name and ensure the session is not already booked
            if (session.getCourseName().equalsIgnoreCase(courseName) && !session.isBooked()) {
                matchingSessions.add(session);
            }
        }

        // Sort the list using natural order (Session must implement Comparable)
        matchingSessions.sort(null);
        return matchingSessions;
    }

    /**
     * Attempts to book a session for a student.
     * If the session is already booked, a message is printed.
     *
     * @param session The session to book
     * @param student The student who is booking the session
     * @throws IllegalArgumentException if either parameter is null
     */
    public void bookSession(Session session, Student student) {
        if (session == null || student == null) {
            throw new IllegalArgumentException("Session and student cannot be null.");
        }

        if (!session.isBooked()) {
            session.bookSession(student);
            System.out.println("Session " + session.getSessionId() + " booked successfully for " + student.getName());
        } else {
            System.out.println("Session " + session.getSessionId() + " is already booked.");
        }
    }

    /**
     * Clears all available sessions from the internal list.
     */
    public void clearSessions() {
        availableSessions.clear();
    }

    /**
     * Returns a string representation of the session list.
     *
     * @return The string form of availableSessions
     */
    @Override
    public String toString() {
        return availableSessions.toString();
    }
}
