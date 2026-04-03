package structures.data;


import structures.data.interfaces.Collection;
import structures.data.interfaces.List;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements List<E> {
    Element<E> head;
    Element<E> tail;
    int size;

    public static class Element<E> {
        E value;
        Element<E> next;
        Element<E> prev;

        public Element(E value) {
            this.value = value;
        }

        public Element(E value, Element<E> next, Element<E> prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    public E get(int index) throws IndexOutOfBoundsException {
        if (isEmpty() || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        // Gets the element at index in the list
        Element<E> ptr = head;
        for (int i = 0; i < index; i++) {
            ptr = ptr.next;
        }
        return ptr.value;
    }

    public int indexOf(E element) {
        if (element == null) {
            int index = 0;
            for (Element<E> ptr = head; ptr != null; ptr = ptr.next) {
                if (ptr.value == null) {
                    return index;
                }
                index++;
            }
        }

        int index = 0;
        for (Element<E> ptr = head; ptr != null; ptr = ptr.next) {
            if (element.equals(ptr.value)) {
                return index;
            }
            index++;
        }

        return -1;
    }

    public E set(int index, E element) throws IndexOutOfBoundsException {
        if (isEmpty() || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        Element<E> indexToChange = head;
        for (int i = 0; i < index; i++) {
            indexToChange = indexToChange.next;
        }

        E retVal = indexToChange.value;
        indexToChange.value = element;

        return retVal;
    }

    public boolean add(E element) {
        if (size == 0) {
            head = tail = new Element<>(element, null, null);
        } else {
            tail = new Element<>(element, null, tail);
            tail.prev.next = tail;
        }

        size++;
        return true;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public boolean contains(E element) {
        return indexOf(element) >= 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public boolean remove(E element) {
        Element<E> ptr = head;

        // find the Element with the element
        while (ptr != null) {
            if (ptr.value.equals(element)) {
                if (size == 1) {
                    head = tail = null;
                    return true;
                }
                // check if removing head
                if (ptr.prev == null) {
                    head = ptr.next;
                    head.prev = null;
                }
                // check if removing tail
                else if (ptr.next == null) {
                    tail = ptr.prev;
                    tail.next = null;
                }
                // removing element in the middle
                else {
                    ptr.prev.next = ptr.next;
                    ptr.next.prev = ptr.prev;
                }

                size--;
                return true;
            }

            ptr = ptr.next;
        }

        return false;
    }

    public int size() {
        return size;
    }

    public Object[] toArray() {
        Element<E> tmp = head;
        Object[] items = new Object[size];
        for (int i = 0; i < size; i++) {
            if (tmp == null) {
                break;
            }
            items[i] = tmp.value;
            tmp = tmp.next;
        }
        return items;
    }

    public LinkedList<E> clone() {
        LinkedList<E> copy = new LinkedList<>();
        copy.head = copy.tail = null;

        for (Element<E> e = head; e != null; e = e.next) {
            copy.add(e.value);
        }
        return copy;
    }

    public void addAll(Collection<E> toAdd) {
        for (E e: toAdd) {
            add(e);
        }
    }

    public void retainAll(Collection<E> set) {

    }

    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<E> {
        private Element<E> current = head; // starts at head on creation

        public boolean hasNext() {
            return current != null;
        }

        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E value = current.value;
            current = current.next;
            return value;
        }
    }
}