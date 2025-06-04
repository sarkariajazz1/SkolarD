package skolard.logic.matching;

import skolard.objects.Session;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * TimeList filters tutoring sessions by a student's available time range.
 */
public class TimeComparator {
    private final List<Session> sessions;

    public TimeComparator(List<Session> sessions) {
        // Defensive shallow copy of input list
        this.sessions = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    /**
     * Filters sessions that fall within the given time range and
     * returns a sorted, unmodifiable result list.
     *
     * @param start      student's preferred start time
     * @param end        student's preferred end time
     * @param courseName optional course name (not used here)
     * @return unmodifiable list of filtered and sorted sessions
     */
    public List<Session> filterByStudentTimeRange(LocalDateTime start, LocalDateTime end, String courseName) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }

        List<Session> filtered = new ArrayList<>();
        for (Session session : sessions) {
            LocalDateTime sessionStart = session.getStartDateTime();
            LocalDateTime sessionEnd = session.getEndDateTime();

            if (sessionStart != null && sessionEnd != null &&
                !sessionStart.isBefore(start) && !sessionEnd.isAfter(end)) {
                filtered.add(session);
            }
        }

        // Sort in-place and return an unmodifiable view
        filtered.sort(Comparator.comparing(Session::getStartDateTime));
        return Collections.unmodifiableList(filtered);
    }
}