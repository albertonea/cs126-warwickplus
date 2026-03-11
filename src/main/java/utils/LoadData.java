package utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import interfaces.*;
import stores.CastCredit;
import stores.Company;
import stores.CrewCredit;
import stores.Genre;
import stores.Keyword;
import interfaces.AbstractStores;

public class LoadData implements Runnable {

    class MovieRecord {
        public int id = -1;
        public String name = "";
        public String title = "";
        public String originalTitle = "";
        public String overview = "";
        public String tagline = "";
        public String status = "";
        public Genre[] genres = new Genre[0];
        public LocalDate release = null;
        public long budget = -1;
        public long revenue = -1;
        public String[] languages = new String[0];
        public String originalLanguage = "";
        public double runtime = -1.0;
        public String homepage = "";
        public boolean adult = false;
        public boolean video = false;
        public String poster = "";
        public double voteAverage = -1.0;
        public int voteCount = -1;
        public int collectionID = -1;
        public String collectionName = "";
        public String collectionPosterPath = "";
        public String collectionBackdropPath = "";
        public String imdb = "";
        public double popularity = -1.0;
        public Company[] productionCompanies = new Company[0];
        public String[] productionCountries = new String[0];
        MovieRecord( int id, String name, String title, String originalTitle, String overview,
                     String tagline, String status, Genre[] genres, LocalDate release, long budget,
                     long revenue, String[] languages, String originalLanguage, double runtime,
                     String homepage, boolean adult, boolean video, String poster, double voteAverage,
                     int voteCount, int collectionID, String collectionName, String collectionPosterPath,
                     String collectionBackdropPath, String imdb, double popularity, 
                     Company[] productionCompanies, String[] productionCountries
            ){
            this.id = id;
            this.name = name;
            this.title = title;
            this.originalTitle = originalTitle;
            this.overview = overview;
            this.tagline = tagline;
            this.status = status;
            this.genres = genres;
            this.release = release;
            this.budget = budget;
            this.revenue = revenue;
            this.languages = languages;
            this.originalLanguage = originalLanguage;
            this.runtime = runtime;
            this.homepage = homepage;
            this.adult = adult;
            this.video = video;
            this.poster = poster;
            this.voteAverage = voteAverage;
            this.voteCount = voteCount;
            this.collectionID = collectionID;
            this.collectionName = collectionName;
            this.collectionPosterPath = collectionPosterPath;
            this.collectionBackdropPath = collectionBackdropPath;
            this.imdb = imdb;
            this.popularity = popularity;
            this.productionCompanies = productionCompanies;
            this.productionCountries = productionCountries;
        }
    }

    class CreditRecord {
        public CastCredit[] cast;
        public CrewCredit[] crew;
        public int movieId;
        CreditRecord(CastCredit[] cast, CrewCredit[] crew, int movieId){
            this.cast = cast;
            this.crew = crew;
            this.movieId = movieId;
        }
    }

    class KeywordRecord {
        public int movieId;
        public Keyword[] keywords;
        KeywordRecord(int movieId, Keyword[] keywords){
            this.movieId = movieId;
            this.keywords = keywords;
        }
    }

    class RatingRecord {
        int userId;
        int movieId;
        float rating;
        LocalDateTime timestamp;
        RatingRecord(int userId, int movieId, float rating, LocalDateTime timestamp){
            this.userId = userId;
            this.movieId = movieId;
            this.rating = rating;
            this.timestamp = timestamp;
        }
    }


    public enum StoreType {CREDITS, KEYWORDS, METADATA, RATINGS}


    private long currentNumber = 0;
    private long totalNumber = 0;
    private String loadingString = "";
    private JProgressBar loadingBar;
    private JLabel loadingText;
    
    // Datastructures that the csv files are loaded into.
    ArrayList<CreditRecord> backendCredits = new ArrayList<>();
    HashMap<Integer, CreditRecord> backendCreditsByMovieId = new HashMap<>();

    ArrayList<KeywordRecord> backendKeywords = new ArrayList<>();
    HashMap<Integer, KeywordRecord> backendKeywordsByMovieId = new HashMap<>();

    ArrayList<MovieRecord> backendMovies = new ArrayList<>();
    HashMap<Integer, MovieRecord> backendMoviesByMovieId = new HashMap<>();

    ArrayList<RatingRecord> backendRatings = new ArrayList<>();
    HashMap<Integer, ArrayList<RatingRecord>> backendRatingsByMovieId = new HashMap<>();

    private class FileLoadUiUpdater {
        int totalNumLines = 0;
        int totalNumCurrentlyProcessed = 0;
        HashMap<StoreType, Long> numLines = new HashMap<>();
        JProgressBar loadingBar;
        JLabel loadingText;
        public FileLoadUiUpdater(JProgressBar loadingBar, JLabel loadingText, File creditsFile, File keywordsFile, File movieFile, File ratingsFile) throws DataLoadException{
            this.loadingBar = loadingBar;
            this.loadingText = loadingText;
            try{
                // Populate numLines with how many lines each file has
                numLines.put(StoreType.CREDITS,  (Files.lines(creditsFile.toPath()).count() - 2));
                numLines.put(StoreType.KEYWORDS, (Files.lines(keywordsFile.toPath()).count() - 2));
                numLines.put(StoreType.METADATA, (Files.lines(movieFile.toPath()).count() - 2));
                numLines.put(StoreType.RATINGS,  (Files.lines(ratingsFile.toPath()).count() - 2));
                for (long fileNumLines : numLines.values()){
                    totalNumLines += fileNumLines;
                }
            }
            catch(IOException e){
                throw new DataLoadException(e.getMessage());
            }
        }
        private void incrementUI(StoreType fileType, int numRecordsProcessed) {
            if (loadingBar == null || loadingText == null) {
                return;
            }
            totalNumCurrentlyProcessed++;
            loadingBar.setValue((int) ((totalNumCurrentlyProcessed / (double) totalNumLines)*(double)loadingBar.getMaximum()));
            loadingString = "[1/2] Loading Data into backend: ";
            switch (fileType) {
                case CREDITS: loadingString += "Credits..."; 
                    break;
                case KEYWORDS: loadingString += "Keywords..."; 
                    break;
                case METADATA: loadingString += "Film Metadata..."; 
                    break;
                case RATINGS: loadingString += "Ratings..."; 
                    break;
            }

            loadingString += " (" + numRecordsProcessed + "/" + numLines.get(fileType) + ")";
            loadingText.setText(loadingString);
    }
    }

    //Load data into memory from default file locations
    public LoadData() throws DataLoadException{
        this(null, null, Constants.defaultCreditsPath, 
                         Constants.defaultKeywordsPath, 
                         Constants.defaultMovieMetadataPath, 
                         Constants.defaultRatingsPath);
    }
    //Loading into memory first method
    public LoadData(String creditsPath, String keywordsPath, String movieMetadataPath, String ratingsPath) throws DataLoadException{
        this(null, null, creditsPath, keywordsPath, movieMetadataPath, ratingsPath);
    }
    public LoadData(JProgressBar loadingBar, JLabel loadingText, String creditsPath, String keywordsPath, String movieMetadataPath, String ratingsPath) throws DataLoadException{
        this.loadingBar = loadingBar;
        this.loadingText = loadingText;
        System.out.println("Loading data into record structures (backend)");

        //Create File objects for all input files and check if they are normal files
        String formatString = "Cannot open %s file (%s). Does not exist or is not a normal file";
        File creditsFile = new File(creditsPath);
        if (!creditsFile.isFile())  { throw new DataLoadException(String.format(formatString, "credits", creditsPath)); }

        File keywordsFile = new File(keywordsPath);
        if (!keywordsFile.isFile()) { throw new DataLoadException(String.format(formatString, "keywords", keywordsPath)); }

        File moviesFile = new File(movieMetadataPath);
        if (!moviesFile.isFile())   { throw new DataLoadException(String.format(formatString, "movies", movieMetadataPath)); }

        File ratingsFile = new File(ratingsPath);
        if (!ratingsFile.isFile())  { throw new DataLoadException(String.format(formatString, "ratings", ratingsPath)); }

        // class to set total number of lines and number of lines for each type so that the actual load function can just do updateUI with what type it is and how many it has loaded
        FileLoadUiUpdater loadingUiUpdater = new FileLoadUiUpdater(loadingBar, loadingText, creditsFile, keywordsFile, moviesFile, ratingsFile);

        //Load Metadata first to load validMovieIds
        Set<Integer> validMovies = loadMetadata(moviesFile, loadingUiUpdater);
        loadCredits(creditsFile, loadingUiUpdater, validMovies);
        loadKeywords(keywordsFile, loadingUiUpdater, validMovies);
        loadRatings(ratingsFile, loadingUiUpdater, validMovies);
    }

    public int getNumMovieRecords(){
        return backendMovies.size();
    }

    public int getNumRatingRecords(){
        return backendRatings.size();
    }

    public int getNumCreditRecords(){
        return backendCredits.size();
    }

    public int getNumKeywordRecords(){
        return backendKeywords.size();
    }

    public int getNumRecords(StoreType ft){
        switch (ft){
            case METADATA:
                return getNumMovieRecords();
            case RATINGS:
                return getNumRatingRecords();
            case CREDITS:
                return getNumCreditRecords();
            case KEYWORDS:
                return getNumKeywordRecords();
            default:
                return -1;
        }
    }

    public class NumRecordsAdded{
        public int credits;
        public int keywords;
        public int movies;
        public int ratings;
        NumRecordsAdded(int credits, int keywords, int movies, int ratings){
            this.credits = credits;
            this.keywords = keywords;
            this.movies = movies;
            this.ratings = ratings;
        }
    }


    /**
     * Populate the student's data stores with the data loaded into the backend structures
     * @param stores
     */
    public NumRecordsAdded populate(AbstractStores stores){
        // Purposefully uses the same function as populating a section of the data.
        // To make sure that these two use cases have the same results!
        return populate(stores, false, -1, -1);
    }
    
    public NumRecordsAdded populate(AbstractStores stores, int firstMovieIndex, int numMovies){
        System.out.println("Populating stores with restriction: " + numMovies + " movies...");
        if (firstMovieIndex < 0){
            System.err.println("Unable to populate. Invalid firstMovieIndex given");
            return null;
        }
        else if (numMovies == 0){
            System.err.println("Unable to populate. Cannot populate with 0 movies");
            return null;
        }
        else if (numMovies < 0){
            System.err.println("Unable to populate. Cannot populate with negative movies");
            return null;
        }
        return populate(stores, true, firstMovieIndex, numMovies);
    }

    /***
     * Populate the student's data structures with a section of the data loaded into the backend structures.
     * This should be the only function that does this, to maintain consistency between
     * calling for everything (as in the ui) and calling for a subsection of the data (as in test suite)
     * 
     * Assumes that loaded backend MovieRecords are unique. If they aren't, loading in multiple sections
     * could lead to the other data structures to gain duplicates that aren't in the original files.
     * when they are added to to make all the stores consistent with each other.
     * @param credits Student's credit store
     * @param keywords Student's keywords store
     * @param movies Student's movies store
     * @param ratings Student's ratings store
     * @param loadSection whether to only load a section of the data
     * @param firstMovieIndex The movie index to load from
     * @param numMovies
     */
    private NumRecordsAdded populate(AbstractStores stores, boolean loadSection, int firstMovieIndex, int numMovies){

        System.out.println("Populating stores...");
        ICredits credits = stores.getCredits();
        IKeywords keywords = stores.getKeywords();
        IMovies movies = stores.getMovies();
        IRatings ratings = stores.getRatings();

        Instant start = Instant.now();


        //Add all of the items for those movies into the other stores for consistency:

        ArrayList<MovieRecord> movieRecords;
        ArrayList<CreditRecord> creditRecords;
        ArrayList<KeywordRecord> keywordRecords;
        ArrayList<RatingRecord> ratingRecords;

        if (!loadSection){
            //Load in the whole dataset
            movieRecords   = backendMovies;
            creditRecords  = backendCredits;
            keywordRecords = backendKeywords;
            ratingRecords  = backendRatings;
        }
        else{
            if (firstMovieIndex < 0 || numMovies <=0){
                System.err.println("Invalid parameters for loading a section of the dataset. first movie index must be >=0, num movies must be > 0");
            }
            if (firstMovieIndex + numMovies > backendMovies.size()){
                System.err.println("Invalid parameters for loading a section of the dataset. Asking to load past the end of the dataset");
            }
            //Load in a subset of the dataset, restricted to a range of movies
            movieRecords = new ArrayList<>();
            ArrayList<Integer> movieIds = new ArrayList<>();
    
            //For all of the items in the 'numMovies' chunk after the current pointer 
            for (int i = firstMovieIndex; i < firstMovieIndex + numMovies; i++){
                MovieRecord mr = backendMovies.get(i);
                movieRecords.add(mr);
                movieIds.add(mr.id);
            }
    
            //update the other stores
            creditRecords = new ArrayList<>();
            keywordRecords = new ArrayList<>();
            ratingRecords = new ArrayList<>();
            for (int movieid : movieIds){
                if (backendCreditsByMovieId.containsKey(movieid)){ creditRecords.add(backendCreditsByMovieId.get(movieid)); }
                if (backendKeywordsByMovieId.containsKey(movieid)){ keywordRecords.add(backendKeywordsByMovieId.get(movieid)); }
                if (backendRatingsByMovieId.containsKey(movieid)){ ratingRecords.addAll(backendRatingsByMovieId.get(movieid)); }
            }
        }

        populateMovies(movies, movieRecords);
        populateCredits(credits, creditRecords);
        populateKeywords(keywords, keywordRecords);
        populateRatings(ratings, ratingRecords);

        Instant end = Instant.now();
        Duration d = Duration.between(start, end);
        System.out.println("Overall time to populate stores:");
        System.out.println(d.toMillis() + "ms");

        return new NumRecordsAdded(creditRecords.size(), keywordRecords.size(), movieRecords.size(), ratingRecords.size());
    }


    private void populateCredits(ICredits credits, ArrayList<CreditRecord> creditRecords){
        System.out.println("Populating Credits Store...");
        for (CreditRecord cr : creditRecords){
            credits.add(cr.cast, cr.crew, cr.movieId);
        }
    }
    
    private void populateKeywords(IKeywords keywords, ArrayList<KeywordRecord> keywordRecords){
        System.out.println("Populating Keywords Store...");
        for (KeywordRecord kr : keywordRecords){
            keywords.add(kr.movieId, kr.keywords);
        }
    }

    private void populateMovies(IMovies movies, ArrayList<MovieRecord> movieRecords){
        System.out.println("Populating Movies Store...");
        for (MovieRecord mr : movieRecords){
            movies.add(mr.id, mr.title, mr.originalTitle, mr.overview, mr.tagline, mr.status, mr.genres, mr.release, mr.budget, mr.revenue, mr.languages, mr.originalLanguage, mr.runtime, mr.homepage, mr.adult, mr.video, mr.poster);

            movies.setVote(mr.id, mr.voteAverage, mr.voteCount);
            movies.setIMDB(mr.id, mr.imdb);
            movies.setPopularity(mr.id, mr.popularity);
            movies.addToCollection(mr.id, mr.collectionID, mr.collectionName, mr.collectionPosterPath, mr.collectionBackdropPath);
            for (Company c : mr.productionCompanies){
                movies.addProductionCompany(mr.id, c);
            }

            for (String country : mr.productionCountries){
                movies.addProductionCountry(mr.id, country);
            }
        }
    }

    private void populateRatings(IRatings ratings, ArrayList<RatingRecord> ratingRecords){
        System.out.println("Populating Ratings Store...");
        for (RatingRecord rr : ratingRecords){
            ratings.add(rr.userId, rr.movieId, rr.rating, rr.timestamp);
        }
    }



    @Override
    public void run() {
    }





    /****************************************************/
    /*                                                  */
    /*                   File Loaders                   */
    /*                                                  */
    /****************************************************/

    private void loadCredits(File creditsCsvFile, FileLoadUiUpdater loadingUiUpdater, Set<Integer> validMovies) throws DataLoadException {
        System.out.println("\nLoading credits from \"" + creditsCsvFile.getPath() + "\"...");

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build();
        int record_count = 1;
        try (CSVParser parser = CSVParser.parse(creditsCsvFile, Charset.forName("UTF-8"), csvFormat)){
            for (CSVRecord csvRecord : parser){
                if (csvRecord.size() != 3){
                    throw new DataLoadException("[CREDITS] Incorrect number of csv fields in record number: " + record_count + ". Number of fields found: " + csvRecord.size());
                }

                // Parse cast
                JSONArray castJsonArray = new JSONArray(csvRecord.get("cast"));
                CastCredit[] castArray = new CastCredit[castJsonArray.length()];
                for (int i = 0; i < castJsonArray.length(); i++){
                    // Each cast member
                    JSONObject castJsonObject = castJsonArray.getJSONObject(i);

                    int castElementId  = castJsonObject.getInt("cast_id");
                    String character   = castJsonObject.getString("character");
                    String creditId    = castJsonObject.getString("credit_id");
                    int gender         = castJsonObject.getInt("gender"); //Note: ignoring this field
                    int castId         = castJsonObject.getInt("id");
                    String name        = castJsonObject.getString("name");
                    int order          = castJsonObject.getInt("order"); 
                    String profilePath = castJsonObject.getString("profile_path");

                    castArray[i] = new CastCredit(castElementId, character, creditId, 
                                            castId, name, order, profilePath);
                }

                // Parse crew
                JSONArray crewJsonArray = new JSONArray(csvRecord.get("crew"));
                CrewCredit[] crewArray = new CrewCredit[crewJsonArray.length()];
                for (int i = 0; i < crewJsonArray.length(); i++){
                    // Each crew member
                    JSONObject crewJsonObject = crewJsonArray.getJSONObject(i);
                    String crewElementId = crewJsonObject.getString("credit_id");
                    String department    = crewJsonObject.getString("department");
                    int gender           = crewJsonObject.getInt("gender"); //Note: ignoring this field
                    int crewId           = crewJsonObject.getInt("id");
                    String job           = crewJsonObject.getString("job");
                    String name          = crewJsonObject.getString("name");
                    String profilePath   = crewJsonObject.getString("profile_path");

                    crewArray[i] = new CrewCredit(crewElementId, department, crewId, job, name, profilePath);
                }

                // Parse top level id in csv file (never empty)
                int movieId = Integer.parseInt(csvRecord.get("tmdb_id"));

                if (!validMovies.contains(movieId)){
                    String message = "Credits file contains a credit for a movie (id:" + movieId + ") that doesn't exist in the movie metadata file!";
                    throw new DataLoadException(message);
                }
                
                if (backendCreditsByMovieId.containsKey(movieId)){
                    String message = "Credits file contains multiple records for movie (id:" + movieId + ")";
                    throw new DataLoadException(message);
                }

                CreditRecord cr = new CreditRecord(castArray, crewArray, movieId);
                backendCredits.add(cr);
                backendCreditsByMovieId.put(movieId, cr);

                loadingUiUpdater.incrementUI(StoreType.CREDITS, record_count++);
                
            } //for each csv record
        }
        catch (IOException e){
            String message = "[ UNRECOVERABLE I/O ERROR ] Unable to open credits file ('" + creditsCsvFile.getPath() +"') for parsing. Please make sure it is in the 'data' directory.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
        catch (JSONException e){
            String message = "[" + record_count + "] --CREDITS-- Unable to read json. Key not found or cannot convert to correct type.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }

    }

    private void loadKeywords(File keywordsCsvFile, FileLoadUiUpdater loadingUiUpdater, Set<Integer> validMovies) throws DataLoadException {
        System.out.println("\nLoading keywords from \"" + keywordsCsvFile.getPath() + "\"...");

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build();
        int record_count = 1;
        try (CSVParser parser = CSVParser.parse(keywordsCsvFile, Charset.forName("UTF-8") , csvFormat)){
            for (CSVRecord csvRecord : parser){ // For every csv line, excluding the header
                if (csvRecord.size() != 2){
                    throw new DataLoadException("[KEYWORDS] Incorrect number of csv fields in record number: " + record_count + ". Number of fields found: " + csvRecord.size());
                }

                int movieId = Integer.parseInt(csvRecord.get("tmdb_id"));

                // Check if have already parsed a keyword record for that movieId 
                // (if there are multiple lines for that movieId in the file)
                if (backendKeywordsByMovieId.containsKey(movieId)){
                    String message = "Keywords file contains multiple records for movie (id:" + movieId + ")";
                    throw new DataLoadException(message);
                }

                // Check if this line in the keywords file is referring to a movie that actually exists
                if (!validMovies.contains(movieId)){
                    String message = "Keywords file contains a keyword record for a movie (id:" + movieId + ") that doesn't exist in the movie metadata file!";
                    throw new DataLoadException(message);
                }

                JSONArray jsonKeywordArray = new JSONArray(csvRecord.get("keywords"));
                
                Keyword[] keywordArray = new Keyword[jsonKeywordArray.length()];
                // Read from the json keyword array that looks like "[{'id':100, 'name':'based on the novel'},...]"
                for (int i = 0; i < jsonKeywordArray.length(); i++){
                    JSONObject jsonKeyword = jsonKeywordArray.getJSONObject(i); 
                    // each {'id':100, 'name':'based on the novel'} in the array

                    int keyword_id      = jsonKeyword.getInt("id");
                    String keyword_name = jsonKeyword.getString("name");
                    
                    keywordArray[i] = new Keyword(keyword_id, keyword_name);
                }

                KeywordRecord kr = new KeywordRecord(movieId, keywordArray);
                backendKeywords.add(kr);
                backendKeywordsByMovieId.put(movieId, kr);
                
                loadingUiUpdater.incrementUI(StoreType.KEYWORDS, record_count++);
            }
        }
        catch (IOException e){
            String message = "[ UNRECOVERABLE I/O ERROR ] Unable to open keyword file ('" + keywordsCsvFile.getPath() +"') for parsing. Please make sure it is in the 'data' directory.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
        catch (IllegalArgumentException e){
            String message = "[" + record_count + "] --KEYWORDS-- Unable to read csv. Item for specified header not found.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
        catch (JSONException e){
            String message = "[" + record_count + "] --KEYWORDS-- Unable to read json. Key not found or cannot convert to correct type.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
    }

    /***
     * Load Film data from csv file.
     * @param metadataCsvFile
     * @param loadingUiUpdater
     * @return Set of movie IDs that were loaded
     * @throws DataLoadException
     */
    private Set<Integer> loadMetadata(File metadataCsvFile, FileLoadUiUpdater loadingUiUpdater) throws DataLoadException {
        System.out.println("\nLoading movies metadata from \"" + metadataCsvFile.getPath() + "\"...");
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build();
        int record_count = 0;
        try (CSVParser parser = CSVParser.parse(metadataCsvFile, Charset.forName("UTF-8"), csvFormat)){
            for (CSVRecord csvRecord : parser){ // For each record line in the file
                if (csvRecord.size() != 24) {
                    throw new DataLoadException("[" + record_count + "] --METADATA-- Incorrect number of fields. Number of fields found: " + csvRecord.size());
                }

                int movieId = Integer.parseInt(csvRecord.get("tmdb_id"));

                //HARD FAIL if duplicate movies exist in the input file
                if (backendMoviesByMovieId.containsKey(movieId)){
                    throw new DataLoadException("[" + record_count + "] --METADATA-- Input file ('" + metadataCsvFile.getPath() +"') contains duplicate Movie! id: " + movieId);
                }

                // Parse simple fields
                long budget    = Long.parseLong(csvRecord.get("budget"));
                long revenue   = Long.parseLong(csvRecord.get("revenue"));
                String runtime_in_file = csvRecord.get("runtime");
                double runtime;
                if (runtime_in_file.equals("")){ 
                    runtime = -1; 
                }
                else{ 
                    runtime = Double.parseDouble(csvRecord.get("runtime")); 
                }
                boolean adult       = Boolean.parseBoolean(csvRecord.get("adult"));
                boolean video       = Boolean.parseBoolean(csvRecord.get("video"));

                //Imdb
                double vote_average = Double.parseDouble(csvRecord.get("vote_average"));
                int vote_count      = Integer.parseInt(csvRecord.get("vote_count"));
                String imdbId       = csvRecord.get("imdb_id");
                Double popularity   = Double.parseDouble(csvRecord.get("popularity"));

                String title            = csvRecord.get("title");
                String originalTitle    = csvRecord.get("original_title");
                String overview         = csvRecord.get("overview");
                String tagline          = csvRecord.get("tagline");
                String status           = csvRecord.get("status");
                String originalLanguage = csvRecord.get("original_language");
                String homepage         = csvRecord.get("homepage");
                String poster_path      = csvRecord.get("poster_path");

                // Parse Genres
                JSONArray jsonGenreArray = new JSONArray(csvRecord.get("genres"));
                Genre[] genreArray = new Genre[jsonGenreArray.length()];
                for (int i = 0; i < jsonGenreArray.length(); i++){
                    JSONObject gObject = jsonGenreArray.getJSONObject(i);
                    int genreId = gObject.getInt("id");
                    String genreName = gObject.getString("name");
                    genreArray[i] = new Genre(genreId, genreName);
                }

                // Parse Languages
                JSONArray jsonLanguageArray = new JSONArray(csvRecord.get("spoken_languages"));
                String[] languageArray = new String[jsonLanguageArray.length()];
                for (int i = 0; i < jsonLanguageArray.length(); i++){
                    JSONObject lObject = jsonLanguageArray.getJSONObject(i);
                    String lang_short = lObject.getString("iso_639_1");
                    languageArray[i] = lang_short;
                }

                // Parse Release Date
                String release_in_file = csvRecord.get("release_date");
                LocalDate release;
                if (!release_in_file.equals("")){
                    release = LocalDate.parse(csvRecord.get("release_date"));
                }
                else{
                    release = null;
                }

                // Add Collection
                String collectionString = csvRecord.get("belongs_to_collection");
                int collectionId = -1;
                String collectionName = null;
                String collectionPoster = null;   //Note: currently unused
                String collectionBackdrop = null; //Note: currently unused
                if (!collectionString.equals("")){
                    JSONObject collectionObject = new JSONObject(csvRecord.get("belongs_to_collection"));
                    collectionId       = collectionObject.getInt("id");
                    collectionName     = collectionObject.getString("name");
                    collectionPoster   = collectionObject.getString("poster_path");
                    collectionBackdrop = collectionObject.getString("backdrop_path");
                }

                // Add Companies
                JSONArray jsonCompanyArray = new JSONArray(csvRecord.get("production_companies"));
                Company[] companyArray = new Company[jsonCompanyArray.length()];
                for (int i = 0; i < jsonCompanyArray.length(); i++){
                    JSONObject jsonCompanyObject = jsonCompanyArray.getJSONObject(i);
                    String companyName = jsonCompanyObject.getString("name");
                    int companyId = jsonCompanyObject.getInt("id");
                    companyArray[i] = new Company(companyId, companyName);
                }

                // Add Countries
                JSONArray jsonCountryArray = new JSONArray(csvRecord.get("production_countries"));
                String[] countryArray = new String[jsonCountryArray.length()];
                for (int i = 0; i < jsonCountryArray.length(); i++){
                    JSONObject jsonCountryObject = jsonCountryArray.getJSONObject(i);
                    String countryIdShort = jsonCountryObject.getString("iso_3166_1");

                    countryArray[i] = countryIdShort;
                }

                MovieRecord mr = new MovieRecord(movieId, collectionName, title, originalTitle, overview, tagline, 
                    status, genreArray, release, budget, revenue, languageArray, originalLanguage, 
                    runtime, homepage, adult, video, poster_path, vote_average, vote_count, collectionId, 
                    collectionName, collectionPoster, collectionBackdrop, imdbId, popularity, companyArray, countryArray);
                
                backendMovies.add(mr);
                backendMoviesByMovieId.put(movieId, mr);
                
                loadingUiUpdater.incrementUI(StoreType.METADATA, record_count++);

            } //for each record

            return backendMoviesByMovieId.keySet();

        }
        catch (IOException e){
            String message = "[ UNRECOVERABLE I/O ERROR ] Unable to open movies metadata file ('" + metadataCsvFile.getPath() +"') for parsing. Please make sure it is in the 'data' directory.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
        catch (IllegalArgumentException e){
            String message = "[" + record_count + "] --MOVIE METADATA-- Unable to read csv. Item for specified header not found.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
        catch (DateTimeParseException e){
            String message = "[" + record_count + "] --MOVIE METADATA-- Unable to read csv. Unable to parse date.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
    }





    /***
     * Load the Ratings file into RatingRecord objects in backend datastructures
     * @param ratingsCsvFile The file to load in
     * @param loadUiUpdater
     * @param validMovies The set of movies that have been parsed from the movies file
     * @throws DataLoadException When the file is of the incorrect format
     */
    private void loadRatings(File ratingsCsvFile, FileLoadUiUpdater loadUiUpdater, Set<Integer> validMovies) throws DataLoadException {
        System.out.println("\nLoading ratings from \"" + ratingsCsvFile.getPath() + "\"...\n\n");
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build();
        int record_count = 0;
        try (CSVParser parser = CSVParser.parse(ratingsCsvFile, Charset.forName("utf-8"), csvFormat)){
            for (CSVRecord csvRecord : parser){
                //For each record in csv file
                if (csvRecord.size() != 5){
                    String message = "[" + record_count + "] --RATINGS-- Incorrect list of ratings... No. fields found = " + csvRecord.size();
                    throw new DataLoadException(message);
                }

                int movieId  = Integer.parseInt(csvRecord.get("tmdbId"));

                // Check if this line in the ratings file is referring to a movie that actually exists
                if (!validMovies.contains(movieId)){
                    String message = "Ratings file contains a rating for a movie (id:" + movieId + ") that doesn't exist in the movie metadata file!";
                    throw new DataLoadException(message);
                }

                int userId   = Integer.parseInt(csvRecord.get("userId"));
                float rating = Float.parseFloat(csvRecord.get("rating"));

                long ts_in_file = Long.parseLong(csvRecord.get("timestamp"));
                LocalDateTime time = LocalDateTime.ofEpochSecond(ts_in_file, 0 , ZoneOffset.UTC);

                RatingRecord rr = new RatingRecord(userId, movieId, rating, time);

                backendRatings.add(rr);
                if (!backendRatingsByMovieId.containsKey(movieId)){
                    backendRatingsByMovieId.put(movieId, new ArrayList<>());
                }
                backendRatingsByMovieId.get(movieId).add(rr);

                loadUiUpdater.incrementUI(StoreType.RATINGS, record_count++);

            }

        }
        catch (IOException e){
            String message = "[ UNRECOVERABLE I/O ERROR ] Unable to open ratings file ('" + ratingsCsvFile.getPath() +"') for parsing. Please make sure it is in the 'data' directory.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
        catch (IllegalArgumentException e){
            String message = "[" + record_count + "] --RATINGS-- Unable to read csv. Item for specified header not found.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
        catch (DateTimeParseException e){
            String message = "[" + record_count + "] --RATINGS-- Unable to read csv. Unable to parse date.";
            System.err.println(message);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            throw new DataLoadException(message);
        }
    }

    public class DataLoadException extends Exception {
        public DataLoadException(String message){
            super(message);
        }
    }
}

