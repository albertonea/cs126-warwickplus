package structures.data.interfaces;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public interface Collection<E> extends Iterable<E> {
    // Adds element to the list, returns true on success and false otherwise.
    public boolean add(E element);

    // Clears (empties) the list.
    public void clear();

    // Returns true when element is in the list, false otherwise.
    public boolean contains(E element);

    // Returns true when the list contains no elements.
    public boolean isEmpty();

    // Removes an element form the list.
    // Returns true on success, false if the element was not found.
    public boolean remove(E element);

    // Returns the number of elements stored in the list.
    public int size();

    public E[] toArray(Class<E> type);

    public int[] toIntArray(ToIntFunction<? super E> mapper);

    public <T> T[] toArray(Class<T> type, Function<? super E, ? extends T> mapper);

    public Iterator<E> iterator();

    public void addAll(Collection<E> collection);

    public void retainAll(Collection<E> collection);
}
