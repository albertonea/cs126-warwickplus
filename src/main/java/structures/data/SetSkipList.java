package structures.data;

import structures.data.interfaces.List;
import structures.data.interfaces.Set;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Skip list keyed in sorted order, where each key holds a {@link Set} of
 * values so the same key can map to many distinct values
 *
 * @param <K> the key type, must be a total order
 * @param <V> the type of value associated with each key
 */
public class SetSkipList<K extends Comparable<? super K>, V> {
    // Max number of levels
    private static final int MAX_LEVEL = 32;

    // Head pointer at full height with no data
    private final Node<K, V> head = new Node<>(null, MAX_LEVEL);
    // Highest level currently in use
    private int level = 1;

    public SetSkipList() {}

    /**
     * Retrieves every value associated with the supplied key
     *
     * @param key the key to look up
     * @return a list of the associated values, or {@code null} if no entry
     *         exists for {@code key}
     */
    public List<V> get(K key) {
        Node<K, V> node = findNode(key);
        if (node == null) {
            return null;
        }
        return node.values.toList();
    }

    /**
     * Returns every value whose key falls within {@code (from, to)} exclusively.
     *
     * @param from the lower bound (exclusive)
     * @param to the upper bound (exclusive)
     * @return a list of values in ascending key order, or {@code null} if
     *         {@code from > to}
     */
    public List<V> getRange(K from, K to) {
        if (from.compareTo(to) > 0) return null;

        // Descend levels, walking forward while next key <= from
        // Lands current on the last node with key <= from
        Node<K, V> current = head;
        for (int i = level - 1; i >= 0; i--) {
            Node<K, V> next = current.forward[i];
            while (next != null && next.key.compareTo(from) <= 0) {
                current = next;
                next = current.forward[i];
            }
        }
        // Step once on level 0 to enter the range
        current = current.forward[0];

        // Walk the bottom level until the upper bound is reached
        // adding all values to result list
        List<V> result = new LinkedList<>();
        while (current != null && current.key.compareTo(to) < 0) {
            result.addAll(current.values);
            current = current.forward[0];
        }
        return result;
    }

    /**
     * Inserts a (key, value) pair. If the key already exists the value is
     * added to its value set, otherwise a new node is woven in at a random
     * level
     *
     * @param key the key to file the value under
     * @param value the value to associate with {@code key}
     */
    public void put(K key, V value) {
        // update[i] records the rightmost node at level i with key < target
        Node<K, V>[] update = getPredecessors(key);

        // If a node with the same key exists, merge into its value-set
        Node<K, V> candidate = update[0].forward[0];
        if (candidate != null && candidate.key.compareTo(key) == 0) {
            candidate.values.add(value);
            return;
        }

        // Create a new node and roll a random level
        int newLevel = randomLevel();
        if (newLevel > level) {
            // Levels above current maximum have head as predecessor
            for (int i = level; i < newLevel; i++) {
                update[i] = head;
            }
            level = newLevel;
        }

        // Splice the new node in at every level it spans
        Node<K, V> newNode = new Node<>(key, newLevel);
        for (int i = 0; i < newLevel; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }
        newNode.values.add(value);
    }

    /**
     * Removes a single (key, value) pair. The node is unlinked when its
     * value-set ends up empty
     *
     * @param key the key whose mapping is being trimmed
     * @param value the value to remove from that mapping
     * @return {@code true} if a matching entry was removed
     */
    public boolean remove(K key, V value) {
        // update[i] records the rightmost node at level i with key < target
        Node<K, V>[] update = getPredecessors(key);

        Node<K, V> target = update[0].forward[0];
        // Check if target node exists
        if (target == null || target.key.compareTo(key) != 0) {
            return false;
        }

        target.values.remove(value);
        // If value set is not empty keep the node in place
        if (!target.values.isEmpty()) {
            return true;
        }

        // If value set is empty, remove the node from every level it has
        for (int i = 0; i < target.forward.length; i++) {
            update[i].forward[i] = target.forward[i];
        }

        // Trim the active level if the top one was emptied
        while (level > 1 && head.forward[level - 1] == null) {
            level--;
        }
        return true;
    }

    /**
     * For every level gets the rightmost node at level i with key < target
     *
     * @param key the key whose mapping is being trimmed
     * @return array of nodes indexed by level
     */
    private Node<K, V>[] getPredecessors(K key) {
       // update[i] records the rightmost node at level i with key < target
        Node<K, V>[] update = (Node<K, V>[]) new Node[MAX_LEVEL];

        // Iterate top to bottom
        Node<K, V> current = head;
        for (int i = level - 1; i >= 0; i--) {
            Node<K, V> next = current.forward[i];
            // Iterate walk left to right until rightmost node with key < target
            while (next != null && next.key.compareTo(key) < 0) {
                current = next;
                next = current.forward[i];
            }
            // Add rightmost node at level i of the new node and descend a level
            update[i] = current;
        }

        return update;
    }

    /**
     * Walks the skip list to find the node holding exactly {@code key}
     *
     * @param key the key to look up
     * @return the matching node, or {@code null} if absent
     */
    private Node<K, V> findNode(K key) {
        // Iterate top to bottom until largest key < target is found
        Node<K, V> current = head;
        for (int i = level - 1; i >= 0; i--) {
            Node<K, V> next = current.forward[i];
            // Iterate left to right until rightmost node with key < target
            while (next != null && next.key.compareTo(key) < 0) {
                current = next;
                next = current.forward[i];
            }
        }

        // Next node should be target
        Node<K, V> candidate = current.forward[0];
        if (candidate != null && candidate.key.compareTo(key) == 0) {
            return candidate;
        }
        return null;
    }

    /**
     * Picks a random tower height for a new node
     *
     * @return a level in the range {@code [1, MAX_LEVEL]}
     */
    private int randomLevel() {
        // ThreadLocalRandom is much faster than Math.Random
        long bits = ThreadLocalRandom.current().nextLong();

        // Trailing zero count is number of "heads flips"
        int lvl = Long.numberOfTrailingZeros(bits) + 1;
        return Math.min(lvl, MAX_LEVEL);
    }

    /**
     * Internal node holding a key, the set of associated values, and the
     * forward pointer array
     *
     * @param <K> the key type
     * @param <V> the value type
     */
    private static final class Node<K extends Comparable<? super K>, V> {
        final K key;
        // Each key maps to a set of distinct values
        final Set<V> values = new HashSet<>();
        // forward[i] is the next node at level i, or null at the end
        final Node<K, V>[] forward;

        /**
         * @param key the key stored at this node (null for the head sentinel)
         * @param level the height of the forward-pointer tower
         */
        @SuppressWarnings("unchecked")
        Node(K key, int level) {
            this.key = key;
            this.forward = (Node<K, V>[]) new Node[level];
        }
    }
}
