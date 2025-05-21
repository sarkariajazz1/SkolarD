package skolard.logic;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import skolard.objects.Session;

/**
 * A specialized version of PriorityList that filters tutoring sessions
 * based on the student's available time range and the course name.
 */
public class TimeList extends PriorityList<Session> {

    /**
     * Default constructor that initializes the internal session list.
     */
    public TimeList(){
        super();
    }

    /**
     * Filters the sessions to find only those that:
     * - Match the specified course name
     * - Start and end within the student's available time range
     *
     * @param studentStart The beginning of the student's available time
     * @param studentEnd The end of the student's available time
     * @param courseName The course the student is looking for
     * @return A list of sessions that match both time and course criteria
     */
    public List<Session> filterByStudentTimeRange(LocalDateTime studentStart, LocalDateTime studentEnd, String courseName) {
        // Return empty list if the session list is empty or input is invalid
        if (items.isEmpty()) {
            return Collections.emptyList();
        } else if (studentStart == null || studentEnd == null || courseName == null || courseName.isEmpty()) {
            return Collections.emptyList();
        }

        // Return only sessions that fall within the student's time range for the given course
        return items.stream()
                .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
                .filter(session ->
                        !session.getEndDateTime().isBefore(studentStart) &&
                        !session.getStartDateTime().isAfter(studentEnd))
                .collect(Collectors.toList());
    }
}
