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

    public TutorList() {
        super();
    }

    public TutorList(List<Session> sessions) {
        super(sessions); // defensive copy
    }

    public void sortByTutorRating() {
        sortWithComparator(Comparator.comparingDouble(
            s -> ((Session)s).getTutor().getAverageRating()
        ).reversed());
    }

    public List<Session> getSessionsForCourse(String courseName) {
        return items.stream()
            .filter(session -> session.getCourseName().equalsIgnoreCase(courseName))
            .collect(Collectors.toList());
    }

    public List<Session> getSessionsByTutor(String courseName) {
        return getSessionsForCourse(courseName).stream()
            .sorted(Comparator.comparingDouble((Session s) ->
                s.getTutor().getAverageRating()
            ).reversed())
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
