package skolard.logic.rating;

import java.time.LocalDateTime;
import java.util.List;

import skolard.objects.Feedback;
import skolard.objects.RatingRequest;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.persistence.RatingPersistence;
import skolard.persistence.RatingRequestPersistence;

/**
 * Handles the logic for creating, processing, and retrieving rating-related data.
 */
public class RatingHandler {
    // Persistence layer for handling rating requests (e.g., pending ratings after a session)
    private final RatingRequestPersistence requestPersistence;

    // Persistence layer for handling finalized feedback and ratings
    private final RatingPersistence ratingPersistence;

    /**
     * Constructs a RatingHandler with the given persistence mechanisms.
     * 
     * @param requestPersistence handles storage and retrieval of RatingRequests
     * @param ratingPersistence handles storage and retrieval of Feedback entries
     */
    public RatingHandler(RatingRequestPersistence requestPersistence, RatingPersistence ratingPersistence) {
        this.requestPersistence = requestPersistence;
        this.ratingPersistence = ratingPersistence;
    }

    /**
     * Creates a new rating request for a student to rate a session they've attended.
     * 
     * @param session the tutoring session that was attended
     * @param student the student who attended the session
     */
    public void createRatingRequest(Session session, Student student) {
        // Initializes a new rating request with default status values and current timestamp
        RatingRequest request = new RatingRequest(-1, session, student, LocalDateTime.now(), false, false);
        requestPersistence.addRequest(request); // Persist the request
    }

    /**
     * Handles the submission of a rating by a student.
     * 
     * @param request the original rating request
     * @param rating  the rating value submitted by the student
     */
    public void processRatingSubmission(RatingRequest request, int rating) {
        request.submit(rating); // Mark the request as submitted with the given rating
        requestPersistence.updateRequest(request); // Persist the updated request state

        // Convert the submitted request into a Feedback object
        Feedback feedback = request.toFeedback();

        // Save the feedback to persistent storage
        ratingPersistence.saveRating(
            feedback.getTutorEmail(),
            feedback.getSessionId(),
            feedback.getStudentEmail(),
            feedback.getCourseName(),
            feedback.getRating()
        );
    }

    /**
     * Handles when a student chooses to skip giving a rating.
     * 
     * @param request the rating request to be marked as skipped
     */
    public void processRatingSkip(RatingRequest request) {
        request.skip(); // Mark the request as skipped
        requestPersistence.updateRequest(request); // Persist the skipped state
    }

    /**
     * Retrieves all rating requests stored in the system.
     * 
     * @return list of all RatingRequest objects
     */
    public List<RatingRequest> getAllRequests() {
        return requestPersistence.getAllRequests();
    }

    /**
     * Retrieves all pending rating requests for a specific student.
     * 
     * @param student the student whose pending requests are being queried
     * @return list of pending RatingRequests for the given student
     */
    public List<RatingRequest> getPendingRequestsForStudent(Student student) {
        return requestPersistence.getPendingRequestsForStudent(student.getEmail());
    }

    /**
     * Retrieves all feedback entries for a given tutor.
     * 
     * @param tutorEmail the email of the tutor
     * @return list of Feedback submitted for the tutor
     */
    public List<Feedback> getTutorFeedback(String tutorEmail) {
        return ratingPersistence.getAllFeedbackForTutor(tutorEmail);
    }
}
