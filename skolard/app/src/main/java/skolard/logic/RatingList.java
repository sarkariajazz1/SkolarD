package skolard.logic;

import java.util.Collections;
import java.util.List;

import skolard.objects.Session;

/**
 * A specialized PriorityList that ranks sessions based on
 * the tutor's grade in a particular course. Sessions with higher
 * numeric grades appear earlier in the list.
 */
public class RatingList extends PriorityList<Session> {

    /**
     * Default constructor that initializes the internal session list.
     */
    public RatingList() {
        super();
    }

    /**
     * Sorts the list of sessions based on the tutor's grade for the given course.
     * Only sessions for the specified course with valid numeric grades are considered.
     * Uses bubble sort for demonstration purposes.
     *
     * @param course The course for which sessions should be ranked
     * @return Sorted list of matching sessions (highest grade first)
     */
    public List<Session> sortByBestCourseRating(String course) {
        if (items.isEmpty() || course == null || course.trim().isEmpty()) {
            return Collections.emptyList(); // Return empty list if no valid input
        }

        // Remove any sessions that don’t match the course or are missing grades
        filterSessionToCourse(course);

        // Perform bubble sort based on parsed numeric grade values
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size() - 1; j++) {
                Session s1 = items.get(j);
                Session s2 = items.get(j + 1);
                String grade1 = s1.getTutor().getGradeForCourse(course);
                String grade2 = s2.getTutor().getGradeForCourse(course);

                double rating1 = parseOrFallback(grade1);
                double rating2 = parseOrFallback(grade2);

                if (rating1 < rating2) {
                    swap(j, j + 1); // Swap if the left one is worse
                }
            }
        }

        return items;
    }

    /**
     * Converts a string grade to a double. If the grade is not numeric,
     * returns a default fallback value of 1.0.
     *
     * @param grade The grade to parse
     * @return Numeric representation of the grade, or 1.0 if invalid
     */
    private double parseOrFallback(String grade) {
        try {
            return Double.parseDouble(grade);
        } catch (NumberFormatException e) {
            return 1.0;
        }
    }

    /**
     * Swaps two sessions in the list at the specified indices.
     *
     * @param i First index
     * @param j Second index
     */
    private void swap(int i, int j) {
        Session temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

    /**
     * Filters the list in place, keeping only sessions that:
     * - Match the given course name (case insensitive)
     * - Have a non-null, numeric grade
     *
     * @param course The course to filter by
     */
    private void filterSessionToCourse(String course) {
        int i = 0;
        while (i < items.size()) {
            Session session = items.get(i);
            boolean courseMismatch = session.getCourseName() == null ||
                    !session.getCourseName().equalsIgnoreCase(course);

            String grade = session.getTutor().getGradeForCourse(course);
            boolean missingGrade = grade == null || grade.equalsIgnoreCase("N/A");

            if (courseMismatch || missingGrade) {
                items.remove(i); // Remove invalid session
            } else {
                i++; // Move to next only if not removed
            }
        }
    }
}
