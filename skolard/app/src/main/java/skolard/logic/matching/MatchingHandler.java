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

    //For BookingHandler?
    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }
        sessionDB.addSession(session);
    }

    /**
     * Enum representing different types of filters that can be applied
     * to a list of tutoring sessions.
     * 
     * Each filter defines a specific strategy to modify or refine
     * the session list based on criteria like rating, time availability, or tutor.
     */
    public enum SessionFilter {
        /**
         * Sorts sessions by best course rating
         */
        RATE((sessions, course, start, end) ->
            new RatingList(sessions).sortByBestCourseRating(course)),

        /**
         * Filters and sorts sessions based on student time range
         */
        TIME((sessions, course, start, end) -> {
            // If either times are null, return an unfiltered sessions list
            if (start != null && end != null) {
                return new TimeList(sessions).filterByStudentTimeRange(start, end, course);
            }
            return sessions;
        }),

        
        /**
         * Sorts sessions by tutor's overall rating
         */
        TUTOR((sessions, course, start, end) ->
            new TutorList(sessions).getSessionsByTutor(course));

        private final SessionFilterStrategy strategy;

        /**
         * Constructor for enum constants that assigns their specific filtering strategy.
         *
         * @param strategy the logic to apply when this filter is used
         */
        SessionFilter(SessionFilterStrategy strategy) {
            this.strategy = strategy;
        }

        /**
         * Applies the filter's strategy to a list of sessions.
         *
         * @param sessions   the initial list of sessions
         * @param courseName the name of the course
         * @param start      the optional start time for time filtering
         * @param end        the optional end time for time filtering
         * @return the filtered list of sessions
         */
        public List<Session> apply(List<Session> sessions, String courseName, LocalDateTime start, LocalDateTime end) {
            return strategy.apply(sessions, courseName, start, end);
        }

        //Functional interface that defines how to filter a list of sessions.Implemented by each enum constant.
        @FunctionalInterface
        private interface SessionFilterStrategy {
            List<Session> apply(List<Session> sessions, String courseName, LocalDateTime start, LocalDateTime end);
        }
    }

    /**
     * Retrieves a list of available (non-booked) tutoring sessions for a specific course,
     * optionally applying a filter
     *
     * @param filter      the filter to apply to the sessions; can be null for no filtering
     * @param courseName  the name of the course to search sessions for (required)
     * @param start       the optional start time (used for time-based filtering)
     * @param end         the optional end time (used for time-based filtering)
     * @return a list of sessions that match the course and optional filter criteria
     * @throws IllegalArgumentException if the courseName is null or empty
     */
    public List<Session> getAvailableSessions(SessionFilter filter, String courseName, LocalDateTime start, LocalDateTime end){
        if (courseName == null || courseName.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }

        List<Session> sessions = getNonBookedSessions(courseName);

        if (filter != null) {
            sessions = filter.apply(sessions, courseName, start, end);
        }

        return sessions;
    }

    public List<Session> getAvailableSessions(String courseName){
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

    // For BookingHandler?
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
