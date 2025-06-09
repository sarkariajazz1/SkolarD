package skolard.logic.rating;

import java.util.ArrayList;
import java.util.List;

import skolard.objects.Feedback;
import skolard.objects.RatingRequest;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.RatingPersistence;


public class RatingHandler {
    private final List<RatingRequest> ratingRequests;
    private final RatingPersistence ratingPersistence;

    public RatingHandler(RatingPersistence ratingPersistence) {
        this.ratingRequests = new ArrayList<>();
        this.ratingPersistence = ratingPersistence;
    }

    public void createRatingRequest(Session session, Student student) {
        RatingRequest request = new RatingRequest(session, student);
        ratingRequests.add(request);
    }

    
    public void processRatingSubmission(RatingRequest request, int tutorRating, int courseRating) {
        request.submit(tutorRating, courseRating);
        Feedback feedback = request.toFeedback();

        ratingPersistence.saveRating(
            feedback.getTutorName(),
            String.valueOf(feedback.getSessionId()),
            feedback.getTutorRating(),
            feedback.getCourseRating(),
            feedback.getStudentName()
        );
    }
    
    public void processRatingSkip(RatingRequest request) {
        request.skip();
    }

    public List<RatingRequest> getAllRequests() {
        return new ArrayList<>(ratingRequests);
    }

    public List<RatingRequest> getPendingRequestsForStudent(Student student) {
        List<RatingRequest> result = new ArrayList<>();
        for (RatingRequest r : ratingRequests) {
            if (r.getStudent().equals(student) && !r.isCompleted()) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Feedback> getTutorFeedback(String tutorId) {
        return ratingPersistence.getAllFeedbackForTutor(tutorId);
    }

    public List<Feedback> getCourseFeedback(String courseName) {
        return ratingPersistence.getAllFeedbackForCourse(courseName);
    }
}
