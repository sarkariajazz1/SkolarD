package skolard.logic.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Tutor;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ProfileUpdaterTest {

    private ProfileUpdater updater;
    private TutorPersistence tutorDB;

    @BeforeEach
    void setup() {
        tutorDB = mock(TutorPersistence.class);
        updater = new ProfileUpdater(mock(StudentPersistence.class), tutorDB);
    }

    @Test
    void testUpdateBio() {
        Tutor t = new Tutor("T", "t@skolard.ca", "pass", "Old", new HashMap<>());
        updater.updateBio(t, "New Bio");
        assertEquals("New Bio", t.getBio());
        verify(tutorDB).updateTutor(t);
    }

    @Test
    void testAddCourse() {
        Tutor t = new Tutor("T", "t@skolard.ca", "pass", "Bio", new HashMap<>());
        updater.addCourse(t, "COMP2140", 95.0);
        assertTrue(t.getCourses().contains("comp2140"));
        verify(tutorDB).addCourseToTutor(t, "COMP2140", 95.0);
    }

    @Test
    void testRemoveCourse() {
        HashMap<String, Double> grades = new HashMap<>();
        grades.put("math1010", 90.0);
        Tutor t = new Tutor("T", "t@skolard.ca", "pass", "Bio", grades);

        updater.removeCourse(t, "math1010");
        assertFalse(t.getCourses().contains("math1010"));
        verify(tutorDB).removeCourseFromTutor(t, "math1010");
    }
}
