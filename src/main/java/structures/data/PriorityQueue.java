package structures.data;

import structures.data.interfaces.Collection;
import structures.data.interfaces.Queue;

import java.util.Comparator;
import java.util.Iterator;

public class PriorityQueue<E> implements Queue<E> {
    int length = 16;
    int size = 0;
    Object[] heap;

    Comparator<E> comparator;

    public PriorityQueue() {
        this.heap = new Object[length];
    }

    public PriorityQueue(int initialSize) {
        if (initialSize > 0)
            this.length = initialSize;
        this.heap = new Object[length];
    }

    public PriorityQueue(Comparator<E> comparator) {
        this.comparator = comparator;
        this.heap = new Object[length];
    }

    public PriorityQueue(int initialSize, Comparator<E> comparator) {
        if (initialSize > 0)
            this.length = initialSize;
        this.comparator = comparator;
        this.heap = new Object[length];
    }

    public boolean add(E element) {
        if (element == null)
            return false;

        heap[size++] = element;
        upheap();
        checkResize();
        return true;
    }

    private int compare(int i1, int i2) {
        return comparator.compare((E) heap[i1], (E) heap[i2]);
    }

    private void upheap() {
        int insertedIndex = size - 1;
        int parent = (insertedIndex - 1) / 2;
        while (insertedIndex != 0 && compare(parent, insertedIndex) > 0) {
            swap(insertedIndex, parent);

            insertedIndex = parent;
            parent = (parent - 1) / 2;
        }
    }

    private void downheap() {
        int parent = 0;
        int left = 2 * parent + 1;
        int right = 2 * parent + 2;
        while ((left < size || right < size) && (compare(parent, left) > 0 || compare(parent, right) > 0)) {
            if (compare(parent, left) > 0) {
                swap(parent, left);
                parent = left;
            } else if (compare(parent, right) > 0) {
                swap(parent, right);
                parent = right;
            }

            left = 2 * parent + 1;
            right = 2 * parent + 2;
        }
    }

    private void swap(int from, int to) {
        E temp = (E) heap[from];
        heap[from] = heap[to];
        heap[to] = temp;
    }

    private void checkResize() {
        if (size == length) {
            this.size = size << 1;
        }
    }

    public E remove() {
        if (size == 0)
            throw new IndexOutOfBoundsException("There are no elements in the queue");

        E removed = (E) heap[0];
        heap[0] = heap[--size];
        downheap();
        return removed;
    }

    public E peek() {
        if (size == 0)
            throw new IndexOutOfBoundsException("There are no elements in the queue");

        return (E) heap[0];
    }

    public void clear() {

    }

    public boolean contains(E element) {
        return false;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean remove(E element) {
        return false;
    }

    public int size() {
        return size;
    }

    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = heap[i];
        }

        return arr;
    }

    public Iterator<E> iterator() {
        return null;
    }

    public void addAll(Collection<E> collection) {

    }

    public void retainAll(Collection<E> collection) {

    }
}
