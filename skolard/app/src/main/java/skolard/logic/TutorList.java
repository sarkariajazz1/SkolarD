package skolard.logic;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import skolard.objects.Session;

/**
 * A specialized version of PriorityList that deals with Session objects,
 * providing sorting and filtering based on tutor performance.
 */
public class TutorList extends PriorityList<Session> {

    /**
     * Default constructor. Initializes the internal list from the parent PriorityList.
     */
    public TutorList() {
        super();
    }

    /**
     * Sorts the sessions either by a provided comparator or by default using
     * the tutor's average rating (highest to lowest).
     *
     * @param comparator Optional comparator to sort sessions; uses default if null.
     */
    @Override
    public void sort(Comparator<? super Session> comparator) {
        if (comparator != null) {
            // Sort using the given comparator via a bubble sort approach
            for (int i = 0; i < items.size(); i++) {
                for (int j = 0; j < items.size() - 1; j++) {
                    if (comparator.compare(items.get(j), items.get(j + 1)) > 0) {
                        swap(j, j + 1);
                    }
                }
            }
        } else {
            // Default sorting by tutor average rating in descending order
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

    /**
     * Swaps two items in the session list at the given indices.
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
     * Retrieves all sessions associated with a given course name.
     * Does not apply any sorting.
     *
     * @param courseName Course to filter by
     * @return List of matching sessions
     */
    public List<Session> getSessionsForCourse(String courseName) {
        return items.stream()
                .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all sessions for a given course, sorted by the tutor's average rating.
     *
     * @param courseName Course to filter by
     * @return List of sorted sessions
     */
    public List<Session> getSessionsByTutor(String courseName) {
        return items.stream()
                .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
                .sorted((s1, s2) -> Double.compare(s2.getTutor().getAverageRating(), s1.getTutor().getAverageRating()))
                .collect(Collectors.toList());
    }

    /**
     * Returns a string representation of the internal session list.
     */
    @Override
    public String toString() {
        return items.toString();
    }
}
