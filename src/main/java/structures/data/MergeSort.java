package structures.data;

import java.util.Comparator;

/**
 * Static utility class providing a sort implementation.
 * Merge sort that switches to insertion sort on small partitions
 */
public final class MergeSort {

    // Below this length insertion sort is faster than merge sort
    private static final int INSERTION_SORT_THRESHOLD = 50;

    /**
     * Hidden constructor: the class is a static utility holder
     */
    private MergeSort() {}

    /**
     * Sorts the entire array in-place
     *
     * @param arr the array to sort
     * @param comparator the ordering to use
     * @param <E> the element type
     */
    public static <E> void sort(E[] arr, Comparator<E> comparator) {
        if (arr.length <= 1) return;
        if (arr.length <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, 0, arr.length, comparator);
        } else {
            // Allocate the auxiliary buffer once; it is reused for every level
            E[] aux = arr.clone();
            mergesort(aux, arr, 0, arr.length, comparator);
        }
    }

    /**
     * Recursive merge-sort core. The {@code tmp} and {@code arr} buffers
     * swap roles at each level so we merge from the fresh buffer into the
     * stale one without an extra copy
     *
     * @param tmp source buffer at this level
     * @param arr destination buffer at this level
     * @param from inclusive lower bound
     * @param to exclusive upper bound
     * @param cmp the ordering to use
     * @param <E> the element type
     */
    private static <E> void mergesort(E[] tmp, E[] arr, int from, int to, Comparator<E> cmp) {
        // Base case when remaining size <= 1
        if (to - from < 2) return;

        int mid = from + (to - from) / 2;

        // Sort each half writing the result into tmp
        mergesort(arr, tmp, from, mid, cmp);
        mergesort(arr, tmp, mid, to, cmp);

        // Already-sorted shortcut: if tmp[mid-1] <= tmp[mid] just copy across
        if (cmp.compare(tmp[mid - 1], tmp[mid]) <= 0) {
            System.arraycopy(tmp, from, arr, from, to - from);
            return;
        }

        // Merge two halves together
        merge(tmp, arr, from, mid, to, cmp);
    }

    /**
     * Merges the two adjacent sorted arrays from {@code tmp} into {@code arr}
     *
     * @param tmp source buffer with the two sorted halves
     * @param arr destination buffer
     * @param from inclusive start of the left half
     * @param mid exclusive end of the left half
     * @param to exclusive end of the right half
     * @param cmp the ordering to use
     * @param <E> the element type
     */
    private static <E> void merge(E[] tmp, E[] arr, int from, int mid, int to, Comparator<E> cmp) {
        int i = from;
        int j = mid;
        for (int k = from; k < to; k++) {
            if (i >= mid) {
                // Left half exhausted: take from the right
                arr[k] = tmp[j++];
            } else if (j >= to) {
                // Right half exhausted: take from the left
                arr[k] = tmp[i++];
            } else if (cmp.compare(tmp[i], tmp[j]) <= 0) {
                // Left head wins (<= preserves stability)
                arr[k] = tmp[i++];
            } else {
                arr[k] = tmp[j++];
            }
        }
    }

    /**
     * In-place insertion sort over {@code arr[from..to)}
     *
     * @param arr the array being sorted
     * @param from inclusive lower bound
     * @param to exclusive upper bound
     * @param cmp the ordering to use
     * @param <E> the element type
     */
    private static <E> void insertionSort(E[] arr, int from, int to, Comparator<E> cmp) {
        for (int i = from + 1; i < to; i++) {
            E key = arr[i];
            int j = i - 1;

            // Slide elements greater than the key one slot right
            while (j >= from && cmp.compare(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }
}
