// package skolard.logic;

<<<<<<< HEAD
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class PriorityListTest {
    private PriorityList<String> priorityList;

    @Before
    public void setUp() {
        priorityList = new PriorityList<>();
    }

    @Test
    public void testAddItemAndSize() {
        // initially empty
        assertEquals(0, priorityList.size());
        // add two distinct items
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        assertEquals(2, priorityList.size());
        // duplicate add should have no effect
        priorityList.addItem("Alice");
        assertEquals(2, priorityList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullItem() {
        priorityList.addItem(null);
    }

    @Test
    public void testGetItemValidIndex() {
        priorityList.addItem("One");
        priorityList.addItem("Two");
        assertEquals("One", priorityList.getItem(0));
        assertEquals("Two", priorityList.getItem(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetItemNegativeIndex() {
        priorityList.getItem(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetItemTooLargeIndex() {
        priorityList.addItem("X");
        priorityList.getItem(1);
    }

    @Test
    public void testRemoveItem() {
        priorityList.addItem("A");
        priorityList.addItem("B");
        // remove existing
        priorityList.removeItem("A");
        assertEquals(1, priorityList.size());
        assertFalse(priorityList.getAllItems().contains("A"));
        // removing non-existent does nothing
        priorityList.removeItem("Z");
        assertEquals(1, priorityList.size());
        assertTrue(priorityList.getAllItems().contains("B"));
    }

    @Test
    public void testIsEmptyAndClear() {
        assertTrue(priorityList.isEmpty());
        priorityList.addItem("Item");
        assertFalse(priorityList.isEmpty());
        priorityList.clear();
        assertTrue(priorityList.isEmpty());
        assertEquals(0, priorityList.size());
    }

    @Test
    public void testGetAllItemsUnmodifiable() {
        priorityList.addItem("X");
        List<String> all = priorityList.getAllItems();
        assertEquals(1, all.size());
        try {
            all.add("Y");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSortWithComparator() {
        priorityList.addItem("Charlie");
        priorityList.addItem("Alice");
        priorityList.addItem("Bob");
        // sort alphabetically
        priorityList.sort(Comparator.naturalOrder());
        List<String> sorted = priorityList.getAllItems();
        assertEquals(Arrays.asList("Alice", "Bob", "Charlie"), sorted);
    }

    @Test
    public void testSortWithNullComparatorDoesNothing() {
        priorityList.addItem("B");
        priorityList.addItem("A");
        priorityList.sort(null);  // should leave order unchanged
        assertEquals(Arrays.asList("B", "A"), priorityList.getAllItems());
    }
}
=======
// import java.util.Arrays;
// import java.util.Comparator;
// import java.util.List;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.fail;
// import org.junit.Before;
// import org.junit.Test;

// public class PriorityListTest {
//     private PriorityList<String> priorityList;

//     @Before
//     public void setUp() {
//         priorityList = new PriorityList<>();
//     }

//     @Test
//     public void testAddItemAndSize() {
//         // initially empty
//         assertEquals(0, priorityList.size());
//         // add two distinct items
//         priorityList.addItem("Alice");
//         priorityList.addItem("Bob");
//         assertEquals(2, priorityList.size());
//         // duplicate add should have no effect
//         priorityList.addItem("Alice");
//         assertEquals(2, priorityList.size());
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testAddNullItem() {
//         priorityList.addItem(null);
//     }

//     @Test
//     public void testGetItemValidIndex() {
//         priorityList.addItem("One");
//         priorityList.addItem("Two");
//         assertEquals("One", priorityList.getItem(0));
//         assertEquals("Two", priorityList.getItem(1));
//     }

//     @Test(expected = IndexOutOfBoundsException.class)
//     public void testGetItemNegativeIndex() {
//         priorityList.getItem(-1);
//     }

//     @Test(expected = IndexOutOfBoundsException.class)
//     public void testGetItemTooLargeIndex() {
//         priorityList.addItem("X");
//         priorityList.getItem(1);
//     }

//     @Test
//     public void testRemoveItem() {
//         priorityList.addItem("A");
//         priorityList.addItem("B");
//         // remove existing
//         priorityList.removeItem("A");
//         assertEquals(1, priorityList.size());
//         assertFalse(priorityList.getAllItems().contains("A"));
//         // removing non-existent does nothing
//         priorityList.removeItem("Z");
//         assertEquals(1, priorityList.size());
//         assertTrue(priorityList.getAllItems().contains("B"));
//     }

//     @Test
//     public void testIsEmptyAndClear() {
//         assertTrue(priorityList.isEmpty());
//         priorityList.addItem("Item");
//         assertFalse(priorityList.isEmpty());
//         priorityList.clear();
//         assertTrue(priorityList.isEmpty());
//         assertEquals(0, priorityList.size());
//     }

//     @Test
//     public void testGetAllItemsUnmodifiable() {
//         priorityList.addItem("X");
//         List<String> all = priorityList.getAllItems();
//         assertEquals(1, all.size());
//         try {
//             all.add("Y");
//             fail("Expected UnsupportedOperationException");
//         } catch (UnsupportedOperationException e) {
//             // expected
//         }
//     }

//     @Test
//     public void testSortWithComparator() {
//         priorityList.addItem("Charlie");
//         priorityList.addItem("Alice");
//         priorityList.addItem("Bob");
//         // sort alphabetically
//         priorityList.sort(Comparator.naturalOrder());
//         List<String> sorted = priorityList.getAllItems();
//         assertEquals(Arrays.asList("Alice", "Bob", "Charlie"), sorted);
//     }

//     @Test
//     public void testSortWithNullComparatorDoesNothing() {
//         priorityList.addItem("B");
//         priorityList.addItem("A");
//         priorityList.sort(null);  // should leave order unchanged
//         assertEquals(Arrays.asList("B", "A"), priorityList.getAllItems());
//     }
// }
>>>>>>> dev
