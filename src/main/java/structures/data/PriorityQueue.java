package structures.data;

import structures.data.interfaces.Collection;
import structures.data.interfaces.Queue;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class PriorityQueue<E> implements Queue<E> {
    int length = 16;
    int size = 0;
    Object[] heap;

    Comparator<E> comparator;

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
        int smallest = smallestChild(parent);
        while (smallest != parent) {
            swap(parent, smallest);
            parent = smallest;
            smallest = smallestChild(parent);
        }
    }

    private int smallestChild(int parent) {
        int left = 2 * parent + 1;
        int right = 2 * parent + 2;
        int smallest = parent;

        if (left < size && compare(left, smallest) < 0)
            smallest = left;
        if (right < size && compare(right, smallest) < 0)
            smallest = right;
        return smallest;
    }

    private void swap(int from, int to) {
        E temp = (E) heap[from];
        heap[from] = heap[to];
        heap[to] = temp;
    }

    private void checkResize() {
        if (size == length) {
            this.length = length << 1;
            Object[] newHeap = new Object[length];
            System.arraycopy(heap, 0, newHeap, 0, size);
            this.heap = newHeap;
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
        return size == 0;
    }

    public boolean remove(E element) {
        return false;
    }

    public int size() {
        return size;
    }

    public E[] toArray(Class<E> type) {
        @SuppressWarnings("unchecked")
        E[] arr =  (E[]) Array.newInstance(type, size);

        for (int i = 0; i < size; i++) {
            arr[i] = (E) heap[i];
        }

        return arr;
    }

    @Override
    public int[] toIntArray(ToIntFunction<? super E> mapper) {
        return new int[0];
    }

    @Override
    public <T> T[] toArray(Class<T> type, Function<? super E, ? extends T> mapper) {
        return null;
    }

    public Iterator<E> iterator() {
        return null;
    }

    public void addAll(Collection<E> collection) {

    }

    public void retainAll(Collection<E> collection) {

    }
}
