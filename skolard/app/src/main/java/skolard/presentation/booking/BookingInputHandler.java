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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static List<Session> getFilteredSessions(String rawCourse, String filter, String startStr, String endStr,
                                                    Student student, BookingHandler handler) {
        String course = CourseUtil.normalizeCourseCode(rawCourse);
        if (course.isEmpty()) throw new IllegalArgumentException("Course is required");

        if ("Sort by Time".equals(filter)) {
            LocalDateTime start = LocalDateTime.parse(startStr.trim(), formatter);
            LocalDateTime end = LocalDateTime.parse(endStr.trim(), formatter);
            return handler.getAvailableSessions(SessionFilter.TIME, course, start, end, student.getEmail());
        } else if ("Sort by Tutor Course Grade".equals(filter)) {
            return handler.getAvailableSessions(SessionFilter.RATE, course, null, null, student.getEmail());
        } else if ("Sort by Overall Tutor Rating".equals(filter)) {
            return handler.getAvailableSessions(SessionFilter.TUTOR, course, null, null, student.getEmail());
        } else {
            return handler.getAvailableSessions(course, student.getEmail());
        }
    }
}
