package skolard.logic.matching;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.SessionPersistence;

/**
 * Handles matching students with available tutoring sessions.
 * Responsible for loading sessions and applying filters like rating, time, or tutor.
 */
public class MatchingHandler {
    private final SessionPersistence sessionDB;

    /**
     * Default constructor using the real persistence layer.
     */
    public MatchingHandler() {
        this(PersistenceRegistry.getSessionPersistence());
    }

    /**
     * Constructor for injecting a custom session database (mock/stub).
     */
    public MatchingHandler(SessionPersistence sessionPersistence) {
        this.sessionDB = sessionPersistence;
    }

    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        sessionDB.addSession(session);
    }

    public List<Session> getAvailableSessions(String filter, String courseName, LocalDateTime start, LocalDateTime end) {
        if (courseName == null || courseName.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }

        List<Session> matchingSessions = getNonBookedSessions(courseName);

        if (filter == null) return matchingSessions;

        switch (filter.toLowerCase()) {
            case "rate" -> {
                RatingList rateList = new RatingList(matchingSessions);
                matchingSessions = rateList.sortByBestCourseRating(courseName);
            }
            case "time" -> {
                if (start != null && end != null) {
                    TimeList timeList = new TimeList(matchingSessions);
                    matchingSessions = timeList.filterByStudentTimeRange(start, end, courseName);
                }
            }
            case "tutor" -> {
                TutorList tutorList = new TutorList(matchingSessions);
                matchingSessions = tutorList.getSessionsByTutor(courseName);
            }
        }

        return matchingSessions;
    }

    public List<Session> getAvailableSessions(String courseName) {
        return getNonBookedSessions(courseName);
    }

    private List<Session> getNonBookedSessions(String courseName) {
        List<Session> allSessions = sessionDB.getAllSessions();
        List<Session> sessions = new ArrayList<>();

        for (Session session : allSessions) {
            if (session.getCourseName().equalsIgnoreCase(courseName) && !session.isBooked()) {
                sessions.add(session);
            }
        }

        return sessions;
    }

    public void bookSession(Session session, Student student) {
        if (session == null || student == null) {
            throw new IllegalArgumentException("Session and student cannot be null.");
        }

        if (!session.isBooked()) {
            session.bookSession(student);
            sessionDB.updateSession(session);
            System.out.println("Session " + session.getSessionId() + " booked successfully for " + student.getName());
        } else {
            System.out.println("Session " + session.getSessionId() + " is already booked.");
        }
    }
}
