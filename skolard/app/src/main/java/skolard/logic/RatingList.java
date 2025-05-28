package skolard.logic;

import java.util.Collections;
import java.util.List;
import skolard.objects.Session;

/**
 * RatingList is a specialized PriorityList that sorts tutoring sessions
 * based on the tutor's grade for a specific course.
 */
public class RatingList extends PriorityList<Session> {

    // Default constructor
    public RatingList(List<Session> sessions) {
        items = sessions;
    }

    /**
     * Filters and sorts sessions by best tutor course grade.
     * Grades must be numeric (e.g., "3.0"). Non-numeric grades fallback to 1.0.
     *
     * @param course the name of the course to sort sessions for
     * @return a sorted list of sessions, or an empty list if none match
     */
    public List<Session> sortByBestCourseRating(String course) {
        if (items.isEmpty() || course == null || course.trim().isEmpty()) {
            return Collections.emptyList();
        }

        filterSessionToCourse(course);  // remove irrelevant or unqualified sessions

        // Bubble sort based on numeric grade values
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size() - 1; j++) {
                Session s1 = items.get(j);
                Session s2 = items.get(j + 1);
                Double grade1 = s1.getTutor().getGradeForCourse(course);
                Double grade2 = s2.getTutor().getGradeForCourse(course);

                if (grade1 < grade2) {
                    swap(j, j + 1);
                }
            }
        }

        return items;
    }


    // Swaps two sessions in the list
    private void swap(int i, int j) {
        Session temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

    // Filters out sessions that don't match the course or have invalid grades
    private void filterSessionToCourse(String course) {
        int i = 0;
        while (i < items.size()) {
            Session session = items.get(i);
            boolean courseMismatch = session.getCourseName() == null ||
                                     !session.getCourseName().equalsIgnoreCase(course);

            Double grade = session.getTutor().getGradeForCourse(course);
            boolean missingGrade = grade == null || grade == 1.0;

            if (courseMismatch || missingGrade) {
                items.remove(i);
            } else {
                i++;
            }
        }
    }
}
