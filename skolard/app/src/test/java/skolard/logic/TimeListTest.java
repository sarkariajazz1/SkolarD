package skolard.logic;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import skolard.objects.Session;

public class TimeListTest {
    private TimeList timeList;

    @Before
    public void setUp() {
        timeList = new TimeList();
    }
    
    @Test
    public void testFilterSessionsWithinTimeRange() {
        Session s1 = new Session(null,null,null,
                LocalDateTime.of(2025, 5, 20, 9, 0),
                LocalDateTime.of(2025, 5, 20, 10, 0), "Math");

        Session s2 = new Session(null,null,null,
                LocalDateTime.of(2025, 5, 20, 11, 0),
                LocalDateTime.of(2025, 5, 20, 12, 0), "Math");

        Session s3 = new Session(null,null,null,
                LocalDateTime.of(2025, 5, 20, 13, 0),
                LocalDateTime.of(2025, 5, 20, 14, 0),"Math");

        timeList.addItem(s1);
        timeList.addItem(s2);
        timeList.addItem(s3);

        LocalDateTime studentStart = LocalDateTime.of(2025, 5, 20, 10, 30);
        LocalDateTime studentEnd = LocalDateTime.of(2025, 5, 20, 13, 30);

        List<Session> result = timeList.filterByStudentTimeRange(studentStart, studentEnd, "Math");

        assertEquals(2, result.size());
    }

    @Test
    public void testNoSessionsInRange() {
        Session outOfRange = new Session(null,null,null,
                LocalDateTime.of(2025, 5, 20, 6, 0),
                LocalDateTime.of(2025, 5, 20, 7, 0), "Math");

        timeList.addItem(outOfRange);

        LocalDateTime studentStart = LocalDateTime.of(2025, 5, 20, 9, 0);
        LocalDateTime studentEnd = LocalDateTime.of(2025, 5, 20, 10, 0);

        List<Session> result = timeList.filterByStudentTimeRange(studentStart, studentEnd, "Math");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptySessionList() {
        List<Session> result = timeList.filterByStudentTimeRange(
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Math");
        assertTrue(result.isEmpty());
    }
}
