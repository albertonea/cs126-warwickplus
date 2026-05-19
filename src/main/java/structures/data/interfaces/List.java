package structures.data.interfaces;

/**
 * A {@link Collection} that preserves the insertion order of its elements and
 * allows them to be addressed by a zero-based index
 *
 * @param <E> the type of element stored in the list
 */
public interface List<E> extends Collection<E> {
    /**
     * Retrieves the element currently held at the supplied index
     *
     * @param index the zero-based position of the element to fetch
     * @return the element at the given index
     */
    public E get(int index);

    /**
     * Reports the position of the first occurrence of {@code element} within
     * the list
     *
     * @param element the value to search for
     * @return the zero-based index of the element, or {@code -1} if not found
     */
    public int indexOf(E element);

    /**
     * Replaces the element at {@code index} with the supplied value and
     * returns the value that was previously stored there
     *
     * @param index the position to overwrite
     * @param element the new value to place at {@code index}
     * @return the element that was overwritten
     */
    public E set(int index, E element);

    /**
     * Produces a shallow copy of the list. The new list contains references
     * to the same element objects, but mutations to one list do not affect
     * the other
     *
     * @return a new list with the same elements in the same order
     */
    public List<E> clone();
}
