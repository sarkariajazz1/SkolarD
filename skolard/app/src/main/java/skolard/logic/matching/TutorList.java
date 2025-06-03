package skolard.logic.matching;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import skolard.objects.Session;

/**
 * TutorList is a PriorityList that supports sorting tutoring sessions
 * based on tutor quality and filtering by course.
 */
public class TutorList extends PriorityList<Session> {

    // Default constructor
    public TutorList() {
        super();
    }
    
    public TutorList(List<Session> sessions){
        items = sessions;
    }

    /**
     * Overrides the default sort method. If no comparator is given, sessions
     * are sorted by the average rating of their assigned tutor.
     */
    @Override
    public void sort(Comparator<? super Session> comparator) {
        if (comparator != null) {
            // Use custom comparator
            for (int i = 0; i < items.size(); i++) {
                for (int j = 0; j < items.size() - 1; j++) {
                    if (comparator.compare(items.get(j), items.get(j + 1)) > 0) {
                        swap(j, j + 1);
                    }
                }
            }
        } else {
            // Default to sorting by tutor rating
            for (int i = 0; i < items.size(); i++) {
                for (int j = 0; j < items.size() - 1; j++) {
                    Session s1 = items.get(j);
                    Session s2 = items.get(j + 1);
                    double rating1 = s1.getTutor().getAverageRating();
                    double rating2 = s2.getTutor().getAverageRating();

                    if (rating1 < rating2) {
                        swap(j, j + 1);
                    }
                }
            }
        }
    }

    // Helper to swap two items in the list
    private void swap(int i, int j) {
        Session temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

    /**
     * Returns sessions that match a specific course without sorting.
     * @param courseName the course name to filter by
     * @return list of sessions
     */
    public List<Session> getSessionsForCourse(String courseName) {
        return items.stream()
            .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
            .collect(Collectors.toList());
    }

    /**
     * Returns sessions for a course, sorted by the tutor's average rating.
     * @param courseName course name
     * @return sorted list of sessions
     */
    public List<Session> getSessionsByTutor(String courseName) {
        return items.stream()
            .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
            .sorted((s1, s2) -> Double.compare(s2.getTutor().getAverageRating(), s1.getTutor().getAverageRating()))
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
