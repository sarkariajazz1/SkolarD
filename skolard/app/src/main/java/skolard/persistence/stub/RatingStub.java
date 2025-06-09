package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.objects.Feedback;
import skolard.persistence.RatingPersistence;

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
    Feedback feedback = new Feedback(sessionIdInt, "[STUB-COURSE]", tutorId, studentId, courseRating, tutorRating);


        tutorFeedbackMap.computeIfAbsent(tutorId, k -> new ArrayList<>()).add(feedback);
        courseFeedbackMap.computeIfAbsent("[STUB-COURSE]", k -> new ArrayList<>()).add(feedback);

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