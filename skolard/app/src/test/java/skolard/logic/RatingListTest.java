package skolard.logic;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import skolard.objects.Session;
import skolard.objects.Tutor;

/**
 * Unit tests for RatingList, specifically for sorting sessions
 * by the tutor's numeric course grade.
 */
public class RatingListTest {

    private RatingList ratingList;

    @Before
    public void setUp() {
        ratingList = new RatingList();
    }

    // Helper method to create a Tutor with a specific grade in a course
    private Tutor createTutor(String name, String course, String grade) {
        Tutor tutor = new Tutor("", name, "", "");
        tutor.addCourseGrade(course, grade);
        return tutor;
    }

    // Helper to create a session
    private Session createSession(String id, Tutor tutor, String course) {
        return new Session(id, tutor, null, null, null, course);
    }

    /**
     * Tests sorting when some grades are valid numbers and some are invalid (like "A").
     */
    @Test
    public void testSortByBestCourseRating_ValidAndInvalidGrades() {
        Tutor tutor1 = createTutor("Alice", "Math", "3.0");   // valid
        Tutor tutor2 = createTutor("Bob", "Math", "A");       // invalid, fallback
        Tutor tutor3 = createTutor("Charlie", "Math", "5.0"); // valid

        ratingList.addItem(createSession("s1", tutor1, "Math"));
        ratingList.addItem(createSession("s2", tutor2, "Math"));
        ratingList.addItem(createSession("s3", tutor3, "Math"));

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        assertEquals(3, sorted.size());
        assertEquals("Charlie", sorted.get(0).getTutor().getName());
        assertEquals("Alice", sorted.get(1).getTutor().getName());
        assertEquals("Bob", sorted.get(2).getTutor().getName());
    }

    /**
     * Sessions with missing or "N/A" grades should be excluded.
     */
    @Test
    public void testSortByBestCourseRating_MissingGradeFilteredOut() {
        Tutor tutor1 = createTutor("Alice", "Math", "3.0");
        Tutor tutor2 = createTutor("Bob", "Math", "N/A");

        ratingList.addItem(createSession("s1", tutor1, "Math"));
        ratingList.addItem(createSession("s2", tutor2, "Math"));

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        assertEquals(1, sorted.size());
        assertEquals("Alice", sorted.get(0).getTutor().getName());
    }

    /**
     * Tests behavior when all grades are invalid (e.g., letters).
     */
    @Test
    public void testSortByBestCourseRating_AllInvalidGrades() {
        Tutor tutor1 = createTutor("Alice", "Math", "B");
        Tutor tutor2 = createTutor("Bob", "Math", "A");

        ratingList.addItem(createSession("s1", tutor1, "Math"));
        ratingList.addItem(createSession("s2", tutor2, "Math"));

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        assertEquals(2, sorted.size());
        assertEquals("Alice", sorted.get(0).getTutor().getName());
        assertEquals("Bob", sorted.get(1).getTutor().getName());
    }

    /**
     * Sorting should return an empty list if there are no sessions.
     */
    @Test
    public void testSortByBestCourseRating_EmptyList() {
        List<Session> sorted = ratingList.sortByBestCourseRating("Math");
        assertNotNull(sorted);
        assertTrue(sorted.isEmpty());
    }

    /**
     * Should return no matches if the requested course isn't found.
     */
    @Test
    public void testSortByBestCourseRating_NoCourseMatch() {
        Tutor tutor = createTutor("Alice", "COMP2140", "4.0");
        ratingList.addItem(createSession("s1", tutor, "COMP2140"));

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");
        assertNotNull(sorted);
        assertTrue(sorted.isEmpty());
    }

    /**
     * Should return empty list when null is passed as course name.
     */
    @Test
    public void testSortByBestCourseRating_NullCourseName() {
        Tutor tutor = createTutor("Alice", "Math", "4.0");
        ratingList.addItem(createSession("s1", tutor, "Math"));

        List<Session> sorted = ratingList.sortByBestCourseRating(null);
        assertNotNull(sorted);
        assertTrue(sorted.isEmpty());
    }

    /**
     * Should return empty list when empty string is passed as course name.
     */
    @Test
    public void testSortByBestCourseRating_EmptyCourseName() {
        Tutor tutor = createTutor("Alice", "Math", "4.0");
        ratingList.addItem(createSession("s1", tutor, "Math"));

        List<Session> sorted = ratingList.sortByBestCourseRating("");
        assertNotNull(sorted);
        assertTrue(sorted.isEmpty());
    }
}
