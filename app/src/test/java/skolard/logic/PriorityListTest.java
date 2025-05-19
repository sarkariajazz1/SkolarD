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

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullItem() {
        priorityList.addItem(null);
    }

    @Test
    public void testRemoveItem() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        priorityList.removeItem("Alice");
        assertEquals(1, priorityList.size());
        assertEquals("Bob", priorityList.getItem(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetItemOutOfRange() {
        priorityList.addItem("Alice");
        priorityList.getItem(1); // Out of bounds
    }

    @Test
    public void testGetAllItems() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        List<String> items = priorityList.getAllItems();
        assertEquals(2, items.size());
        assertEquals("Alice", items.get(0));
        assertEquals("Bob", items.get(1));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(priorityList.isEmpty());
        priorityList.addItem("Alice");
        assertFalse(priorityList.isEmpty());
    }

    @Test
    public void testClear() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        priorityList.clear();
        assertEquals(0, priorityList.size());
        assertTrue(priorityList.isEmpty());
    }

    @Test
    public void testSortWithComparator() {
        priorityList.addItem("Charlie");
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        priorityList.sort(Comparator.naturalOrder());
        assertEquals("Alice", priorityList.getItem(0));
        assertEquals("Bob", priorityList.getItem(1));
        assertEquals("Charlie", priorityList.getItem(2));
    }

    @Test
    public void testSortWithCustomComparator() {
        priorityList.addItem("Alice");
        priorityList.addItem("Charlie");
        priorityList.addItem("Bob");
        priorityList.sort((a, b) -> b.compareTo(a)); // Reverse order
        assertEquals("Charlie", priorityList.getItem(0));
        assertEquals("Bob", priorityList.getItem(1));
        assertEquals("Alice", priorityList.getItem(2));
    }

    @Test
    public void testToString() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        assertEquals("[Alice, Bob]", priorityList.toString());
    }
}

