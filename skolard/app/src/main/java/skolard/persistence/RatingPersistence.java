package skolard.persistence;

import java.util.List;

import skolard.objects.Feedback;

/**
 * Interface defining persistence operations related to ratings and feedback.
 */
public interface RatingPersistence {

    /**
     * Saves a rating given by a student for a specific tutor's session and course.
     * 
     * @param tutorEmail the email of the tutor being rated
     * @param sessionId the ID of the session during which the rating was given
     * @param studentEmail the email of the student giving the rating
     * @param courseName the name of the course associated with the rating
     * @param rating the rating value (typically a numeric score)
     */
    void saveRating(String tutorEmail, int sessionId, String studentEmail, String courseName, int rating);

    /**
     * Retrieves all feedback entries associated with a particular tutor.
     * 
     * @param tutorEmail the email of the tutor whose feedback is requested
     * @return a list of Feedback objects related to the tutor
     */
    List<Feedback> getAllFeedbackForTutor(String tutorEmail);
}
