package skolard.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the PriorityList class using JUnit.
 * Verifies behavior for common operations such as add, remove, and clear.
 */
public class PriorityListTest {
    private PriorityList<String> priorityList;

    /**
     * Initializes a fresh PriorityList instance before each test.
     */
    @Before
    public void setUp() {
        priorityList = new PriorityList<>();
    }

    /**
     * Tests that items are added correctly and in the expected order.
     */
    @Test
    public void testAddItem() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        assertEquals(2, priorityList.size());
        assertEquals("Alice", priorityList.getItem(0));
        assertEquals("Bob", priorityList.getItem(1));
    }

    /**
     * Tests that removing an item actually removes it from the list.
     */
    @Test
    public void testRemoveItem() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        priorityList.removeItem("Alice");
        assertEquals(1, priorityList.size());
        assertEquals("Bob", priorityList.getItem(0));
    }

    /**
     * Verifies the isEmpty method for both empty and non-empty states.
     */
    @Test
    public void testIsEmpty() {
        assertTrue(priorityList.isEmpty());
        priorityList.addItem("Alice");
        assertFalse(priorityList.isEmpty());
    }

    /**
     * Ensures that the clear method removes all items from the list.
     */
    @Test
    public void testClear() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        priorityList.clear();
        assertTrue(priorityList.isEmpty());
    }

    /**
     * Verifies that getAllItems returns the full, unmodifiable list.
     */
    @Test
    public void testGetAllItems() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        assertEquals(2, priorityList.getAllItems().size());
        assertTrue(priorityList.getAllItems().contains("Alice"));
        assertTrue(priorityList.getAllItems().contains("Bob"));
    }
}
