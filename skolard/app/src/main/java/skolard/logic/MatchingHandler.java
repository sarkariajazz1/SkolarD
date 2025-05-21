package skolard.logic;

import java.util.ArrayList;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;
import skolard.persistence.SessionPersistence;

public class MatchingHandler {
    private PriorityList<Session> availableSessions;

    public MatchingHandler() {
        this.availableSessions = new TutorList();

        // Load sessions from stub (persistence layer)
        SessionPersistence sessionDao = PersistenceFactory.getSessionPersistence();

        for (Session s : sessionDao.getAllSessions()) {
            this.availableSessions.addItem(s);
        }
    }

    public MatchingHandler(PriorityList<Session> sessionList) {
        this.availableSessions = sessionList;
    }

    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        availableSessions.addItem(session);
    }

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

        matchingSessions.sort(null); // uses natural order
        return matchingSessions;
    }

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

    public void clearSessions() {
        availableSessions.clear();
    }

    @Override
    public String toString() {
        return availableSessions.toString();
    }
}
