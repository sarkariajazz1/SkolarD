package skolardtmp.logictmp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriorityList<T> {
    protected List<T> items;

    public PriorityList() {
        this.items = new ArrayList<>();
    }

    // Add a single item to the list
    public void addItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        items.add(item);
    }

    // Remove a single item from the list
    public void removeItem(T item) {
        items.remove(item);
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

    @Override
    public String toString() {
        return items.toString();
    }
}
