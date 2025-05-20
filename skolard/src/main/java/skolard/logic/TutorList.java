package skolard.logic;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import skolard.objects.Session;

public class TutorList extends PriorityList<Session> {

    // Default constructor
    public TutorList() {
        super();
    }

    // Override the sort method to sort by overall tutor rating by default
    // Override the sort method 
    @Override
    public void sort(Comparator<? super Session> comparator) {
        if (comparator != null) {
            // Custom comparator-based sorting
            for (int i = 0; i < items.size(); i++) {
                for (int j = 0; j < items.size() - 1; j++) {
                    if (comparator.compare(items.get(j), items.get(j + 1)) > 0) {
                        swap(j, j + 1);
                    }
                }
            }
        } else {
            // Default sorting by overall average tutor rating
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
    // Swap function
    private void swap(int i, int j) {
        Session temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

    // Get sessions for a specific course without sorting
    public List<Session> getSessionsForCourse(String courseName) {
        return items.stream()
                .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
                .collect(Collectors.toList());
    }

    // Get sessions for a specific course, sorted by tutor rating
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
