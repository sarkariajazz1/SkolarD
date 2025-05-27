package skolard.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A generic class for managing prioritized lists.
 * Supports sorting, adding, and removing items.
 */
public class PriorityList<T> {
    protected List<T> items;

    public PriorityList() {
        this.items = new ArrayList<>();
    }

    // Add a single item to the list if it's not already present
    public void addItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    // Remove a single item from the list if it exists
    public void removeItem(T item) {
        if (items.contains(item)) {
        items.remove(item);
        }
    }

    // Get an item at a specific index
    public T getItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }
        return items.get(index);
    }

    // Get the entire list (unmodifiable for safety)
    public List<T> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    // Check if the list is empty
    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Get the current size of the list
    public int size() {
        return items.size();
    }

    // Clear the entire list
    public void clear() {
        items.clear();
    }

    // Sort the list using a custom sorting function
    public void sort(Comparator<? super T> comparator) {
        if (comparator != null) {
            items.sort(comparator);
        }
    }
}
