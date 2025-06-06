package skolard.persistence;
import java.util.List;

import skolard.objects.Feedback;

public interface RatingPersistence {
    void saveRating(String tutorId, String sessionId, int tutorRating, int courseRating, String studentId);
    List<Feedback> getAllFeedbackForTutor(String tutorId);
    List<Feedback> getAllFeedbackForCourse(String courseName);
}

