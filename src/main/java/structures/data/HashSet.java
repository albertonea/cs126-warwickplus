package structures.data;

import structures.data.interfaces.Collection;
import structures.data.interfaces.List;
import structures.data.interfaces.Map;
import structures.data.interfaces.Set;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class HashSet<E> implements Set<E> {
    Map<E, Object> mapping = new HashMap<>();
    Object placeholder = new Object();

    public HashSet() {}

    public HashSet(Collection<E> collection) {
        addAll(collection);
    }

    public boolean add(E element) {
        mapping.put(element, placeholder);
        return true;
    }

    public boolean remove(E element) {
        return placeholder != mapping.remove(element);
    }

    public boolean contains(E element) {
        return mapping.get(element) != null;
    }

    public int size() {
        return mapping.size();
    }

    public void clear() {
        mapping.clear();
    }

    public boolean isEmpty() {
        return mapping.size() == 0;
    }

    public List<E> toList() {
        return mapping.keySet().toList();
    }

    public E[] toArray(Class<E> type) {
        return mapping.keySet().toArray(type);
    }

    public int[] toIntArray(ToIntFunction<? super E> mapper) {
        return mapping.keySet().toIntArray(mapper);
    }

    public <T> T[] toArray(Class<T> type, Function<? super E, ? extends T> mapper) {
        return mapping.keySet().toArray(type, mapper);
    }

    public Iterator<E> iterator() {
        return mapping.keySet().iterator();
    }

    public void addAll(Collection<E> collection) {
        for (E element: collection) {
            mapping.put(element, placeholder);
        }
    }

    public void retainAll(Collection<E> collection) {
        for (E item : mapping.keySet()) {
            if (!collection.contains(item)) {
                mapping.remove(item);
            }
        }
    }
}
