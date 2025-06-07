package skolard.persistence.stub;

import skolard.objects.Feedback;
import skolard.persistence.RatingPersistence;

import java.util.*;

public class RatingStub implements RatingPersistence {

    private final Map<String, List<Feedback>> tutorFeedbackMap = new HashMap<>();
    private final Map<String, List<Feedback>> courseFeedbackMap = new HashMap<>();

    @Override
    public void saveRating(String tutorId, String sessionId, int tutorRating, int courseRating, String studentId) {
        Feedback feedback = new Feedback(tutorId, sessionId, tutorRating, courseRating, studentId);

        // Save by tutor
        tutorFeedbackMap.computeIfAbsent(tutorId, k -> new ArrayList<>()).add(feedback);

        // Save by course
        courseFeedbackMap.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(feedback); // or use courseName if available
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