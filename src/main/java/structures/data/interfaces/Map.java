package structures.data.interfaces;

/**
 * Generic key-to-value association
 *
 * @param <K> the type of the key
 * @param <V> the type of the value associated with each key
 */
public interface Map<K, V> {
    /**
     * A single key/value pairing
     *
     * @param <K> the key type
     * @param <V> the value type
     */
    interface Entry<K, V> {
        /**
         * @return the key half of this entry
         */
        K getKey();

        /**
         * @return the value half of this entry
         */
        V getValue();

        /**
         * @return a hash code consistent with the key/value pair
         */
        int hashCode();
    }

    /**
     * Associates {@code value} with {@code key}, overwriting any previous
     * mapping
     *
     * @param key the key under which the value is stored
     * @param value the value to be stored
     * @return the previous value associated with {@code key}, or {@code null}
     *         if there was no mapping
     */
    public V put(K key, V value);

    /**
     * Looks up the value currently associated with {@code key}
     *
     * @param key the key to look up
     * @return the associated value, or {@code null} if no mapping exists
     */
    public V get(K key);

    /**
     * Removes the entry for {@code key} from the map and returns the value
     * associated with it
     *
     * @param key the key whose entry should be removed
     * @return the previous value, or {@code null} if the key was absent
     */
    public V remove(K key);

    /**
     * Like {@link #get(Object)}, but returns the supplied fallback if the key
     * is not present
     *
     * @param key the key to look up
     * @param fallback the value to return if no mapping exists
     * @return the mapped value or the fallback
     */
    public V getOrDefault(K key, V fallback);

    /**
     * @return the number of key/value pairs currently held
     */
    public int size();

    /**
     * Removes every mapping from the map
     */
    public void clear();

    /**
     * @return every entry held by the map, packaged for iteration
     */
    public List<Entry<K, V>> entryList();

    /**
     * @return a list of every key in the map
     */
    public List<K> keyList();

    /**
     * @return a list of every value in the map
     */
    public List<V> valueList();

    /**
     * Reports whether the supplied key currently has a mapping
     *
     * @param key the key to test
     * @return {@code true} if there is a mapping for {@code key}
     */
    public boolean containsKey(K key);

}
