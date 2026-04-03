package structures.data.interfaces;

public interface Map<K, V> {
    interface Entry<K, V> {
        K getKey();
        V getValue();
        int hashCode();
    }

    public V put(K key, V value);
    public V get(K key);
    public V remove(K key);
    public V getOrDefault(K key, V fallback);
    public int size();
    public void clear();
    public Set<Entry<K, V>> entrySet();
    public Set<K> keySet();
    public boolean containsKey(K key);

}
