package skolard.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A generic priority-based list structure that stores and manages items of any type.
 * Supports basic operations such as add, remove, get, clear, and sorting.
 * 
 * This class is extended by other logic-specific classes such as TutorList and RatingList.
 *
 * @param <T> The type of item stored in the priority list
 */
public class PriorityList<T> {
    protected List<T> items;  // Internal list that holds items of type T

    /**
     * Default constructor that initializes the internal list.
     */
    public PriorityList() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a new item to the list.
     *
     * @param item The item to add
     * @throws IllegalArgumentException if the item is null
     */
    public void addItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        items.add(item);
    }

    /**
     * Removes a given item from the list.
     *
     * @param item The item to remove
     */
    public void removeItem(T item) {
        items.remove(item);
    }

    /**
     * Retrieves an item by its index in the list.
     *
     * @param index The index of the item to retrieve
     * @return The item at the given index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public T getItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }
        return items.get(index);
    }

    /**
     * Returns an unmodifiable view of all items in the list.
     * This ensures external code cannot change the list directly.
     *
     * @return An unmodifiable list of all items
     */
    public List<T> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Checks whether the list is empty.
     *
     * @return true if the list has no items, false otherwise
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Returns the number of items in the list.
     *
     * @return The size of the list
     */
    public int size() {
        return items.size();
    }

    /**
     * Removes all items from the list.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Sorts the list using a provided comparator.
     * If the comparator is null, no sorting is applied.
     *
     * @param comparator The sorting logic to use
     */
    public void sort(Comparator<? super T> comparator) {
        if (comparator != null) {
            items.sort(comparator);
        }
    }

    /**
     * Returns a string representation of the list.
     *
     * @return The string form of the list's contents
     */
    @Override
    public String toString() {
        return items.toString();
    }
}
