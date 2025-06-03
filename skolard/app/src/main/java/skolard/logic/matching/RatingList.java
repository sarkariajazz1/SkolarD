package skolard.logic.matching;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import skolard.objects.Session;

/**
 * RatingList is a PriorityList that sorts tutoring sessions
 * based on the tutor's grade for a specific course.
 */
public class RatingList extends PriorityList<Session> {

    private static final double MISSING_GRADE = 1.0;

    public RatingList(List<Session> sessions) {
        super(sessions); // defensive copy
    }

    public List<Session> sortByBestCourseRating(String course) {
        if (items.isEmpty() || course == null || course.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Remove sessions with wrong course or missing grade
        items.removeIf(session -> {
            String courseName = session.getCourseName();
            Double grade = session.getTutor().getGradeForCourse(course);
            return courseName == null || !courseName.equalsIgnoreCase(course) || grade == null || grade.equals(MISSING_GRADE);
        });

        // Sort sessions by grade (higher is better)
        sortWithComparator(Comparator.comparingDouble(
            (Session s) -> s.getTutor().getGradeForCourse(course)
        ).reversed());

        return getAllItems();
    }
}
