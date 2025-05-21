package skolard.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;

public class MatchingHandler {
    // Main decision-making class to match tutors and students
    private PriorityList<Session> availableSessions;

    // Default constructor uses TutorList (sorted by tutor rating)
    public  MatchingHandler() {
        this.availableSessions = new TutorList();
    }

    // Alternate constructor with custom priority list
    public  MatchingHandler(PriorityList<Session> sessionList) {
        this.availableSessions = sessionList;
    }

    // Add a session to the available list
    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        availableSessions.addItem(session);
    }

    // List available sessions for a specific course, sorted by tutor rating
    public List<Session> getAvailableSessions(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }

        List<Session> matchingSessions = new ArrayList<>();
        for (Session session : availableSessions.getAllItems()) {
            if (session.getCourseName().equalsIgnoreCase(courseName) && !session.isBooked()) {
                matchingSessions.add(session);
            }
        }

        // Sort by descending tutor average rating
        matchingSessions.sort(Comparator.comparingDouble(
                s -> -s.getTutor().getAverageRating()));

        return matchingSessions;
    }

    // Book a selected session for a student
    public void bookSession(Session session, Student student) {
        if (session == null || student == null) {
            throw new IllegalArgumentException("Session and student cannot be null.");
        }

        if (!session.isBooked()) {
            session.bookSession(student);
            System.out.println("Session " + session.getSessionId() +
                    " booked successfully for " + student.getName());
        } else {
            System.out.println("Session " + session.getSessionId() + " is already booked.");
        }
    }

    // Clear all available sessions
    public void clearSessions() {
        availableSessions.clear();
    }

    @Override
    public String toString() {
        return availableSessions.toString();
    }
}
