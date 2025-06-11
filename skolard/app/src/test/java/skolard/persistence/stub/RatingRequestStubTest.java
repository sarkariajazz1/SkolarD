package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RatingRequestStubTest {

    private RatingRequestStub stub;
    private Student student;
    private Tutor tutor;
    private Session session;

    @BeforeEach
    void setUp() {
        stub = new RatingRequestStub();
        student = new Student("student@example.com", "John", "Doe");

        Map<String, Double> courses = new HashMap<>();
        courses.put("Math", 4.5);
        tutor = new Tutor("Jane Smith", "tutor@example.com", "hashedPassword123", "Experienced Math tutor", courses);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        session = new Session(1, tutor, student, start, end, "Math");
    }



    @Test
    void testAddRequestSuccessfully() {
        RatingRequest request = new RatingRequest(-1, session, student, LocalDateTime.now(), false, false);
        RatingRequest added = stub.addRequest(request);

        assertNotNull(added);
        assertEquals(student.getEmail(), added.getStudent().getEmail());
        assertFalse(added.isCompleted());
    }

    @Test
    void testAddDuplicateRequestThrowsException() {
        RatingRequest original = new RatingRequest(-1, session, student, LocalDateTime.now(), false, false);
        RatingRequest added = stub.addRequest(original);

        // Attempt to add another request with the same ID
        RatingRequest duplicate = new RatingRequest(added.getId(), session, student, LocalDateTime.now(), false, false);
        assertThrows(RuntimeException.class, () -> stub.addRequest(duplicate));
    }

    @Test
    void testUpdateRequestSuccessfully() {
        RatingRequest request = new RatingRequest(-1, session, student, LocalDateTime.now(), false, false);
        RatingRequest added = stub.addRequest(request);

        RatingRequest updated = new RatingRequest(added.getId(), session, student, added.getCreatedAt(), true, false);
        stub.updateRequest(updated);

        List<RatingRequest> all = stub.getAllRequests();
        assertEquals(1, all.size());
        assertTrue(all.get(0).isCompleted());
    }

    @Test
    void testUpdateNonExistentRequestThrowsException() {
        RatingRequest request = new RatingRequest(999, session, student, LocalDateTime.now(), false, false);
        assertThrows(RuntimeException.class, () -> stub.updateRequest(request));
    }

    @Test
    void testGetAllRequests() {
        stub.addRequest(new RatingRequest(-1, session, student, LocalDateTime.now(), false, false));
        stub.addRequest(new RatingRequest(-1, session, student, LocalDateTime.now(), true, false));

        List<RatingRequest> all = stub.getAllRequests();
        assertEquals(2, all.size());
    }

    @Test
    void testGetPendingRequestsForStudent() {
        stub.addRequest(new RatingRequest(-1, session, student, LocalDateTime.now(), false, false));
        stub.addRequest(new RatingRequest(-1, session, student, LocalDateTime.now(), true, false));

        List<RatingRequest> pending = stub.getPendingRequestsForStudent(student.getEmail());
        assertEquals(1, pending.size());
        assertFalse(pending.get(0).isCompleted());
    }

}
