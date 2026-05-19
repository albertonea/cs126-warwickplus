package structures.data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

/**
 * Returns the {@code k} smallest elements of an array under the supplied
 * comparator. Uses a partial quicksort that only recurses into the side
 * less than the target index
 *
 * @param <E> the type of element being ranked
 */
public class TopK<E> {
    // Comparator that defines the ranking
    private final Comparator<E> comparator;

    /**
     * Creates a ranker with the supplied comparator
     *
     * @param comparator the ranking order (smallest first)
     */
    public TopK(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns the {@code k} smallest elements of {@code arr} in ranked order
     *
     * @param arr the source array
     * @param k the number of elements to extract
     * @return an array of length {@code min(k, arr.length)}
     */
    public E[] topK(E[] arr, int k) {
        // Check for array length 0
        if (k <= 0 || arr.length == 0) return Arrays.copyOf(arr, 0);

        // Clamp k to the array length
        k = Math.min(k, arr.length);

        // Cases where a partial sort costs more than a full sort
        if (k >= arr.length || arr.length < 3) {
            E[] copy = arr.clone();
            MergeSort.sort(copy, comparator);
            return Arrays.copyOfRange(copy, 0, k);
        }

        // Partial quicksort; only the first k positions end up sorted
        E[] copy = Arrays.copyOf(arr, arr.length);
        quickSortToTopK(copy, 0, copy.length - 1, k - 1);
        return Arrays.copyOfRange(copy, 0, k);
    }

    /**
     * Top-k followed by a mapping projection
     *
     * @param arr the source array
     * @param k the number of elements to extract
     * @param mapper the projection applied to each kept element
     * @param arrayFactory factory that allocates the result array
     * @param <R> the projected element type
     * @return the mapped top-k array
     */
    public <R> R[] topK(E[] arr, int k, Function<E, R> mapper, IntFunction<R[]> arrayFactory) {
        // Get the TopK on the original array
        E[] sorted = topK(arr, k);

        // Map the sorted array to new data type using the mapper function
        R[] result = arrayFactory.apply(sorted.length);

        for (int i = 0; i < sorted.length; i++) {
            result[i] = mapper.apply(sorted[i]);
        }

        return result;
    }

    /**
     * Top-k projected to primitive ints
     *
     * @param arr the source array
     * @param k the number of elements to extract
     * @param mapper projection from element to int
     * @return the mapped top-k integers
     */
    public int[] topKInt(E[] arr, int k, ToIntFunction<E> mapper) {
        // Get the TopK on the original array
        E[] sorted = topK(arr, k);

        // Map the sorted array to primitive int[] to avoid boxing
        int[] result = new int[sorted.length];

        for (int i = 0; i < sorted.length; i++) {
            result[i] = mapper.applyAsInt(sorted[i]);
        }

        return result;
    }

    /**
     * Partial quicksort that only recurses into partitions overlapping the
     * target prefix {@code [0, targetIdx]}
     *
     * @param arr the working buffer
     * @param from inclusive low index of the current partition
     * @param to inclusive high index of the current partition
     * @param targetIdx the largest index that needs to end up sorted
     */
    private void quickSortToTopK(E[] arr, int from, int to, int targetIdx) {
        if (from < to) {
            int pivotIdx = partition(arr, from, to);

            if (pivotIdx >= targetIdx) {
                // Pivot greater than target index, only the left half matters
                quickSortToTopK(arr, from, pivotIdx - 1, targetIdx);
            }
            else {
                // Pivot less than target index, both sides still matter
                quickSortToTopK(arr, from, pivotIdx - 1, targetIdx);
                quickSortToTopK(arr, pivotIdx + 1, to, targetIdx);
            }
        }
    }

    /**
     * Partitions the array around a pivot chosen by median-of-three
     *
     * @param arr the working buffer
     * @param left inclusive low index of the partition
     * @param right inclusive high index of the partition
     * @return the final index of the pivot
     */
    private int partition(E[] arr, int left, int right) {
        // Median-of-three pivot, sort arr[left], arr[mid], arr[right] in place
        int mid = left + (right - left) / 2;
        if (comparator.compare(arr[left], arr[mid]) > 0) swap(arr, left, mid);
        if (comparator.compare(arr[left], arr[right]) > 0) swap(arr, left, right);
        if (comparator.compare(arr[mid], arr[right]) > 0) swap(arr, mid, right);
        E pivot = arr[mid];

        // Hoare partitioning algorithm to minimise swaps made
        swap(arr, mid, right - 1);
        int i = left, j = right - 1;
        while (true) {
            // Outer guards make these scans safe without bounds checks
            while (comparator.compare(arr[++i], pivot) < 0);
            while (comparator.compare(arr[--j], pivot) > 0);
            if (i >= j) break;
            swap(arr, i, j);
        }
        // Restore the pivot to its final position
        swap(arr, i, right - 1);
        return i;
    }

    /**
     * Swaps two elements of the given array in place
     *
     * @param arr the array
     * @param i first index
     * @param j second index
     */
    private void swap(E[] arr, int i, int j) {
        E tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
