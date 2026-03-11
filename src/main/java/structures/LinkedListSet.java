package structures;

import structures.interfaces.List;
import structures.interfaces.Set;

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

    public E[] toArray() {
        return linkedList.toArray();
    }

    public LinkedList<E> toLinkedList() {
        return linkedList.copy();
    }
}
