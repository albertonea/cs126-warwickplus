package structures;

import java.util.Objects;

public class HashMap<K, V> {
    class Node<K, V> {
        public final int hash;
        public final K key;
        V value;
        Node next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
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
        this.buckets = nextPowerOfTwo(initialSize);
        this.items = new Node[buckets];
    }

    public HashMap(int initialSize, float loadFactor) {
        this.buckets = nextPowerOfTwo(initialSize);
        this.items = new Node[buckets];
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

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
