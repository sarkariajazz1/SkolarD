package skolard.logic.matching;

import skolard.objects.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TimeComparator filters and sorts sessions based on student time preferences.
 */
public class TimeComparator{
    private final List<Session> sessions;

    public TimeComparator(List<Session> sessions) {
        // Defensive shallow copy
        this.sessions = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    /**
     * Filters sessions within [start, end] and sorts them by start time.
     *
     * @param start      preferred start time
     * @param end        preferred end time
     * @param courseName unused in this implementation
     * @return sorted, unmodifiable list of matching sessions
     */
    public List<Session> filterByStudentTimeRange(LocalDateTime start, LocalDateTime end, String courseName) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }

        return sessions.stream()
            .filter(session -> {
                LocalDateTime sessionStart = session.getStartDateTime();
                LocalDateTime sessionEnd = session.getEndDateTime();
                return sessionStart != null && sessionEnd != null &&
                    !sessionStart.isBefore(start) && !sessionEnd.isAfter(end);
            })
            .sorted(Comparator.comparing(Session::getStartDateTime))
            .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
}