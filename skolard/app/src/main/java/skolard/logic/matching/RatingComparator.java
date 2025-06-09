package skolard.logic.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import skolard.objects.Session;

/**
 * RatingComparator sorts tutoring sessions based on the tutor's grade for a specific course.
 */
public class RatingComparator extends PriorityList<Session> {
    private final List<Session> sessions;

    public RatingComparator(List<Session> sessions) {
        // Defensive shallow copy
        this.sessions = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    /**
     * Filters and sorts sessions by the tutor's grade for a specific course, in descending order.
     *
     * @param course The course name to filter sessions by.
     * @return An unmodifiable list of sessions sorted by highest tutor grade for the course.
     */
    public List<Session> sortByBestCourseRating(String course) {
        
        List<Session> filtered = sessions.stream()
            .filter(session -> {
                String courseName = session.getCourseName();
                Double grade = session.getTutor().getGradeForCourse(course);
                return courseName != null
                    && courseName.equalsIgnoreCase(course)
                    && grade != null;
            })
            .collect(Collectors.toList());

        // Sort in-place by grade descending
        Collections.sort(filtered,
            Comparator.comparingDouble((Session s) -> s.getTutor().getGradeForCourse(course))
                      .reversed()
        );

        return Collections.unmodifiableList(filtered);
    }
}
