package skolard.logic.matching;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.SessionPersistence;

/**
 * Handles matching students with available tutoring sessions.
 * Responsible for loading sessions and applying filters like rating, time, or tutor.
 */
public class MatchingHandler {
    private final SessionPersistence sessionDB;

    /**
     * Constructor for dependency injection of a session database implementation.
     */
    public MatchingHandler(SessionPersistence sessionPersistence) {
        this.sessionDB = sessionPersistence;
    }

    /**
     * Adds a session to the database.
     */
    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        sessionDB.addSession(session);
    }

    /**
     * Returns sessions that match the given course and are not booked.
     * Applies a filter: "rate", "time", or "tutor".
     */
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

    /**
     * Returns sessions for the course that are not yet booked (no filter applied).
     */
    public List<Session> getAvailableSessions(String courseName) {
        return getNonBookedSessions(courseName);
    }

    /**
     * Helper method to get non-booked sessions for a course.
     */
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

    /**
     * Books a session for a student, if the session is available.
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
}
