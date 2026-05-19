package structures.data;

import structures.data.interfaces.Collection;
import structures.data.interfaces.List;
import structures.data.interfaces.Map;
import structures.data.interfaces.Set;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * HashMap backed implementation of {@link Set}. Stores members as keys
 * in a {@link HashMap} with a single shared placeholder object as the value,
 * reusing the hashmap collision handling and resize logic
 *
 * @param <E> the type of element stored in the set
 */
public class HashSet<E> implements Set<E> {
    // Underlying map, keys are the set members, values are a placeholder
    Map<E, Object> mapping = new HashMap<>();
    // Single shared placeholder
    private static final Object placeholder = new Object();

    /**
     * Constructs an empty set
     */
    public HashSet() {}

    /**
     * Constructs a set pre-populated with every element of the supplied
     * collection
     *
     * @param collection the collection whose elements seed this set
     */
    public HashSet(Collection<E> collection) {
        addAll(collection);
    }

    /**
     * Adds an element to the set
     *
     * @param element the value to add
     * @return always {@code true}; adding a duplicate is a no-op
     */
    public boolean add(E element) {
        mapping.put(element, placeholder);
        return true;
    }

    /**
     * Removes an element from the set
     *
     * @param element the value to remove
     * @return {@code true} if the element was present
     */
    public boolean remove(E element) {
        return placeholder == mapping.remove(element);
    }

    /**
     * @param element the value to test
     * @return whether the set contains {@code element}
     */
    public boolean contains(E element) {
        return mapping.containsKey(element);
    }

    /**
     * @return the number of elements in the set
     */
    public int size() {
        return mapping.size();
    }

    /**
     * Removes every element from the set
     */
    public void clear() {
        mapping.clear();
    }

    /**
     * @return whether the set has no elements
     */
    public boolean isEmpty() {
        return mapping.size() == 0;
    }

    /**
     * @return the set's elements as a {@link List}
     */
    public List<E> toList() {
        return mapping.keyList();
    }

    /**
     * @param type the runtime array type
     * @return the set's elements as a typed array
     */
    public E[] toArray(Class<E> type) {
        return mapping.keyList().toArray(type);
    }

    /**
     * @param mapper function that maps each element to an int
     * @return the mapped elements as a primitive int array
     */
    public int[] toIntArray(ToIntFunction<? super E> mapper) {
        return mapping.keyList().toIntArray(mapper);
    }

    /**
     * @param type runtime element type of the result array
     * @param mapper function applied to each element
     * @param <T> the type stored by the result array
     * @return an array of the mapped values
     */
    public <T> T[] toArray(Class<T> type, Function<? super E, ? extends T> mapper) {
        return mapping.keyList().toArray(type, mapper);
    }

    /**
     * @return an iterator over the set's elements
     */
    public Iterator<E> iterator() {
        return mapping.keyList().iterator();
    }

    /**
     * Adds every element of {@code collection} to this set
     *
     * @param collection the collection whose elements should be added
     */
    public void addAll(Collection<E> collection) {
        for (E element: collection) {
            mapping.put(element, placeholder);
        }
    }

    /**
     * Removes every element not present in {@code collection}
     *
     * @param collection the collection whose members must be retained
     */
    public void retainAll(Collection<E> collection) {
        // iterate over elements removing any not present in collection
        for (E item : mapping.keyList()) {
            if (!collection.contains(item)) {
                mapping.remove(item);
            }
        }
    }
}
