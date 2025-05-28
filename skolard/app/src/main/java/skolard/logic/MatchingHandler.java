package skolard.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.SessionPersistence;

/**
 * Handles matching students with available tutoring sessions.
 * Responsible for loading sessions and allowing booking.
 */
public class MatchingHandler {
    private SessionPersistence sessionDB;

    /**
     * Constructor for dependency injection of a custom session list.
     */
    public MatchingHandler(SessionPersistence sessionPersistence) {
        this.sessionDB = sessionPersistence;
    }

    /**
     * Adds a session to the list of available sessions.
     */
    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        sessionDB.addSession(session);
    }

    /**
     * Filters and returns sessions that match a course and are not booked.
     * Applies additional filters such as rating, time range, or tutor.
     */
    public List<Session> getAvailableSessions(String filter, String courseName, LocalDateTime start, LocalDateTime end) {
        if (courseName == null || courseName.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }

        List<Session> matchingSessions = addNonBookedSessions(courseName);

        switch (filter) {
            case "Rate":
                RatingList rateList = new RatingList(matchingSessions);
                matchingSessions = rateList.sortByBestCourseRating(courseName);
                break;

            case "Time":
                TimeList timeList = new TimeList(matchingSessions);
                matchingSessions = timeList.filterByStudentTimeRange(start, end, courseName);
                break;

            case "Tutor":
                TutorList tutorList = new TutorList(matchingSessions);
                matchingSessions = tutorList.getSessionsByTutor(courseName);
                break;
        }

        return matchingSessions;
    }

    /**
     * Overloaded version for simple course-only search with no filter.
     * Returns all non-booked sessions for the given course.
     */
    public List<Session> getAvailableSessions(String courseName) {
        return addNonBookedSessions(courseName);
    }

    /**
     * Extracts only the non-booked sessions that match the given course.
     */
    public List<Session> addNonBookedSessions(String courseName) {
        List<Session> allSessions = sessionDB.getAllSessions();
        List<Session> sessions = new ArrayList<>();

        for (Session session : allSessions) {
            if (session.getCourseName().equalsIgnoreCase(courseName) && !session.isBooked()) {
                sessions.add(session);
            }
        }

        return sessions;
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
            sessionDB.updateSession(session);
            System.out.println("Session " + session.getSessionId() + " booked successfully for " + student.getName());
        } else {
            System.out.println("Session " + session.getSessionId() + " is already booked.");
        }
    }

    @Override
    public String toString() {
        List<Session> sessions = sessionDB.getAllSessions();
        PriorityList<Session> sessionPriority = new PriorityList<>();
        
        for (Session s : sessions) {
            sessionPriority.addItem(s);
        }

        return sessionPriority.toString();
    }
}
