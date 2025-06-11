package skolard.logic.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A generic utility class that maintains a list of unique items in a prioritized order.
 * Ensures no duplicate or null elements are added, and provides controlled access to the list.
 *
 * @param <T> the type of elements maintained in this list
 */
public class PriorityList<T> {

    // Internal list that stores the items
    protected List<T> items;

    /**
     * Default constructor initializes an empty list.
     */
    public PriorityList() {
        this.items = new ArrayList<>();
    }

    /**
     * Constructor that initializes the list with a given set of items.
     * A defensive copy is made to avoid external modification of internal state.
     *
     * @param initialItems list of initial items
     * @throws IllegalArgumentException if the input list is null
     */
    public PriorityList(List<T> initialItems) {
        if (initialItems == null) {
            throw new IllegalArgumentException("Initial list cannot be null.");
        }
        this.items = new ArrayList<>(initialItems);
    }

    /**
     * Adds a new item to the list if it doesn't already exist.
     *
     * @param item the item to be added
     * @throws IllegalArgumentException if the item is null
     */
    public void addItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    /**
     * Removes an item from the list, if it exists.
     *
     * @param item the item to be removed
     * @throws IllegalArgumentException if the item is null
     */
    public void removeItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        items.remove(item);
    }

    /**
     * Retrieves the item at the specified index.
     *
     * @param index the index of the item
     * @return the item at the given index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public T getItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }
        return items.get(index);
    }

    /**
     * Returns a read-only view of all items in the list.
     * Prevents external modification.
     *
     * @return unmodifiable list of items
     */
    public List<T> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Checks if the list contains any items.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Returns the total number of items currently in the list.
     *
     * @return the list size
     */
    public int size() {
        return items.size();
    }

}
