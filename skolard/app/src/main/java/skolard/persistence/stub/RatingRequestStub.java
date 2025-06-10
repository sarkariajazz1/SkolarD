package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import skolard.objects.RatingRequest;
import skolard.objects.Student;
import skolard.persistence.RatingRequestPersistence;

public class RatingRequestStub implements RatingRequestPersistence{
    private final Map<Integer, RatingRequest> ratingRequests;
    private static int uniqueID = 0;

    public RatingRequestStub() {
        this.ratingRequests = new HashMap<>(); 
    }

    public RatingRequest addRequest(RatingRequest request) {
        if(ratingRequests.containsKey(request.getId())) {
            throw new RuntimeException("Updated Existing Request, that was an update, not an add");
        }
        RatingRequest newRequest = new RatingRequest(uniqueID++, request.getSession(), request.getStudent(),
            request.getCreatedAt(), request.isCompleted(), request.isSkipped());
        ratingRequests.put(newRequest.getId(), newRequest);
        return newRequest;
    }

    @Override
    public void updateRequest(RatingRequest request) {
        if(!ratingRequests.containsKey(request.getId())) {
            throw new RuntimeException("Cannot update a Message that does not exist.");
        } 
        ratingRequests.replace(request.getId(), request);
    }

    @Override
    public List<RatingRequest> getAllRequests() {
        return new ArrayList<>(ratingRequests.values());
    }

    @Override
    public List<RatingRequest> getPendingRequestsForStudent(String studentEmail) {
        List<RatingRequest> result = new ArrayList<>();
        Student student;
        for (RatingRequest r : ratingRequests.values()) {
            student = r.getStudent();
            if (student != null && student.getEmail().equals(studentEmail) && !r.isCompleted()) {
                result.add(r);
            }
        }
        return result;
    }
}
