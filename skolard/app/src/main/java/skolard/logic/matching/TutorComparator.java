package skolard.logic.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import skolard.objects.Session;

/**
 * TutorComparator sorts sessions based on tutor's overall average rating
 */
public class TutorComparator extends PriorityList<Session> {
    private final List<Session> sessions;
    public TutorComparator(List<Session> sessions) {
        this.sessions = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    /**
     * Filters sessions by course and sorts by average tutor rating (descending).
     *
     * @param courseName course to filter on
     * @return unmodifiable sorted list of sessions
     */
    public List<Session> getSessionsByTutor(String courseName) {

        List<Session> filtered = sessions.stream()
            .filter(session -> courseName.equalsIgnoreCase(session.getCourseName()))
            .collect(Collectors.toList());

        // Sort by tutor's average rating in descending order
        Collections.sort(filtered,
            Comparator.comparingDouble((Session s) -> s.getTutor().getAverageRating())
                      .reversed()
        );

        return Collections.unmodifiableList(filtered);
    }
}
