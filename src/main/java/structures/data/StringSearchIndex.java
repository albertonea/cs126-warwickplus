package structures.data;

import structures.data.interfaces.List;
import structures.data.interfaces.Map;
import structures.data.interfaces.Set;

public class StringSearchIndex<E> {
    private static final int N = 3;

    private final Map<E, String> searchTerms = new HashMap<>();
    private final Map<String, Set<E>> idx = new HashMap<>();

    public void add(E element, String searchTerm) {
        searchTerms.put(element, searchTerm);
        for (String gram : ngrams(searchTerm)) {
            Set<E> set = idx.get(gram);
            if (set != null) {
                set.add(element);
                continue;
            }
            Set<E> newSet = new HashSet<>();
            newSet.add(element);
            idx.put(gram, newSet);
        }
    }

    public List<E> search(String query) {
        if (query.length() < N) {
            return linearSearch(query);
        }

        Set<E> candidates = null;
        for (String gram : ngrams(query)) {
            Set<E> bucket = idx.getOrDefault(gram, new HashSet<>());
            if (candidates == null)
                candidates = new HashSet<>(bucket);

            else candidates.retainAll(bucket);

            if (candidates.isEmpty()) return new LinkedList<>();
        }

        if (candidates == null) return new LinkedList<>();

        List<E> checked = new LinkedList<>();
        for (E candidate: candidates) {
            if (matches(searchTerms.get(candidate), query)) {
                checked.add(candidate);
            }
        }
        return checked;
    }

    public boolean remove(E element) {
        String searchTerm = searchTerms.remove(element);
        if (searchTerm == null)
            return false;

        for (String gram : ngrams(searchTerm)) {
            Set<E> set = idx.get(gram);
            if (set != null) {
                set.remove(element);
                if (set.isEmpty())
                    idx.remove(gram);
            }
        }
        return true;
    }

    private boolean matches(String item, String query) {
        return item.contains(query);
    }

    private List<String> ngrams(String text) {
        List<String> result = new LinkedList<>();
        for (int i = 0; i <= text.length() - N; i++) {
            result.add(text.substring(i, i + N));
        }
        return result;
    }

    private List<E> linearSearch(String query) {
        List<E> keys = new LinkedList<>();
        for (Map.Entry<E, String> entry : searchTerms.entrySet()) {
            if (matches(entry.getValue(), query)) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}
