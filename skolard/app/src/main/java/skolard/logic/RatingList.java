package skolard.logic;

import java.util.Collections;
import java.util.List;

import skolard.objects.Session;

public class RatingList extends PriorityList<Session> {

    public RatingList() {
        super();
    }

    public List<Session> sortByBestCourseRating(String course) {
        if (items.isEmpty() || course == null || course.trim().isEmpty()) {
            return Collections.emptyList(); // Safely return empty list
        }

        // Filter in-place
        filterSessionToCourse(course);

        // Sort using bubble sort based on tutor grade for the course
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size() - 1; j++) {
                Session s1 = items.get(j);
                Session s2 = items.get(j + 1);
                String grade1 = s1.getTutor().getGradeForCourse(course);
                String grade2 = s2.getTutor().getGradeForCourse(course);

                double rating1 = parseOrFallback(grade1);
                double rating2 = parseOrFallback(grade2);

                if (rating1 < rating2) {
                    swap(j, j + 1);
                }
            }
        }

        return items;
    }

    // Converts grade to double, or defaults to 1.0 if non-numeric
    private double parseOrFallback(String grade) {
        try {
            return Double.parseDouble(grade);
        } catch (NumberFormatException e) {
            return 1.0;
        }
    }

    // Bubble-swap helper
    private void swap(int i, int j) {
        Session temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

    // Remove sessions not matching course name or with no grade
    private void filterSessionToCourse(String course) {
        int i = 0;
        while (i < items.size()) {
            Session session = items.get(i);
            boolean courseMismatch = session.getCourseName() == null ||
                    !session.getCourseName().equalsIgnoreCase(course);

            String grade = session.getTutor().getGradeForCourse(course);
            boolean missingGrade = grade == null || grade.equalsIgnoreCase("N/A");

            if (courseMismatch || missingGrade) {
                items.remove(i);
            } else {
                i++;
            }
        }
    }
}
