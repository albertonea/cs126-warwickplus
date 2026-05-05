package structures.data;

import java.util.Comparator;

public final class MergeSort {

    private static final int INSERTION_SORT_THRESHOLD = 48;

    private MergeSort() {}

    public static <E> void sort(E[] arr, Comparator<E> comparator) {
        if (arr.length <= 1) return;
        if (arr.length <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, 0, arr.length, comparator);
        } else {
            E[] aux = arr.clone();
            mergesort(aux, arr, 0, arr.length, comparator);
        }
    }

    public static <E> void sort(E[] arr, int from, int to, Comparator<E> comparator) {
        if (to - from <= 1) return;
        else if (to - from <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, from, to, comparator);
        } else {
            E[] tmp = arr.clone();
            mergesort(tmp, arr, from, to, comparator);
        }
    }

    private static <E> void mergesort(E[] tmp, E[] arr, int from, int to, Comparator<E> cmp) {
        if (to - from < 2) return;

        int mid = from + (to - from) / 2;

        mergesort(arr, tmp, from, mid, cmp);
        mergesort(arr, tmp, mid, to, cmp);

        if (cmp.compare(tmp[mid - 1], tmp[mid]) <= 0) {
            System.arraycopy(tmp, from, arr, from, to - from);
            return;
        }

        merge(tmp, arr, from, mid, to, cmp);
    }

    private static <E> void merge(E[] tmp, E[] arr, int from, int mid, int to, Comparator<E> cmp) {
        int i = from;
        int j = mid;
        for (int k = from; k < to; k++) {
            if (i >= mid) {
                arr[k] = tmp[j++];
            } else if (j >= to) {
                arr[k] = tmp[i++];
            } else if (cmp.compare(tmp[i], tmp[j]) <= 0) {
                arr[k] = tmp[i++];
            } else {
                arr[k] = tmp[j++];
            }
        }
    }

    private static <E> void insertionSort(E[] arr, int from, int to, Comparator<E> cmp) {
        for (int i = from + 1; i < to; i++) {
            E key = arr[i];
            int j = i - 1;

            while (j >= from && cmp.compare(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }
}
