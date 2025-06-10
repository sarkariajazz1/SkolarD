package skolard.logic.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.SessionPersistence;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingHandlerTest {

    private SessionPersistence mockSessionDB;
    private BookingHandler bookingHandler;
    private Tutor tutor;
    private Student student;
    private List<Session> mockSessions;

    @BeforeEach
    public void setup() {
        mockSessionDB = mock(SessionPersistence.class);
        bookingHandler = new BookingHandler(mockSessionDB);

        tutor = new Tutor("Tutor Name", "tutor@skolard.ca", "hashed", "Math Tutor", new HashMap<>());
        tutor.addCourse("COMP1010", 96.0);
        tutor.addCourse("COMP2140", 84.0);

        student = new Student("Student Name", "student@skolard.ca", "password");

        mockSessions = new ArrayList<>();

        // Upcoming session (valid)
        mockSessions.add(new Session(
                1,
                tutor,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1),
                "COMP1010"
        ));

        // Past session (should be filtered)
        mockSessions.add(new Session(
                2,
                tutor,
                null,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(2).plusHours(1),
                "COMP1010"
        ));

        // Booked session (should be filtered)
        Session bookedSession = new Session(
                3,
                tutor,
                student,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1),
                "COMP1010"
        );
        mockSessions.add(bookedSession);

        when(mockSessionDB.getAllSessions()).thenReturn(mockSessions);
    }

    @Test
    public void testGetAvailableSessions_FilterByRate() {
        List<Session> result = bookingHandler.getAvailableSessions(
                BookingHandler.SessionFilter.RATE,
                "COMP1010",
                null,
                null,
                student.getEmail()
        );

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getSessionId());
    }

    @Test
    public void testGetAvailableSessions_FilterByTime() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        List<Session> result = bookingHandler.getAvailableSessions(
                BookingHandler.SessionFilter.TIME,
                "COMP1010",
                start,
                end,
                student.getEmail()
        );

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getSessionId());
    }

    @Test
    public void testGetAvailableSessions_FilterByTutor() {
        List<Session> result = bookingHandler.getAvailableSessions(
                BookingHandler.SessionFilter.TUTOR,
                "COMP1010",
                null,
                null,
                student.getEmail()
        );

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getSessionId());
    }

    @Test
    public void testGetAvailableSessions_NoFilter() {
        List<Session> result = bookingHandler.getAvailableSessions(
                "COMP1010",
                student.getEmail()
        );

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getSessionId());
    }

    @Test
    public void testGetAvailableSessions_InvalidCourseName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookingHandler.getAvailableSessions(null, student.getEmail());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            bookingHandler.getAvailableSessions("", student.getEmail());
        });
    }

    @Test
    public void testGetAvailableSessions_TutorEqualsStudentEmail_FiltersOut() {
        Session selfSession = new Session(
                4,
                new Tutor("Self", student.getEmail(), "hashed", "Self Bio", null),
                null,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1),
                "COMP1010"
        );
        mockSessions.add(selfSession);

        List<Session> result = bookingHandler.getAvailableSessions("COMP1010", student.getEmail());

        // Should still be 1, not 2, since self-booking isn't allowed
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAvailableSessions_PastSessionOnly_ReturnsEmptyList() {
        when(mockSessionDB.getAllSessions()).thenReturn(List.of(
            new Session(5, tutor, null,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1).plusHours(1),
                "COMP1010"
            )
        ));

        List<Session> result = bookingHandler.getAvailableSessions("COMP1010", student.getEmail());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAvailableSessions_BookingsForOtherCourses_Ignored() {
        Session otherCourseSession = new Session(
                6,
                tutor,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1),
                "MATH1010"
        );
        mockSessions.add(otherCourseSession);

        List<Session> result = bookingHandler.getAvailableSessions("COMP1010", student.getEmail());

        assertEquals(1, result.size()); // Should ignore MATH1010
    }

    @Test
    public void testGetAvailableSessions_FilterByTime_OutsideRange_ReturnsEmpty() {
        LocalDateTime start = LocalDateTime.now().plusDays(10);
        LocalDateTime end = LocalDateTime.now().plusDays(11);

        List<Session> result = bookingHandler.getAvailableSessions(
                BookingHandler.SessionFilter.TIME,
                "COMP1010",
                start,
                end,
                student.getEmail()
        );

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAvailableSessions_FilterByRate_NullGradeHandled() {
        Tutor tutorWithNullGrade = new Tutor("No Grade", "nograde@skolard.ca", "hashed", "Tutor", new HashMap<>());

        Session nullGradeSession = new Session(
                7,
                tutorWithNullGrade,
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1),
                "COMP1010"
        );

        mockSessions.add(nullGradeSession);

        List<Session> result = bookingHandler.getAvailableSessions(
                BookingHandler.SessionFilter.RATE,
                "COMP1010",
                null,
                null,
                student.getEmail()
        );

        // getGradeForCourse() returned 1.0, so this is still a valid session
        assertEquals(2, result.size());
    }

    
}
