import stores.*;

import java.time.LocalDateTime;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatingsTest {
    private Stores store = new Stores();

    @BeforeEach
    void setup() {
        store = new Stores();
    }

    boolean checkContentsOfArray(float[] test, float[] result) {
        boolean finalFlag = true;
        if (test.length != result.length) {
            return false;
        }
        for (int i = 0; i < test.length; i++) {
            boolean innerFlag = false;
            for (int j = 0; j < result.length; j++) {
                if (test[i] == result[j]) {
                    innerFlag = true;
                    break;
                }
            }
            finalFlag &= innerFlag;
        }
        return finalFlag;
    }

    boolean checkContentsOfArray(int[] test, int[] result) {
        boolean finalFlag = true;
        if (test.length != result.length) {
            return false;
        }
        for (int i = 0; i < test.length; i++) {
            boolean innerFlag = false;
            for (int j = 0; j < result.length; j++) {
                if (test[i] == result[j]) {
                    innerFlag = true;
                    break;
                }
            }
            finalFlag &= innerFlag;
        }
        return finalFlag;
    }

    @Test void testRatingsAddDefault() {
        assertTrue(store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0)), "Data should be able to be added when the store is empty");
    }

    @Test void testRatingsAddPosUnique() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertTrue(store.getRatings().add(3, 4, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0)), "Data should be able to be added as user and movie IDs together are unique");
    }

    @Test void testRatingsAddPosSameUser() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertTrue(store.getRatings().add(1, 3, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0)), "Data should be able to be added, as user and movie IDs together are unique (user on its own is not unique)");
    }

    @Test void testRatingsAddPosSameMovie() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertTrue(store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0)), "Data should be able to be added, as user and movie IDs together are unique (movie on its own is not unique)");
    }

    @Test void testRatingsAddNeg() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertFalse(store.getRatings().add(1, 2, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0)), "Data cannot be added, as there is already an entry with this unique combination of user and movie IDs");
    }

    @Test void testRatingsRemoveDefault() {
        assertFalse(store.getRatings().remove(1, 2), "Data cannot be removed when there is no data in the store");
    }

    @Test void testRatingsRemovePos() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertTrue(store.getRatings().remove(1, 2), "Data should be able to be removed, as the element exists");
    }

    @Test void testRatingsRemoveNegUnique() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertFalse(store.getRatings().remove(3, 4), "Data cannot be removed as the unique combination of user and movie ID is not in the store");
    }

    @Test void testRatingsRemoveNegSameUser(){
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertFalse(store.getRatings().remove(1, 3), "Data cannot be removed as the unique combination of user and movie ID is not in the store (though the user does have a rating in the store)");
    }

    @Test void testRatingsRemoveNegSameMovie() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertFalse(store.getRatings().remove(3, 2), "Data cannot be removed as the unique combination of user and movie ID is not in the store (though the movie does have a rating in the store)");
    }

    @Test void testRatingsSetDefault() {
        assertTrue(store.getRatings().set(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0)), "Data should be able to be set (in this case, added) when the store is empty");
    }

    @Test void testRatingsSetPosUnique() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertTrue(store.getRatings().set(3, 4, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0)), "Data should be able to be set (in this case, added) as user and movie IDs together are unique");
    }

    @Test void testRatingsSetPosSameUser() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertTrue(store.getRatings().set(1, 3, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0)), "Data should be able to be set (in this case, added), as user and movie IDs together are unique (user on its own is not unique)");
    }

    @Test void testRatingsSetPosSameMovie() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertTrue(store.getRatings().set(3, 2, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0)), "Data should be able to be set (in this case, added), as user and movie IDs together are unique (movie on its own is not unique)");
    }

    @Test void testRatingsSetPosSameUserAndMovie() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        assertTrue(store.getRatings().set(1, 2, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0)), "Data should be able to be set, as user and movie IDs together match a record in the store");
    }

    @Test void testRatingsGetMovieRatingsDefault() {
        float[] results = store.getRatings().getMovieRatings(1);
        float[] expected = new float[0];

        assertNotNull(results, "The results from getMovieArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An empty array should be returned, as there are no ratings for the store to find");
    }

    @Test void testRatingsGetMovieRatingsPos() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        float[] results = store.getRatings().getMovieRatings(2);
        float[] expected = {3.0f};

        assertNotNull(results, "The results from getMovieArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An array with a single element of \"3.0f\" should be returned, as there is only 1 ratings for the store to find");
    }

    @Test void testRatingsGetMovieRatingsPos2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2025, 1, 1, 1, 0, 0));

        float[] results = store.getRatings().getMovieRatings(2);
        float[] expected = {3.0f, 4.0f};

        assertNotNull(results, "The results from getMovieArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An array with 2 elements of \"3.0f\" and \"4.0f\" should be returned, as there are 2 ratings for the store to find");
    }

    @Test void testRatingsGetMovieRatingsMix() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 4, 4.0f, LocalDateTime.of(2025, 1, 1, 1, 0, 0));

        float[] results = store.getRatings().getMovieRatings(2);
        float[] expected = { 3.0f };

        assertNotNull(results, "The results from getMovieArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An array with a single element of \"3.0f\" should be returned, as there is only 1 ratings that matches for the store to find");
    }

    @Test void testRatingsGetMovieRatingsNeg() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        float[] results = store.getRatings().getMovieRatings(3);
        float[] expected = new float[0];

        assertNotNull(results, "The results from getMovieArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An array with a no elements should be returned, as there is no matching ratings for the store to find");
    }

    @Test void testRatingsGetUserRatingsDefault() {
        float[] results = store.getRatings().getUserRatings(1);
        float[] expected = new float[0];

        assertNotNull(results, "The results from getUserArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An empty array should be returned, as there are no ratings for the store to find");
    }

    @Test void testRatingsGetUserRatingsPos() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        float[] results = store.getRatings().getUserRatings(1);
        float[] expected = { 3.0f };

        assertNotNull(results, "The results from getUserArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An array with a single element of \"3.0f\" should be returned, as there is only 1 ratings for the store to find");
    }

    @Test void testRatingsGetUserRatingsPos2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(1, 3, 4.0f, LocalDateTime.of(2025, 1, 1, 1, 0, 0));

        float[] results = store.getRatings().getUserRatings(1);
        float[] expected = { 3.0f, 4.0f };

        assertNotNull(results, "The results from getUserArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An array with 2 elements of \"3.0f\" and \"4.0f\" should be returned, as there are 2 ratings for the store to find");
    }

    @Test void testRatingsGetUserRatingsMix() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 4, 4.0f, LocalDateTime.of(2025, 1, 1, 1, 0, 0));

        float[] results = store.getRatings().getUserRatings(1);
        float[] expected = { 3.0f };

        assertNotNull(results, "The results from getUserArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An array with a single element of \"3.0f\" should be returned, as there is only 1 ratings that matches for the store to find");
    }

    @Test void testRatingsGetUserRatingsNeg() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        float[] results = store.getRatings().getUserRatings(3);
        float[] expected = new float[0];

        assertNotNull(results, "The results from getUserArray should never be null");
        assertTrue(checkContentsOfArray(results, expected), "An array with a no elements should be returned, as there is no matching ratings for the store to find");
    }

    @Test void testRatingsGetMovieAverageRatingDefault() {
        assertEquals(-1.0f, store.getRatings().getMovieAverageRating(1), "Ratings and Movies is empty, so the value returned should be -1.0f");
    }

    @Test void testRatingsGetMovieAverageRatingsPos() {
        Genre[] testGenres =  {new Genre(6, "Test")};
        String[] testLanguages = {"en"};
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(3.0f, store.getRatings().getMovieAverageRating(2), "There is only one rating for the film, and the film exists in the Movies store. Therefore, the result should be the entry");
    }

    @Test void testRatingsGetMovieAverageRatingsPos2() {
        Genre[] testGenres = { new Genre(6, "Test") };
        String[] testLanguages = { "en" };
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 1, 1, 0, 0));

        assertEquals(3.5f, store.getRatings().getMovieAverageRating(2), "There are 2 ratings for the film, and the film exists in the Movies store. Therefore, the result should be the average of the 2 ratings");
    }

    @Test void testRatingsGetMovieAverageRatingsPos2Mix() {
        Genre[] testGenres = { new Genre(6, "Test") };
        String[] testLanguages = { "en" };
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 1, 1, 0, 0));
        store.getRatings().add(4, 5, 2.0f, LocalDateTime.of(2024, 3, 1, 1, 0, 0));

        assertEquals(3.5f, store.getRatings().getMovieAverageRating(2), "There are 2 ratings for this film, and the film exists in the Movies store. Therefore, the result should be the average of the applicable 2 ratings");
    }

    @Test void testRatingsGetMovieAverageRatingsNoMovie() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(3.0f, store.getRatings().getMovieAverageRating(2), "There is only one rating for the film. Therefore, the result should be the entry");
    }
    
    @Test void testRatingsGetMovieAverageRatingsNeg() {
        Genre[] testGenres = { new Genre(6, "Test") };
        String[] testLanguages = { "en" };
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(-1.0f, store.getRatings().getMovieAverageRating(4), "There are ratings, but not for the movie specified. The movie specified does not exist in Movies, so should return -1.0f");
    }

    @Test void testRatingsGetMovieAverageRatingsNegNoRatings() {
        Genre[] testGenres = { new Genre(6, "Test") };
        String[] testLanguages = { "en" };
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        assertEquals(0.0f, store.getRatings().getMovieAverageRating(2), "There is no rating for the film, but the film does exist in Movies. Therefore, 0.0f should be returned.");
    }

    @Test void testRatingsGetUserAverageRatingsDefault() {
        assertEquals(-1.0f, store.getRatings().getUserAverageRating(1), "There are no ratings to find, so should return -1.0f");
    }

    @Test void testRatingsGetUserAverageRatingsPos() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(3.0f, store.getRatings().getUserAverageRating(1), "There is only 1 rating for the user, so should return that");
    }

    @Test void testRatingsGetUserAverageRatingsPos2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(1, 3, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0));

        assertEquals(3.5f, store.getRatings().getUserAverageRating(1), "There are 2 ratings for the user, so should return the average of those ratings");
    }

    @Test void testRatingsGetUserAverageRatingsPosMix() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(1, 3, 4.0f, LocalDateTime.of(2025, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 3.0f, LocalDateTime.of(2024, 6, 6, 6, 0, 0));

        assertEquals(3.5f, store.getRatings().getUserAverageRating(1), "There are 2 applicable ratings for the user, so should return the average of the applicable ratings");
    }

    @Test void testRatingsGetUserAverageRatingsNeg() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(-1.0f, store.getRatings().getUserAverageRating(3), "There are no ratings with this user (though there are ratings), therefore return -1.0f");
    }

    @Test void testRatingsGetMostRatedMoviesDefault0() {
        assertNotNull(store.getRatings().getMostRatedMovies(0), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(new int[0], store.getRatings().getMostRatedMovies(0), "There are no movies requested, so should return an empty array");
    }


    @Test void testRatingsGetMostRatedMoviesDefault() {
        assertNotNull(store.getRatings().getMostRatedMovies(2), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(new int[0], store.getRatings().getMostRatedMovies(2), "Some movies are requested but the store is empty, so should return an empty array");
    }

    @Test void testRatingsGetMostRatedMoviesPos() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        int[] result = store.getRatings().getMostRatedMovies(1);
        int[] expected = {2};

        assertNotNull(store.getRatings().getMostRatedMovies(1), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The only rating in the store is for the movie with an ID of 2, so this should be the only element in the returned array");
    }

    @Test void testRatingsGetMostRatedMoviesPos2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));

        int[] result = store.getRatings().getMostRatedMovies(1);
        int[] expected = {2};

        assertNotNull(store.getRatings().getMostRatedMovies(1), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The only rating in the store is for the movie with an ID of 2 (even though there are 2 ratings for this movie), so this should be the only element in the returned array");
    }

    @Test void testRatingsGetMostRatedMoviesPosMix() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedMovies(2);
        int[] expected = {2, 5};

        assertNotNull(store.getRatings().getMostRatedMovies(2), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies. The MovieID 2 has 2 ratings, and the MovieID 5 has 1 rating. Therefore, the result should be 2, then 5");
    }

    @Test void testRatingsGetMostRatedMoviesPosMix2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 5, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedMovies(2);
        int[] expected = {5, 2};

        assertNotNull(store.getRatings().getMostRatedMovies(2), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies. The MovieID 2 has 1 rating, and the MovieID 5 has 2 ratings. Therefore, the result should be 5, then 2");
    }

    @Test void testRatingsGetMostRatedMoviesPosMixLimit() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedMovies(1);
        int[] expected = {2};

        assertNotNull(store.getRatings().getMostRatedMovies(1), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies. The MovieID 2 has 2 ratings, and the MovieID 5 has 1 rating. However, output is limited to 1, so should get a singular element, \"2\"");
    }

    @Test void testRatingsGetMostRatedMoviesPosMixLimit2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 5, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedMovies(1);
        int[] expected = {5};

        assertNotNull(store.getRatings().getMostRatedMovies(1), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies. The MovieID 2 has 1 rating, and the MovieID 5 has 2 ratings. However, output is limited to 1, so should get a singular element, \"5\"");
    }

    @Test void testRatingsGetMostRatedMoviesPosOver() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        int[] result = store.getRatings().getMostRatedMovies(2);
        int[] expected = {2};

        assertNotNull(store.getRatings().getMostRatedMovies(2), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 1 movie, despite 2 movies being requested. Therefore, the function should return just an array, with the singular element");
    }

    @Test void testRatingsGetMostRatedMoviesPosMixOver() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedMovies(3);
        int[] expected = { 2, 5 };

        assertNotNull(store.getRatings().getMostRatedMovies(3), "The function getMostRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies, despite the function asking for 3 movies. The MovieID 2 has 2 ratings, and the MovieID 5 has 1 rating. Therefore, the result should be an array of 2, then 5 only");
    }

    @Test void testRatingsGetMostRatedUsersDefault0() {
        assertNotNull(store.getRatings().getMostRatedUsers(0), "The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(new int[0], store.getRatings().getMostRatedUsers(0), "There are no movies requested, so should return an empty array");
    }

    @Test void testRatingsGetMostRatedUsersDefault() {
        assertNotNull(store.getRatings().getMostRatedUsers(2), "The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(new int[0], store.getRatings().getMostRatedUsers(2), "Some movies are requested but the store is empty, so should return an empty array");
    }

    @Test void testRatingsGetMostRatedUsersPos() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        int[] result = store.getRatings().getMostRatedUsers(1);
        int[] expected = {1};

        assertNotNull(store.getRatings().getMostRatedUsers(1), "The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(expected, result, "The only rating in the store is for the user with an ID of 1, so this should be the only element in the returned array");
    }

    @Test void testRatingsGetMostRatedUsersPos2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(1, 3, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));

        int[] result = store.getRatings().getMostRatedUsers(1);
        int[] expected = {1};

        assertNotNull(store.getRatings().getMostRatedUsers(1), "The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(expected, result, "The only rating in the store is for the user with an ID of 1 (even though there are 2 ratings for this user), so this should be the only element in the returned array");
    }

    @Test void testRatingsGetMostRatedUsersPosMix() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(1, 3, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedUsers(2);
        int[] expected = {1, 4};

        assertNotNull(store.getRatings().getMostRatedUsers(2), "The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 users. The UserID 1 has 2 ratings, and the UserID 4 has 1 rating. Therefore, the result should be 1, then 4");
    }

    @Test void testRatingsGetMostRatedUsersPosMix2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(4, 3, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedUsers(2);
        int[] expected = {4, 1};

        assertNotNull(store.getRatings().getMostRatedUsers(2),"The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 users. The UserID 1 has 1 rating, and the UserID 4 has 2 ratings. Therefore, the result should be 4, then 1");
    }

    @Test void testRatingsGetMostRatedUsersPosMixLimit() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(1, 3, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedUsers(1);
        int[] expected = {1};

        assertNotNull(store.getRatings().getMostRatedUsers(1), "The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 users. The UserID 1 has 2 ratings, and the UserID 4 has 1 rating. However, output is limited to 1, so should get a singular element, \"1\"");
    }

    @Test void testRatingsGetMostRatedUsersPosMixLimit2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(4, 3, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedUsers(1);
        int[] expected = {4};

        assertNotNull(store.getRatings().getMostRatedUsers(1), "The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 users. The UserID 1 has 1 rating, and the UserID 4 has 2 ratings. However, output is limited to 1, so should get a singular element, \"4\"");
    }

    @Test void testRatingsGetMostRatedUsersPosOver() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        int[] result = store.getRatings().getMostRatedUsers(2);
        int[] expected = {1};

        assertNotNull(store.getRatings().getMostRatedUsers(2), "The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 1 user, despite 2 users being requested. Therefore, the function should return just an array, with the singular element");
    }

    @Test void testRatingsGetMostRatedUsersPosMixOver() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(1, 3, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 4.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getMostRatedUsers(3);
        int[] expected = {1, 4};

        assertNotNull(store.getRatings().getMostRatedUsers(3), "The function getMostRatedUsers should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 users, despite the function asking for 3 users. The UserID 1 has 2 ratings, and the UserID 4 has 1 rating. Therefore, the result should be an array of 1, then 4 only");
    }

    @Test void testRatingsGetNumRatingsDefault() {
        assertEquals(-1, store.getRatings().getNumRatings(1), "The stores are empty, so there are no ratings or movies to check against. Therefore, return -1");
    }

    @Test void testRatingsGetNumRatingsPos() {
        Genre[] testGenres = { new Genre(6, "Test") };
        String[] testLanguages = { "en" };
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(1, store.getRatings().getNumRatings(2), "The Movies store has an entry for the movie, and there is only 1 rating for this movie");
    }

    @Test void testRatingsGetNumRatingsPos2() {
        Genre[] testGenres = { new Genre(6, "Test") };
        String[] testLanguages = { "en" };
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));

        assertEquals(2, store.getRatings().getNumRatings(2), "The Movies store has an entry for the movie, and there are 2 ratings for this movie");
    }

    @Test void testRatingsGetNumRatingsPos2Mix() {
        Genre[] testGenres = { new Genre(6, "Test") };
        String[] testLanguages = { "en" };
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 2.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));

        assertEquals(2, store.getRatings().getNumRatings(2), "The Movies store has an entry for the movie, and there are 2 ratings for this movie (even though there are 3 ratings overall)");
    }

    @Test void testRatingsGetNumRatingsNoMovie() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(1, store.getRatings().getNumRatings(2), "There is only 1 rating for this movie");
    }

    @Test void testRatingsGetNumRatingsNeg() {
        Genre[] testGenres = { new Genre(6, "Test") };
        String[] testLanguages = { "en" };
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(-1, store.getRatings().getNumRatings(4), "Ratings and Movies have entries, but not for the requested movie. Therefore, return -1");
    }

    @Test void testRatingsGetNumRatingsNegNoRating() {
        Genre[] testGenres = { new Genre(6, "Test") };
        String[] testLanguages = { "en" };
        store.getMovies().add(2, "Test title", "Test original title", "Test overview", "Test tagline", "Test status", testGenres, LocalDate.of(2023, 1, 1), 1000000, 1000001, testLanguages, "en", 123, "Test homepage", false, false, "Test poster");

        assertEquals(0, store.getRatings().getNumRatings(2), "The Movies store has an entry for the film, but there are no ratings. Therefore, return 0");
    }

    @Test void testRatingsGetTopAverageRatedMoviesDefault0() {
        assertNotNull(store.getRatings().getTopAverageRatedMovies(0), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(new int[0], store.getRatings().getTopAverageRatedMovies(0), "There are no movies requested, so should return an empty array");
    }

    @Test void testRatingsGetTopAverageRatedMoviesDefault() {
        assertNotNull(store.getRatings().getTopAverageRatedMovies(2), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(new int[0], store.getRatings().getTopAverageRatedMovies(2), "Some movies are requested but the store is empty, so should return an empty array");
    }

    @Test void testRatingsGetTopAverageRatedMoviesPos() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        int[] result = store.getRatings().getTopAverageRatedMovies(1);
        int[] expected = {2};

        assertNotNull(store.getRatings().getTopAverageRatedMovies(1), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The only rating in the store is for the movie with an ID of 2, so this should be the only element in the returned array");
    }

    @Test void testRatingsGetTopAverageRatedMoviesPos2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));

        int[] result = store.getRatings().getTopAverageRatedMovies(1);
        int[] expected = { 2 };

        assertNotNull(store.getRatings().getTopAverageRatedMovies(1), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The only rating in the store is for the movie with an ID of 2 (even though there are 2 ratings for this movie), so this should be the only element in the returned array");
    }

    @Test void testRatingsGetTopAverageRatedMoviesPosMix() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 2.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getTopAverageRatedMovies(2);
        int[] expected = { 2, 5 };

        assertNotNull(store.getRatings().getTopAverageRatedMovies(2), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies. The MovieID 2 has 2 ratings with an average of 3.5f, and the MovieID 5 has 1 rating with an average of 2.0f. Therefore, the result should be 2, then 5");
    }

    @Test void testRatingsGetTopAverageRatedMoviesPosMix2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 5, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 3.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getTopAverageRatedMovies(2);
        int[] expected = { 5, 2 };

        assertNotNull(store.getRatings().getTopAverageRatedMovies(2), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies. The MovieID 2 has 1 rating with an average of 3.0f, and the MovieID 5 has 2 ratings with an average of 3.5f. Therefore, the result should be 5, then 2");
    }

    @Test void testRatingsGetTopAverageRatedMoviesPosMix2Swap() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 5, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 1.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getTopAverageRatedMovies(2);
        int[] expected = { 2, 5 };

        assertNotNull(store.getRatings().getTopAverageRatedMovies(2), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies. The MovieID 2 has 1 rating with an average of 3.0f, and the MovieID 5 has 2 ratings with an average of 2.5f. Therefore, the result should be 2, then 5");
    }

    @Test void testRatingsGetTopAverageRatedMoviesPosMixLimit() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 3.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getTopAverageRatedMovies(1);
        int[] expected = { 2 };

        assertNotNull(store.getRatings().getTopAverageRatedMovies(1), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies. The MovieID 2 has 2 ratings with an average of 3.5f, and the MovieID 5 has 1 rating with an average of 3.0f. However, output is limited to 1, so should get a singular element, \"2\"");
    }

    @Test void testRatingsGetTopAverageRatedMoviesPosMixLimit2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 5, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 3.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getTopAverageRatedMovies(1);
        int[] expected = { 5 };

        assertNotNull(store.getRatings().getTopAverageRatedMovies(1), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies. The MovieID 2 has 1 rating with an average of 3.0f, and the MovieID 5 has 2 ratings with an average of 3.5f. However, output is limited to 1, so should get a singular element, \"5\"");
    }

    @Test void testRatingsGetTopAverageRatedMoviesPosOver() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        int[] result = store.getRatings().getTopAverageRatedMovies(2);
        int[] expected = { 2 };

        assertNotNull(store.getRatings().getTopAverageRatedMovies(2), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 1 movie, despite 2 movies being requested. Therefore, the function should return just an array, with the singular element");
    }

    @Test void testRatingsGetTopAverageRatedMoviesPosMixOver() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 2, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().add(4, 5, 3.0f, LocalDateTime.of(2024, 3, 3, 3, 0, 0));

        int[] result = store.getRatings().getTopAverageRatedMovies(3);
        int[] expected = { 2, 5 };

        assertNotNull(store.getRatings().getTopAverageRatedMovies(3), "The function getTopAverageRatedMovies should never return \"null\"");
        assertArrayEquals(expected, result, "The store should have ratings for 2 movies, despite the function asking for 3 movies. The MovieID 2 has 2 ratings with an average of 3.5f, and the MovieID 5 has 1 rating with an average of 3.0f. Therefore, the result should be an array of 2, then 5 only");
    }

    @Test void testRatingsSizeDefault() {
        assertEquals(0, store.getRatings().size(), "The Ratings store is empty, so should have a size of 0");
    }

    @Test void testRatingsSizeAfterAdd() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(1, store.getRatings().size(), "1 rating have been added, so the size of Ratings should be 1");
    }

    @Test void testRatingsSizeAfterAdd2() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 4, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));

        assertEquals(2, store.getRatings().size(), "2 ratings have been added, so the size of Ratings should be 2");
    }

    @Test void testRatingsSizeAfterAddThenRemove() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().remove(1, 2);

        assertEquals(0, store.getRatings().size(), "A rating was added, then removed. Therefore, there should be nothing in the store");
    }

    @Test void testRatingsSizeAfterAdd2ThenRemove() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));
        store.getRatings().add(3, 4, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));
        store.getRatings().remove(1, 2);

        assertEquals(1, store.getRatings().size(), "2 ratings have been added then 1 rating was removed, so the size of Ratings should be 1");
    }

    @Test void testRatingsSizeAfterAddViaSet() {
        store.getRatings().set(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0, 0));

        assertEquals(1, store.getRatings().size(), "An element was added via the set function, so the size should be 1");
    }

    @Test void testRatingsSizeAfterAddThenSet() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0 , 0));
        store.getRatings().set(1, 2, 4.0f, LocalDateTime.of(20204, 2, 2, 2, 0, 0));

        assertEquals(1, store.getRatings().size(), "An element was added, then was altered using set. As there is still only 1 element, the size should be 1");
    }

    @Test void testRatingsSizeAfterAddThenAddViaSet() {
        store.getRatings().add(1, 2, 3.0f, LocalDateTime.of(2024, 1, 1, 1, 0 , 0));
        store.getRatings().set(3, 4, 4.0f, LocalDateTime.of(2024, 2, 2, 2, 0, 0));

        assertEquals(2, store.getRatings().size(), "An element was added, then another element was added via the set function. Therefore, there should be 2 elements in the store");
    }

    @Test void testRatingsSizeRemove() {
        store.getRatings().remove(1, 2);

        assertEquals(0, store.getRatings().size(), "Removing an element from an empty store will result in an empty store. Therefore, the size should be 0");
    }
}
