package skolard.persistence;
import java.util.List;

import skolard.objects.Feedback;

public interface RatingPersistence {
    void saveRating(String tutorId, int sessionId, int tutorRating, int courseRating, String studentId);
    List<Feedback> getAllFeedbackForTutor(String tutorEmail);
    List<Feedback> getAllFeedbackForCourse(String courseName);
}

