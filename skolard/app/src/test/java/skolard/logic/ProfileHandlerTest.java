package skolard.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;

/**
 * Unit tests for the ProfileHandler class.
 * Verifies profile viewing and editing logic for Student and Tutor objects.
 */
public class ProfileHandlerTest {
    private Student mockStudent;
    private Tutor mockTutor;

    /**
     * Sets up a mock tutor and student before each test.
     * Initializes with sample courses, grades, and sessions.
     */
    @Before
    public void setUp() {
        mockTutor = new Tutor("0000001", "Alice Tutor", "alicetutor@myumanitoba.ca",
                            "Experienced in Math and Physics", null, null);
        mockTutor.setCourses(new ArrayList<>(List.of("Math 101", "Physics 202")));
        Map<String, String> grades = new HashMap<>();
        grades.put("Math 101", "4.0");
        grades.put("Physics 202", "3.0");
        mockTutor.setCourseGrades(grades);

        mockStudent = new Student("0000002", "Bob Student", "bobstudent@myumanitoba.ca");
        Session pastSession = new Session("1", null, null, null, null, null);
        Session upcomingSession = new Session("2", null, null, null, null, null);
        mockStudent.setUpcomingSessions(List.of(upcomingSession));
        mockStudent.setPastSessions(List.of(pastSession));
    }

    /**
     * Tests that basic profile info (name and email) is displayed correctly.
     */
    @Test
    public void testViewBasicProfile_Tutor() {
        String result = ProfileHandler.viewBasicProfile(mockTutor);
        assertTrue(result.contains("Name: Alice Tutor"));
        assertTrue(result.contains("Email: alicetutor@myumanitoba.ca"));
    }

    /**
     * Tests full profile details for a tutor including bio, courses, and grades.
     */
    @Test
    public void testViewFullProfile_Tutor() {
        String result = ProfileHandler.viewFullProfile(mockTutor);
        assertTrue(result.contains("Bio: Experienced in Math and Physics"));
        assertTrue(result.contains("Courses Taken: Math 101, Physics 202"));
        assertTrue(result.contains("Grades:"));
        assertTrue(result.contains(" - Math 101: 4.0"));
        assertTrue(result.contains(" - Physics 202: 3.0"));
        // assertTrue(result.contains("Average Rating: 3.5")); // Uncomment if rating implemented
    }

    /**
     * Tests full profile view for a student showing session counts.
     */
    @Test
    public void testViewFullProfile_Student() {
        String result = ProfileHandler.viewFullProfile(mockStudent);
        assertTrue(result.contains("Upcoming Sessions: 1"));
        assertTrue(result.contains("Past Sessions: 1"));
    }

    /**
     * Tests updating a tutor's bio using ProfileHandler.
     */
    @Test
    public void testUpdateBio() {
        ProfileHandler.updateBio(mockTutor, "Updated Bio");
        assertEquals("Updated Bio", mockTutor.getBio());
    }

    /**
     * Tests adding a new course and grade to a tutor profile.
     */
    @Test
    public void testAddTutoringCourse_NewCourse() {
        ProfileHandler.addTutoringCourse(mockTutor, "Chemistry 303", "3.5");
        assertTrue(mockTutor.getCourses().contains("Chemistry 303"));
        assertEquals("3.5", mockTutor.getCourseGrades().get("Chemistry 303"));
    }

    /**
     * Tests removal of a course and its grade from a tutor profile.
     */
    @Test
    public void testRemoveTutoringCourse() {
        ProfileHandler.removeTutoringCourse(mockTutor, "Math 101");
        assertFalse(mockTutor.getCourses().contains("Math 101"));
        assertFalse(mockTutor.getCourseGrades().containsKey("Math 101"));
    }
}
