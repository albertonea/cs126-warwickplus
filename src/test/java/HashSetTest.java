import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.data.HashSet;
import structures.data.interfaces.Set;

import static org.junit.jupiter.api.Assertions.*;

public class HashSetTest {
    Set<Integer> hashSet;

    @BeforeEach
    void init() {
        hashSet = new HashSet<>();
    }

    @Test
    void givenEmptyHashSet_sizeIsZero() {
        assertEquals(0, hashSet.size());
    }

    @Test
    void givenHashSetWithOneElement_sizeIsOne() {
        hashSet.add(1);
        assertEquals(1, hashSet.size());
    }

    @Test
    void givenHashSetWithTwoElementsAndRemovingOne_oneElementRemains() {
        hashSet.add(1);
        hashSet.add(2);

        hashSet.remove(2);
        assertEquals(1, hashSet.size());
        assertFalse(hashSet.contains(2));
        assertTrue(hashSet.contains(1));
    }

    @Test
    void givenHashSetWithElements_clearRemovesEverything() {
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(3);

        hashSet.clear();
        assertTrue(hashSet.isEmpty());
    }

    @Test
    void givenTwoHashSetsWithTwoMutualElements_retainAllReturnsAHashSetWithTheMutualElements() {
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(3);
        hashSet.add(4);
        hashSet.add(5);

        Set<Integer> hashSet1 = new HashSet<>();
        hashSet1.add(4);
        hashSet1.add(5);
        hashSet1.add(6);
        hashSet1.add(7);
        hashSet1.add(8);

        hashSet.retainAll(hashSet1);
        assertEquals(2, hashSet.size());
        assertTrue(hashSet.contains(4));
        assertTrue(hashSet.contains(5));
    }
}
