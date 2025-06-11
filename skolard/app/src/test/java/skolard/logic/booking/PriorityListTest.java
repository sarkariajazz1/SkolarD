package skolard.logic.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PriorityListTest {

    private PriorityList<String> priorityList;

    @BeforeEach
    void setup() {
        priorityList = new PriorityList<>();
    }

    @Test
    void testDefaultConstructorStartsEmpty() {
        assertTrue(priorityList.isEmpty());
        assertEquals(0, priorityList.size());
    }

    @Test
    void testConstructorWithValidList() {
        List<String> data = Arrays.asList("a", "b", "c");
        PriorityList<String> list = new PriorityList<>(data);

        assertEquals(3, list.size());
        assertFalse(list.isEmpty());
        assertEquals("b", list.getItem(1));
    }

    @Test
    void testConstructorWithNullListThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new PriorityList<>(null));
    }

    @Test
    void testAddItemSuccessfully() {
        priorityList.addItem("Hello");
        assertEquals(1, priorityList.size());
        assertEquals("Hello", priorityList.getItem(0));
    }

    @Test
    void testAddDuplicateItemDoesNotAddTwice() {
        priorityList.addItem("Hello");
        priorityList.addItem("Hello");

        assertEquals(1, priorityList.size());
    }

    @Test
    void testAddItem_NullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> priorityList.addItem(null));
    }

    @Test
    void testRemoveItemSuccessfully() {
        priorityList.addItem("RemoveMe");
        priorityList.removeItem("RemoveMe");

        assertTrue(priorityList.isEmpty());
    }

    @Test
    void testRemoveItem_NullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> priorityList.removeItem(null));
    }

    @Test
    void testGetItem_OutOfBoundsThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> priorityList.getItem(0));
    }

    @Test
    void testGetAllItemsIsUnmodifiable() {
        priorityList.addItem("Test");
        List<String> items = priorityList.getAllItems();

        assertEquals(1, items.size());
        assertThrows(UnsupportedOperationException.class, () -> items.add("Another"));
    }

    @Test
    void testSizeAndIsEmptyAccurate() {
        assertTrue(priorityList.isEmpty());

        priorityList.addItem("X");
        assertFalse(priorityList.isEmpty());
        assertEquals(1, priorityList.size());
    }
}
