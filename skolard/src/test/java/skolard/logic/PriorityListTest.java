package skolard.logic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import skolard.logic.PriorityList;// for JUnit 4

public class PriorityListTest {
    private PriorityList<String> priorityList;

    @BeforeEach
    public void setUp() {
        // Correctly initialize the PriorityList
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

    @Test
    public void testRemoveItem() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        priorityList.removeItem("Alice");
        assertEquals(1, priorityList.size());
        assertEquals("Bob", priorityList.getItem(0));
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
        assertTrue(priorityList.isEmpty());
    }

    @Test
    public void testGetAllItems() {
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        assertEquals(2, priorityList.getAllItems().size());
        assertTrue(priorityList.getAllItems().contains("Alice"));
        assertTrue(priorityList.getAllItems().contains("Bob"));
    }
}
