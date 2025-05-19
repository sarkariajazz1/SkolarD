package skolard.logic;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Comparator;

import static org.junit.Assert.*;

public class PriorityListTest {
    private PriorityList<String> priorityList;

    @Before
    public void setUp() {
        priorityList = new PriorityList<>();
    }

    @Test
    public void testAddItem() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        assertEquals(2, priorityList.size());
        assertEquals("Alice", priorityList.getItem(0));
        assertEquals("Bob", priorityList.getItem(1));
    }

}

