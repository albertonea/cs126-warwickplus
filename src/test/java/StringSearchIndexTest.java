import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.data.StringSearchIndex;

import static org.junit.jupiter.api.Assertions.*;

public class StringSearchIndexTest {
    StringSearchIndex<Integer> searchIndex;

    @BeforeEach
    void setup() {
        this.searchIndex = new StringSearchIndex<>();
    }

    @Test
    void searchInEmptyIndexReturnsEmptyList() {
        assertTrue(searchIndex.search("test").isEmpty());
    }

    @Test
    void searchInIndexWithSingleItemReturnsItem() {
        searchIndex.add(1, "testing 123");
        searchIndex.add(2, "tes 123");
        searchIndex.add(3, "test 123");
        assertArrayEquals(new Object[]{1, 3}, searchIndex.search("test").toArray(int.class));
    }
}
