import stores.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CreditsTest {

    private Stores store = new Stores();

    @BeforeEach void setup() {
        store = new Stores();
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

    @Test void testCreditsAddDefault() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        assertTrue(store.getCredits().add(cast, crew, 1), "The store is empty, so should be able to add an element");
    }

    @Test void testCreditsAddPos() {
        CastCredit[] cast1 = new CastCredit[1];
        CrewCredit[] crew1 = new CrewCredit[1];

        cast1[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,"Test cast profile path");
        crew1[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name","Test crew profile path");

        store.getCredits().add(cast1, crew1, 1);

        CastCredit[] cast2 = new CastCredit[1];
        CrewCredit[] crew2 = new CrewCredit[1];

        cast2[0] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 1,"Test cast profile path2");
        crew2[0] = new CrewCredit("202", "Test crew department", 2002, "Test crew job", "Test crew name2","Test crew profile path2");

        assertTrue(store.getCredits().add(cast2, crew2, 2), "There is no element with this film ID, so should be able to be added");
    }

    @Test void testCreditsAddNeg() {
        CastCredit[] cast1 = new CastCredit[1];
        CrewCredit[] crew1 = new CrewCredit[1];

        cast1[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,"Test cast profile path");
        crew1[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast1, crew1, 1);

        CastCredit[] cast2 = new CastCredit[1];
        CrewCredit[] crew2 = new CrewCredit[1];

        cast2[0] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 1,"Test cast profile path2");
        crew2[0] = new CrewCredit("202", "Test crew department", 2002, "Test crew job", "Test crew name2","Test crew profile path2");

        assertFalse(store.getCredits().add(cast2, crew2, 1),"There is already credits added for this film, so should nto be able to be added");
    }

    @Test void testCreditsRemoveDefault() {
        assertFalse(store.getCredits().remove(1), "The store is empty, so there is nothing that can be removed");
    }

    @Test void testCreditsRemovePos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,"Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name","Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertTrue(store.getCredits().remove(1), "There is a movie with an ID of 1 in Credits, so should be able to remove it");
    }

    @Test void testCreditsRemoveNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertFalse(store.getCredits().remove(2), "There is no movie with an ID of 2 in Credits, so cannot remove this element");
    }

    @Test void testCreditsGetFilmCastDefault() {
        assertNotNull(store.getCredits().getFilmCast(1), "The function \"getFilmCast\" should never return null");
        assertArrayEquals(new CastCredit[0], store.getCredits().getFilmCast(1), "The store is empty, so there are no cast credits to retrieve. Therefore, an empty array should be returned");
    }

    @Test void testCreditsGetFilmCastPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,"Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name","Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CastCredit[] result = store.getCredits().getFilmCast(1);

        assertNotNull(result, "The function \"getFilmCast\" should never return null");
        assertEquals(1, result.length, "There was only 1 cast member on movie 1, so only 1 cast member should be returned");
        assertEquals(cast[0], result[0], "The cast member objects should be the same");
    }

    @Test void testCreditsGetFilmCastPos2() {
        CastCredit[] cast = new CastCredit[2];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character1", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CastCredit[] result = store.getCredits().getFilmCast(1);

        assertNotNull(result, "The function \"getFilmCast\" should never return null");
        assertEquals(2, result.length, "There are 2 cast members on movie 1, so both cast members should be returned");
        assertEquals(cast[0], result[0], "The cast member objects were already ordered, so the first elements in each should be the same");
        assertEquals(cast[1], result[1],"The cast member objects were already ordered, so the second elements in each should be the same");
    }

    @Test void testCreditsGetFilmCastPos2Reorder() {
        CastCredit[] cast = new CastCredit[2];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");
        cast[1] = new CastCredit(101, "Test cast character1", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        CastCredit[] expected = { cast[1], cast[0] };

        store.getCredits().add(cast, crew, 1);

        CastCredit[] result = store.getCredits().getFilmCast(1);

        for (int i = 0; i < result.length; i++) {
            System.out.println("i: " + i + "\t order: " + result[i].getOrder());
        }

        assertNotNull(result, "The function \"getFilmCast\" should never return null");
        assertEquals(2, result.length, "There are 2 cast members on movie 1 (even if they were inserted in the wrong order), so both cast members should be returned");
        assertEquals(expected[0], result[0], "The cast member objects were in the wrong order, so the first elements in the input should be the second element in the result");
        assertEquals(expected[1], result[1], "The cast member objects were in the wrong order, so the second elements in the input should be the first element in the result");
    }

    @Test void testCreditsGetFilmCastNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CastCredit[] result = store.getCredits().getFilmCast(2);

        assertNotNull(result, "The function \"getFilmCast\" should never return null");
        assertEquals(0, result.length, "There are no movies in Credits with this ID, and thus no cast members. Therefore, it should return an empty array");
        assertArrayEquals(new CastCredit[0], result, "The result of searching for a movie that is not in Credits is an empty array");
    }

    @Test
    void testCreditsGetFilmCrewDefault() {
        assertNotNull(store.getCredits().getFilmCrew(1), "The function \"getFilmCrew\" should never return null");
        assertArrayEquals(new CastCredit[0], store.getCredits().getFilmCrew(1),"The store is empty, so there are no crew credits to retrieve. Therefore, an empty array should be returned");
    }

    @Test
    void testCreditsGetFilmCrewPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CrewCredit[] result = store.getCredits().getFilmCrew(1);

        assertNotNull(result, "The function \"getFilmCrew\" should never return null");
        assertEquals(1, result.length, "There was only 1 crew member on movie 1, so only 1 crew member should be returned");
        assertEquals(crew[0], result[0], "The crew member objects should be the same");
    }

    @Test
    void testCreditsGetFilmCrewPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[2];

        cast[0] = new CastCredit(101, "Test cast character1", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");
        crew[1] = new CrewCredit("202", "Test crew department2", 2002, "Test crew job2", "Test crew name2", "Test crew profile path2");

        store.getCredits().add(cast, crew, 1);

        CrewCredit[] result = store.getCredits().getFilmCrew(1);

        assertNotNull(result, "The function \"getFilmCrew\" should never return null");
        assertEquals(2, result.length, "There are 2 crew members on movie 1, so both crew members should be returned");
        assertEquals(crew[0], result[0], "The crew member objects were already ordered, so the first elements in each should be the same");
        assertEquals(crew[1], result[1], "The crew member objects were already ordered, so the second elements in each should be the same");
    }

    @Test
    void testCreditsGetFilmCrewPos2Reorder() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[2];

        cast[0] = new CastCredit(101, "Test cast character1", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("202", "Test crew department2", 2002, "Test crew job2", "Test crew name2", "Test crew profile path2");
        crew[1] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        CrewCredit[] expected = { crew[1], crew[0] };

        store.getCredits().add(cast, crew, 1);

        CrewCredit[] result = store.getCredits().getFilmCrew(1);

        assertNotNull(result, "The function \"getFilmCrew\" should never return null");
        assertEquals(2, result.length, "There are 2 crew members on movie 1 (even if they were inserted in the wrong order), so both crew members should be returned");
        assertEquals(expected[0], result[0], "The crew member objects were in the wrong order, so the first elements in the input should be the second element in the result");
        assertEquals(expected[1], result[1], "The crew member objects were in the wrong order, so the second elements in the input should be the first element in the result");
    }

    @Test
    void testCreditsGetFilmCrewNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CrewCredit[] result = store.getCredits().getFilmCrew(2);

        assertNotNull(result, "The function \"getFilmCrew\" should never return null");
        assertEquals(0, result.length, "There are no movies in Credits with this ID, and thus no crew members. Therefore, it should return an empty array");
        assertArrayEquals(new CrewCredit[0], result, "The result of searching for a movie that is not in Credits is an empty array");
    }

    @Test void testCreditsSizeOfCastDefault() {
        assertEquals(-1, store.getCredits().sizeOfCast(1), "When the store is empty, then regardless of which movie is requested, it should always return -1");
    }

    @Test void testCreditsSizeOfCastPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertEquals(1, store.getCredits().sizeOfCast(1), "There is 1 cast members who worked on movie 1, so \"1\" should be returned");
    }

    @Test void testCreditsSizeOfCastPos2() {
        CastCredit[] cast = new CastCredit[2];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character1", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertEquals(2, store.getCredits().sizeOfCast(1), "There are 2 cast members who worked on movie 1, so \"2\" should be returned");
    }

    @Test void testCreditsSizeOfCastNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertEquals(-1, store.getCredits().sizeOfCast(2), "There is no movie with an ID of 2 in credits, so should return -1");
    }

    @Test void testCreditsSizeOfCrewDefault() {
        assertEquals(-1, store.getCredits().sizeOfCrew(1),
                "When the store is empty, then regardless of which movie is requested, it should always return -1");
    }

    @Test void testCreditsSizeOfCrewPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertEquals(1, store.getCredits().sizeOfCrew(1), "There is 1 crew members who worked on movie 1, so \"1\" should be returned");
    }

    @Test void testCreditsSizeOfCrewPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[2];

        cast[0] = new CastCredit(101, "Test cast character1", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");
        crew[1] = new CrewCredit("202", "Test crew department2", 2002, "Test crew job2", "Test crew name2", "Test crew profile path2");

        store.getCredits().add(cast, crew, 1);

        assertEquals(2, store.getCredits().sizeOfCrew(1), "There are 2 crew members who worked on movie 1, so \"2\" should be returned");
    }

    @Test void testCreditsSizeOfCrewNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertEquals(-1, store.getCredits().sizeOfCrew(2), "There is no movie with an ID of 2 in credits, so should return -1");
    }

    @Test void testCreditsGetUniqueCastDefault() {
        assertNotNull(store.getCredits().getUniqueCast(), "The function \"getUniqueCast\" should never return null");
        assertArrayEquals(new Person[0], store.getCredits().getUniqueCast(), "The Credits store is empty, so there is no cast members to produce a list from");
    }

    @Test void testCreditsGetUniqueCastPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[cast.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new Person(cast[i].getID(), cast[i].getName(), cast[i].getProfilePath());
        }

        Person[] results = store.getCredits().getUniqueCast();

        assertNotNull(results, "The function \"getUniqueCast\" should never return null");
        assertEquals(expected.length, results.length, "The number of cast people should be 1, as the number of cast members in the only movie added is 1");
        for (int i = 0; i < results.length; i++) {
            assertNotNull(results[i], "The entry for Person " + i + " is null when it shouldn't be");
            assertEquals(expected[i].getID(), results[i].getID(), "The ID for Person " + i + " is incorrect");
            assertEquals(expected[i].getName(), results[i].getName(), "The name for Person " + i + " is incorrect");
            assertEquals(expected[i].getProfilePath(), results[i].getProfilePath(), "The profile path for Person " + i + " is incorrect");
        }
    }

    @Test
    void testCreditsGetUniqueCastPos2() {
        CastCredit[] cast = new CastCredit[2];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[cast.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new Person(cast[i].getID(), cast[i].getName(), cast[i].getProfilePath());
        }

        Person[] results = store.getCredits().getUniqueCast();

        assertNotNull(results, "The function \"getUniqueCast\" should never return null");
        assertEquals(expected.length, results.length, "The number of cast people should be 2, as the number of cast members in the only movie added is 2");
        for (int i = 0; i < expected.length; i++) {
            int instances = 0;
            for (int j = 0; j < results.length; j++){
                assertNotNull(results[i], "The person at index " + j + " is null when it shouldn't be");
                if ((expected[i].getID() == results[j].getID()) && (expected[i].getName() == results[j].getName()) && (expected[i].getProfilePath() == results[j].getProfilePath())) {
                    instances++;
                }
            }
            assertNotEquals(0, instances, "There was a person missing from the list (ID: " + expected[i].getID() + ")");
            assertEquals(1, instances, "There should only be 1 instance of the person (ID: " + expected[i].getID() + ")\t Number of instances found: " + instances);
        }
    }

    @Test void testCreditsGetUniqueCastPos2Duplicate() {
        CastCredit[] cast = new CastCredit[2];
        CastCredit[] cast2 = new CastCredit[1];

        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");
        cast2[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast2, crew, 2);

        Person[] expected = new Person[cast.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new Person(cast[i].getID(), cast[i].getName(), cast[i].getProfilePath());
        }

        Person[] results = store.getCredits().getUniqueCast();

        assertNotNull(results, "The function \"getUniqueCast\" should never return null");
        assertEquals(expected.length, results.length, "The number of cast people should be 2, as the number of unique cast members across both films is 2");
        for (int i = 0; i < expected.length; i++) {
            int instances = 0;
            for (int j = 0; j < results.length; j++) {
                assertNotNull(results[i], "The person at index " + j + " is null when it shouldn't be");
                if ((expected[i].getID() == results[j].getID()) && (expected[i].getName() == results[j].getName()) && (expected[i].getProfilePath() == results[j].getProfilePath())) {
                    instances++;
                }
            }
            assertNotEquals(0, instances, "There was a person missing from the list (ID: " + expected[i].getID() + ")");
            assertEquals(1, instances, "There should only be 1 instance of the person (ID: " + expected[i].getID() + ")\t Number of instances found: " + instances);
        }
    }

    @Test
    void testCreditsGetUniqueCrewDefault() {
        assertNotNull(store.getCredits().getUniqueCrew(), "The function \"getUniqueCrew\" should never return null");
        assertArrayEquals(new Person[0], store.getCredits().getUniqueCrew(), "The Credits store is empty, so there is no crew members to produce a list from");
    }

    @Test
    void testCreditsGetUniqueCrewPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[crew.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new Person(crew[i].getID(), crew[i].getName(), crew[i].getProfilePath());
        }

        Person[] results = store.getCredits().getUniqueCrew();

        assertNotNull(results, "The function \"getUniqueCrew\" should never return null");
        assertEquals(expected.length, results.length, "The number of crew people should be 1, as the number of crew members in the only movie added is 1");
        for (int i = 0; i < results.length; i++) {
            assertNotNull(results[i], "The entry for Person " + i + " is null when it shouldn't be");
            assertEquals(expected[i].getID(), results[i].getID(), "The ID for Person " + i + " is incorrect");
            assertEquals(expected[i].getName(), results[i].getName(), "The name for Person " + i + " is incorrect");
            assertEquals(expected[i].getProfilePath(), results[i].getProfilePath(),
                    "The profile path for Person " + i + " is incorrect");
        }
    }

    @Test
    void testCreditsGetUniqueCrewPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[2];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");
        crew[1] = new CrewCredit("202", "Test crew department2", 2002, "Test crew job2", "Test crew name2", "Test crew profile path2");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[crew.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new Person(crew[i].getID(), crew[i].getName(), crew[i].getProfilePath());
        }

        Person[] results = store.getCredits().getUniqueCrew();

        assertNotNull(results, "The function \"getUniqueCrew\" should never return null");
        assertEquals(expected.length, results.length, "The number of crew people should be 2, as the number of crew members in the only movie added is 2");
        for (int i = 0; i < expected.length; i++) {
            int instances = 0;
            for (int j = 0; j < results.length; j++) {
                assertNotNull(results[i], "The person at index " + j + " is null when it shouldn't be");
                if ((expected[i].getID() == results[j].getID()) && (expected[i].getName() == results[j].getName()) && (expected[i].getProfilePath() == results[j].getProfilePath())) {
                    instances++;
                }
            }
            assertNotEquals(0, instances, "There was a person missing from the list (ID: " + expected[i].getID() + ")");
            assertEquals(1, instances, "There should only be 1 instance of the person (ID: " + expected[i].getID() + ")\t Number of instances found: " + instances);
        }
    }

    @Test void testCreditsGetUniqueCrewPos2Duplicate() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[2];
        CrewCredit[] crew2 = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");
        crew[1] = new CrewCredit("202", "Test crew department2", 2002, "Test crew job2", "Test crew name2", "Test crew profile path2");
        crew2[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew2, 2);

        Person[] expected = new Person[crew.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new Person(crew[i].getID(), crew[i].getName(), crew[i].getProfilePath());
        }

        Person[] results = store.getCredits().getUniqueCrew();

        assertNotNull(results, "The function \"getUniqueCast\" should never return null");
        assertEquals(expected.length, results.length, "The number of crew people should be 2, as the number of unique crew members across both films is 2");
        for (int i = 0; i < expected.length; i++) {
            int instances = 0;
            for (int j = 0; j < results.length; j++) {
                assertNotNull(results[i], "The person at index " + j + " is null when it shouldn't be");
                if ((expected[i].getID() == results[j].getID()) && (expected[i].getName() == results[j].getName()) && (expected[i].getProfilePath() == results[j].getProfilePath())) {
                    instances++;
                }
            }
            assertNotEquals(0, instances, "There was a person missing from the list (ID: " + expected[i].getID() + ")");
            assertEquals(1, instances, "There should only be 1 instance of the person (ID: " + expected[i].getID() + ")\t Number of instances found: " + instances);
        }
    }

    @Test void testCreditsFindCastDefault() {
        assertNotNull(store.getCredits().findCast("test"), "The store is empty, so there is nothing to find");
        assertArrayEquals(new Person[0], store.getCredits().findCast("test"), "The store is empty, so there is no person to be found");
    }

    @Test void testCreditsFindCastPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[1];
        expected[0] = new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath());

        Person[] results = store.getCredits().findCast("Test cast name");

        assertNotNull(results, "The function \"findCast\" should never return null");
        assertEquals(expected.length, results.length, "There is only one person, and that person was search for using there exact name. Therefore, an array with 1 person should be returned");
        assertEquals(expected[0].getID(), results[0].getID(), "The ID's are not the same");
        assertEquals(expected[0].getName(), results[0].getName(), "The names are not the same");
        assertEquals(expected[0].getProfilePath(), results[0].getProfilePath(), "The profile paths are not the same");
    }

    @Test
    void testCreditsFindCastPos2() {
        CastCredit[] cast = new CastCredit[2];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test2cast2name", 2,"Test cast profile path2");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[1];
        expected[0] = new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath());

        Person[] results = store.getCredits().findCast("Test cast name");

        assertNotNull(results, "The function \"findCast\" should never return null");
        assertEquals(expected.length, results.length, "There is only one person that matches, and that person was search for using there exact name. Therefore, an array with 1 person should be returned");
        assertEquals(expected[0].getID(), results[0].getID(), "The ID's are not the same");
        assertEquals(expected[0].getName(), results[0].getName(), "The names are not the same");
        assertEquals(expected[0].getProfilePath(), results[0].getProfilePath(), "The profile paths are not the same");
    }

    @Test
    void testCreditsFindCastPosPart() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,"Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name","Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[1];
        expected[0] = new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath());

        Person[] results = store.getCredits().findCast("name");

        assertNotNull(results, "The function \"findCast\" should never return null");
        assertEquals(expected.length, results.length, "There is only one person, and that person was search for using part of there name. Therefore, an array with 1 person should be returned");
        assertEquals(expected[0].getID(), results[0].getID(), "The ID's are not the same");
        assertEquals(expected[0].getName(), results[0].getName(), "The names are not the same");
        assertEquals(expected[0].getProfilePath(), results[0].getProfilePath(), "The profile paths are not the same");
    }

    @Test
    void testCreditsFindCastPos2Part() {
        CastCredit[] cast = new CastCredit[2];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,"Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast n2me", 2,"Test cast profile path2");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name","Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[1];
        expected[0] = new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath());

        Person[] results = store.getCredits().findCast("name");

        assertNotNull(results, "The function \"findCast\" should never return null");
        assertEquals(expected.length, results.length,"There is only one person that matches out of the 2, and that person was search for using part of there name. Therefore, an array with 1 person should be returned");
        assertEquals(expected[0].getID(), results[0].getID(), "The ID's are not the same");
        assertEquals(expected[0].getName(), results[0].getName(), "The names are not the same");
        assertEquals(expected[0].getProfilePath(), results[0].getProfilePath(), "The profile paths are not the same");
    }

    @Test
    void testCreditsFindCastNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,"Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name","Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] results = store.getCredits().findCast("character");

        assertNotNull(results, "The function \"findCast\" should never return null");
        assertArrayEquals(new Person[0], results, "There is no cast member in the Credits store with that in there name. Therefore, an empty array should be returned");
    }

    @Test
    void testCreditsFindCrewDefault() {
        assertNotNull(store.getCredits().findCrew("test"), "The function \"findCrew\" should never return null");
        assertArrayEquals(new Person[0], store.getCredits().findCrew("test"), "The store is empty, so there is no person to be found");
    }

    @Test
    void testCreditsFindCrewPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,"Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name","Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[1];
        expected[0] = new Person(crew[0].getID(), crew[0].getName(), crew[0].getProfilePath());

        Person[] results = store.getCredits().findCrew("Test crew name");

        assertNotNull(results, "The function \"findCrew\" should never return null");
        assertEquals(expected.length, results.length, "There is only one person, and that person was search for using there exact name. Therefore, an array with 1 person should be returned");
        assertEquals(expected[0].getID(), results[0].getID(), "The ID's are not the same");
        assertEquals(expected[0].getName(), results[0].getName(), "The names are not the same");
        assertEquals(expected[0].getProfilePath(), results[0].getProfilePath(), "The profile paths are not the same");
    }

    @Test
    void testCreditsFindCrewPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[2];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");
        crew[1] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test2crew2name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[1];
        expected[0] = new Person(crew[0].getID(), crew[0].getName(), crew[0].getProfilePath());

        Person[] results = store.getCredits().findCrew("Test crew name");

        assertNotNull(results, "The function \"findCrew\" should never return null");
        assertEquals(expected.length, results.length, "There is only one person that matches, and that person was search for using there exact name. Therefore, an array with 1 person should be returned");
        assertEquals(expected[0].getID(), results[0].getID(), "The ID's are not the same");
        assertEquals(expected[0].getName(), results[0].getName(), "The names are not the same");
        assertEquals(expected[0].getProfilePath(), results[0].getProfilePath(), "The profile paths are not the same");
    }

    @Test
    void testCreditsFindCrewPosPart() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[1];
        expected[0] = new Person(crew[0].getID(), crew[0].getName(), crew[0].getProfilePath());

        Person[] results = store.getCredits().findCrew("name");

        assertNotNull(results, "The function \"findCrew\" should never return null");
        assertEquals(expected.length, results.length, "There is only one person, and that person was search for using part of there name. Therefore, an array with 1 person should be returned");
        assertEquals(expected[0].getID(), results[0].getID(), "The ID's are not the same");
        assertEquals(expected[0].getName(), results[0].getName(), "The names are not the same");
        assertEquals(expected[0].getProfilePath(), results[0].getProfilePath(), "The profile paths are not the same");
    }

    @Test
    void testCreditsFindCrewPos2Part() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[2];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");
        crew[1] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew n2me", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] expected = new Person[1];
        expected[0] = new Person(crew[0].getID(), crew[0].getName(), crew[0].getProfilePath());

        Person[] results = store.getCredits().findCrew("name");

        assertNotNull(results, "The function \"findCrew\" should never return null");
        assertEquals(expected.length, results.length, "There is only one person that matches out of the 2, and that person was search for using part of there name. Therefore, an array with 1 person should be returned");
        assertEquals(expected[0].getID(), results[0].getID(), "The ID's are not the same");
        assertEquals(expected[0].getName(), results[0].getName(), "The names are not the same");
        assertEquals(expected[0].getProfilePath(), results[0].getProfilePath(), "The profile paths are not the same");
    }

    @Test
    void testCreditsFindCrewNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,
                "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name",
                "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] results = store.getCredits().findCrew("department");

        assertNotNull(results, "The function \"findCrew\" should never return null");
        assertArrayEquals(new Person[0], results, "There is no crew member in the Credits store with that in there name. Therefore, an empty array should be returned");
    }

    @Test void testCreditsGetCastDefault() {
        assertNull(store.getCredits().getCast(1001), "The store is empty, so searching for any cast member will result in them not being found, thus returning null");
    }

    @Test void testCreditsGetCastPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person expected = new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath());

        Person result = store.getCredits().getCast(1001);

        assertNotNull(result, "The cast member exists, so should be able to be found");
        assertEquals(expected.getID(), result.getID(), "The IDs are not the same");
        assertEquals(expected.getName(), result.getName(), "The names are not the same");
        assertEquals(expected.getProfilePath(), result.getProfilePath(), "The profile paths are not the same");
    }

    @Test void testCreditsGetCastPos2() {
        CastCredit[] cast = new CastCredit[2];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2,"Test cast profile path2");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person expected = new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath());

        Person result = store.getCredits().getCast(1001);

        assertNotNull(result, "The cast member exists, so should be able to be found");
        assertEquals(expected.getID(), result.getID(), "The IDs are not the same");
        assertEquals(expected.getName(), result.getName(), "The names are not the same");
        assertEquals(expected.getProfilePath(), result.getProfilePath(), "The profile paths are not the same");
    }

    @Test void testCreditsGetCastNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,
                "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name",
                "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person result = store.getCredits().getCast(9999);

        assertNull(result, "There is no cast member with this ID number, so return null");
    }

    @Test void testCreditsGetCrewDefault() {
        assertNull(store.getCredits().getCrew(2001), "The store is empty, so searching for any crew member will result in them not being found, thus returning null");
    }

    @Test void testCreditsGetCrewPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person expected = new Person(crew[0].getID(), crew[0].getName(), crew[0].getProfilePath());

        Person result = store.getCredits().getCrew(2001);

        assertNotNull(result, "The crew member exists, so should be able to be found");
        assertEquals(expected.getID(), result.getID(), "The IDs are not the same");
        assertEquals(expected.getName(), result.getName(), "The names are not the same");
        assertEquals(expected.getProfilePath(), result.getProfilePath(), "The profile paths are not the same");
    }

    @Test void testCreditsGetCrewPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[2];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");
        crew[1] = new CrewCredit("202", "Test crew department2", 2002, "Test crew job2", "Test crew name2", "Test crew profile path2");

        store.getCredits().add(cast, crew, 1);

        Person expected = new Person(crew[0].getID(), crew[0].getName(), crew[0].getProfilePath());

        Person result = store.getCredits().getCrew(2001);

        assertNotNull(result, "The crew member exists, so should be able to be found");
        assertEquals(expected.getID(), result.getID(), "The IDs are not the same");
        assertEquals(expected.getName(), result.getName(), "The names are not the same");
        assertEquals(expected.getProfilePath(), result.getProfilePath(), "The profile paths are not the same");
    }

    @Test void testCreditsGetCrewNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person result = store.getCredits().getCrew(9999);

        assertNull(result, "There is no crew member with this ID number, so return null");
    }

    @Test void testCreditsGetCastFilmsDefault() {
        assertNotNull(store.getCredits().getCastFilms(1001), "The function \"getCastFilms\" should never return null");
        assertArrayEquals(new int[0], store.getCredits().getCastFilms(1001), "There is no movies in the store and no cast members. Therefore, should return an empty array");
    }

    @Test void testCreditsGetCastFilmsPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        int[] expected = {1};

        assertNotNull(store.getCredits().getCastFilms(1001), "The function \"getCastFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, store.getCredits().getCastFilms(1001)), "The cast member has only been in 1 movie, so that singular movie should be returned");
    }

    @Test void testCreditsGetCastFilmsPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 2);

        int[] expected = {1, 2};

        assertNotNull(store.getCredits().getCastFilms(1001), "The function \"getCastFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, store.getCredits().getCastFilms(1001)), "The cast member has been in 2 movies, so those movies should be returned");
    }

    @Test void testCreditsGetCastFilmsPos2Mix() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 3);

        CastCredit[] cast2 = new CastCredit[1];
        cast2[0] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");

        store.getCredits().add(cast2, crew, 2);

        int[] expected = {1, 3};

        assertNotNull(store.getCredits().getCastFilms(1001), "The function \"getCastFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, store.getCredits().getCastFilms(1001)), "The cast member has been in 2 movies, so those movies should be returned");
    }

    @Test void testCreditsGetCastFilmsNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertNotNull(store.getCredits().getCastFilms(1001), "The function \"getCastFilms\" should never return null");
        assertArrayEquals(new int[0], store.getCredits().getCastFilms(1002), "There are movies and credits in the store, but none that have that cast ID. Therefore, return an empty array");
    }

    @Test void testCreditsGetCrewFilmsDefault() {
        assertNotNull(store.getCredits().getCrewFilms(2002), "The function \"getCrewFilms\" should never return null");
        assertArrayEquals(new int[0], store.getCredits().getCrewFilms(2001), "There is no movies in the store and no crew members. Therefore, should return an empty array");
    }

    @Test void testCreditsGetCrewFilmsPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        int[] expected = {1};

        assertNotNull(store.getCredits().getCrewFilms(2001), "The function \"getCrewFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, store.getCredits().getCrewFilms(2001)), "The crew member has only been in 1 movie, so that singular movie should be returned");
    }

    @Test void testCreditsGetCrewFilmsPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 2);

        int[] expected = {1, 2};

        assertNotNull(store.getCredits().getCrewFilms(2001), "The function \"getCrewFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, store.getCredits().getCrewFilms(2001)), "The crew member has been in 2 movies, so those movies should be returned");
    }

    @Test void testCreditsGetCrewFilmsPos2Mix() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 3);

        CrewCredit[] crew2 = new CrewCredit[1];
        crew2[0] = new CrewCredit("202", "Test crew department", 2002, "Test crew job2", "Test crew name2", "Test crew profile path2");

        store.getCredits().add(cast, crew2, 2);

        int[] expected = {1, 3};

        assertNotNull(store.getCredits().getCrewFilms(2001), "The function \"getCrewFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, store.getCredits().getCrewFilms(2001)), "The crew member has been in 2 movies, so those movies should be returned");
    }

    @Test void testCreditsGetCrewFilmsNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertNotNull(store.getCredits().getCrewFilms(2002), "The function \"getCrewFilms\" should never return null");
        assertArrayEquals(new int[0], store.getCredits().getCrewFilms(2002), "There are movies and credits in the store, but none that have that crew ID. Therefore, return an empty array");
    }

    @Test void testCreditsGetCastStarsInFilmsDefault() {
        assertNotNull(store.getCredits().getCastStarsInFilms(1001), "The function \"getCastStarsInFilms\" should never return null");
        assertArrayEquals(new int[0], store.getCredits().getCastStarsInFilms(1001), "The store is empty, so there are no cast members to search for stars. Therefore, an empty array should be returned");
    }

    @Test void testCreditsGetCastStarsInFilmsPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        int[] results = store.getCredits().getCastStarsInFilms(1001);
        int[] expected = {1};

        assertNotNull(results, "The function \"getCastStarsInFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, results), "The cast member has only appeared in 1 movie, and was in the top 3 by order. Therefore, the array should consist of just that 1 movie");
    }

    @Test void testCreditsGetCastStarsInFilmsPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 2);

        int[] results = store.getCredits().getCastStarsInFilms(1001);
        int[] expected = {1, 2};

        assertNotNull(results, "The function \"getCastStarsInFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, results),"The cast member has appeared in 2 movies, and was in the top 3 by order in both. Therefore, the array should consist of both movies");
    }

    @Test void testCreditsGetStarsInFilmsPos2Multi() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 2);

        CastCredit[] cast2 = new CastCredit[1];
        cast2[0] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 1,"Test cast profile path2");

        store.getCredits().add(cast2, crew, 3);

        int[] results = store.getCredits().getCastStarsInFilms(1001);
        int[] expected = {1, 2};

        assertNotNull(results, "The function \"getCastStarsInFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, results), "The cast member has appeared in 2 movies out of the 3 in the store, and was in the top 3 by order in both. Therefore, the array should consist of both movies");
    }

    @Test void testCreditsGetStarsInFilmsPos2NotAllFirst() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CastCredit[] cast2 = new CastCredit[3];
        cast2[0] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 1, "Test cast profile path2");
        cast2[1] = new CastCredit(103, "Test cast character3", "Test cast creditID3", 1003, "Test cast name3", 2, "Test cast profile path3");
        cast2[2] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 3, "Test cast profile path");

        store.getCredits().add(cast2, crew, 3);

        int[] results = store.getCredits().getCastStarsInFilms(1001);
        int[] expected = {1, 3};

        assertNotNull(results, "The function \"getCastStarsInFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, results), "The cast member has appeared in 2 movies but was not the first billing in all films, and was in the top 3 by order in both. Therefore, the array should consist of both movies");
    }

    @Test void testCreditsGetStarsInFilmsPos2Mix() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CastCredit[] cast2 = new CastCredit[4];
        cast2[0] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 1, "Test cast profile path2");
        cast2[1] = new CastCredit(103, "Test cast character3", "Test cast creditID3", 1003, "Test cast name3", 2, "Test cast profile path3");
        cast2[2] = new CastCredit(104, "Test cast character4", "Test cast creditID4", 1004, "Test cast name4", 3, "Test cast profile path4");
        cast2[3] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 4, "Test cast profile path");

        store.getCredits().add(cast2, crew, 3);

        int[] results = store.getCredits().getCastStarsInFilms(1001);
        int[] expected = {1};

        assertNotNull(results, "The function \"getCastStarsInFilms\" should never return null");
        assertTrue(checkContentsOfArray(expected, results), "The cast member has appeared in 2 movies but was not the top billing in all films, and was in the top 3 by order in only 1. Therefore, the array should consist of just the 1 movie");
    }

    @Test void testCreditsGetCastStarsInFilmsNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        int[] results = store.getCredits().getCastStarsInFilms(1002);
        int[] expected = new int[0];

        assertNotNull(results, "The function \"getCastStarsInFilms\" should never return null");
        assertArrayEquals(expected, results, "There are cast members, but the one selected doesn't exist. Therefore, return an empty array");
    }

    @Test void testCreditsGetCastStarsInFilmsNegOrder() {
        CastCredit[] cast = new CastCredit[4];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 1, "Test cast profile path2");
        cast[1] = new CastCredit(103, "Test cast character3", "Test cast creditID3", 1003, "Test cast name3", 2, "Test cast profile path3");
        cast[2] = new CastCredit(104, "Test cast character4", "Test cast creditID4", 1004, "Test cast name4", 3, "Test cast profile path4");
        cast[3] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 4, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        int[] results = store.getCredits().getCastStarsInFilms(1001);
        int[] expected = new int[0];

        assertNotNull(results, "The function \"getCastStarsInFilms\" should never return null");
        assertArrayEquals(expected, results, "The cast member is in a film, but not in the top 3 by order. Therefore, there are no films to be returned, so an empty array is returned");
    }

    @Test void testCreditsGetMostCastCreditsDefault() {
        assertNotNull(store.getCredits().getMostCastCredits(5), "The function \"getMostCastCredits\" should never return null");
        assertArrayEquals(new Person[0], store.getCredits().getMostCastCredits(5), "The store has no cast members or movies, so should return an empty array of Person objects");
    }

    @Test void testCreditsGetMostCastCreditsPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] results = store.getCredits().getMostCastCredits(1);
        Person[] expected = {new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath())};

        assertNotNull(results, "The function \"getMostCastCredits\" should never return null");
        assertEquals(expected.length, results.length, "The array of Person objects is the wrong size. As there is only 1 cast member in the store, and only 1 was requested, the returned array should be of size 1");

        for (int i = 0; i < expected.length; i++) {
            assertNotNull(results[i], "The person at index "+ i + " is null when there should be no person who is null");
            assertEquals(expected[i].getID(), results[i].getID(), "The person at index " + i + "has the wrong ID (Expected: " + expected[i].getID() + ", Received: "+ results[i].getID() + ")");
            assertEquals(expected[i].getName(), results[i].getName(), "The person at index " + i + " has the wrong name (Expected: " + expected[i].getName() + ", Received: " + results[i].getName() + ")");
            assertEquals(expected[i].getProfilePath(), results[i].getProfilePath(), "The person at index "+ i +" has the wrong profile path (Expected: "+ expected[i].getProfilePath() +" , Received: "+ results[i].getProfilePath() +" )");
        }
    }


    @Test void testCreditsGetMostCastCreditsPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 2);

        Person[] results = store.getCredits().getMostCastCredits(1);
        Person[] expected = { new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath()) };

        assertNotNull(results, "The function \"getMostCastCredits\" should never return null");
        assertEquals(expected.length, results.length, "The array of Person objects is the wrong size. As there is only 1 unique cast member in the store, and only 1 was requested, the returned array should be of size 1");

        for (int i = 0; i < expected.length; i++) {
            assertNotNull(results[i], "The person at index "+ i + " is null when there should be no person who is null");
            assertEquals(expected[i].getID(), results[i].getID(), "The person at index " + i + "has the wrong ID (Expected: " + expected[i].getID() + ", Received: "+ results[i].getID() + ")");
            assertEquals(expected[i].getName(), results[i].getName(), "The person at index " + i + " has the wrong name (Expected: " + expected[i].getName() + ", Received: " + results[i].getName() + ")");
            assertEquals(expected[i].getProfilePath(), results[i].getProfilePath(), "The person at index "+ i +" has the wrong profile path (Expected: "+ expected[i].getProfilePath() +" , Received: "+ results[i].getProfilePath() +" )");
        }
    }

    @Test void testCreditsGetMostCastCreditsPos2Multi() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CastCredit[] cast2 = new CastCredit[2];
        cast2[0] = cast[0];
        cast2[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");

        store.getCredits().add(cast2, crew, 2);

        Person[] results = store.getCredits().getMostCastCredits(2);
        Person[] expected = {new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath()), new Person(cast2[1].getID(), cast2[1].getName(), cast2[1].getProfilePath())};

        assertNotNull(results, "The function \"getMostCastCredits\" should never return null");
        assertEquals(expected.length, results.length, "The array of Person objects is the wrong size. As there are 2 unique cast members in the store, and 2 was requested, the returned array should be of size 2");

        for (int i = 0; i < expected.length; i++) {
            assertNotNull(results[i], "The person at index "+ i + " is null when there should be no person who is null");
            assertEquals(expected[i].getID(), results[i].getID(), "The person at index " + i + "has the wrong ID (Expected: " + expected[i].getID() + ", Received: "+ results[i].getID() + ")");
            assertEquals(expected[i].getName(), results[i].getName(), "The person at index " + i + " has the wrong name (Expected: " + expected[i].getName() + ", Received: " + results[i].getName() + ")");
            assertEquals(expected[i].getProfilePath(), results[i].getProfilePath(), "The person at index "+ i +" has the wrong profile path (Expected: "+ expected[i].getProfilePath() +" , Received: "+ results[i].getProfilePath() +" )");
        }
    }

    @Test void testCreditsGetMostCastCreditsPosOver() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] results = store.getCredits().getMostCastCredits(3);
        Person[] expected = { new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath()) };

        assertNotNull(results, "The function \"getMostCastCredits\" should never return null");
        assertEquals(expected.length, results.length, "The array of Person objects is the wrong size. As there is only 1 cast member in the store, and 3 was requested, the returned array should be of size 1");

        for (int i = 0; i < expected.length; i++) {
            assertNotNull(results[i], "The person at index "+ i + " is null when there should be no person who is null");
            assertEquals(expected[i].getID(), results[i].getID(), "The person at index " + i + "has the wrong ID (Expected: " + expected[i].getID() + ", Received: "+ results[i].getID() + ")");
            assertEquals(expected[i].getName(), results[i].getName(), "The person at index " + i + " has the wrong name (Expected: " + expected[i].getName() + ", Received: " + results[i].getName() + ")");
            assertEquals(expected[i].getProfilePath(), results[i].getProfilePath(), "The person at index "+ i +" has the wrong profile path (Expected: "+ expected[i].getProfilePath() +" , Received: "+ results[i].getProfilePath() +" )");
        }
    }

    @Test void testCreditsGetMostCastCreditsPos2TwoInMovie() {
        CastCredit[] cast = new CastCredit[3];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,
                "Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2,
                "Test cast profile path2");
        cast[2] = new CastCredit(103, "Test cast character3", "Test cast creditID3", 1001, "Test cast name", 3,
                "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name",
                "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        Person[] results = store.getCredits().getMostCastCredits(2);
        Person[] expected = { new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath()), new Person(cast[1].getID(), cast[1].getName(), cast[1].getProfilePath()) };

        assertNotNull(results, "The function \"getMostCastCredits\" should never return null");
        assertEquals(expected.length, results.length, "The array of Person objects is the wrong size. As there are 2 unique cast members in the store (a cast member had 2 roles in the same film), and 2 was requested, the returned array should be of size 2");

        for (int i = 0; i < expected.length; i++) {
            assertNotNull(results[i], "The person at index "+ i + " is null when there should be no person who is null");
            assertEquals(expected[i].getID(), results[i].getID(), "The person at index " + i + "has the wrong ID (Expected: " + expected[i].getID() + ", Received: "+ results[i].getID() + ")");
            assertEquals(expected[i].getName(), results[i].getName(), "The person at index " + i + " has the wrong name (Expected: " + expected[i].getName() + ", Received: " + results[i].getName() + ")");
            assertEquals(expected[i].getProfilePath(), results[i].getProfilePath(), "The person at index "+ i +" has the wrong profile path (Expected: "+ expected[i].getProfilePath() +" , Received: "+ results[i].getProfilePath() +" )");
        }
    }

    @Test void testCreditsGetMostCastCreditsPos2MultiLimit() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CastCredit[] cast2 = new CastCredit[2];
        cast2[0] = cast[0];
        cast2[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");

        store.getCredits().add(cast2, crew, 2);

        Person[] results = store.getCredits().getMostCastCredits(1);
        Person[] expected = { new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath()) };

        assertNotNull(results, "The function \"getMostCastCredits\" should never return null");
        assertEquals(expected.length, results.length, "The array of Person objects is the wrong size. As there are 2 unique cast members in the store, and 1 was requested, the returned array should be of size 1");

        for (int i = 0; i < expected.length; i++) {
            assertNotNull(results[i], "The person at index " + i + " is null when there should be no person who is null");
            assertEquals(expected[i].getID(), results[i].getID(), "The person at index " + i + "has the wrong ID (Expected: " + expected[i].getID() + ", Received: " + results[i].getID() + ")");
            assertEquals(expected[i].getName(), results[i].getName(), "The person at index " + i + " has the wrong name (Expected: " + expected[i].getName() + ", Received: " + results[i].getName() + ")");
            assertEquals(expected[i].getProfilePath(), results[i].getProfilePath(), "The person at index " + i + " has the wrong profile path (Expected: " + expected[i].getProfilePath() + " , Received: " + results[i].getProfilePath() + " )");
        }
    }

    @Test void testCreditsGetMostCastCreditsPos2MultiOver() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        CastCredit[] cast2 = new CastCredit[2];
        cast2[0] = cast[0];
        cast2[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");

        store.getCredits().add(cast2, crew, 5);

        Person[] results = store.getCredits().getMostCastCredits(2);
        Person[] expected = { new Person(cast[0].getID(), cast[0].getName(), cast[0].getProfilePath()), new Person(cast2[1].getID(), cast2[1].getName(), cast2[1].getProfilePath()) };

        assertNotNull(results, "The function \"getMostCastCredits\" should never return null");
        assertEquals(expected.length, results.length, "The array of Person objects is the wrong size. As there are 2 unique cast members in the store, and 5 was requested, the returned array should be of size 2");

        for (int i = 0; i < expected.length; i++) {
            assertNotNull(results[i], "The person at index " + i + " is null when there should be no person who is null");
            assertEquals(expected[i].getID(), results[i].getID(), "The person at index " + i + "has the wrong ID (Expected: " + expected[i].getID() + ", Received: " + results[i].getID() + ")");
            assertEquals(expected[i].getName(), results[i].getName(), "The person at index " + i + " has the wrong name (Expected: " + expected[i].getName() + ", Received: " + results[i].getName() + ")");
            assertEquals(expected[i].getProfilePath(), results[i].getProfilePath(), "The person at index " + i + " has the wrong profile path (Expected: " + expected[i].getProfilePath() + " , Received: " + results[i].getProfilePath() + " )");
        }
    }

    @Test void testCreditsGetNumCastCreditsDefault() {
        assertEquals(-1, store.getCredits().getNumCastCredits(1001), "There are no cast members in the store, so should return -1");
    }

    @Test void testCreditsGetNumCastCreditsPos() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertEquals(1, store.getCredits().getNumCastCredits(1001), "The cast member has been in 1 movie, and has 1 credit in the movie. Therefore, the number of credits for the cast member is 1");
    }

    @Test void testCreditsGetNumCastCreditsPos2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 2);

        assertEquals(2, store.getCredits().getNumCastCredits(1001), "The cast member has been in 2 movies, and has 1 credit in the movie. Therefore, the number of credits for the cast member is 2");
    }

    @Test void testCreditsGetNumCastCreditsPos2Multi() {
        CastCredit[] cast = new CastCredit[2];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1,
                "Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2,
                "Test cast profile path2");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name",
                "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 2);

        assertEquals(2, store.getCredits().getNumCastCredits(1001), "The cast member has been in 2 movies, and has 1 credit in the movie. Therefore, the number of credits for the cast member is 2");
    }

    @Test void testCreditsGetNumCastCreditsPos2TwoInMovie() {
        CastCredit[] cast = new CastCredit[3];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        cast[1] = new CastCredit(102, "Test cast character2", "Test cast creditID2", 1002, "Test cast name2", 2, "Test cast profile path2");
        cast[2] = new CastCredit(103, "Test cast character3", "Test cast creditID3", 1001, "Test cast name", 3,
                "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertEquals(2, store.getCredits().getNumCastCredits(1001), "The cast member has been in 1 movie, and has 2 credits in the movie. Therefore, the number of credits for the cast member is 2");
    }

    @Test void testCreditsGetNumCastCreditsNeg() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertEquals(-1, store.getCredits().getNumCastCredits(1002), "The cast member does not appear in any movie (even though there are both movies and credits). Therefore, -1 should be returned");
    }

    @Test void testCreditsSizeDefault() {
        assertEquals(0, store.getCredits().size(), "The Credits store is empty, so there should be no films");
    }

    @Test void testCreditsSizeAfterAdd() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);

        assertEquals(1, store.getCredits().size(), "1 movie has been added (number of cast and crew do not matter for this function), so expected a size of 1");
    }

    @Test void testCreditsSizeAfterAdd2() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name",  "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 2);

        assertEquals(2, store.getCredits().size(), "2 movies has been added (number of cast and crew do not matter for this function), so expected a size of 2");
    }

    @Test void testCreditsSizeAfterAddRemove() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().remove(1);

        assertEquals(0, store.getCredits().size(),"1 movie was added, and then removed. No other movie was added, so the size should be 0");
    }

    @Test void testCreditsSizeAfterAdd2Remove() {
        CastCredit[] cast = new CastCredit[1];
        CrewCredit[] crew = new CrewCredit[1];

        cast[0] = new CastCredit(101, "Test cast character", "Test cast creditID1", 1001, "Test cast name", 1, "Test cast profile path");
        crew[0] = new CrewCredit("201", "Test crew department", 2001, "Test crew job", "Test crew name", "Test crew profile path");

        store.getCredits().add(cast, crew, 1);
        store.getCredits().add(cast, crew, 2);
        store.getCredits().remove(1);

        assertEquals(1, store.getCredits().size(),"2 movies were added, then 1 was removed. Therefore the total number of movies left is 1");
    }

    @Test void testCreditsSizeAfterRemove() {
        store.getCredits().remove(9);
        assertEquals(0, store.getCredits().size(), "A film was attempted to be removed, but the store was empty. Therefore, the store is still empty, thus has a size of 0");
    }

}
