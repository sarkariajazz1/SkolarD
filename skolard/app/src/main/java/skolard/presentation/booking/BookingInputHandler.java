package skolard.presentation.booking;

import skolard.logic.booking.BookingHandler;
import skolard.logic.booking.BookingHandler.SessionFilter;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.utils.CourseUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingInputHandler {
    // Formatter for parsing date-time input strings in "yyyy-MM-dd HH:mm" format
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Returns a list of available sessions filtered and sorted based on given parameters.
     *
     * @param rawCourse Raw course code input that will be normalized
     * @param filter    Sorting/filtering criteria ("Sort by Time", "Sort by Tutor Course Grade", etc.)
     * @param startStr  Start time string for time-based filtering (format: yyyy-MM-dd HH:mm)
     * @param endStr    End time string for time-based filtering (format: yyyy-MM-dd HH:mm)
     * @param student   The student requesting the sessions (used for filtering out booked sessions)
     * @param handler   BookingHandler instance used to retrieve sessions
     * @return List of filtered and sorted sessions
     * @throws IllegalArgumentException if course is empty or parsing fails
     */
    public static List<Session> getFilteredSessions(String rawCourse, String filter, String startStr, String endStr,
                                                    Student student, BookingHandler handler) {
        // Normalize course code input to a standard format
        String course = CourseUtil.normalizeCourseCode(rawCourse);
        if (course.isEmpty()) throw new IllegalArgumentException("Course is required");

        // Apply different filters depending on the filter string
        if ("Sort by Time".equals(filter)) {
            // Parse start and end time strings to LocalDateTime objects
            LocalDateTime start = LocalDateTime.parse(startStr.trim(), formatter);
            LocalDateTime end = LocalDateTime.parse(endStr.trim(), formatter);
            // Retrieve sessions filtered by time range
            return handler.getAvailableSessions(SessionFilter.TIME, course, start, end, student.getEmail());
        } else if ("Sort by Tutor Course Grade".equals(filter)) {
            // Retrieve sessions sorted by tutor's course grade
            return handler.getAvailableSessions(SessionFilter.RATE, course, null, null, student.getEmail());
        } else if ("Sort by Overall Tutor Rating".equals(filter)) {
            // Retrieve sessions sorted by tutor's overall rating
            return handler.getAvailableSessions(SessionFilter.TUTOR, course, null, null, student.getEmail());
        } else {
            // Default: retrieve sessions for the course without additional filters
            return handler.getAvailableSessions(course, student.getEmail());
        }
    }
}