package skolard.logic.rating;

import java.util.ArrayList;
import java.util.List;

import skolard.objects.Feedback;
import skolard.objects.RatingRequest;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;
import skolard.persistence.TutorPersistence;

/**
 * Handles rating submission and processing.
 * Responsible for passing rating data to the persistence layer.
 */
public class RatingHandler {
    private final List<RatingRequest> ratingRequests;
    private final SessionPersistence sessionDB;
    private final TutorPersistence tutorDB;

    public RatingHandler(SessionPersistence sessionDB, TutorPersistence tutorDB) {
        this.ratingRequests = new ArrayList<>();
        this.sessionDB = sessionDB;
        this.tutorDB = tutorDB;
    }

    /**
     * Creates and stores a new rating request.
     */
    public void createRatingRequest(Session session, Student student) {
        RatingRequest request = new RatingRequest(session, student);
        ratingRequests.add(request);
        // UI/notification logic can be triggered elsewhere
    }

    /**
     * Processes a submitted rating by the student.
     */
    public void processRatingSubmission(RatingRequest request, int tutorRating, int courseRating) {
        request.submit(tutorRating, courseRating);

        Session session = request.getSession();
        Tutor tutor = session.getTutor();

        // Pass ratings to the persistence layer
        // sessionDB.recordCourseRating(session.getName(), courseRating);
        // tutorDB.recordTutorRating(tutor.getId(), tutorRating);

        // Save feedback (numeric only)
        Feedback feedbackRecord = request.toFeedback();
        //sessionDB.saveFeedback(feedbackRecord);
    }

    /**
     * Marks a rating request as skipped.
     */
    public void processRatingSkip(RatingRequest request) {
        request.skip();
    }

    /**
     * Returns all in-memory rating requests.
     */
    public List<RatingRequest> getAllRequests() {
        return new ArrayList<>(ratingRequests);
    }

    /**
     * Optional: Get pending rating requests for a given student.
     */
    public List<RatingRequest> getPendingRequestsForStudent(Student student) {
        List<RatingRequest> result = new ArrayList<>();
        for (RatingRequest r : ratingRequests) {
            if (r.getStudent().equals(student) && !r.isCompleted()) {
                result.add(r);
            }
        }
        return result;
    }
}
