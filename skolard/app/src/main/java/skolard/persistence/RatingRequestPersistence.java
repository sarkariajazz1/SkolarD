package skolard.persistence;

import java.util.List;

import skolard.objects.RatingRequest;

/**
 * Interface defining persistence operations for rating requests.
 */
public interface RatingRequestPersistence {

    /**
     * Adds a new rating request to the persistence layer.
     * 
     * @param request the RatingRequest object to add
     * @return the added RatingRequest, possibly with updated fields like ID
     */
    RatingRequest addRequest(RatingRequest request);

    /**
     * Updates an existing rating request.
     * 
     * @param request the RatingRequest object containing updated data
     */
    void updateRequest(RatingRequest request);

    /**
     * Retrieves all rating requests.
     * 
     * @return a list of all RatingRequest objects
     */
    List<RatingRequest> getAllRequests();

    /**
     * Retrieves all pending rating requests for a specific student.
     * 
     * @param studentEmail the email of the student
     * @return a list of pending RatingRequest objects for the student
     */
    List<RatingRequest> getPendingRequestsForStudent(String studentEmail);

    /**
     * Retrieves all pending rating requests related to a specific session.
     * 
     * @param sessionId the ID of the session
     * @return a list of pending RatingRequest objects for the session
     */
    List<RatingRequest> getPendingSessionRequest(int sessionId);
}

