import stores.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)

class MoviesTest {
    private int batchSize = 20;
    private int fakeID = 10;

    private Stores stores = new Stores();
    private Stores batchStores = new Stores();

    // batch random data
    private ArrayList<Integer> IDs = new ArrayList<Integer>();
    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> originalTitles = new ArrayList<String>();
    private ArrayList<String> overviews = new ArrayList<String>();
    private ArrayList<String> taglines = new ArrayList<String>();
    private ArrayList<String> statuses = new ArrayList<String>();
    private ArrayList<Genre[]> genresList = new ArrayList<Genre[]>();
    private ArrayList<LocalDate> releases = new ArrayList<LocalDate>();
    private ArrayList<Long> budgets = new ArrayList<Long>();
    private ArrayList<Long> revenues = new ArrayList<Long>();
    private ArrayList<String[]> languagesList = new ArrayList<String[]>();
    private ArrayList<String> originalLanguages = new ArrayList<String>();
    private ArrayList<Double> runtimes = new ArrayList<Double>();
    private ArrayList<String> homepages = new ArrayList<String>();
    private ArrayList<Boolean> adults = new ArrayList<Boolean>();
    private ArrayList<Boolean> videos = new ArrayList<Boolean>();
    private ArrayList<String> posters = new ArrayList<String>();
    
    private ArrayList<Genre> bankOfGenres = new ArrayList<Genre>();

    // languages for random adding
    private final String[][] iso639Data = {
        {"Abkhazian","ab"},
        {"Afar","aa"},
        {"Afrikaans","af"},
        {"Akan","ak"},
        {"Albanian","sq"},
        {"Amharic","am"},
        {"Arabic","ar"},
        {"Aragonese","an"},
        {"Armenian","hy"},
        {"Assamese","as"},
        {"Avaric","av"},
        {"Avestan","ae"},
        {"Aymara","ay"},
        {"Azerbaijani","az"},
        {"Bambara","bm"},
        {"Bashkir","ba"},
        {"Basque","eu"},
        {"Belarusian","be"},
        {"Bengali","bn"},
        {"Bislama","bi"},
        {"Bosnian","bs"},
        {"Breton","br"},
        {"Bulgarian","bg"},
        {"Burmese","my"},
        {"Catalan, Valencian","ca"},
        {"Chamorro","ch"},
        {"Chechen","ce"},
        {"Chichewa, Chewa, Nyanja","ny"},
        {"Chinese","zh"},
        {"Church Slavic, Old Slavonic, Church Slavonic, Old Bulgarian, Old Church Slavonic","cu"},
        {"Chuvash","cv"},
        {"Cornish","kw"},
        {"Corsican","co"},
        {"Cree","cr"},
        {"Croatian","hr"},
        {"Czech","cs"},
        {"Danish","da"},
        {"Divehi, Dhivehi, Maldivian","dv"},
        {"Dutch, Flemish","nl"},
        {"Dzongkha","dz"},
        {"English","en"},
        {"Esperanto","eo"},
        {"Estonian","et"},
        {"Ewe","ee"},
        {"Faroese","fo"},
        {"Fijian","fj"},
        {"Finnish","fi"},
        {"French","fr"},
        {"Western Frisian","fy"},
        {"Fulah","ff"},
        {"Gaelic, Scottish Gaelic","gd"},
        {"Galician","gl"},
        {"Ganda","lg"},
        {"Georgian","ka"},
        {"German","de"},
        {"Greek, Modern (1453–)","el"},
        {"Kalaallisut, Greenlandic","kl"},
        {"Guarani","gn"},
        {"Gujarati","gu"},
        {"Haitian, Haitian Creole","ht"},
        {"Hausa","ha"},
        {"Hebrew","he"},
        {"Herero","hz"},
        {"Hindi","hi"},
        {"Hiri Motu","ho"},
        {"Hungarian","hu"},
        {"Icelandic","is"},
        {"Ido","io"},
        {"Igbo","ig"},
        {"Indonesian","id"},
        {"Interlingua (International Auxiliary Language Association)","ia"},
        {"Interlingue, Occidental","ie"},
        {"Inuktitut","iu"},
        {"Inupiaq","ik"},
        {"Irish","ga"},
        {"Italian","it"},
        {"Japanese","ja"},
        {"Javanese","jv"},
        {"Kannada","kn"},
        {"Kanuri","kr"},
        {"Kashmiri","ks"},
        {"Kazakh","kk"},
        {"Central Khmer","km"},
        {"Kikuyu, Gikuyu","ki"},
        {"Kinyarwanda","rw"},
        {"Kirghiz, Kyrgyz","ky"},
        {"Komi","kv"},
        {"Kongo","kg"},
        {"Korean","ko"},
        {"Kuanyama, Kwanyama","kj"},
        {"Kurdish","ku"},
        {"Lao","lo"},
        {"Latin","la"},
        {"Latvian","lv"},
        {"Limburgan, Limburger, Limburgish","li"},
        {"Lingala","ln"},
        {"Lithuanian","lt"},
        {"Luba-Katanga","lu"},
        {"Luxembourgish, Letzeburgesch","lb"},
        {"Macedonian","mk"},
        {"Malagasy","mg"},
        {"Malay","ms"},
        {"Malayalam","ml"},
        {"Maltese","mt"},
        {"Manx","gv"},
        {"Maori","mi"},
        {"Marathi","mr"},
        {"Marshallese","mh"},
        {"Mongolian","mn"},
        {"Nauru","na"},
        {"Navajo, Navaho","nv"},
        {"North Ndebele","nd"},
        {"South Ndebele","nr"},
        {"Ndonga","ng"},
        {"Nepali","ne"},
        {"Norwegian","no"},
        {"Norwegian Bokmål","nb"},
        {"Norwegian Nynorsk","nn"},
        {"Sichuan Yi, Nuosu","ii"},
        {"Occitan","oc"},
        {"Ojibwa","oj"},
        {"Oriya","or"},
        {"Oromo","om"},
        {"Ossetian, Ossetic","os"},
        {"Pali","pi"},
        {"Pashto, Pushto","ps"},
        {"Persian","fa"},
        {"Polish","pl"},
        {"Portuguese","pt"},
        {"Punjabi, Panjabi","pa"},
        {"Quechua","qu"},
        {"Romanian, Moldavian, Moldovan","ro"},
        {"Romansh","rm"},
        {"Rundi","rn"},
        {"Russian","ru"},
        {"Northern Sami","se"},
        {"Samoan","sm"},
        {"Sango","sg"},
        {"Sanskrit","sa"},
        {"Sardinian","sc"},
        {"Serbian","sr"},
        {"Shona","sn"},
        {"Sindhi","sd"},
        {"Sinhala, Sinhalese","si"},
        {"Slovak","sk"},
        {"Slovenian","sl"},
        {"Somali","so"},
        {"Southern Sotho","st"},
        {"Spanish, Castilian","es"},
        {"Sundanese","su"},
        {"Swahili","sw"},
        {"Swati","ss"},
        {"Swedish","sv"},
        {"Tagalog","tl"},
        {"Tahitian","ty"},
        {"Tajik","tg"},
        {"Tamil","ta"},
        {"Tatar","tt"},
        {"Telugu","te"},
        {"Thai","th"},
        {"Tibetan","bo"},
        {"Tigrinya","ti"},
        {"Tonga (Tonga Islands)","to"},
        {"Tsonga","ts"},
        {"Tswana","tn"},
        {"Turkish","tr"},
        {"Turkmen","tk"},
        {"Twi","tw"},
        {"Uighur, Uyghur","ug"},
        {"Ukrainian","uk"},
        {"Urdu","ur"},
        {"Uzbek","uz"},
        {"Venda","ve"},
        {"Vietnamese","vi"},
        {"Volapük","vo"},
        {"Walloon","wa"},
        {"Welsh","cy"},
        {"Wolof","wo"},
        {"Xhosa","xh"},
        {"Yiddish","yi"},
        {"Yoruba","yo"},
        {"Zhuang, Chuang","za"},
        {"Zulu","zu"}
    };
        

    @BeforeEach
    void setup() {

        stores = new Stores();
        batchStores = new Stores();

        bankOfGenres = new ArrayList<Genre>();

        // Bank of generes to randomly select a genre from
        bankOfGenres.add(new Genre(1, "Horror"));
        bankOfGenres.add(new Genre(2, "Comedy"));
        bankOfGenres.add(new Genre(3, "Action"));
        bankOfGenres.add(new Genre(4, "Drama"));
        bankOfGenres.add(new Genre(5, "Romantic"));
        bankOfGenres.add(new Genre(6, "Thriller"));
        bankOfGenres.add(new Genre(7, "Anime"));
        bankOfGenres.add(new Genre(8, "SciFi"));
        bankOfGenres.add(new Genre(9, "Musical"));
        bankOfGenres.add(new Genre(10, "Documentary"));

        //  generate first fake data
        int id = 1;
        String title = "title";
        String originalTitle = "originalTitle";
        String overview = "overview";
        String tagline = "tagline";
        String status = "status";
        Genre[] genres = new Genre[2];
        genres[0] = new Genre(1, "Genre");
        genres[1] = new Genre(2, "Genre2");

        LocalDate release = LocalDate.of(2000, 1, 1);

        long budget = 1;
        long revenue = 2;
        String[] languages = new String[2];
        languages[0] = "en";
        languages[1] = "th";
        String originalLanguage = "en";
        double runtime = 90.0;
        String homepage = "http://homepage.com/homepage";
        boolean adult = false;
        boolean video = false;
        String poster = "/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg";


        stores.getMovies().add(id, title, originalTitle, overview, tagline, status,
        genres, release, budget, revenue, languages, originalLanguage,
        runtime, homepage, adult, video, poster);
        
        release = LocalDate.of(2001, 1, 1);

        stores.getMovies().add(2, "Toy Story", "Toy Story", "Toy Story is a good movie", "Toy Story is a really good movie", "released",
        genres, release, budget, revenue, languages, originalLanguage,
        runtime, homepage, adult, video, poster);

        int filmID = 2; 
        int collectionID = 1; 
        String collectionName = "Toy Story Series";
        String collectionPosterPath = "collectionposter";
        String collectionBackdropPath = "collectionbackdrop";
        stores.getMovies().addToCollection(filmID, collectionID, collectionName, collectionPosterPath, collectionBackdropPath);

        stores.getMovies().setPopularity(id, 3.0f);

        // generate lots of data for batch testing
       batchGeneration();

    }

    void batchGeneration(){
        // add data to arraylists
        batchData(batchSize);
        // add data from arraylists to batchMovies
        for (int i =0; i<batchSize;i++){
            batchStores.getMovies().add(IDs.get(i), titles.get(i), originalTitles.get(i),overviews.get(i),
                taglines.get(i), statuses.get(i),genresList.get(i), releases.get(i), 
                budgets.get(i), revenues.get(i), languagesList.get(i), originalLanguages.get(i), 
                runtimes.get(i), homepages.get(i),adults.get(i),videos.get(i),posters.get(i));
        }

        
    }

    void batchData(int batchSize){
        
        Random random = new Random();
        int stringLength = 10;
        double upperRuntime = 180.0;
        double lowerRuntime = 70.0;
        double upperLong = 180.0;
        double lowerLong = 70.0;

        IDs = new ArrayList<Integer>();
        titles = new ArrayList<String>();
        originalTitles = new ArrayList<String>();
        overviews = new ArrayList<String>();
        taglines = new ArrayList<String>();
        statuses = new ArrayList<String>();
        genresList = new ArrayList<Genre[]>();
        releases = new ArrayList<LocalDate>();
        budgets = new ArrayList<Long>();
        revenues = new ArrayList<Long>();
        languagesList = new ArrayList<String[]>();
        originalLanguages = new ArrayList<String>();
        runtimes = new ArrayList<Double>();
        homepages = new ArrayList<String>();
        adults = new ArrayList<Boolean>();
        videos = new ArrayList<Boolean>();
        posters = new ArrayList<String>();
        

        for (int j = 0; j<batchSize; j++)
        {
            // add the data to arraylists
            IDs.add(j);
            titles.add(randStringMaker(stringLength));
            originalTitles.add(randStringMaker(stringLength));
            overviews.add(randStringMaker(stringLength));
            taglines.add(randStringMaker(stringLength));
            statuses.add(randStringMaker(stringLength));
            genresList.add(randGenres());
            releases.add(randCalendar());    
            budgets.add((long) (random.nextLong() * (upperLong - lowerLong) + lowerLong));
            revenues.add((long) (random.nextLong() * (upperLong - lowerLong) + lowerLong));
            languagesList.add(randLanguages());
            originalLanguages.add(randStringMaker(stringLength));
            runtimes.add(random.nextDouble() * (upperRuntime - lowerRuntime) + lowerRuntime);
            homepages.add(randStringMaker(stringLength));
            adults.add(random.nextBoolean());
            videos.add(random.nextBoolean());
            posters.add(randStringMaker(stringLength));
            
        }
        // IDs are shuffled rather than randomly generated so that they are unique
        Collections.shuffle(IDs);

    }

    /**
     * @param stringLength how long the string should be
     * @return random string of stringlength
     */
    String randStringMaker(int stringLength){
        Random random = new Random(); 
        int lowerLimit = 97; // letter 'a'
        int upperLimit = 122; // letter 'z'
        
        String generatedString = random.ints(lowerLimit, upperLimit + 1)
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    /**
     * @return random Calendar date between 1950 and 2020
     */
    LocalDate randCalendar(){
        Random random = new Random(); 
        
        LocalDate release = LocalDate.of(random.nextInt(70)+1950, 1, 1);

        return release;
    }

    Genre[] randGenres(){
        Random random = new Random(); 
        // make 1 or 2 genres
        int numberOfGenres = random.nextInt(1)+1;
        
        Genre[] randGenres = new Genre[numberOfGenres];
        for (int i = 0; i < numberOfGenres; i++){
            randGenres[i] = bankOfGenres.get(random.nextInt(bankOfGenres.size()));
        }

        return randGenres;
    }

    String[] randLanguages(){
        Random random = new Random(); 
        // make 1 - 5 languages
        int numberOfLanguages = random.nextInt(4)+1;

        Set<String> randLanguages = new HashSet<String>();;
        
        
        // add random languages to set
        for (int i = 0; i < numberOfLanguages; i++){
            randLanguages.add(iso639Data[random.nextInt(iso639Data.length)][1]);
        }

        //  make array of unique values
        String[] uniqueRandLanguages = new String[randLanguages.size()];
        randLanguages.toArray(uniqueRandLanguages);

        return uniqueRandLanguages;
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


    @Test void testMoviesAddPos() {
        Genre[] tmpGenre = {new Genre(99, "newGenre1")};
        LocalDate tmpRelease = LocalDate.of(2020, 1, 1);
        String[] tmpLanguages = {"en", "fr"};

        assertTrue(stores.getMovies().add(99, "newTitle", "newOriginalTitle", "newOverview", "newTagline", "newStatus", tmpGenre, tmpRelease, 1000000000, 2000000000, tmpLanguages, "en", 120.0, "newHomepage", false, true, "newPoster"), "This is a new film with a unique ID, so should be able to be added");
    }


    @Test void testMoviesAddNeg() {
        Genre[] tmpGenre = { new Genre(99, "newGenre1") };
        LocalDate tmpRelease = LocalDate.of(2020, 1, 1);
        String[] tmpLanguages = { "en", "fr" };

        assertFalse(stores.getMovies().add(1, "newTitle", "newOriginalTitle", "newOverview", "newTagline", "newStatus", tmpGenre, tmpRelease, 1000000000, 2000000000, tmpLanguages, "en", 120.0, "newHomepage", false, true,"newPoster"), "This film does not have a unique ID, so should not be added");
    }


    @Test void testMoviesRemovePos() {
        assertTrue(stores.getMovies().remove(1), "The film id is valid, so should be able to be removed");
    }

    @Test void testMoviesRemoveNeg() {
        assertFalse(stores.getMovies().remove(99), "Cannot remove a film ID that is not in the store");
    }


    @Test void testMoviesGetAllIDs() {
        int[] expected = {1, 2};
        int[] result = stores.getMovies().getAllIDs();

        assertTrue(checkContentsOfArray(expected, result), "The list of IDs does not match what is expected");
    }


    @Test void testMoviesGetAllIDsReleasedInRangePos() {
        int[] expected = {1};
        int[] result = stores.getMovies().getAllIDsReleasedInRange(LocalDate.of(1999, 12, 31), LocalDate.of(2000, 12, 31));

        assertTrue(checkContentsOfArray(expected, result), "The list of IDs does not match what is expected");
    }


    @Test void testMoviesGetAllIDsReleasedInRangeNeg() {
        int[] expected = {};
        int[] result = stores.getMovies().getAllIDsReleasedInRange(LocalDate.of(1900, 1, 1), LocalDate.of(1999, 12, 31));

        assertTrue(checkContentsOfArray(expected, result), "The list of IDs does not match what is expected");
    }


    @Test void testMoviesGetTitlePos() {
        assertEquals("title", stores.getMovies().getTitle(1), "Incorrect value returned.");
    }


    @Test void testMoviesGetTitleNeg() {
        assertNull(stores.getMovies().getTitle(fakeID),"Getting values for a non existent ID should return null.");
    }


    @Test void testMoviesGetTitleAll() {
        ArrayList<String> resultTitles = new ArrayList<String>();
        for (int i = 0; i< batchSize; i++){
            resultTitles.add(batchStores.getMovies().getTitle(IDs.get(i)));
        }
        assertEquals(titles, resultTitles, "Incorrect values returned.");
        
    }

    @Test void testMoviesGetOriginalTitlePos() {
        assertEquals("originalTitle", stores.getMovies().getOriginalTitle(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetOriginalTitleNeg() {
        assertNull(stores.getMovies().getOriginalTitle(fakeID),"Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetOriginalTitleAll() {
        ArrayList<String> resultOriginalTitles = new ArrayList<String>();

        for (int i = 0; i< batchSize; i++){
            resultOriginalTitles.add(batchStores.getMovies().getOriginalTitle(IDs.get(i)));
        }
        
        assertEquals(originalTitles, resultOriginalTitles, "Incorrect values returned.");
    }

    @Test void testMoviesGetOverviewPos() {
        assertEquals("overview", stores.getMovies().getOverview(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetOverviewNeg() { 
        assertNull(stores.getMovies().getOverview(fakeID), "Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetOverviewAll() {
        ArrayList<String> resultOverview = new ArrayList<String>();

        for (int i = 0; i< batchSize; i++){
            resultOverview.add(batchStores.getMovies().getOverview(IDs.get(i)));
        }

        assertEquals(overviews, resultOverview, "Incorrect values returned.");
    }

    @Test void testMoviesGetTaglinePos() {
        assertEquals("tagline", stores.getMovies().getTagline(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetTaglineNeg() {        
        assertNull( stores.getMovies().getTagline(fakeID), "Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetTaglineAll() {
        ArrayList<String> resultTagline = new ArrayList<String>();

        for (int i = 0; i< batchSize; i++){
            resultTagline.add(batchStores.getMovies().getTagline(IDs.get(i)));
        }

        assertEquals(taglines, resultTagline, "Incorrect values returned.");
    }

    @Test void testMoviesGetStatusPos() {
        assertEquals("status", stores.getMovies().getStatus(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetStatusNeg() {
        assertNull(stores.getMovies().getStatus(fakeID), "Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetStatusAll() {
        ArrayList<String> resultStatus = new ArrayList<String>();

        for (int i = 0; i< batchSize; i++){
            resultStatus.add(batchStores.getMovies().getStatus(IDs.get(i)));
        }

        assertEquals(statuses, resultStatus, "Incorrect values returned.");
    }

    @Test void testMoviesGetGenrePos() {
        boolean allSame = true;

        Genre[] testGenres = stores.getMovies().getGenres(1);
        Genre[] tmpGenres = new Genre[2];
        tmpGenres[0] = new Genre(1, "Genre");
        tmpGenres[1] = new Genre(2, "Genre2");
        
        boolean tmpSame = false;

        assertEquals(tmpGenres.length, testGenres.length, "They are not the same length.");

        for (int i = 0; i < tmpGenres.length; i++){
            tmpSame = (0 == tmpGenres[i].compareTo(testGenres[i]));
            if (!tmpSame){
                allSame = false;
            }
        }
        
        assertTrue(allSame, "Incorrect values returned.");
    }

    @Test void testMoviesGetGenreAll() {
        ArrayList<Genre[]> resultGenre = new ArrayList<Genre[]>();

        for (int i = 0; i< batchSize; i++){
            resultGenre.add(batchStores.getMovies().getGenres(IDs.get(i)));
        }

        assertEquals(genresList, resultGenre, "Incorrect values returned.");
    }


    @Test void testMoviesGenreNegID() {
        assertNull(stores.getMovies().getGenres(fakeID), "Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetReleasePos() {
        LocalDate tmpRelease = LocalDate.of(2000, 1, 1);
        assertEquals(0, stores.getMovies().getRelease(1).compareTo(tmpRelease), "Incorrect value returned.");
    }

    @Test void testMoviesGetReleaseNegVal() {
        assertNull( stores.getMovies().getRelease(fakeID), "Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetReleaseAll() {
        ArrayList<LocalDate> resultRelease = new ArrayList<LocalDate>();

        for (int i = 0; i< batchSize; i++){
            resultRelease.add(batchStores.getMovies().getRelease(IDs.get(i)));
        }

        assertEquals(releases, resultRelease, "Does not return the correct values.");
    }

    @Test void testMoviesGetReleaseNeg() {
        assertNull( stores.getMovies().getRelease(fakeID),"Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetBudgetPos() {
        assertEquals(1, stores.getMovies().getBudget(1), "Does not return the correct value.");
    }

    @Test void testMoviesGetBudgetNeg() {
        assertEquals(-1, stores.getMovies().getBudget(fakeID), "When supplied with a non existent ID should return -1");
    }

    @Test void testMoviesGetBudgetAll() {
        ArrayList<Long> resultBudget = new ArrayList<Long>();

        for (int i = 0; i< batchSize; i++){
            resultBudget.add(batchStores.getMovies().getBudget(IDs.get(i)));
        }

        assertEquals(budgets, resultBudget, "Does not return the correct values.");
    }


    @Test void testMoviesGetRevenuePos() {
        assertEquals(2, stores.getMovies().getRevenue(1), "Does not return the correct value.");
    }

    @Test void testMoviesGetRevenueNeg() {
        assertEquals(-1, stores.getMovies().getRevenue(fakeID), "Getting values for a non existent ID should return -1.");
    }

    @Test void testMoviesGetRevenueAll() {
        ArrayList<Long> resultRevenue = new ArrayList<Long>();

        for (int i = 0; i< batchSize; i++){
            resultRevenue.add(batchStores.getMovies().getRevenue(IDs.get(i)));
        }

        assertEquals(revenues, resultRevenue, "Does not return the correct values.");
    }

    @Test void testMoviesGetLanguagesPos(){
        String[] languages = new String[2];
        languages[0] = "en";
        languages[1] = "th";

        assertArrayEquals(languages, stores.getMovies().getLanguages(1),"Does not return the correct values.");
    }

    @Test void testMoviesGetLanguagesNeg(){
        assertNull(stores.getMovies().getLanguages(fakeID), "Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetLanguagesMany(){
        ArrayList<String[]> resultLanguage = new ArrayList<String[]>();

        for (int i = 0; i< batchSize; i++){
            resultLanguage.add(batchStores.getMovies().getLanguages(IDs.get(i)));
        }

        assertEquals(languagesList, resultLanguage, "Does not return the correct values.");
    }
    

    @Test void testMoviesGetOriginalLanguagePos() {
        assertEquals("en", stores.getMovies().getOriginalLanguage(1), "Does not return the correct value.");
    }

    @Test void testMoviesGetOriginalLanguageNeg() {
        assertNull(stores.getMovies().getOriginalLanguage(fakeID), "Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetOriginalLanguageAll() {
        ArrayList<String> resultOriginalLanguage = new ArrayList<String>();

        for (int i = 0; i< batchSize; i++){
            resultOriginalLanguage.add(batchStores.getMovies().getOriginalLanguage(IDs.get(i)));
        }

        assertEquals(originalLanguages, resultOriginalLanguage, "Does not return the correct values.");
    }


    @Test void testMoviesGetRuntimePos() {
        assertEquals(90.0, stores.getMovies().getRuntime(1), "Does not return the correct value.");
    }


    @Test void testMoviesGetRuntimeNeg() {
        assertEquals(-1, stores.getMovies().getRuntime(fakeID), "Getting values for a non existent ID should return -1.");
    }

    @Test void testMoviesGetRuntimeAll() {
        ArrayList<Double> resultRuntime = new ArrayList<Double>();

        for (int i = 0; i< batchSize; i++){
            resultRuntime.add(batchStores.getMovies().getRuntime(IDs.get(i)));

        }
        assertEquals(runtimes, resultRuntime, "Does not return the correct values.");
    }

    @Test void testMoviesGetHomepagePos() {
        assertEquals("http://homepage.com/homepage", stores.getMovies().getHomepage(1), "Does not return the correct value.");
    }

    @Test void testMoviesGetHomepageNeg() {
        assertNull(stores.getMovies().getHomepage(fakeID),"Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetHomepageAll() {
        ArrayList<String> resultHomepage = new ArrayList<String>();

        for (int i = 0; i< batchSize; i++){
            resultHomepage.add(batchStores.getMovies().getHomepage(IDs.get(i)));
        }

        assertEquals(homepages, resultHomepage);
    }

    @Test void testMoviesAdultPos() {
        assertFalse(stores.getMovies().getAdult(1));
    }

    @Test void testMoviesGetAdultNeg() {
        assertEquals(false, stores.getMovies().getAdult(fakeID), "Getting values for a non existent ID should return false.");
    }

    @Test void testMoviesGetAdultAll() {
        ArrayList<Boolean> resultAdult = new ArrayList<Boolean>();

        
        for (int i = 0; i< batchSize; i++){
            resultAdult.add(batchStores.getMovies().getAdult(IDs.get(i)));
        }

        assertEquals(adults, resultAdult);
    }

    @Test void testMoviesGetVideoPos() {
        assertEquals(false, stores.getMovies().getVideo(1));
    }

    @Test void testMoviesGetVideoNeg() {
        assertFalse(stores.getMovies().getVideo(fakeID), "Getting values for a non existent ID should return false.");
    }

    @Test void testMoviesGetVideoAll() {
        ArrayList<Boolean> resultVideo = new ArrayList<Boolean>();

        for (int i = 0; i< batchSize; i++){
            resultVideo.add(batchStores.getMovies().getVideo(IDs.get(i)));
        }

        assertEquals(videos, resultVideo);
    }

    @Test void testMoviesGetPosterPos() {
        assertEquals("/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg", stores.getMovies().getPoster(1));
    }

    @Test void testMoviesGetPosterNeg() {
        assertNull(stores.getMovies().getPoster(fakeID), "Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesGetPosterAll() {
        ArrayList<String> resultPoster = new ArrayList<String>();

        for (int i = 0; i< batchSize; i++){
            resultPoster.add(batchStores.getMovies().getPoster(IDs.get(i)));
        }

        assertEquals(posters, resultPoster, "Incorrect values returned.");

    }

    @Test void testMoviesGetIMDBPos() {
        stores.getMovies().setIMDB(1,"IMDBid");

        assertEquals("IMDBid", stores.getMovies().getIMDB(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetIMDBNeg() {
        assertNull(stores.getMovies().getIMDB(fakeID), "Getting values for a non existent ID should return null.");
    }

    @Test void testMoviesSetIMDBPos() {
        assertTrue(stores.getMovies().setIMDB(1, "changedIMDBid"), "Setting a value for a valid ID should return true.");
    }

    @Test void testMoviesSetIMDBNeg() {
        assertFalse(stores.getMovies().setIMDB(fakeID, "changedIMDBid"), "Setting a value for a non existent ID should return false.");
    }

    @Test void testMoviesSetPopularityPos() {
        assertTrue(stores.getMovies().setPopularity(1, 1.0f), "The popularity should be able to be set, as the film exists");
    }

    @Test void testMoviesSetPopularityNeg() {
        assertFalse(stores.getMovies().setPopularity(99, 1.0f), "The popularity should not be able to be set, as the film does not exist");
    }

    @Test void testMoviesGetPopularityPos() {
        assertEquals(3.0, stores.getMovies().getPopularity(1), "The value produced is not correct");
    }

    @Test void testMoviesGetPopularityEmpty() {
        assertEquals(0.0, stores.getMovies().getPopularity(2), "The value produced is not correct");
    }

    @Test void testMoviesGetPopularityNeg() {
        assertEquals(-1.0, stores.getMovies().getPopularity(99), "The value produced is not correct");
    }

    @Test void testMoviesSetVotePos() {
        assertTrue(stores.getMovies().setVote(1, 2.2, 2000), "Setting a value for a valid ID should return true.");
    }

    @Test void testMoviesSetVoteNeg() {
        assertFalse(stores.getMovies().setVote(fakeID, 3.3, 3000), "Setting a value for a non existent ID should return false.");
    }

    @Test void testMoviesGetVoteCountPos(){
        double voteAverage = 1.1;
        int voteCount = 1000;

        stores.getMovies().setVote(1, voteAverage, voteCount);

        assertEquals(voteCount, stores.getMovies().getVoteCount(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetVoteCountNeg(){
        assertEquals(-1, stores.getMovies().getVoteCount(fakeID), "Getting values for a non existent ID should return -1.");
    }

    @Test void testMoviesGetVoteAveragePos(){
        double voteAverage = 1.1;
        int voteCount = 1000;

        stores.getMovies().setVote(1, voteAverage, voteCount);

        assertEquals(voteAverage, stores.getMovies().getVoteAverage(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetVoteAverageNeg(){
        assertEquals(-1, stores.getMovies().getVoteAverage(fakeID), "Getting values for a non existent ID should return -1.");
    }

    @Test void testMoviesAddProductionCountryPos() {
        assertTrue(stores.getMovies().addProductionCountry(1, "GB"), "Adding a valid movie should return true.");
    }

    @Test void testMoviesAddProductionCountryNeg() {
        assertFalse(stores.getMovies().addProductionCountry(fakeID, "GB"), "Adding a production country to a non existent movie should return false.");
    }

    @Test void testMoviesGetProductionCountryPos(){
        String[] tmpCountries = {"GB", "YE"};

        stores.getMovies().addProductionCountry(2, tmpCountries[0]);
        stores.getMovies().addProductionCountry(2, tmpCountries[1]);

        assertArrayEquals(tmpCountries, stores.getMovies().getProductionCountries(2), "Values dont match.");
    }

    @Test void testMoviesGetProductionCountriesNeg(){
        String[] tmpCountries = {"GB", "YE"};

        // Other values added just ot make sure it doesn't catch these.
        stores.getMovies().addProductionCountry(1, tmpCountries[0]);
        stores.getMovies().addProductionCountry(1, tmpCountries[1]);

        assertNull(stores.getMovies().getProductionCountries(fakeID), "Non existent ID input should return null.");
    }

    @Test void testMoviesAddProductionCompanyPos() {
        assertTrue( stores.getMovies().addProductionCompany(1, new Company(1, "Pixar Animation Studios")), "Should return true if successfully added.");
    }

    @Test void testMoviesAddProductionCompanyNeg() {
        assertFalse(stores.getMovies().addProductionCompany(fakeID, new Company(1, "Pixar Animation Studios")), "Invalid add should return false.");
    }

    @Test void testMoviesGetProductionCompanyPos() {
        Company[] tmpCompanies = {new Company(1, "Pixar Animation Studios"),new Company(2, "Pixar Animation Studios2")};

        stores.getMovies().addProductionCompany(2, tmpCompanies[0]);
        stores.getMovies().addProductionCompany(2, tmpCompanies[1]);

        Company[] testCompanies = stores.getMovies().getProductionCompanies(2);

        assertArrayEquals(tmpCompanies, testCompanies, "Incorrect result.");
    }

    @Test void testMoviesGetProductionCompanyNeg() {
        Company[] tmpCompanies = {new Company(1, "Pixar Animation Studios"),new Company(2, "Pixar Animation Studios2")};

        stores.getMovies().addProductionCompany(1, tmpCompanies[0]);
        stores.getMovies().addProductionCompany(1, tmpCompanies[1]);

        assertNull( stores.getMovies().getProductionCompanies(fakeID), "Invalid film ID supplied should return null.");
    }

    @Test void testMoviesAddToCollectionPos () {
        int filmID = 1; 
        int collectionID = 1; 
        String collectionName = "Toy Story Series";
        String collectionPosterPath = "collectionposter";
        String collectionBackdropPath = "collectionbackdrop";

        assertTrue(stores.getMovies().addToCollection(filmID, collectionID, collectionName, collectionPosterPath, collectionBackdropPath), "A valid add should return true.");
    }

    @Test void testMoviesAddToCollectionNeg (){
        int collectionID = 1; 
        String collectionName = "Toy Story Series";
        String collectionPosterPath = "collectionposter";
        String collectionBackdropPath = "collectionbackdrop";

        assertFalse(stores.getMovies().addToCollection(fakeID, collectionID, collectionName, collectionPosterPath, collectionBackdropPath), "A non existent film ID should not add and then return false.");
    }

    @Test void testMoviesGetFilmsInCollectionPos() {
        int[] expected = {2};
        int[] result = stores.getMovies().getFilmsInCollection(1);

        assertTrue(checkContentsOfArray(expected, result), "The values do not match what is expected");
    }

    @Test void testMoviesGetFilmsInCollectionNeg() {
        int[] expected = {};
        int[] result = stores.getMovies().getFilmsInCollection(2);

        assertTrue(checkContentsOfArray(expected, result), "The values do not match what is expected");
    }

    @Test void testMoviesGetCollectionNamePos(){
        assertEquals("Toy Story Series", stores.getMovies().getCollectionName(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetCollectionNameNeg(){
        assertNull(stores.getMovies().getCollectionName(fakeID), "Non existent ID should return null.");
    }

    @Test void testMoviesGetCollectionPosterPos(){
        assertEquals("collectionposter", stores.getMovies().getCollectionPoster(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetCollectionPosterNeg(){
        assertNull(stores.getMovies().getCollectionPoster(fakeID), "Non existent ID should return null.");
    }

    @Test void testMoviesGetCollectionBackdropPos(){
        assertEquals("collectionbackdrop", stores.getMovies().getCollectionBackdrop(1), "Incorrect value returned.");
    }

    @Test void testMoviesGetCollectionBackdropNeg(){
        assertNull(stores.getMovies().getCollectionBackdrop(fakeID), "Non existent ID should return null.");
    }


    @Test void testMoviesGetCollectionIDPos(){
        assertEquals(1, stores.getMovies().getCollectionID(2), "Incorrect ID returned.");
    }

    @Test void testMoviesGetCollectionIDNeg(){
        assertEquals(-1, stores.getMovies().getCollectionID(fakeID), "If a film does not have a collection -1 should be returned.");
    }

    @Test void testMoviesfindFilmsPos(){
        int[] toyFilms = {2};
        assertArrayEquals(toyFilms, stores.getMovies().findFilms("Toy"), "Could not find a valid film.");
    }

    @Test void testMoviesfindFilmsNeg(){
        int[] emptyFilms = {};
        assertArrayEquals(emptyFilms, stores.getMovies().findFilms("Value returned when there are no valid matches."));
    }

    @Test void testMoviesSize(){
        assertEquals(2, stores.getMovies().size(), "Size not equal.");
    }
 
}
