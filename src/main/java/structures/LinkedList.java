package structures;


import structures.interfaces.List;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements List<E> {
    ListElement<E> head;
    int size;
    
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    public E get(int index) {
        if (isEmpty() || index >= size()) {
            return null;
        }
        // Gets the element at index in the list
        ListElement<E> ptr = head;
        for (int i = 0; i < index; i++) {
            ptr = ptr.getNext();
        }
        return ptr.getValue();
    }

    public int indexOf(E element) {
        // Gets the index of element in the list
        ListElement<E> ptr = head;
        int i = 0;
        while (ptr != null) {
            if (element.equals(ptr.getValue())) {
                return i;
            }
            i++;
            ptr = ptr.getNext();
        }
        return -1;
    }

    public E set(int index, E element) {
        if (isEmpty()) {
            return null;
        }

        // Sets element at index in the list
        ListElement<E> ptr = head;
        ListElement<E> prev = null;

        for (int i = 0; i < index; i++) {
            prev = ptr;
            ptr = ptr.getNext();
        }

        E ret = ptr.getValue();

        ListElement<E> newLink = new ListElement<>(element);
        newLink.setNext(ptr.getNext());
        if (prev != null) {
            prev.setNext(newLink);
        } else {
            head = newLink;
        }

        return ret;
    }

    public boolean add(E element) {
        ListElement<E> temp = new ListElement<>(element);
        
        // if the list is not empty, point the new link to head
        if (head != null) {
            temp.setNext(head);
        }
        // update the head
        head = temp;

        size++;
        return true;
    }

    public void clear() {
        head = null;
    }

    public boolean contains(E element) {
        return indexOf(element) != -1;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public boolean remove(E element) {
        ListElement<E> ptr = head;
        ListElement<E> prev = null;

        while (ptr != null) {
            if (ptr.getValue().equals(element)) {
                if (prev == null) {
                    head = ptr.getNext();
                } else {
                    prev.setNext(ptr.getNext());
                }

                size--;
                return true;
            }

            prev = ptr;
            ptr = ptr.getNext();
        }

        return false;
    }

    public int size() {
        return size;
    }

    public E[] toArray() {
        ListElement<E> tmp = head;
        Object[] items = new Object[size];
        for (int i = 0; i < size; i++) {
            if (tmp == null) {
                break;
            }
            items[i] = tmp.getValue();
            tmp = tmp.getNext();
        }
        return (E[]) items;
    }

    public LinkedList<E> copy() {
        LinkedList<E> copy = new LinkedList<>();
        copy(head, copy, null);
        return copy;
    }

    public static <E> ListElement<E> copy(ListElement<E> srcHead,
                                           LinkedList<E> dest,
                                           ListElement<E> tail) {
        ListElement<E> ptr = srcHead;
        while (ptr != null) {
            ListElement<E> newNode = new ListElement<>(ptr.getValue());
            if (dest.head == null) {
                dest.head = newNode;
            } else {
                tail.setNext(newNode);
            }
            tail = newNode;
            dest.size++;
            ptr = ptr.getNext();
        }
        return tail; // return updated tail for chaining
    }

    public static <E> LinkedList<E> merge(LinkedList<E> first, LinkedList<E> second) {
        LinkedList<E> result = new LinkedList<>();
        ListElement<E> tail = copy(first.head, result, null);
        copy(second.head, result, tail);
        return result;
    }

    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<E> {
        private ListElement<E> current = head; // starts at head on creation

        public boolean hasNext() {
            return current != null;
        }

        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E value = current.getValue();
            current = current.getNext();
            return value;
        }
    }
}