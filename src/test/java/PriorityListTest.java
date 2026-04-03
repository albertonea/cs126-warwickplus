import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.data.PriorityQueue;
import structures.data.interfaces.Queue;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class PriorityListTest {
    Queue<Integer> queue;

    @BeforeEach
    void setup() {
        queue = new PriorityQueue<>(Comparator.comparingInt(a -> a));
    }

    @Test
    void givenEmptyQueue_addingNullInsertsNothing() {
        assertFalse(queue.add(null));
        assertEquals(0, queue.size());
    }

    @Test
    void givenAddingASingleElement_queueHasSizeOne() {
        assertTrue(queue.add(1));
        assertEquals(1, queue.size());
//        assertTrue(queue.contains(1));
    }

    @Test
    void givenAddingASingleElement_removeRemovesThatElement() {
        assertTrue(queue.add(1));
        assertEquals(1, queue.remove());
        assertEquals(0, queue.size());
    }

    @Test
    void givenAddingMultipleElements_queuePreservesOrderWithComparator() {
        queue.add(5);
        queue.add(4);
        queue.add(3);
        queue.add(2);
        queue.add(1);
        assertEquals(1, queue.remove());
        assertEquals(2, queue.remove());
        assertEquals(3, queue.remove());
        assertEquals(4, queue.remove());
        assertEquals(5, queue.remove());
    }

    @Test
    void givenQueueWithElements_removingMoreThanThereAreThrows() {
        queue.add(5);
        queue.add(4);
        queue.add(3);
        queue.remove();
        queue.remove();
        queue.remove();
        assertThrows(IndexOutOfBoundsException.class, () -> queue.remove());
    }
}
