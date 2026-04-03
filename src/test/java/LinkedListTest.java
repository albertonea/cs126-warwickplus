import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.data.LinkedList;
import structures.data.interfaces.List;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTest {
    List<Integer> linkedList;

    @BeforeEach
    void init() {
         this.linkedList = new LinkedList<>();
    }

    @Test
    void givenAddingNull_sizeIsOne() {
        linkedList.add(null);
        assertTrue(linkedList.contains(null));
        assertEquals(1, linkedList.size());
    }

    @Test
    void givenSequentiallyAddingElements_containsAllElements() {
        linkedList.add(1);
        assertTrue(linkedList.contains(1));

        linkedList.add(2);
        assertTrue(linkedList.contains(2));

        linkedList.add(3);
        assertTrue(linkedList.contains(3));
        assertEquals(3, linkedList.size());
    }

    @Test
    void givenEmptyList_getIndexThrowsIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            linkedList.get(0);
        });
    }

    @Test
    void givenEmptyList_setIndexThrowsIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            linkedList.set(0, 1);
        });
    }

    @Test
    void givenSingleElementList_getSecondIndexThrowsIndexOutOfBounds() {
        linkedList.add(0);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            linkedList.get(1);
        });
    }

    @Test
    void givenSingleElementList_setSecondIndexThrowsIndexOutOfBounds() {
        linkedList.add(0);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            linkedList.set(1, 1);
        });
    }

    @Test
    void givenSequentialAddingOfElments_getIndexReturnsInOrder() {
        linkedList.add(0);
        linkedList.add(1);
        linkedList.add(2);
        assertEquals(2, linkedList.get(2));
        assertEquals(0, linkedList.get(0));
        assertEquals(1, linkedList.get(1));
    }

    @Test
    void givenThreeElementList_setIndexChangesValuesAndMaintainsIndex() {
        linkedList.add(0);
        linkedList.add(1);
        linkedList.add(2);

        linkedList.set(0, 1);
        linkedList.set(1, 2);
        linkedList.set(2, 3);

        assertEquals(1, linkedList.get(0));
        assertEquals(2, linkedList.get(1));
        assertEquals(3, linkedList.get(2));
    }

    @Test
    void givenThreeElementList_setIndexReturnsReplacedValues() {
        linkedList.add(0);
        linkedList.add(null);
        linkedList.add(2);

        assertEquals(0, linkedList.set(0, 1));
        assertEquals(null, linkedList.set(1, 1));
        assertEquals(2, linkedList.set(2, 3));
    }

    @Test
    void givenThreeElementList_removingFirstElementKeepsRemainingTwoInOrder() {
        linkedList.add(0);
        linkedList.add(1);
        linkedList.add(2);

        linkedList.remove(0);

        assertFalse(linkedList.contains(0));

        assertEquals(1, linkedList.get(0));
        assertEquals(2, linkedList.get(1));

        assertEquals(0, linkedList.indexOf(1));
        assertEquals(1, linkedList.indexOf(2));
    }

    @Test
    void givenTwoThreeElementLists_addAllOnListCreatesASixElementList() {
        linkedList.add(0);
        linkedList.add(1);
        linkedList.add(2);
        List<Integer> listToAdd = new LinkedList<>();
        listToAdd.add(3);
        listToAdd.add(4);
        listToAdd.add(5);

        linkedList.addAll(listToAdd);
        assertEquals(6, linkedList.size());
        assertTrue(linkedList.contains(0));
        assertTrue(linkedList.contains(1));
        assertTrue(linkedList.contains(2));
        assertTrue(linkedList.contains(3));
        assertTrue(linkedList.contains(4));
        assertTrue(linkedList.contains(5));

        linkedList.remove(4);
        assertEquals(5, linkedList.size());
        assertFalse(linkedList.contains(4));
    }
}
