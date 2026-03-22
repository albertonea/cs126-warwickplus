package structures;

import java.util.*;
import java.util.HashMap;
import java.util.stream.*;

public class StringSearchIndex<E> {

    private static final int N = 3; // trigram size

    private final Map<E, String> searchTerms = new HashMap<>();
    private final Map<String, Set<E>> idx = new HashMap<>();

    /** Add a movie to the index. */
    public void add(E element, String searchTerm) {
        searchTerms.put(element, searchTerm);
        for (String gram : ngrams(searchTerm)) {
            idx.computeIfAbsent(gram, k -> new HashSet<>()).add(element);
        }
    }

    /** Return all movie IDs whose title or description contains the query as a substring. */
    public List<E> search(String query) {
        // Short queries can't form a full trigram — fall back to linear scan
        if (query.length() < N) {
            return linearSearch(query);
        }

        // 1. Intersect candidate sets across every trigram in the query
        Set<E> candidates = null;
        for (String gram : ngrams(query)) {
            Set<E> bucket = idx.getOrDefault(gram, Collections.emptySet());
            if (candidates == null) candidates = new HashSet<>(bucket);
            else                    candidates.retainAll(bucket);
            if (candidates.isEmpty()) return Collections.emptyList(); // early exit
        }

        if (candidates == null) return Collections.emptyList();

        // 2. Verify surviving candidates (eliminates any false positives)
        return candidates.stream()
                .filter(id -> matches(searchTerms.get(id), query))
                .collect(Collectors.toList());
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private boolean matches(String item, String query) {
        return item.contains(query);
    }

    private List<String> ngrams(String text) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= text.length() - N; i++) {
            result.add(text.substring(i, i + N));
        }
        return result;
    }

    private List<E> linearSearch(String query) {
        return searchTerms.entrySet().stream()
                .filter(m -> matches(m.getValue(), query))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
