package structures.data;

import structures.data.interfaces.Collection;
import structures.data.interfaces.List;
import structures.data.interfaces.Set;

import java.util.Iterator;

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

    public Object[] toArray() {
        return linkedList.toArray();
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
