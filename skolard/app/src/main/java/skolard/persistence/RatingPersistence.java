package skolard.persistence;
import java.util.List;

import skolard.objects.Feedback;

public interface RatingPersistence {
    void saveRating(String tutorEmail, int sessionId, String studentEmail, String courseName, int rating);
    List<Feedback> getAllFeedbackForTutor(String tutorEmail);
}

