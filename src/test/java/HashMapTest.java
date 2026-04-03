import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.data.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HashMapTest {
    HashMap<String, String> hashMap;

    @BeforeEach
    void setup() {
        hashMap = new HashMap<>(8);
    }


    @Test
    void addingAnItemInEmptyHashMapReturnsNull() {
        String value = hashMap.put("hello", "there");
        assertNull(value);
    }

    @Test
    void addingTwoItemsInSameBucketRetunsNullThenPrevItem() {
        String value1 = hashMap.put("hello", "there");
        String value2 = hashMap.put("hello", "world");

        assertNull(value1);
        assertEquals("there", value2);
    }

    @Test
    void addingItemThenDeletingReturnsElementAndSubsequentGetDoesNothing() {
        String key = "hello";
        hashMap.put(key, "there");
        String value1 = hashMap.remove(key);

        assertEquals("there", value1);
    }

    @Test
    void addingAndGettingReturnsItem() {
        hashMap.put("hello", "there");
        String value = hashMap.get("hello");

        assertEquals("there", value);
    }

    @Test
    void addingTwoItemsAndGettingBothReturnsBoth() {
        hashMap.put("hello", "there");
        hashMap.put("welcome", "world");

        String value1 = hashMap.get("hello");
        String value2 = hashMap.get("welcome");

        assertEquals("there", value1);
        assertEquals("world", value2);
    }

    class Item {
        int id;

        public Item(int id) {
            this.id = id;
        }

        public int hashCode() {
            return id;
        }
    }

    @Test
    void gettingAnEntryWithANewInstanceOfAKeyThatProducesTheSameHashReturnsNull() {
        HashMap<Item, String> hashMap1 = new HashMap<>();

        hashMap1.put(new Item(10), "hello");
        hashMap1.put(new Item(10), "there");

        String value = hashMap1.get(new Item(10));
        assertNull(value);
    }

    @Test
    void gettingAnEntryWithSameInstanceOfAKeyReturnsElement() {
        HashMap<Item, String> hashMap1 = new HashMap<>();
        var item = new Item(10);
        hashMap1.put(new Item(10), "hello");
        hashMap1.put(item, "there");

        String value = hashMap1.get(item);
        assertEquals("there", value);

    }

    @Test
    void addingMoreItemsToTriggerAResizeKeepsFunctionality() {
        hashMap.put("why", "are");
        hashMap.put("are", "you");
        hashMap.put("you", "running");
        hashMap.put("running", "you");
        hashMap.put("you", "cant");
        hashMap.put("cant", "see");
        hashMap.put("see", "me");
        hashMap.put("are", "you");
        hashMap.put("you", "blind");
        hashMap.put("anakin", "i");
        hashMap.put("i", "have");
        hashMap.put("have", "the");
        hashMap.put("the", "highground");
        hashMap.put("highground", "it");
        hashMap.put("it", "is");
        hashMap.put("is", "over");

        assertEquals("blind", hashMap.get("you"));
        assertEquals("you", hashMap.get("are"));
        assertEquals("blind", hashMap.get("you"));
        assertEquals("i", hashMap.get("anakin"));
    }


}
