package skolard.logic;

import java.util.ArrayList;
import java.util.List;
import skolard.objects.Session;
import skolard.objects.Student;

public class matchingHandler {
    // main decision making class
    // this class will be used to match the tutor and student
    private PriorityList<Session> availableSessions;

    public matchingHandler(){
        //Can be instance of any other class that extends PriorityList
        this.availableSessions = new TutorList();
    }

    public matchingHandler(PriorityList<Session> sessionList){
        this.availableSessions = sessionList;
    }

    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        availableSessions.addItem(session);
    }

    // List all available sessions for a specific course (automatically sorted)
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
        // Sort
        matchingSessions.sort(null);

        return matchingSessions;
    }

    // Book a selected session
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

    // Clear all available sessions
    public void clearSessions() {
        availableSessions.clear();
    }

    @Override
    public String toString() {
        return availableSessions.toString();
    }
}
