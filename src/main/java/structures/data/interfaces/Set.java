package structures.data.interfaces;

/**
 * A {@link Collection} whose elements are unique
 *
 * @param <E> the type of element stored in the set
 */
public interface Set<E> extends Collection<E> {
    /**
     * Returns the elements of the set as a {@link List}. The ordering is
     * not guaranteed to match insertion order
     *
     * @return a list containing every member of this set exactly once
     */
    public List<E> toList();

    /**
     * Removes from this set every element that is not also present in
     * {@code collection}. This is the in-place set intersection operation
     *
     * @param collection the collection whose members must be retained
     */
    public void retainAll(Collection<E> collection);
}
