import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import skolard.logic.PriorityList;


public class PriorityListTest {
    private PriorityList<String> priorityList;

    @Before
    public void setUp() {
        // priorityList = new PriorityList<>();
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

