package skolard.logic.matching;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import skolard.objects.Session;
import skolard.persistence.SessionPersistence;

/**
 * Handles matching students with available tutoring sessions.
 * Responsible for loading sessions and applying filters like rating, time, or tutor.
 */
public class MatchingHandler {
    private final SessionPersistence sessionDB;

    /**
     * Constructor for injecting a custom session database (mock/stub).
     */
    public MatchingHandler(SessionPersistence sessionPersistence) {
        this.sessionDB = sessionPersistence;
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
            new RatingComparator(sessions).sortByBestCourseRating(course)),

        /**
         * Filters and sorts sessions based on student time range
         */
        TIME((sessions, course, start, end) -> {
            // If either times are null, return an unfiltered sessions list
            if (start != null && end != null) {
                return new TimeComparator(sessions).filterByStudentTimeRange(start, end, course);
            }
            return sessions;
        }),

        
        /**
         * Sorts sessions by tutor's overall rating
         */
        TUTOR((sessions, course, start, end) ->
            new TutorComparator(sessions).getSessionsByTutor(course));

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
     * and applying a filter
     *
     * @param filter      the filter to apply to the sessions; can be null for no filtering
     * @param courseName  the name of the course to search sessions for (required)
     * @param start       the optional start time (used for time-based filtering)
     * @param end         the optional end time (used for time-based filtering)
     * @return a list of sessions that match the course and optional filter criteria
     */
    public List<Session> getAvailableSessions(SessionFilter filter, String courseName, LocalDateTime start, LocalDateTime end, String studentEmail){
        if (courseName == null || courseName.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }

        List<Session> sessions = getNonBookedSessions(courseName, studentEmail);
        sessions = getNonPastSession(sessions);

        if (filter != null) {
            sessions = filter.apply(sessions, courseName, start, end);
        }

        return sessions;
    }

    /**
     * Overloaded method that retrieves a list of available (non-booked) tutoring sessions for a specific course not filtered
     * 
     *
     * @param courseName  the name of the course to search sessions for (required)
     * @return a list of sessions not booked and not filtered
     */
    public List<Session> getAvailableSessions(String courseName, String studentEmail){
        List<Session> sessions = new ArrayList<>();

        if (courseName == null || courseName.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty.");
        }
        
        sessions = getNonBookedSessions(courseName,studentEmail);
        sessions = getNonPastSession(sessions);

        return sessions;
    }

    /**
     * Iterates through all sessions in and filters out the non-booked tutoring sessions for a specific course
     *
     * @param courseName  the name of the course to search sessions for (required)
     * @return a list of sessions that are not booked
     */
    private List<Session> getNonBookedSessions(String courseName, String studentEmail) {
        List<Session> allSessions = sessionDB.getAllSessions();

        return allSessions.stream()
            .filter(session -> session.getCourseName().equalsIgnoreCase(courseName) && !session.isBooked())
            .filter(session -> isStudentDifferentFromTutor(session.getTutor().getEmail(), studentEmail))
            .collect(Collectors.toList());
    }

    /**
     * Filters and returns only the sessions that are scheduled to start in the future.
     *
     * @param sessions the list of all session objects to filter
     * @return a list of sessions that have not yet started (upcoming sessions)
     */
    private List<Session> getNonPastSession(List<Session> sessions){
        LocalDateTime now = LocalDateTime.now();
        return sessions.stream()
                .filter(session -> session.getStartDateTime() != null && session.getStartDateTime().isAfter(now))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the student and tutor in a session have different emails.
     *
     * @param session the session to check
     * @return true if emails are different or student is null, false otherwise
     */
    private boolean isStudentDifferentFromTutor(String tutorEmail, String studentEmail) {
        return !studentEmail.equalsIgnoreCase(tutorEmail);
    }

}
