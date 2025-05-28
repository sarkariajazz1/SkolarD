package skolard.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import skolard.objects.Session;
import skolard.objects.Tutor;

/**
 * Unit tests for the TutorList class.
 * Verifies sorting and filtering functionality based on tutor ratings and course names.
 */
public class TutorListTest {

    private TutorList tutorList;
    private Session session1, session2, session3;

    // Helper to create tutors with fake course grades
    private Tutor createTutor(String name, double numericRating) {
        Map<String, Double> courseGrades = new HashMap<>();
        courseGrades.put("COMP1010", numericRating);

        List<String> courses = new ArrayList<>();
        courses.add("COMP1010");

        return new Tutor(name, name + "@email.com", "Bio", courses, courseGrades);
    }

    private Session createSession(int id, String course, Tutor tutor) {
        return new Session(id, tutor, null,
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 11, 0),
                course);
    }

    @Before
    public void setUp() {
        tutorList = new TutorList();

        Tutor tutorA = createTutor("Alice", 4.8);
        Tutor tutorB = createTutor("Bob", 4.2);
        Tutor tutorC = createTutor("Charlie", 5.0);

        session1 = createSession(1, "COMP1010", tutorA);
        session2 = createSession(2, "COMP1010", tutorB);
        session3 = createSession(3, "COMP2140", tutorC);

        tutorList.addItem(session1);
        tutorList.addItem(session2);
        tutorList.addItem(session3);
    }

    @Test
    public void testDefaultSortByTutorRating() {
        tutorList.sort(null);
        List<Session> sorted = tutorList.getAllItems();

        assertEquals("Charlie", sorted.get(0).getTutor().getName());
        assertEquals("Alice", sorted.get(1).getTutor().getName());
        assertEquals("Bob", sorted.get(2).getTutor().getName());
    }

    @Test
    public void testCustomSortComparatorByName() {
        tutorList.sort((s1, s2) -> s1.getTutor().getName().compareTo(s2.getTutor().getName()));

        List<Session> sorted = tutorList.getAllItems();
        assertEquals("Alice", sorted.get(0).getTutor().getName());
        assertEquals("Bob", sorted.get(1).getTutor().getName());
        assertEquals("Charlie", sorted.get(2).getTutor().getName());
    }

    @Test
    public void testGetSessionsForCourse() {
        List<Session> result = tutorList.getSessionsForCourse("COMP1010");
        assertEquals(2, result.size());
    }

    @Test
    public void testGetSessionsByTutorSorted() {
        List<Session> result = tutorList.getSessionsByTutor("COMP1010");
        assertEquals("Alice", result.get(0).getTutor().getName());
        assertEquals("Bob", result.get(1).getTutor().getName());
    }

    @Test
    public void testEmptyTutorList() {
        TutorList emptyList = new TutorList();
        emptyList.sort(null);
        assertTrue(emptyList.getAllItems().isEmpty());
        assertTrue(emptyList.getSessionsForCourse("ANY").isEmpty());
        assertTrue(emptyList.getSessionsByTutor("ANY").isEmpty());
    }

    @Test
    public void testSortWithEqualRatings() {
        Tutor t1 = createTutor("Tom", 4.5);
        Tutor t2 = createTutor("Tim", 4.5);

        Session s1 = createSession(4, "MATH123", t1);
        Session s2 = createSession(5, "MATH123", t2);

        TutorList list = new TutorList();
        list.addItem(s1);
        list.addItem(s2);

        list.sort(null);
        assertEquals(2, list.getAllItems().size());
    }

    @Test
    public void testNullTutorHandlingGracefully() {
        TutorList list = new TutorList();
        Session s = new Session(6, null, null,
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP1010");

        list.addItem(s);
        try {
            list.sort(null); // Should not throw
        } catch (Exception e) {
            fail("Sort threw exception on null tutor: " + e.getMessage());
        }
    }
}
