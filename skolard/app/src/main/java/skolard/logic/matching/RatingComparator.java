package skolard.logic.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import skolard.objects.Session;

/**
 * RatingList is a PriorityList that sorts tutoring sessions
 * based on the tutor's grade for a specific course.
 */
public class RatingComparator extends PriorityList<Session> {
    private final List<Session> sessions;
    private static final double MISSING_GRADE = 1.0;

    public RatingComparator(List<Session> sessions) {
        this.sessions = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    public List<Session> sortByBestCourseRating(String course) {
        if (course == null || course.isBlank()) {
            return Collections.emptyList();
        }

        List<Session> filtered = new ArrayList<>();
        for (Session session : sessions) {
            String courseName = session.getCourseName();
            Double grade = session.getTutor().getGradeForCourse(course);

            if (courseName != null && courseName.equalsIgnoreCase(course) && grade != null && !grade.equals(MISSING_GRADE)) {
                filtered.add(session);
            }
        }

        // Sort in-place by grade descending
        Collections.sort(filtered,
            Comparator.comparingDouble((Session s) -> s.getTutor().getGradeForCourse(course))
                      .reversed()
        );

        return Collections.unmodifiableList(filtered);
    }
}
