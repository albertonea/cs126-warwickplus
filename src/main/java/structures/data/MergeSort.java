package structures.data;

import java.util.Comparator;

public final class MergeSort {

    private static final int INSERTION_SORT_THRESHOLD = 16;

    private MergeSort() {}

    public static <E> void sort(E[] arr, Comparator<E> comparator) {
        if (arr.length <= 1) return;
        E[] aux = arr.clone();
        mergesort(aux, arr, 0, arr.length, comparator);
    }

    public static <E> void sort(E[] arr, int from, int to, Comparator<E> comparator) {
        if (to - from <= 1) return;
        E[] aux = arr.clone();
        mergesort(aux, arr, from, to, comparator);
    }

    private static <E> void mergesort(E[] src, E[] dst, int lo, int hi, Comparator<E> cmp) {
        if (hi - lo <= INSERTION_SORT_THRESHOLD) {
            insertionSort(dst, lo, hi, cmp);
            return;
        }

        int mid = lo + (hi - lo) / 2;

        // Recursively sort into src (note the swap: dst becomes src's source)
        mergesort(dst, src, lo, mid, cmp);
        mergesort(dst, src, mid, hi, cmp);

        // If already ordered, just copy
        if (cmp.compare(src[mid - 1], src[mid]) <= 0) {
            System.arraycopy(src, lo, dst, lo, hi - lo);
            return;
        }

        merge(src, dst, lo, mid, hi, cmp);
    }

    private static <E> void merge(E[] src, E[] dst, int lo, int mid, int hi, Comparator<E> cmp) {
        int i = lo;
        int j = mid;
        for (int k = lo; k < hi; k++) {
            if (i >= mid) {
                dst[k] = src[j++];
            } else if (j >= hi) {
                dst[k] = src[i++];
            } else if (cmp.compare(src[i], src[j]) <= 0) {
                dst[k] = src[i++];
            } else {
                dst[k] = src[j++];
            }
        }
    }

    private static <E> void insertionSort(E[] arr, int lo, int hi, Comparator<E> cmp) {
        for (int i = lo + 1; i < hi; i++) {
            E key = arr[i];
            int j = i - 1;
            while (j >= lo && cmp.compare(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
}
