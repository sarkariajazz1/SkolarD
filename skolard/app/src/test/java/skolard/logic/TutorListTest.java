package skolard.logic;

import org.junit.Before;
import org.junit.Test;
import skolard.objects.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

public class TutorListTest {

    private TutorList tutorList;
    private Session session1, session2, session3;

    private Tutor createTutor(String name, double numericRating) {
        Map<String, String> grades = new HashMap<>();
        grades.put("COMP1010", String.valueOf((int)(numericRating * 10))); // e.g., 4.8 -> "48"
        return new Tutor(UUID.randomUUID().toString(), name, name + "@email.com", "Bio", new ArrayList<>(Arrays.asList("COMP1010")), grades);
    }

    private Session createSession(String id, String course, Tutor tutor) {
        return new Session(
                id,
                tutor,
                null,
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 11, 0),
                course
        );
    }

    @Before
    public void setUp() {
        tutorList = new TutorList();

        Tutor tutorA = createTutor("Alice", 4.8);
        Tutor tutorB = createTutor("Bob", 4.2);
        Tutor tutorC = createTutor("Charlie", 5.0);

        session1 = createSession("S1", "COMP1010", tutorA);
        session2 = createSession("S2", "COMP1010", tutorB);
        session3 = createSession("S3", "COMP2140", tutorC);

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
        tutorList.sort(new Comparator<Session>() {
            @Override
            public int compare(Session s1, Session s2) {
                return s1.getTutor().getName().compareTo(s2.getTutor().getName());
            }
        });

        List<Session> sorted = tutorList.getAllItems();
        assertEquals("Alice", sorted.get(0).getTutor().getName());
        assertEquals("Bob", sorted.get(1).getTutor().getName());
        assertEquals("Charlie", sorted.get(2).getTutor().getName());
    }

    @Test
    public void testGetSessionsForCourse() {
        List<Session> result = tutorList.getSessionsForCourse("COMP1010");
        assertEquals(2, result.size());
        for (Session s : result) {
            assertEquals("COMP1010", s.getCourseName());
        }
    }

    @Test
    public void testGetSessionsByTutorSorted() {
        List<Session> result = tutorList.getSessionsByTutor("COMP1010");
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getTutor().getName()); // 4.8 > 4.2
        assertEquals("Bob", result.get(1).getTutor().getName());
    }

    @Test
    public void testEmptyTutorList() {
        TutorList emptyList = new TutorList();
        emptyList.sort(null); // should not throw
        assertTrue(emptyList.getAllItems().isEmpty());
        assertTrue(emptyList.getSessionsForCourse("ANY").isEmpty());
        assertTrue(emptyList.getSessionsByTutor("ANY").isEmpty());
    }

    @Test
    public void testToStringContainsSessions() {
        String str = tutorList.toString();
        assertTrue(str.contains("COMP1010"));
        assertTrue(str.contains("COMP2140"));
    }

    @Test
    public void testSortWithEqualRatings() {
        Tutor t1 = createTutor("Tom", 4.5);
        Tutor t2 = createTutor("Tim", 4.5);

        Session s1 = createSession("S4", "MATH123", t1);
        Session s2 = createSession("S5", "MATH123", t2);

        TutorList list = new TutorList();
        list.addItem(s1);
        list.addItem(s2);

        list.sort(null); // Should not crash

        List<Session> result = list.getAllItems();
        assertEquals(2, result.size());
    }

    @Test
    public void testNullTutorHandlingGracefully() {
        TutorList list = new TutorList();
        Session s = new Session("S6", null, null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "COMP1010");
        list.addItem(s);
        try {
            list.sort(null);
        } catch (Exception e) {
            fail("Sort threw exception on null tutor: " + e.getMessage());
        }
    }
}
