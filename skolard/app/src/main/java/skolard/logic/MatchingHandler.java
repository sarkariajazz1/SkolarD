package skolard.logic;

import java.util.ArrayList;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.SessionPersistence;

/**
 * Handles matching students with available tutoring sessions.
 * Responsible for loading sessions and allowing booking.
 */
public class MatchingHandler {
    private PriorityList<Session> availableSessions;

    /**
     * Default constructor that initializes available sessions
     * by loading them from the persistence layer (SessionStub).
     */
    public MatchingHandler() {
        this.availableSessions = new TutorList(); // default to using TutorList

        SessionPersistence sessionDao = PersistenceFactory.getSessionPersistence();

        // Load all sessions from the stub database
        for (Session s : sessionDao.getAllSessions()) {
            this.availableSessions.addItem(s);
        }
    }

    /**
     * Constructor for dependency injection of a custom session list.
     */
    public MatchingHandler(PriorityList<Session> sessionList) {
        this.availableSessions = sessionList;
    }

    /**
     * Adds a session to the list of available sessions.
     */
    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        availableSessions.addItem(session);
    }

    /**
     * Filters and returns sessions that match a course and are not booked.
     */
    public List<Session> getAvailableSessions(String courseName) {
        if (courseName == null || courseName.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }

        List<Session> matchingSessions = new ArrayList<>();
        for (Session session : availableSessions.getAllItems()) {
            if (session.getCourseName().equalsIgnoreCase(courseName) && !session.isBooked()) {
                matchingSessions.add(session);
            }
        }

        matchingSessions.sort(null); // Natural order (if Comparable is implemented)
        return matchingSessions;
    }

    /**
     * Attempts to book a session for the provided student.
     * Also updates tutor/student internal session state.
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
     * Clears all sessions from the in-memory list.
     */
    public void clearSessions() {
        availableSessions.clear();
    }

    @Override
    public String toString() {
        return availableSessions.toString();
    }
}
