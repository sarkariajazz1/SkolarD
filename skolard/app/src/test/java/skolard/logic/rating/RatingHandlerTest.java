package skolard.logic.rating;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import skolard.objects.*;
import skolard.persistence.RatingPersistence;
import skolard.persistence.RatingRequestPersistence;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class RatingHandlerTest {

    private RatingRequestPersistence mockRequestPersistence;
    private RatingPersistence mockRatingPersistence;
    private RatingHandler ratingHandler;

    private Student student;
    private Session session;

    @BeforeEach
    public void setUp() {
        mockRequestPersistence = mock(RatingRequestPersistence.class);
        mockRatingPersistence = mock(RatingPersistence.class);
        ratingHandler = new RatingHandler(mockRequestPersistence, mockRatingPersistence);

        // Real student object
        student = new Student("stud@example.com", "Student Name", "password");

        // Mock session and nested tutor
        session = mock(Session.class);
        when(session.getSessionId()).thenReturn(42);
        when(session.getCourseName()).thenReturn("COMP 3010");

        Tutor tutor = mock(Tutor.class);
        when(tutor.getEmail()).thenReturn("tutor@example.com");
        when(session.getTutor()).thenReturn(tutor);
    }

    @Test
    public void testCreateRatingRequest() {
        ratingHandler.createRatingRequest(session, student);

        ArgumentCaptor<RatingRequest> captor = ArgumentCaptor.forClass(RatingRequest.class);
        verify(mockRequestPersistence).addRequest(captor.capture());

        RatingRequest request = captor.getValue();
        assertNotNull(request);
        assertEquals(student, request.getStudent());
        assertEquals(session, request.getSession());
        assertFalse(request.isCompleted());
        assertFalse(request.isSkipped());
    }

    @Test
    public void testProcessRatingSubmission() {
        Student mockStudent = mock(Student.class);
        when(mockStudent.getEmail()).thenReturn("stud@example.com");

        RatingRequest request = new RatingRequest(1, session, mockStudent, LocalDateTime.now(), false, false);

        ratingHandler.processRatingSubmission(request, 5);

        verify(mockRequestPersistence).updateRequest(request);
        verify(mockRatingPersistence).saveRating(
            "tutor@example.com",
            42,
            "stud@example.com",
            "COMP 3010",
            5
        );
    }


    @Test
    public void testProcessRatingSkip() {
        RatingRequest request = new RatingRequest(2, session, student, LocalDateTime.now(), false, false);

        ratingHandler.processRatingSkip(request);

        assertTrue(request.isCompleted());
        assertTrue(request.isSkipped());

        verify(mockRequestPersistence).updateRequest(request);
        verify(mockRatingPersistence, never()).saveRating(any(), anyInt(), any(), any(), anyInt());
    }

    @Test
    public void testGetAllRequests() {
        RatingRequest mockRequest = new RatingRequest(1, session, student, LocalDateTime.now(), false, false);
        when(mockRequestPersistence.getAllRequests()).thenReturn(List.of(mockRequest));

        List<RatingRequest> result = ratingHandler.getAllRequests();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    public void testGetPendingRequestsForStudent() {
        Student mockStudent = mock(Student.class);
        when(mockStudent.getEmail()).thenReturn("stud@example.com");

        RatingRequest req = new RatingRequest(3, session, mockStudent, LocalDateTime.now(), false, false);
        when(mockRequestPersistence.getPendingRequestsForStudent("stud@example.com"))
            .thenReturn(List.of(req));

        List<RatingRequest> result = ratingHandler.getPendingRequestsForStudent(mockStudent);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getId());
    }

    @Test
    public void testGetTutorFeedback() {
        Feedback feedback = new Feedback(42, "COMP 3010", "tutor@example.com", "stud@example.com", 4);
        when(mockRatingPersistence.getAllFeedbackForTutor("tutor@example.com")).thenReturn(List.of(feedback));

        List<Feedback> result = ratingHandler.getTutorFeedback("tutor@example.com");
        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getRating());
    }
}
