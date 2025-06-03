package skolard.logic.matching;

import skolard.objects.Session;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TimeList filters tutoring sessions by a student's available time range.
 */
public class TimeList extends PriorityList<Session> {

    public TimeList(List<Session> sessions) {
        super(sessions); // defensive copy
    }

    public List<Session> filterByStudentTimeRange(LocalDateTime studentStart, LocalDateTime studentEnd, String courseName) {
        if (items.isEmpty() || studentStart == null || studentEnd == null || courseName == null || courseName.isEmpty()) {
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
