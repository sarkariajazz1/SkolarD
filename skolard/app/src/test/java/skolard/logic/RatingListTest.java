package skolard.logic;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import skolard.objects.Session;
import skolard.objects.Tutor;

public class RatingListTest {

    private RatingList ratingList;

    @Before
    public void setUp() {
        ratingList = new RatingList();
    }

    private Tutor createTutor(String name, String course, String grade) {
        Tutor tutor = new Tutor("", name, "", "");
        tutor.addCourseGrade(course, grade);
        return tutor;
    }

    private Session createSession(String id, Tutor tutor, String course) {
        return new Session(id, tutor, null, null, null, course);
    }

    @Test
    public void testSortByBestCourseRating_ValidAndInvalidGrades() {
        Tutor tutor1 = createTutor("Alice", "Math", "3.0");   // valid
        Tutor tutor2 = createTutor("Bob", "Math", "A");       // invalid
        Tutor tutor3 = createTutor("Charlie", "Math", "5.0"); // valid

        Session session1 = createSession("s1", tutor1, "Math");
        Session session2 = createSession("s2", tutor2, "Math");
        Session session3 = createSession("s3", tutor3, "Math");

        ratingList.addItem(session1);
        ratingList.addItem(session2);
        ratingList.addItem(session3);

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        assertEquals(3, sorted.size());
        assertEquals("Charlie", sorted.get(0).getTutor().getName()); // 5.0
        assertEquals("Alice", sorted.get(1).getTutor().getName());   // 3.0
        assertEquals("Bob", sorted.get(2).getTutor().getName());     // fallback 1.0
    }

    @Test
    public void testSortByBestCourseRating_MissingGradeFilteredOut() {
        Tutor tutor1 = createTutor("Alice", "Math", "3.0");
        Tutor tutor2 = createTutor("Bob", "Math", "N/A");

        Session session1 = createSession("s1", tutor1, "Math");
        Session session2 = createSession("s2", tutor2, "Math");

        ratingList.addItem(session1);
        ratingList.addItem(session2);

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        assertEquals(1, sorted.size());
        assertEquals("Alice", sorted.get(0).getTutor().getName());
    }

    @Test
    public void testSortByBestCourseRating_AllInvalidGrades() {
        Tutor tutor1 = createTutor("Alice", "Math", "B");
        Tutor tutor2 = createTutor("Bob", "Math", "A");

        Session session1 = createSession("s1", tutor1, "Math");
        Session session2 = createSession("s2", tutor2, "Math");

        ratingList.addItem(session1);
        ratingList.addItem(session2);

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");

        assertEquals(2, sorted.size());
        // Order should stay the same since both grades fallback to 1.0
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
        Session session = createSession("s1", tutor, "COMP2140");
        ratingList.addItem(session);

        List<Session> sorted = ratingList.sortByBestCourseRating("Math");
        assertNotNull(sorted);
        assertTrue(sorted.isEmpty());
    }

    @Test
    public void testSortByBestCourseRating_NullCourseName() {
        Tutor tutor = createTutor("Alice", "Math", "4.0");
        Session session = createSession("s1", tutor, "Math");
        ratingList.addItem(session);

        List<Session> sorted = ratingList.sortByBestCourseRating(null);
        assertNotNull(sorted);
        assertTrue(sorted.isEmpty());
    }

    @Test
    public void testSortByBestCourseRating_EmptyCourseName() {
        Tutor tutor = createTutor("Alice", "Math", "4.0");
        Session session = createSession("s1", tutor, "Math");
        ratingList.addItem(session);

        List<Session> sorted = ratingList.sortByBestCourseRating("");
        assertNotNull(sorted);
        assertTrue(sorted.isEmpty());
    }
}
