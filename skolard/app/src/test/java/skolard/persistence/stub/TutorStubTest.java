package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Tutor;
import skolard.utils.PasswordUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TutorStubTest {

    private TutorStub tutorStub;

    @BeforeEach
    void setUp() {
        tutorStub = new TutorStub();
    }

    @Test
    void testAddTutor_Success() {
        Tutor newTutor = new Tutor("Alice Smith", "alice@example.com", PasswordUtil.hash("abc123"), "Math expert", new HashMap<>());
        Tutor added = tutorStub.addTutor(newTutor);
        assertNotNull(added);
        assertEquals("alice@example.com", added.getEmail());
    }

    @Test
    void testAddTutor_DuplicateEmail() {
        Tutor duplicate = new Tutor("Yab Matt", "mattyab@myumanitoba.ca", PasswordUtil.hash("pass123"), "Duplicate", new HashMap<>());
        assertThrows(RuntimeException.class, () -> tutorStub.addTutor(duplicate));
    }

    @Test
    void testGetTutorByEmail_Exists() {
        Tutor tutor = tutorStub.getTutorByEmail("mattyab@myumanitoba.ca");
        assertNotNull(tutor);
        assertEquals("Yab Matt", tutor.getName());
    }

    @Test
    void testGetTutorByEmail_NotExists() {
        Tutor tutor = tutorStub.getTutorByEmail("nonexistent@example.com");
        assertNull(tutor);
    }

    @Test
    void testDeleteTutorByEmail() {
        tutorStub.deleteTutorByEmail("mattyab@myumanitoba.ca");
        assertNull(tutorStub.getTutorByEmail("mattyab@myumanitoba.ca"));
    }

    @Test
    void testUpdateTutor() {
        Tutor updated = new Tutor("Yab Matt", "mattyab@myumanitoba.ca", PasswordUtil.hash("newpass"), "Updated bio", new HashMap<>());
        tutorStub.updateTutor(updated);
        Tutor result = tutorStub.getTutorByEmail("mattyab@myumanitoba.ca");
        assertEquals("Updated bio", result.getBio());
    }

    @Test
    void testGetAllTutors() {
        List<Tutor> tutors = tutorStub.getAllTutors();
        assertFalse(tutors.isEmpty());
    }

    @Test
    void testAddCourseToTutor() {
        Tutor tutor = tutorStub.getTutorByEmail("mattyab@myumanitoba.ca");
        tutorStub.addCourseToTutor(tutor, "COMP 2140", 4.5);
        Tutor updated = tutorStub.getTutorByEmail("mattyab@myumanitoba.ca");
        assertTrue(updated.getCoursesWithGrades().containsKey("COMP 2140"));
    }

    @Test
    void testRemoveCourseFromTutor() {
        Tutor tutor = tutorStub.getTutorByEmail("mattyab@myumanitoba.ca");
        tutorStub.removeCourseFromTutor(tutor, "COMP 1010");
        Tutor updated = tutorStub.getTutorByEmail("mattyab@myumanitoba.ca");
        assertFalse(updated.getCoursesWithGrades().containsKey("COMP 1010"));
    }

    @Test
    void testUpdateTutor_NonExistent() {
        Tutor nonExistent = new Tutor("Ghost", "ghost@nowhere.com", PasswordUtil.hash("ghostpass"), "Invisible", new HashMap<>());
        assertDoesNotThrow(() -> tutorStub.updateTutor(nonExistent));
        assertNull(tutorStub.getTutorByEmail("ghost@nowhere.com"));
    }

    @Test
    void testAddCourseToTutor_NullCourseOrGrade() {
        Tutor tutor = tutorStub.getTutorByEmail("mattyab@myumanitoba.ca");

        // Null course
        tutorStub.addCourseToTutor(tutor, null, 4.5);
        assertFalse(tutor.getCoursesWithGrades().containsValue(4.5));

        // Null grade
        tutorStub.addCourseToTutor(tutor, "COMP 3010", null);
        assertFalse(tutor.getCoursesWithGrades().containsKey("COMP 3010"));
    }

    @Test
    void testRemoveCourseFromTutor_NonExistentCourse() {
        Tutor tutor = tutorStub.getTutorByEmail("mattyab@myumanitoba.ca");
        Map<String, Double> originalCourses = new HashMap<>(tutor.getCoursesWithGrades());

        tutorStub.removeCourseFromTutor(tutor, "NON_EXISTENT_COURSE");

        Tutor updated = tutorStub.getTutorByEmail("mattyab@myumanitoba.ca");
        assertEquals(originalCourses, updated.getCoursesWithGrades());
    }

    @Test
    void testAuthenticate_NullEmailOrPassword() {
        assertNull(tutorStub.authenticate(null, PasswordUtil.hash("somepass")));
        assertNull(tutorStub.authenticate("mattyab@myumanitoba.ca", null));
    }

    @Test
    void testAuthenticate_Success() {
        String hashed = PasswordUtil.hash("pass123");
        Tutor tutor = tutorStub.authenticate("mattyab@myumanitoba.ca", hashed);
        assertNotNull(tutor);
    }

    @Test
    void testAuthenticate_Failure() {
        Tutor tutor = tutorStub.authenticate("mattyab@myumanitoba.ca", PasswordUtil.hash("wrongpass"));
        assertNull(tutor);
    }
}

