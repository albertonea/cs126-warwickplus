package structures.data;

import structures.data.interfaces.List;
import structures.data.interfaces.Map;
import structures.data.interfaces.Set;

import java.util.Objects;

/**
 * Separately chained hash map. Each
 * bucket is a singly-linked chain of {@link Node} entries
 *
 * @param <K> the type of key stored in the map
 * @param <V> the type of value stored in the map
 */
public class HashMap<K, V> implements Map<K, V> {
    /**
     * A single key/value cell that lives in a bucket chain
     *
     * @param <K> the key type
     * @param <V> the value type
     */
    class Node<K, V> implements Entry<K, V> {
        final int hash;
        final K key;
        V value;
        // Next pointer in the chain, null for the tail
        Node next;

        /**
         * Creates a chain cell
         *
         * @param hash the pre-computed hash of {@code key}
         * @param key the key stored in this cell
         * @param value the value stored in this cell
         */
        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        /**
         * @return a hash code derived from both the key and the value
         */
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        /**
         * @return the key stored in this entry
         */
        public K getKey() {
            return key;
        }

        /**
         * @return the value currently stored in this entry
         */
        public V getValue() {
            return value;
        }
    }

    // Bucket array
    Node[] items;
    // Number of buckets, always a power of two
    int buckets = 16;
    // Total number of entries currently held
    int size = 0;
    // Threshold ratio of size/buckets that triggers a resize
    float loadFactor = 0.75f;

    /**
     * Constructs an empty map with the default capacity and load factor
     */
    public HashMap() {
        this.items = new Node[buckets];
    }

    /**
     * Rounds {@code n} up to the nearest power of two
     *
     * @param n the requested capacity
     * @return the smallest power of two greater than or equal to {@code n}
     */
    private int nextPowerOfTwo(int n) {
        int p = 1;
        while (p < n) p <<= 1;
        return p;
    }

    /**
     * Constructs a map given an initial size
     *
     * @param initialSize the expected initial capacity
     */
    public HashMap(int initialSize) {
        if (initialSize > 0) {
            // round the initial size up to the next power of two
            this.buckets = nextPowerOfTwo(initialSize);
        }
        this.items = new Node[buckets];
    }

    /**
     * Constructs a map with a given initial size and load factor
     *
     * @param initialSize the expected initial capacity
     * @param loadFactor the size/buckets ratio at which to resize
     */
    public HashMap(int initialSize, float loadFactor) {
        if (initialSize > 0) {
            // round the initial size up to the next power of two
            this.buckets = nextPowerOfTwo(initialSize);
        }

        this.items = new Node[buckets];

        // Only use if load factor is between 0 and 1
        if (loadFactor > 0.0f && loadFactor < 1.0f) {
            this.loadFactor = loadFactor;
        }
    }

    /**
     * Maps a hash value to a bucket index using a bitmask
     *
     * @param hash the hashed key
     * @param length the current number of buckets
     * @return the bucket index for the key
     */
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    /**
     * @return {@code true} when the load factor has been exceeded
     */
    private boolean shouldResize() {
        return ((float) size / buckets) > loadFactor;
    }

    /**
     * Associates {@code value} with {@code key}, replacing any previous mapping
     *
     * @param key the key under which the value is stored
     * @param value the value to be stored
     * @return the previous value, or {@code null} if there was no mapping
     */
    public V put(K key, V value) {
        // get hash and index for key
        int hash = hash(key);
        int i = indexFor(hash, buckets);
        Node current = items[i];

        // Walk the chain looking for an existing entry to overwrite
        while (current != null) {
            if (current.hash == hash && Objects.equals(key, current.key)) {
                V oldValue = (V) current.value;
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }

        // No existing entry prepend a new node to the chain
        Node newNode = new Node(hash, key, value);
        newNode.next = items[i];
        items[i] = newNode;

        size++;
        // Grow the table if the load factor has been crossed
        if (shouldResize()) resize();
        return null;
    }

    /**
     * Doubles the bucket array and rehashes every node into the new array
     */
    private void resize() {
        this.buckets = buckets << 1;
        Node[] newItems = new Node[buckets];

        // Walk every old bucket and link each node into its new bucket
        for (Node head : items) {
            Node current = head;
            while (current != null) {
                Node next = current.next;
                int i = indexFor(current.hash, buckets);
                // Prepend to the new bucket chain
                current.next = newItems[i];
                newItems[i] = current;
                current = next;
            }
        }
        this.items = newItems;
    }

    /**
     * Looks up the value currently associated with {@code key}
     *
     * @param key the key to look up
     * @return the associated value, or {@code null} if no mapping exists
     */
    public V get(K key) {
        int hash = hash(key);
        Node node = items[indexFor(hash, buckets)];
        // Walk the bucket chain comparing both hash and key equality
        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                return (V) node.value;
            }
            node = node.next;
        }
        return null;
    }

    /**
     * Removes the entry for {@code key} and returns the value
     *
     * @param key the key whose entry should be removed
     * @return the previous value, or {@code null} if the key was absent
     */
    public V remove(K key) {
        int hash = hash(key);
        int i = indexFor(hash, buckets);
        Node current = items[i];
        Node prev = null;

        // Walk the chain, keeping the previous node so we can splice the target out
        while (current != null) {
            if (current.hash == hash && Objects.equals(key, current.key)) {
                // Check if removing the head of the chain
                if (prev == null) {
                    items[i] = current.next;
                } else {
                    prev.next = current.next;
                }
                // Detach the removed node and decrement size
                current.next = null;
                size--;
                return (V) current.value;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    /**
     * Like {@link #get(Object)}, but returns the supplied fallback if the key
     * is not present
     *
     * @param key the key to look up
     * @param fallback the value to return if no mapping exists
     * @return the mapped value or the fallback
     */
    public V getOrDefault(K key, V fallback) {
        V value = get(key);
        if (value == null) {
            return fallback;
        }

        return value;
    }

    /**
     * @return the number of key/value pairs currently held
     */
    public int size() {
        return size;
    }

    /**
     * Resets the map to its default capacity, dropping every entry
     */
    public void clear() {
        items = new Node[16];
        buckets = 16;
        size = 0;
    }

    /**
     * @return every entry held by the map
     */
    public List<Entry<K, V>> entryList() {
        List<Entry<K, V>> entries = new LinkedList<>();

        // Walk every bucket chain and add each node to the list
        for (Node<K, V> head : items) {
            Node<K, V> current = head;
            while (current != null) {
                entries.add(current);
                current = current.next;
            }
        }
        return entries;
    }

    /**
     * @return a list of every key in the map
     */
    public List<K> keyList(){
        List<K> keys = new LinkedList<>();

        // Walk every bucket chain and add each node key to the list
        for (Node<K, V> head : items) {
            Node<K, V> current = head;
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }

        return keys;
    }

    /**
     * @return a list of every value in the map
     */
    public List<V> valueList() {
        List<V> values = new LinkedList<>();

        // Walk every bucket chain and add each node value to the list
        for (Node<K, V> head : items) {
            Node<K, V> current = head;
            while (current != null) {
                values.add(current.value);
                current = current.next;
            }
        }

        return values;
    }

    /**
     * Reports whether {@code key} currently has a mapping
     *
     * @param key the key to test
     * @return {@code true} if a mapping exists
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Hash function that spreads the upper bits of the key's hash into the lower bits so all
     * bits contribute to the bucket index
     *
     * @param key the key being hashed
     * @return a well-spread hash value (0 for {@code null} keys)
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        // XOR the top 16 bits into the bottom 16
        return h ^ (h >>> 16);
    }
}
