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


public class ProfileHandlerTest {
    private Student mockStudent;
    private Tutor mockTutor;

    @Before
    public void setUp() {
        mockTutor = new Tutor("0000001", "Alice Tutor", "alicetutor@myumanitoba.ca",
                            "Experienced in Math and Physics",null, null);
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

    @Test
    public void testViewBasicProfile_Tutor() {
        String result = profileHandler.viewBasicProfile(mockTutor);
        assertTrue(result.contains("Name: Alice Tutor"));
        assertTrue(result.contains("Email: alicetutor@myumanitoba.ca"));
    }

    @Test
    public void testViewFullProfile_Tutor() {
        String result = profileHandler.viewFullProfile(mockTutor);
        assertTrue(result.contains("Bio: Experienced in Math and Physics"));
        assertTrue(result.contains("Courses Taken: Math 101, Physics 202"));
        assertTrue(result.contains("Grades:"));
        assertTrue(result.contains(" - Math 101: A"));
        assertTrue(result.contains("Average Rating: 4.0"));
    }

    @Test
    public void testViewFullProfile_Student() {
        String result = profileHandler.viewFullProfile(mockStudent);
        assertTrue(result.contains("Upcoming Sessions: 1"));
        assertTrue(result.contains("Past Sessions: 1"));
    }

    @Test
    public void testUpdateBio() {
        profileHandler.updateBio(mockTutor, "Updated Bio");
        assertEquals("Updated Bio", mockTutor.getBio());
    }

    @Test
    public void testAddTutoringCourse_NewCourse() {
        profileHandler.addTutoringCourse(mockTutor, "Chemistry 303", "3.5");
        assertTrue(mockTutor.getCourses().contains("Chemistry 303"));
        assertEquals("3.5", mockTutor.getCourseGrades().get("Chemistry 303"));
    }

    @Test
    public void testRemoveTutoringCourse() {
        profileHandler.removeTutoringCourse(mockTutor, "Math 101");
        assertFalse(mockTutor.getCourses().contains("Math 101"));
        assertFalse(mockTutor.getCourseGrades().containsKey("Math 101"));
    }
}