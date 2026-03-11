import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.SetSkipList;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SetSkipListTest {
    SetSkipList<LocalDate, Integer> list;

    @BeforeEach
    void setup() {
        list = new SetSkipList<>();
    }

    @Test void searchingInEmptySkipListReturnsNull() {
        assertNull(list.get(LocalDate.MAX));
    }

    @Test void searchingSingleItemInSkipListReturnsItem() {
        LocalDate date = LocalDate.of(2007, 3, 11);
        list.put(date, 1);
        assertArrayEquals(new Object[]{1}, list.get(date));
    }

    @Test void searchingMultipleItemsInSkipListReturnsItems() {
        LocalDate date = LocalDate.of(2007, 3, 11);
        LocalDate date1 = LocalDate.of(2001, 9, 11);
        LocalDate date2 = LocalDate.of(1969, 6, 9);
        list.put(date, 1);
        list.put(date, 2);
        list.put(date1, 2);
        list.put(date2, 3);
        assertArrayEquals(new Object[]{2, 1}, list.get(date));
        assertArrayEquals(new Object[]{2}, list.get(date1));
        assertArrayEquals(new Object[]{3}, list.get(date2));
    }

    @Test void removingItemInSkipListAndGettingReturnsNull() {
        LocalDate date = LocalDate.of(2007, 3, 11);
        list.put(date, 1);
        list.remove(date, 1);
        assertNull(list.get(date));
    }

    @Test void removingSingleItemSingleItemSkipListAndGettingReturnsSingleItem() {
        LocalDate date = LocalDate.of(2007, 3, 11);
        list.put(date, 1);
        list.put(date, 2);
        list.remove(date, 1);
        assertArrayEquals(new Object[]{2}, list.get(date));
    }

    @Test void removingSingleItemInThreeItemSkipListAndGettingReturnsSingleItem() {
        LocalDate date = LocalDate.of(2007, 3, 11);
        LocalDate date1 = LocalDate.of(2001, 9, 11);
        LocalDate date2 = LocalDate.of(1969, 6, 9);
        list.put(date, 1);
        list.put(date, 2);
        list.put(date1, 2);
        list.put(date2, 3);
        list.remove(date1, 2);
        assertArrayEquals(new Object[]{2, 1}, list.get(date));
        assertArrayEquals(new Object[]{3}, list.get(date2));
        assertNull(list.get(date1));
    }

    @Test void getElementsInBackwardsRangeReturnsNull() {
        LocalDate date = LocalDate.of(2007, 3, 11);
        LocalDate date1 = LocalDate.of(2002, 9, 11);
        LocalDate date2 = LocalDate.of(2001, 9, 11);
        LocalDate date3 = LocalDate.of(1969, 6, 9);
        list.put(date, 1);
        list.put(date, 2);
        list.put(date1, 3);
        list.put(date2, 4);
        list.put(date3, 5);
        assertNull(list.getRange(date1, date2));
    }

    @Test void getElementsInRangeReturnsElementsInRange() {
        LocalDate date = LocalDate.of(2007, 3, 11);
        LocalDate date1 = LocalDate.of(2002, 9, 11);
        LocalDate date2 = LocalDate.of(2001, 9, 11);
        LocalDate date3 = LocalDate.of(1969, 6, 9);
        list.put(date, 1);
        list.put(date, 2);
        list.put(date1, 3);
        list.put(date2, 4);
        list.put(date3, 5);
        assertArrayEquals(new Object[]{4, 3}, list.getRange(date2, date1).toArray());
    }
}
