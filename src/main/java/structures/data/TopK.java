package structures.data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class TopK<E> {
    private final Comparator<E> comparator;

    public TopK(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public E[] topK(E[] arr, int k) {
        if (k <= 0 || arr.length == 0) return Arrays.copyOf(arr, 0);

        k = Math.min(k, arr.length);
        E[] copy = Arrays.copyOf(arr, arr.length);

        if (k >= arr.length || arr.length < 3) {
            MergeSort.sort(copy, comparator);
            return Arrays.copyOfRange(copy, 0, k);
        }

        quickSortToTopK(copy, 0, copy.length - 1, k - 1);
        return Arrays.copyOfRange(copy, 0, k);
    }

    public <R> R[] topK(E[] arr, int k, Function<E, R> mapper, IntFunction<R[]> arrayFactory) {
        E[] sorted = topK(arr, k);
        R[] result = arrayFactory.apply(sorted.length);

        for (int i = 0; i < sorted.length; i++) {
            result[i] = mapper.apply(sorted[i]);
        }

        return result;
    }

    public int[] topKInt(E[] arr, int k, ToIntFunction<E> mapper) {
        E[] sorted = topK(arr, k);
        int[] result = new int[sorted.length];

        for (int i = 0; i < sorted.length; i++) {
            result[i] = mapper.applyAsInt(sorted[i]);
        }

        return result;
    }

    private void quickSortToTopK(E[] arr, int from, int to, int targetIdx) {
        if (from < to) {
            int pivotIdx = partition(arr, from, to);

            if (pivotIdx >= targetIdx) {
                quickSortToTopK(arr, from, pivotIdx - 1, targetIdx);
            }
            else {
                quickSortToTopK(arr, from, pivotIdx - 1, targetIdx);
                quickSortToTopK(arr, pivotIdx + 1, to, targetIdx);
            }
        }
    }

    private int partition(E[] arr, int left, int right) {
        int mid = left + (right - left) / 2;
        if (comparator.compare(arr[left], arr[mid]) > 0) swap(arr, left, mid);
        if (comparator.compare(arr[left], arr[right]) > 0) swap(arr, left, right);
        if (comparator.compare(arr[mid], arr[right]) > 0) swap(arr, mid, right);
        E pivot = arr[mid];
        swap(arr, mid, right - 1);
        int i = left, j = right - 1;
        while (true) {
            while (comparator.compare(arr[++i], pivot) < 0);
            while (comparator.compare(arr[--j], pivot) > 0);
            if (i >= j) break;
            swap(arr, i, j);
        }
        swap(arr, i, right - 1);
        return i;
    }

    private void swap(E[] arr, int i, int j) {
        E tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }



}
