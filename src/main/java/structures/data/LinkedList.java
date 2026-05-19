package structures.data;


import structures.data.interfaces.Collection;
import structures.data.interfaces.List;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * Doubly linked list with head and tail pointers
 *
 * @param <E> the type of element stored
 */
public class LinkedList<E> implements List<E> {
    // First element of the list, or null when the list is empty
    Element<E> head;
    // Last element of the list, kept in sync so append is O(1)
    Element<E> tail;
    // Cached element count so size() is O(1)
    int size;

    /**
     * A single node in the doubly-linked list
     *
     * @param <E> the payload type
     */
    public static class Element<E> {
        E value;
        Element<E> next;
        Element<E> prev;

        /**
         * Creates an element with no neighbours set
         *
         * @param value the payload
         */
        public Element(E value) {
            this.value = value;
        }

        /**
         * Creates an element linked into a chain
         *
         * @param value the payload
         * @param next the next element
         * @param prev the previous element
         */
        public Element(E value, Element<E> next, Element<E> prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * Constructs an empty list
     */
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Retrieves the element at the given index
     *
     * @param index the zero-based position
     * @return the element at that position
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     */
    public E get(int index) throws IndexOutOfBoundsException {
        if (isEmpty() || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        // Walk from the head to the index
        Element<E> ptr = head;
        for (int i = 0; i < index; i++) {
            ptr = ptr.next;
        }
        return ptr.value;
    }

    /**
     * Reports the index of the first occurrence of {@code element}
     *
     * @param element the value to find
     * @return the index, or -1 if not found
     */
    public int indexOf(E element) {
        int index = 0;
        for (Element<E> ptr = head; ptr != null; ptr = ptr.next) {
            if (element.equals(ptr.value)) {
                return index;
            }
            index++;
        }

        return -1;
    }

    /**
     * Replaces the element at {@code index}
     *
     * @param index the position to overwrite
     * @param element the new value
     * @return the value that was previously at that position
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     */
    public E set(int index, E element) throws IndexOutOfBoundsException {
        if (isEmpty() || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        // Walk to the target node
        Element<E> indexToChange = head;
        for (int i = 0; i < index; i++) {
            indexToChange = indexToChange.next;
        }

        // Overwrite value
        E retVal = indexToChange.value;
        indexToChange.value = element;

        return retVal;
    }

    /**
     * Appends an element to the tail of the list
     *
     * @param element the value to append
     * @return always {@code true}
     */
    public boolean add(E element) {
        if (size == 0) {
            // First element: head and tail point to the same node
            head = tail = new Element<>(element, null, null);
        } else {
            // Stitch the new node in after the existing tail
            tail = new Element<>(element, null, tail);
            // Make old tail point to new tail
            tail.prev.next = tail;
        }

        size++;
        return true;
    }

    /**
     * Empties the list
     */
    public void clear() {
        // Dropping the head reference makes the whole chain unreachable
        head = tail = null;
        size = 0;
    }

    /**
     * @param element the value to look for
     * @return whether the list contains {@code element}
     */
    public boolean contains(E element) {
        return indexOf(element) >= 0;
    }

    /**
     * @return whether the list has no elements
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Removes the first occurrence of {@code element}
     *
     * @param element the value to remove
     * @return {@code true} if a value was removed
     */
    public boolean remove(E element) {
        Element<E> ptr = head;

        // Walk the chain until the matching node is found
        while (ptr != null) {
            if (ptr.value.equals(element)) {
                if (size == 1) {
                    clear();
                    return true;
                }
                // Removing the head
                if (ptr.prev == null) {
                    head = ptr.next;
                    head.prev = null;
                }
                // Removing the tail
                else if (ptr.next == null) {
                    tail = ptr.prev;
                    tail.next = null;
                }
                // Removing an element in the middle
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

    /**
     * @return the number of elements
     */
    public int size() {
        return size;
    }

    /**
     * @param type the runtime array type
     * @return the list's elements as a typed array
     */
    public E[] toArray(Class<E> type) {
        // Instantiate a new array of provided type
        @SuppressWarnings("unchecked")
        E[] items = (E[]) Array.newInstance(type, size);

        // Single pass from head to tail copying each value into the array
        Element<E> tmp = head;
        for (int i = 0; i < size; i++) {
            if (tmp == null) {
                break;
            }
            items[i] = tmp.value;
            tmp = tmp.next;
        }

        return items;
    }

    /**
     * @param mapper function that maps each element to an int
     * @return the mapped values as a primitive int array
     */
    public int[] toIntArray(ToIntFunction<? super E> mapper) {
        int[] items = new int[size];

        // Apply the mapper while traversing the chain
        Element<E> tmp = head;
        for (int i = 0; i < size && tmp != null; i++) {
            items[i] = mapper.applyAsInt(tmp.value);
            tmp = tmp.next;
        }

        return items;
    }

    /**
     * @param type the runtime array type
     * @param mapper function applied to each element
     * @param <T> the result array's element type
     * @return an array of the mapped values
     */
    public <T> T[] toArray(Class<T> type, Function<? super E, ? extends T> mapper) {
        // Instantiate a new array of provided type
        @SuppressWarnings("unchecked")
        T[] items = (T[]) Array.newInstance(type, size);

        // Apply the mapper while traversing the chain
        Element<E> tmp = head;
        for (int i = 0; i < size && tmp != null; i++) {
            items[i] = mapper.apply(tmp.value);
            tmp = tmp.next;
        }

        return items;
    }

    /**
     * @return a shallow copy of the list
     */
    public LinkedList<E> clone() {
        LinkedList<E> copy = new LinkedList<>();
        copy.head = copy.tail = null;

        // Walk the original chain and append each value to the copy
        for (Element<E> e = head; e != null; e = e.next) {
            copy.add(e.value);
        }
        return copy;
    }

    /**
     * Appends every element of {@code collection} to this list
     *
     * @param collection the source collection
     */
    public void addAll(Collection<E> collection) {
        for (E e: collection) {
            add(e);
        }
    }

    /**
     * @return an iterator that walks from head to tail
     */
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    /**
     * Iterator over the chain
     */
    private class LinkedListIterator implements Iterator<E> {
        private Element<E> current = head; // starts at head on creation

        /**
         * @return whether another value is available
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * @return the next value in the chain
         * @throws NoSuchElementException if the iterator is exhausted
         */
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E value = current.value;
            current = current.next;
            return value;
        }
    }
}
