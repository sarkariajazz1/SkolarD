package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.objects.Feedback;
import skolard.persistence.RatingPersistence;

public class RatingStub implements RatingPersistence {

    private final Map<String, List<Feedback>> tutorFeedbackMap;

    public RatingStub() {
        tutorFeedbackMap = new HashMap<>();
    }

    @Override
    public void saveRating(String tutorEmail, int sessionId, String studentEmail, String courseName, int rating) {
    
        // Since courseName is unavailable here, store a placeholder empty string, or adapt if you can access it.
        Feedback feedback = new Feedback(sessionId, courseName, tutorEmail, studentEmail, rating);


        tutorFeedbackMap.computeIfAbsent(tutorEmail, k -> new ArrayList<>()).add(feedback);

    }

    @Override
    public List<Feedback> getAllFeedbackForTutor(String tutorEmail) {
        return tutorFeedbackMap.getOrDefault(tutorEmail, Collections.emptyList());
    }
}