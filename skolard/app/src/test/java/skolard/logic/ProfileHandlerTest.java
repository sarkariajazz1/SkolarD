package skolard.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

public class ProfileHandlerTest {
    private ProfileHandler profileHandler;
    private StudentPersistence sp;
    private TutorPersistence tp;

    /**
     * Sets up a mock tutor and student before each test.
     * Initializes with sample courses, grades, and sessions.
     */
    @Before
    public void setUp() {
        PersistenceFactory.initialize(PersistenceType.STUB, false);
        profileHandler = new ProfileHandler(
            PersistenceFactory.getStudentPersistence(),
            PersistenceFactory.getTutorPersistence()
        );

        Tutor mockTutor = new Tutor("Alice Tutor", "alicetutor@myumanitoba.ca",
                            "Experienced in Math and Physics", null, null);
        profileHandler.addTutor(mockTutor.getName(), mockTutor.getEmail());
        mockTutor.setCourses(new ArrayList<>(List.of("Math 101", "Physics 202")));
        Map<String, String> grades = new HashMap<>();
        grades.put("Math 101", "4.0");
        grades.put("Physics 202", "3.0");
        mockTutor.setCourseGrades(grades);
        profileHandler.updateTutor(mockTutor);

        Student mockStudent = new Student("Bob Student", "bobstudent@myumanitoba.ca");
        profileHandler.addStudent(mockStudent.getName(), mockStudent.getEmail());
        Session pastSession = new Session(1, null, null, null, null, null);
        Session upcomingSession = new Session(2, null, null, null, null, null);
        mockStudent.setUpcomingSessions(List.of(upcomingSession));
        mockStudent.setPastSessions(List.of(pastSession));
        profileHandler.updateStudent(mockStudent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTutor_InvalidName() {
        profileHandler.addTutor("A", "a@domain.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTutor_InvalidEmail() {
        profileHandler.addTutor("Valid Name", "not-an-email");
    }

    @Test
    public void testGetTutor_InvalidEmailReturnsNull() {
        assertNull(profileHandler.getTutor(null));
        assertNull(profileHandler.getTutor(""));
        assertNull(profileHandler.getTutor("bad@@email"));
    }

    // ─── addStudent / getStudent ────────────────────────────────────────────────

    @Test
    public void testAddAndGetStudent() {
        profileHandler.addStudent("Student One", "stu@domain.com");
        Student s = profileHandler.getStudent("stu@domain.com");
        assertNotNull(s);
        assertEquals("Student One", s.getName());
        assertEquals("stu@domain.com", s.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddStudent_InvalidName() {
        profileHandler.addStudent("", "ok@domain.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddStudent_InvalidEmail() {
        profileHandler.addStudent("Name", "bad-email");
    }

    @Test
    public void testGetStudent_InvalidEmailReturnsNull() {
        assertNull(profileHandler.getStudent(null));
        assertNull(profileHandler.getStudent(""));
        assertNull(profileHandler.getStudent("no-at-domain"));
    }

    // ─── viewBasicProfile ───────────────────────────────────────────────────────

    @Test
    public void testViewBasicProfile() {
        Tutor t = new Tutor("Basic", "basic@dom.com", "bio",
                            new ArrayList<>(), new HashMap<>());
        String out = profileHandler.viewBasicProfile(t);
        assertTrue(out.contains("Name: Basic"));
        assertTrue(out.contains("Email: basic@dom.com"));
    }

    // ─── viewFullProfile for Tutor ──────────────────────────────────────────────

    @Test
    public void testViewFullProfile_Tutor() {
        Tutor t = new Tutor("TutorX", "tx@d.com", "My Bio",
                            new ArrayList<>(Arrays.asList("C1")), new HashMap<>());
        t.addCourseGrade("C1", 2.0);
        String out = profileHandler.viewFullProfile(t);
        assertTrue(out.contains("Bio: My Bio"));
        assertTrue(out.contains("Courses: C1"));
        assertTrue(out.contains(" - C1: 2.0"));
        assertTrue(out.contains("Avg Rating: 2.0"));
    }

    // ─── viewFullProfile for Student ────────────────────────────────────────────

    @Test
    public void testViewFullProfile_Student() {
        Student s = new Student("Stu", "stu@d.com");
        Session past    = new Session(1, null, null, null, null, null);
        Session upcoming = new Session(2, null, null, null, null, null);
        s.setPastSessions(Collections.singletonList(past));
        s.setUpcomingSessions(Collections.singletonList(upcoming));

        String out = profileHandler.viewFullProfile(s);
        assertTrue(out.contains("Past: 1"));
        assertTrue(out.contains("Upcoming: 1"));
    }

    // ─── updateBio ──────────────────────────────────────────────────────────────

    @Test
    public void testUpdateBio_Direct() {
        Tutor t = new Tutor("BioName", "bio@d.com", "OrigBio",
                            new ArrayList<>(), new HashMap<>());
        profileHandler.updateBio(t, "NewBio");
        assertEquals("NewBio", t.getBio());
    }

    // ─── addTutoringCourse ──────────────────────────────────────────────────────

    @Test
    public void testAddTutoringCourse() {
        Tutor t = new Tutor("TC", "tc@d.com", "", new ArrayList<>(), new HashMap<>());
        profileHandler.addTutoringCourse(t, "Course X", 4.5);
        assertTrue(t.getCourses().contains("course x"));
        assertEquals(Double.valueOf(4.5), t.getCourseGrades().get("course x"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddTutoringCourse_NullTutor() {
        profileHandler.addTutoringCourse(null, "C", 1.0);
    }

    // ─── removeTutoringCourse ────────────────────────────────────────────────────

    @Test
    public void testRemoveTutoringCourse() {
        Tutor t = new Tutor("RT", "rt@d.com", "", 
                            new ArrayList<>(Arrays.asList("M1")), new HashMap<>());
        t.getCourseGrades().put("M1", 3.0);

        profileHandler.removeTutoringCourse(t, "M1");
        assertFalse(t.getCourses().contains("m1"));
        assertFalse(t.getCourseGrades().containsKey("m1"));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveTutoringCourse_NullTutor() {
        profileHandler.removeTutoringCourse(null, "M1");
    }
}
