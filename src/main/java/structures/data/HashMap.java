package structures.data;

import structures.data.interfaces.Map;
import structures.data.interfaces.Set;

import java.util.Objects;

public class HashMap<K, V> implements Map<K, V> {
    class Node<K, V> implements Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    Node[] items;
    int buckets = 16;
    int size = 0;
    float loadFactor = 0.75f;

    public HashMap() {
        this.items = new Node[buckets];
    }

    private int nextPowerOfTwo(int n) {
        int p = 1;
        while (p < n) p <<= 1;
        return p;
    }

    public HashMap(int initialSize) {
        if (initialSize > 0) {
            this.buckets = nextPowerOfTwo(initialSize);
        }
        this.items = new Node[buckets];
    }

    public HashMap(int initialSize, float loadFactor) {
        if (initialSize > 0)
            this.buckets = nextPowerOfTwo(initialSize);

        this.items = new Node[buckets];

        if (loadFactor > 0.0f && loadFactor < 1.0f)
            this.loadFactor = loadFactor;
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private boolean shouldResize() {
        return ((float) size / buckets) > loadFactor;
    }

    public V put(K key, V value) {
        int hash = hash(key);
        int i = indexFor(hash, buckets);
        Node current = items[i];

        while (current != null) {
            if (current.hash == hash && Objects.equals(key, current.key)) {
                V oldValue = (V) current.value;
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }

        Node newNode = new Node(hash, key, value);
        newNode.next = items[i];
        items[i] = newNode;

        size++;
        if (shouldResize()) resize();
        return null;
    }

    private void resize() {
        this.buckets = buckets << 1;
        Node[] newItems = new Node[buckets];

        for (Node head : items) {
            Node current = head;
            while (current != null) {
                Node next = current.next;
                int i = indexFor(current.hash, buckets);
                current.next = newItems[i];
                newItems[i] = current;
                current = next;
            }
        }
        this.items = newItems;
    }

    public V get(K key) {
        int hash = hash(key);
        Node node = items[indexFor(hash, buckets)];
        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key))
                return (V) node.value;
            node = node.next;
        }
        return null;
    }

    public V remove(K key) {
        int hash = hash(key);
        int i = indexFor(hash, buckets);
        Node current = items[i];
        Node prev = null;

        while (current != null) {
            if (current.hash == hash && Objects.equals(key, current.key)) {
                if (prev == null) {
                    items[i] = current.next;
                } else {
                    prev.next = current.next;
                }
                current.next = null;
                size--;
                return (V) current.value;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    public V getOrDefault(K key, V fallback) {
        V value = get(key);
        if (value == null) {
            return fallback;
        }

        return value;
    }

    public int size() {
        return size;
    }

    public void clear() {
        items = new Node[16];
        buckets = 16;
        size = 0;
    }

    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new LinkedListSet<>();

        for (Node<K, V> head : items) {
            Node<K, V> current = head;
            while (current != null) {
                entries.add(current);
                current = current.next;
            }
        }
        return entries;
    }

    public Set<K> keySet(){
        Set<K> keys = new LinkedListSet<>();

        for (Node<K, V> head : items) {
            Node<K, V> current = head;
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }

        return keys;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
