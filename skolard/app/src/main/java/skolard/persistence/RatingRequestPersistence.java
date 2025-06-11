package skolard.persistence;

import java.util.List;

import skolard.objects.RatingRequest;

public interface RatingRequestPersistence {
    RatingRequest addRequest(RatingRequest request);    
    void updateRequest(RatingRequest request);
    List<RatingRequest> getAllRequests();
    List<RatingRequest> getPendingRequestsForStudent(String studentEmail);
} 
