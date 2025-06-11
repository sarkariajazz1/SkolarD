package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Feedback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RatingStubTest {

    private RatingStub ratingStub;

    @BeforeEach
    void setUp() {
        ratingStub = new RatingStub();
    }

    @Test
    void testSaveRatingAndRetrieveFeedback() {
        String tutorEmail = "tutor@example.com";
        String studentEmail = "student@example.com";
        int sessionId = 101;
        String courseName = "Math";
        int rating = 5;

        ratingStub.saveRating(tutorEmail, sessionId, studentEmail, courseName, rating);

        List<Feedback> feedbackList = ratingStub.getAllFeedbackForTutor(tutorEmail);
        assertEquals(1, feedbackList.size());

        Feedback feedback = feedbackList.get(0);
        assertEquals(sessionId, feedback.getSessionId());
        assertEquals(courseName, feedback.getCourseName());
        assertEquals(tutorEmail, feedback.getTutorEmail());
        assertEquals(studentEmail, feedback.getStudentEmail());
        assertEquals(rating, feedback.getRating());
    }

    @Test
    void testGetAllFeedbackForTutorWhenNoneExists() {
        String tutorEmail = "nonexistent@example.com";
        List<Feedback> feedbackList = ratingStub.getAllFeedbackForTutor(tutorEmail);
        assertNotNull(feedbackList);
        assertTrue(feedbackList.isEmpty());
    }

    @Test
    void testMultipleFeedbacksForSameTutor() {
        String tutorEmail = "tutor@example.com";

        ratingStub.saveRating(tutorEmail, 101, "student1@example.com", "Math", 4);
        ratingStub.saveRating(tutorEmail, 102, "student2@example.com", "Science", 5);

        List<Feedback> feedbackList = ratingStub.getAllFeedbackForTutor(tutorEmail);
        assertEquals(2, feedbackList.size());
    }
}
