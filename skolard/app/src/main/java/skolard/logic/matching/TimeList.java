package skolard.logic.matching;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import skolard.objects.Session;

/**
 * TimeList is a PriorityList that filters tutoring sessions by a student's available time range.
 */
public class TimeList extends PriorityList<Session> {

    // Default constructor
    public TimeList(List<Session> sessions) {
        items = sessions;
    }

    /**
     * Filters sessions based on course name and whether they fit within the student's availability window.
     *
     * @param studentStart the earliest time the student is available
     * @param studentEnd   the latest time the student is available
     * @param courseName   the course the student is looking for
     * @return a list of sessions that match the criteria
     */
    public List<Session> filterByStudentTimeRange(LocalDateTime studentStart, LocalDateTime studentEnd, String courseName) {
        if (items.isEmpty() ||studentStart == null || studentEnd == null || courseName == null || courseName.isEmpty()) {
            return Collections.emptyList();
        }

        return items.stream()
            .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
            .filter(session ->
                !session.getEndDateTime().isBefore(studentStart) &&
                !session.getStartDateTime().isAfter(studentEnd))
            .collect(Collectors.toList());
    }
}
