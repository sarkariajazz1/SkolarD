package skolard.logic;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
    private ProfileHandler handler;
    private StudentPersistence sp;
    private TutorPersistence tp;

    @Before
    public void setUp() {
        // Reset & initialize stub persistence
        PersistenceFactory.initialize(PersistenceType.STUB, false);
        sp = PersistenceFactory.getStudentPersistence();
        tp = PersistenceFactory.getTutorPersistence();
        handler = new ProfileHandler(sp, tp);
    }

    // ─── addTutor / getTutor ─────────────────────────────────────────────────────

    @Test(expected = IllegalArgumentException.class)
    public void testAddTutor_InvalidName() {
        handler.addTutor("A", "a@domain.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTutor_InvalidEmail() {
        handler.addTutor("Valid Name", "not-an-email");
    }

    @Test
    public void testGetTutor_InvalidEmailReturnsNull() {
        assertNull(handler.getTutor(null));
        assertNull(handler.getTutor(""));
        assertNull(handler.getTutor("bad@@email"));
    }

    // ─── addStudent / getStudent ────────────────────────────────────────────────

    @Test
    public void testAddAndGetStudent() {
        handler.addStudent("Student One", "stu@domain.com");
        Student s = handler.getStudent("stu@domain.com");
        assertNotNull(s);
        assertEquals("Student One", s.getName());
        assertEquals("stu@domain.com", s.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddStudent_InvalidName() {
        handler.addStudent("", "ok@domain.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddStudent_InvalidEmail() {
        handler.addStudent("Name", "bad-email");
    }

    @Test
    public void testGetStudent_InvalidEmailReturnsNull() {
        assertNull(handler.getStudent(null));
        assertNull(handler.getStudent(""));
        assertNull(handler.getStudent("no-at-domain"));
    }

    // ─── viewBasicProfile ───────────────────────────────────────────────────────

    @Test
    public void testViewBasicProfile() {
        Tutor t = new Tutor("Basic", "basic@dom.com", "bio",
                            new ArrayList<>(), new HashMap<>());
        String out = handler.viewBasicProfile(t);
        assertTrue(out.contains("Name: Basic"));
        assertTrue(out.contains("Email: basic@dom.com"));
    }

    // ─── viewFullProfile for Tutor ──────────────────────────────────────────────

    @Test
    public void testViewFullProfile_Tutor() {
        Tutor t = new Tutor("TutorX", "tx@d.com", "My Bio",
                            new ArrayList<>(Arrays.asList("C1")), new HashMap<>());
        t.addCourseGrade("C1", 2.0);
        String out = handler.viewFullProfile(t);
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

        String out = handler.viewFullProfile(s);
        assertTrue(out.contains("Past: 1"));
        assertTrue(out.contains("Upcoming: 1"));
    }

    // ─── updateBio ──────────────────────────────────────────────────────────────

    @Test
    public void testUpdateBio_Direct() {
        // operate on a standalone Tutor instance
        Tutor t = new Tutor("BioName", "bio@d.com", "OrigBio",
                            new ArrayList<>(), new HashMap<>());
        handler.updateBio(t, "NewBio");
        assertEquals("NewBio", t.getBio());
    }

    // ─── addTutoringCourse ──────────────────────────────────────────────────────

    @Test
    public void testAddTutoringCourse() {
        Tutor t = new Tutor("TC", "tc@d.com", "", new ArrayList<>(), new HashMap<>());
        handler.addTutoringCourse(t, "Course X", 4.5);
        assertTrue(t.getCourses().contains("course x"));
        assertEquals(Double.valueOf(4.5), t.getCourseGrades().get("course x"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddTutoringCourse_NullTutor() {
        handler.addTutoringCourse(null, "C", 1.0);
    }

    // ─── removeTutoringCourse ────────────────────────────────────────────────────

    @Test
    public void testRemoveTutoringCourse() {
        Tutor t = new Tutor("RT", "rt@d.com", "", 
                            new ArrayList<>(Arrays.asList("M1")), new HashMap<>());
        t.getCourseGrades().put("M1", 3.0);

        handler.removeTutoringCourse(t, "M1");
        assertFalse(t.getCourses().contains("m1"));
        assertFalse(t.getCourseGrades().containsKey("m1"));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveTutoringCourse_NullTutor() {
        handler.removeTutoringCourse(null, "M1");
    }
}
=======
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.HashMap;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertNotNull;
// import static org.junit.Assert.assertNull;
// import static org.junit.Assert.assertTrue;
// import org.junit.Before;
// import org.junit.Test;

// import skolard.objects.Session;
// import skolard.objects.Student;
// import skolard.objects.Tutor;
// import skolard.persistence.PersistenceFactory;
// import skolard.persistence.PersistenceType;
// import skolard.persistence.StudentPersistence;
// import skolard.persistence.TutorPersistence;

// public class ProfileHandlerTest {
//     private ProfileHandler handler;
//     private StudentPersistence sp;
//     private TutorPersistence tp;

//     @Before
//     public void setUp() {
//         // Reset & initialize stub persistence
//         PersistenceFactory.initialize(PersistenceType.STUB, false);
//         sp = PersistenceFactory.getStudentPersistence();
//         tp = PersistenceFactory.getTutorPersistence();
//         handler = new ProfileHandler(sp, tp);
//     }

//     // ─── addTutor / getTutor ─────────────────────────────────────────────────────

//     @Test(expected = IllegalArgumentException.class)
//     public void testAddTutor_InvalidName() {
//         handler.addTutor("A", "a@domain.com");
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testAddTutor_InvalidEmail() {
//         handler.addTutor("Valid Name", "not-an-email");
//     }

//     @Test
//     public void testGetTutor_InvalidEmailReturnsNull() {
//         assertNull(handler.getTutor(null));
//         assertNull(handler.getTutor(""));
//         assertNull(handler.getTutor("bad@@email"));
//     }

//     // ─── addStudent / getStudent ────────────────────────────────────────────────

//     @Test
//     public void testAddAndGetStudent() {
//         handler.addStudent("Student One", "stu@domain.com");
//         Student s = handler.getStudent("stu@domain.com");
//         assertNotNull(s);
//         assertEquals("Student One", s.getName());
//         assertEquals("stu@domain.com", s.getEmail());
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testAddStudent_InvalidName() {
//         handler.addStudent("", "ok@domain.com");
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testAddStudent_InvalidEmail() {
//         handler.addStudent("Name", "bad-email");
//     }

//     @Test
//     public void testGetStudent_InvalidEmailReturnsNull() {
//         assertNull(handler.getStudent(null));
//         assertNull(handler.getStudent(""));
//         assertNull(handler.getStudent("no-at-domain"));
//     }

//     // ─── viewBasicProfile ───────────────────────────────────────────────────────

//     @Test
//     public void testViewBasicProfile() {
//         Tutor t = new Tutor("Basic", "basic@dom.com", "bio",
//                             new ArrayList<>(), new HashMap<>());
//         String out = handler.viewBasicProfile(t);
//         assertTrue(out.contains("Name: Basic"));
//         assertTrue(out.contains("Email: basic@dom.com"));
//     }

//     // ─── viewFullProfile for Tutor ──────────────────────────────────────────────

//     @Test
//     public void testViewFullProfile_Tutor() {
//         Tutor t = new Tutor("TutorX", "tx@d.com", "My Bio",
//                             new ArrayList<>(Arrays.asList("C1")), new HashMap<>());
//         t.addCourseGrade("C1", 2.0);
//         String out = handler.viewFullProfile(t);
//         assertTrue(out.contains("Bio: My Bio"));
//         assertTrue(out.contains("Courses: C1"));
//         assertTrue(out.contains(" - C1: 2.0"));
//         assertTrue(out.contains("Avg Rating: 2.0"));
//     }

//     // ─── viewFullProfile for Student ────────────────────────────────────────────

//     @Test
//     public void testViewFullProfile_Student() {
//         Student s = new Student("Stu", "stu@d.com");
//         Session past    = new Session(1, null, null, null, null, null);
//         Session upcoming = new Session(2, null, null, null, null, null);
//         s.setPastSessions(Collections.singletonList(past));
//         s.setUpcomingSessions(Collections.singletonList(upcoming));

//         String out = handler.viewFullProfile(s);
//         assertTrue(out.contains("Past: 1"));
//         assertTrue(out.contains("Upcoming: 1"));
//     }

//     // ─── updateBio ──────────────────────────────────────────────────────────────

//     @Test
//     public void testUpdateBio_Direct() {
//         // operate on a standalone Tutor instance
//         Tutor t = new Tutor("BioName", "bio@d.com", "OrigBio",
//                             new ArrayList<>(), new HashMap<>());
//         handler.updateBio(t, "NewBio");
//         assertEquals("NewBio", t.getBio());
//     }

//     // ─── addTutoringCourse ──────────────────────────────────────────────────────

//     @Test
//     public void testAddTutoringCourse() {
//         Tutor t = new Tutor("TC", "tc@d.com", "", new ArrayList<>(), new HashMap<>());
//         handler.addTutoringCourse(t, "Course X", 4.5);
//         assertTrue(t.getCourses().contains("course x"));
//         assertEquals(Double.valueOf(4.5), t.getCourseGrades().get("course x"));
//     }

//     @Test(expected = NullPointerException.class)
//     public void testAddTutoringCourse_NullTutor() {
//         handler.addTutoringCourse(null, "C", 1.0);
//     }

//     // ─── removeTutoringCourse ────────────────────────────────────────────────────

//     @Test
//     public void testRemoveTutoringCourse() {
//         Tutor t = new Tutor("RT", "rt@d.com", "", 
//                             new ArrayList<>(Arrays.asList("M1")), new HashMap<>());
//         t.getCourseGrades().put("M1", 3.0);

//         handler.removeTutoringCourse(t, "M1");
//         assertFalse(t.getCourses().contains("m1"));
//         assertFalse(t.getCourseGrades().containsKey("m1"));
//     }

//     @Test(expected = NullPointerException.class)
//     public void testRemoveTutoringCourse_NullTutor() {
//         handler.removeTutoringCourse(null, "M1");
//     }
// }
>>>>>>> dev
