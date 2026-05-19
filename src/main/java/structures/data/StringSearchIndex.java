package structures.data;

import structures.data.interfaces.List;
import structures.data.interfaces.Map;
import structures.data.interfaces.Set;

/**
 * Index over string n-grams. Each indexed string is split into
 * its overlapping length-N substrings and each gram is mapped to the set of
 * elements whose search text contains it
 *
 * @param <E> the type of element being indexed
 */
public class StringSearchIndex<E> {
    // Gram length
    private static final int N = 3;

    // Store entire string each element was added with
    private final Map<E, String> searchTerms = new HashMap<>();
    // Map from each trigram to the elements that contain it
    private final Map<String, Set<E>> trigramIndex = new HashMap<>();

    /**
     * Registers an element under the supplied search text
     *
     * @param element the element to index
     * @param searchTerm the haystack text used for future searches
     */
    public void add(E element, String searchTerm) {
        searchTerms.put(element, searchTerm);
        // Split search term to n-grams
        for (String gram : ngrams(searchTerm)) {
            Set<E> set = trigramIndex.get(gram);
            // If n-gram index exists append it
            if (set != null) {
                set.add(element);
                continue;
            }
            // Otherwise allocate a fresh set
            Set<E> newSet = new HashSet<>();
            newSet.add(element);
            trigramIndex.put(gram, newSet);
        }
    }

    /**
     * Returns every indexed element whose search text contains {@code query}
     * as a substring
     *
     * @param query the substring to look for
     * @return the matching elements
     */
    public List<E> search(String query) {
        // Queries shorter than N can't use the trigram index
        if (query.length() < N) {
            // Revert to linear search
            return linearSearch(query);
        }

        // Build the intersection of every n-gram set
        Set<E> candidates = null;
        for (String gram : ngrams(query)) {
            Set<E> bucket = trigramIndex.getOrDefault(gram, new HashSet<>());

            if (candidates == null) {
                // First gram, seed the candidate set with a copy of its bucket
                candidates = new HashSet<>(bucket);
            }

            // Keep only elements that also appear in this set
            else candidates.retainAll(bucket);

            // Empty intersection can't grow back
            if (candidates.isEmpty()) return new LinkedList<>();
        }

        if (candidates == null) return new LinkedList<>();

        // Verify each candidate with an exact substring check
        List<E> checked = new LinkedList<>();
        for (E candidate: candidates) {
            if (searchTerms.get(candidate).contains(query)) {
                checked.add(candidate);
            }
        }
        return checked;
    }

    /**
     * Removes an element from the index
     *
     * @param element the element to remove
     * @return {@code true} if the element was previously indexed
     */
    public boolean remove(E element) {
        // Get complete search term stored for element
        String searchTerm = searchTerms.remove(element);
        // Check if it exists
        if (searchTerm == null)
            return false;

        // Get the grams of the original search term to find every set the element is in
        for (String gram : ngrams(searchTerm)) {
            Set<E> set = trigramIndex.get(gram);
            if (set != null) {
                set.remove(element);
                // Delete the set if it is now empty
                if (set.isEmpty())
                    trigramIndex.remove(gram);
            }
        }
        return true;
    }

    /**
     * Splits {@code text} into its overlapping length-N substrings
     *
     * @param text the source string
     * @return every length-N substring of {@code text}
     */
    private List<String> ngrams(String text) {
        List<String> result = new LinkedList<>();
        if (text.length() < N) {
            return result;
        }
        for (int i = 0; i <= text.length() - N; i++) {
            result.add(text.substring(i, i + N));
        }
        return result;
    }

    /**
     * Linear search over the complete search terms
     *
     * @param query the substring to look for
     * @return the matching elements
     */
    private List<E> linearSearch(String query) {
        List<E> keys = new LinkedList<>();
        for (Map.Entry<E, String> entry : searchTerms.entryList()) {
            if (entry.getValue().contains(query)) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}
