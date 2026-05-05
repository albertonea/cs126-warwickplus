package structures.data;

import structures.data.interfaces.Collection;
import structures.data.interfaces.List;
import structures.data.interfaces.Set;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class LinkedListSet<E> implements Set<E> {
    List<E> linkedList = new LinkedList<>();

    public boolean add(E element) {
        if (linkedList.contains(element)) {
            return true;
        }

        return linkedList.add(element);
    }

    public void clear() {
        linkedList.clear();
    }

    public boolean contains(E element) {
        return linkedList.contains(element);
    }

    public boolean isEmpty() {
        return linkedList.isEmpty();
    }

    public boolean remove(E element) {
        return linkedList.remove(element);
    }

    public int size() {
        return linkedList.size();
    }

    public E[] toArray(Class<E> type) {
        return linkedList.toArray(type);
    }

    @Override
    public int[] toIntArray(ToIntFunction<? super E> mapper) {
        return linkedList.toIntArray(mapper);
    }

    @Override
    public <T> T[] toArray(Class<T> type, Function<? super E, ? extends T> mapper) {
        return linkedList.toArray(type, mapper);
    }

    public List<E> toList() {
        return linkedList.clone();
    }

    public Iterator<E> iterator() {
        return linkedList.iterator();
    }

    public void addAll(Collection<E> toAdd) {
    }

    public void retainAll(Collection<E> toAdd) {

    }
}
