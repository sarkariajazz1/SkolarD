package skolard.logic.rating;

import java.time.LocalDateTime;
import java.util.List;

import skolard.objects.Feedback;
import skolard.objects.RatingRequest;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.RatingPersistence;
import skolard.persistence.RatingRequestPersistence;


public class RatingHandler {
    private final RatingRequestPersistence requestPersistence;
    private final RatingPersistence ratingPersistence;

    public RatingHandler(RatingRequestPersistence requestPersistence, RatingPersistence ratingPersistence) {
        this.requestPersistence = requestPersistence;
        this.ratingPersistence = ratingPersistence;
    }

    public void createRatingRequest(Session session, Student student) {
        RatingRequest request = new RatingRequest(-1, session, student, LocalDateTime.now(), false, false);
        requestPersistence.addRequest(request);
    }

    
    public void processRatingSubmission(RatingRequest request, int rating) {
        request.submit(rating);
        requestPersistence.updateRequest(request);
        Feedback feedback = request.toFeedback();

        ratingPersistence.saveRating(
            feedback.getTutorEmail(),
            feedback.getSessionId(),
            feedback.getStudentEmail(),
            feedback.getCourseName(),
            feedback.getRating()
        );
    }
    
    public void processRatingSkip(RatingRequest request) {
        request.skip();
        requestPersistence.updateRequest(request);
    }

    public List<RatingRequest> getAllRequests() {
        return requestPersistence.getAllRequests();
    }

    public List<RatingRequest> getPendingRequestsForStudent(Student student) {
        return requestPersistence.getPendingRequestsForStudent(student.getEmail());
    }

    public List<Feedback> getTutorFeedback(String tutorEmail) {
        return ratingPersistence.getAllFeedbackForTutor(tutorEmail);
    }
}
