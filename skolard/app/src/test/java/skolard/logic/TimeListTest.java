package skolard.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import skolard.objects.Session;

/**
 * Unit tests for the TimeList class.
 * Ensures time-based session filtering works correctly.
 */
public class TimeListTest {

    private TimeList timeList;

    @Before
    public void setUp() {
        timeList = new TimeList(new ArrayList<>());  // Pass empty session list to constructor
    }

    @Test
    public void testFilterSessionsWithinTimeRange() {
        // Create 3 sessions
        Session s1 = new Session(1, null, null,
                LocalDateTime.of(2025, 5, 20, 9, 0),
                LocalDateTime.of(2025, 5, 20, 10, 0),
                "Math");

        Session s2 = new Session(2, null, null,
                LocalDateTime.of(2025, 5, 20, 11, 0),
                LocalDateTime.of(2025, 5, 20, 12, 0),
                "Math");

        Session s3 = new Session(3, null, null,
                LocalDateTime.of(2025, 5, 20, 13, 0),
                LocalDateTime.of(2025, 5, 20, 14, 0),
                "Math");

        timeList.addItem(s1);
        timeList.addItem(s2);
        timeList.addItem(s3);

        // Range overlaps with s2 and s3
        LocalDateTime studentStart = LocalDateTime.of(2025, 5, 20, 10, 30);
        LocalDateTime studentEnd = LocalDateTime.of(2025, 5, 20, 13, 30);

        List<Session> result = timeList.filterByStudentTimeRange(studentStart, studentEnd, "Math");

        assertEquals(2, result.size());
        assertTrue(result.contains(s2));
        assertTrue(result.contains(s3));
    }

    @Test
    public void testNoSessionsInRange() {
        Session outOfRange = new Session(4, null, null,
                LocalDateTime.of(2025, 5, 20, 6, 0),
                LocalDateTime.of(2025, 5, 20, 7, 0),
                "Math");

        timeList.addItem(outOfRange);

        LocalDateTime studentStart = LocalDateTime.of(2025, 5, 20, 9, 0);
        LocalDateTime studentEnd = LocalDateTime.of(2025, 5, 20, 10, 0);

        List<Session> result = timeList.filterByStudentTimeRange(studentStart, studentEnd, "Math");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptySessionList() {
        List<Session> result = timeList.filterByStudentTimeRange(
                LocalDateTime.of(2025, 5, 20, 9, 0),
                LocalDateTime.of(2025, 5, 20, 10, 0),
                "Math");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
