import org.junit.jupiter.api.Test;
import structures.data.MergeSort;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MergeSortTest {
    @Test
    void arraySortedAscending() {
        Integer[] arr = new Integer[]{38, 27, 43, 10, 100, 350, 700, 900};

        MergeSort.sort(arr, 0, arr.length, Comparator.comparingInt(a -> a));
        for (int i:arr) {
            System.out.println(i);
        }
        assertArrayEquals(new Integer[]{10, 27, 38, 43, 100, 350, 700, 900}, arr);
    }
}
