package skolard.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the PriorityList class using String elements.
 * Ensures correct behavior for add, remove, clear, and retrieval operations.
 */
public class PriorityListTest {
    private PriorityList<String> priorityList;

    /**
     * Set up a fresh PriorityList instance before each test.
     */
    @Before
    public void setUp() {
        // Correctly initialize the PriorityList
        priorityList = new PriorityList<>();
    }

    /**
     * Test adding items to the list and retrieving them by index.
     * Verifies both size and element order.
     */
    @Test
    public void testAddItem() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");

        // Verify the size of the list after adding
        assertEquals(2, priorityList.size());

        // Check order of insertion
        assertEquals("Alice", priorityList.getItem(0));
        assertEquals("Bob", priorityList.getItem(1));
    }

    /**
     * Test removing an item from the list and checking the resulting contents.
     */
    @Test
    public void testRemoveItem() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");

        // Remove one item
        priorityList.removeItem("Alice");

        // Check that the size is reduced and correct item remains
        assertEquals(1, priorityList.size());
        assertEquals("Bob", priorityList.getItem(0));
    }

    /**
     * Test isEmpty() method before and after adding elements.
     */
    @Test
    public void testIsEmpty() {
        // Should be empty initially
        assertTrue(priorityList.isEmpty());

        // Add one element
        priorityList.addItem("Alice");

        // Now the list should not be empty
        assertFalse(priorityList.isEmpty());
    }

    /**
     * Test clearing the list and verifying it becomes empty.
     */
    @Test
    public void testClear() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");

        // Clear the list
        priorityList.clear();

        // Verify it's now empty
        assertTrue(priorityList.isEmpty());
    }

    /**
     * Test retrieving all items as an unmodifiable list.
     * Ensures the correct elements are returned.
     */
    @Test
    public void testGetAllItems() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");

        // Confirm the list size
        assertEquals(2, priorityList.getAllItems().size());

        // Check contents
        assertTrue(priorityList.getAllItems().contains("Alice"));
        assertTrue(priorityList.getAllItems().contains("Bob"));
    }
}
