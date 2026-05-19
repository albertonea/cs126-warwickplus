package structures.data.interfaces;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * The base interface for any unordered or ordered group of elements that can
 * be iterated through so that they can share a common contract for storing,
 * inspecting and emitting their contents.
 *
 * @param <E> the type of element that the collection stores
 */
public interface Collection<E> extends Iterable<E> {
    /**
     * Inserts a single element into the collection
     *
     * @param element the value to be added
     * @return {@code true} if the element was added successfully,
     *         {@code false} otherwise
     */
    public boolean add(E element);

    /**
     * Removes every element from the collection
     */
    public void clear();

    /**
     * Reports whether the supplied element is currently in the
     * collection
     *
     * @param element the element to search for
     * @return {@code true} if the element is present, {@code false} otherwise
     */
    public boolean contains(E element);

    /**
     * Reports whether the collection currently holds any elements
     *
     * @return {@code true} if the collection has no elements
     */
    public boolean isEmpty();

    /**
     * Removes the first occurrence of the supplied element from the
     * collection
     *
     * @param element the element to remove
     * @return {@code true} on a successful removal, {@code false} if the
     *         element was not found
     */
    public boolean remove(E element);

    /**
     * Returns the current number of elements held by the collection
     *
     * @return the size of the collection
     */
    public int size();

    /**
     * Copies the contents of the collection into a typed array
     * The runtime class of the array is fixed by {@code type} so that the
     * caller receives a strongly-typed array rather than {@code Object[]}
     *
     * @param type the {@link Class} object describing the element type
     * @return an array containing every element of the collection
     */
    public E[] toArray(Class<E> type);

    /**
     * Projects each element of the collection to a primitive {@code int} via
     * the supplied mapping function and returns the results as a primitive
     * array. This avoids the boxing cost that would be incurred by
     * {@link #toArray(Class)} when the caller only needs the integer view
     *
     * @param mapper the function that extracts an {@code int} from each
     *               element
     * @return an array of the mapped integer values
     */
    public int[] toIntArray(ToIntFunction<? super E> mapper);

    /**
     * Copies the collection into a typed array, applying {@code mapper} to
     * each element first. This is useful for emitting an array of a field
     * (e.g. {@code Cast::getPerson}) without materialising an intermediate
     * list.
     *
     * @param type   the runtime class of the array element
     * @param mapper the function applied to each source element
     * @param <T>    the type stored by the resulting array
     * @return a new array containing the mapped values
     */
    public <T> T[] toArray(Class<T> type, Function<? super E, ? extends T> mapper);

    /**
     * Returns an iterator that walks every element of the collection in the
     * order defined by the implementation.
     *
     * @return a new iterator
     */
    public Iterator<E> iterator();

    /**
     * Adds every element of {@code collection} to this collection
     *
     * @param collection the collection whose elements should be appended
     */
    public void addAll(Collection<E> collection);
}
