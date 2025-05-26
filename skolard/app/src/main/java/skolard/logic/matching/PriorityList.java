package skolard.logic.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriorityList<T> {
    protected List<T> items;

    public PriorityList() {
        this.items = new ArrayList<>();
    }

    public PriorityList(List<T> initialItems) {
        if (initialItems == null) {
            throw new IllegalArgumentException("Initial list cannot be null.");
        }
        this.items = new ArrayList<>(initialItems);
    }

    public void addItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    public void removeItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        items.remove(item);
    }

    public T getItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }
        return items.get(index);
    }

    public List<T> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

}
