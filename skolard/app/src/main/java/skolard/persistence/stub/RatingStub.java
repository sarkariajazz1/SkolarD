package skolard.persistence.stub;

import skolard.objects.Feedback;
import skolard.persistence.RatingPersistence;

import java.util.*;

public class RatingStub implements RatingPersistence {

    private final Map<String, List<Feedback>> tutorFeedbackMap = new HashMap<>();
    private final Map<String, List<Feedback>> courseFeedbackMap = new HashMap<>();

    @Override
    public void saveRating(String tutorId, String sessionId, int tutorRating, int courseRating, String studentId) {
        int sessionIdInt;
        try {
            sessionIdInt = Integer.parseInt(sessionId);
        } catch (NumberFormatException e) {
            sessionIdInt = -1; // Fallback if sessionId string is not a number
        }
        // Since courseName is unavailable here, store a placeholder empty string, or adapt if you can access it.
        Feedback feedback = new Feedback(sessionIdInt, "", tutorId, studentId, courseRating);

        // Save by tutor
        tutorFeedbackMap.computeIfAbsent(tutorId, k -> new ArrayList<>()).add(feedback);

        // Save by course (the "" can be replaced once courseName is available in the interface)
        courseFeedbackMap.computeIfAbsent("", k -> new ArrayList<>()).add(feedback);
    }

    @Override
    public List<Feedback> getAllFeedbackForTutor(String tutorId) {
        return tutorFeedbackMap.getOrDefault(tutorId, Collections.emptyList());
    }

    @Override
    public List<Feedback> getAllFeedbackForCourse(String courseName) {
        return courseFeedbackMap.getOrDefault(courseName, Collections.emptyList());
    }
}