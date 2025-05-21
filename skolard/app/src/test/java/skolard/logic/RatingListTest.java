package skolard.logic;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import skolard.objects.Session;
import skolard.objects.Tutor;

/**
 * Unit tests for the RatingList class.
 * Tests sorting logic based on tutor course grades.
 */
public class RatingListTest {

    private RatingList ratingList;

    @Before
    public void setUp() {
        ratingList = new RatingList(); // Initialize the list before each test
    }

    // Helper to create a Tutor with a single course and grade
    private Tutor createTutor(String name, String course, String grade) {
        Tutor tutor = new Tutor("", name, "", "");
        tutor.addCourseGrade(course, grade);
        return tutor;
    }

    // Helper to create a Session object
    private Session createSession(String id, Tutor tutor, String course) {
        return new Session(id, tutor, null, null, null, course);
    }

    @Test
    public void testSortByBestCourseRating_ValidAndInvalidGrades() {
        // Setup: 2 valid grades and 1 invalid
        Tutor tutor1 = createTutor("Alice", "Math", "3.0");
        Tutor tutor2 = createTutor("Bob", "Math", "A");    // non-numeric grade
        Tutor tutor3 = createTutor("Charlie", "Math", "5.0");

        // Create sessions and add them
        ratingList.addItem(createSession("s1", tutor1, "Math"));
        ratingList.addItem(createSession("s2", tutor2, "Math"));
        ratingList.addItem(createSession("s3", tutor3, "Math"));

        // Act: Sort by course rating
        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        // Assert: Expect Charlie > Alice > Bob
        assertEquals(3, sorted.size());
        assertEquals("Charlie", sorted.get(0).getTutor().getName());
        assertEquals("Alice", sorted.get(1).getTutor().getName());
        assertEquals("Bob", sorted.get(2).getTutor().getName()); // fallback rating
    }

    @Test
    public void testSortByBestCourseRating_MissingGradeFilteredOut() {
        Tutor tutor1 = createTutor("Alice", "Math", "3.0");
        Tutor tutor2 = createTutor("Bob", "Math", "N/A"); // Should be ignored

        ratingList.addItem(createSession("s1", tutor1, "Math"));
        ratingList.addItem(createSession("s2", tutor2, "Math"));

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        assertEquals(1, sorted.size());
        assertEquals("Alice", sorted.get(0).getTutor().getName());
    }

    @Test
    public void testSortByBestCourseRating_AllInvalidGrades() {
        // All tutors have non-numeric grades
        ratingList.addItem(createSession("s1", createTutor("Alice", "Math", "B"), "Math"));
        ratingList.addItem(createSession("s2", createTutor("Bob", "Math", "A"), "Math"));

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        assertEquals(2, sorted.size());
        // Order unchanged since all grades fallback to 1.0
        assertEquals("Alice", sorted.get(0).getTutor().getName());
        assertEquals("Bob", sorted.get(1).getTutor().getName());
    }

    @Test
    public void testSortByBestCourseRating_EmptyList() {
        List<Session> sorted = ratingList.sortByBestCourseRating("Math");
        assertNotNull(sorted);
        assertTrue(sorted.isEmpty());
    }

    @Test
    public void testSortByBestCourseRating_NoCourseMatch() {
        Tutor tutor = createTutor("Alice", "COMP2140", "4.0");
        ratingList.addItem(createSession("s1", tutor, "COMP2140"));

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");
        assertTrue(sorted.isEmpty());
    }

    @Test
    public void testSortByBestCourseRating_NullCourseName() {
        Tutor tutor = createTutor("Alice", "Math", "4.0");
        ratingList.addItem(createSession("s1", tutor, "Math"));

        List<Session> sorted = ratingList.sortByBestCourseRating(null);
        assertTrue(sorted.isEmpty());
    }

    @Test
    public void testSortByBestCourseRating_EmptyCourseName() {
        Tutor tutor = createTutor("Alice", "Math", "4.0");
        ratingList.addItem(createSession("s1", tutor, "Math"));

        List<Session> sorted = ratingList.sortByBestCourseRating("");
        assertTrue(sorted.isEmpty());
    }
}
