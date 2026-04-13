package stores;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

import interfaces.IMovies;
import structures.data.HashMap;
import structures.data.SetSkipList;
import structures.data.StringSearchIndex;
import structures.data.interfaces.List;
import structures.data.interfaces.Map;
import structures.movies.Movie;
import structures.movies.MovieCollection;

public class Movies implements IMovies {
    Stores stores;
    private int INITIAL_CAPACITY = 1000;

    int size = 0;
    int capacity = INITIAL_CAPACITY;

    private Map<Integer, Movie> movies = new HashMap<>();
    private Map<Integer, MovieCollection> collections = new HashMap<>();
    private Map<Integer, Integer> filmToCollection = new HashMap<>(); // filmID -> collectionID
    private SetSkipList<LocalDate, Integer> dateIndex = new SetSkipList<>();
    private StringSearchIndex<Integer> searchIndex = new StringSearchIndex<>();

    /**
     * The constructor for the Movies data store. This is where you should
     * initialise your data structures.
     * 
     * @param stores An object storing all the different key stores,
     *               including itself
     */
    public Movies(Stores stores) {
        this.stores = stores;
        // TODO Add initialisation of data structure here
    }

    /**
     * Adds data about a film to the data structure
     *
     * @param id               The unique ID for the film
     * @param title            The English title of the film
     * @param originalTitle    The original language title of the film
     * @param overview         An overview of the film
     * @param tagline          The tagline for the film (empty string if there is no
     *                         tagline)
     * @param status           Current status of the film
     * @param genres           An array of Genre objects related to the film
     * @param release          The release date for the film
     * @param budget           The budget of the film in US Dollars
     * @param revenue          The revenue of the film in US Dollars
     * @param languages        An array of ISO 639 language codes for the film
     * @param originalLanguage An ISO 639 language code for the original language of
     *                         the film
     * @param runtime          The runtime of the film in minutes
     * @param homepage         The URL to the homepage of the film
     * @param adult            Whether the film is an adult film
     * @param video            Whether the film is a "direct-to-video" film
     * @param poster           The unique part of the URL of the poster (empty if
     *                         the URL is not known)
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(int id,
            String title,
            String originalTitle,
            String overview,
            String tagline,
            String status,
            Genre[] genres,
            LocalDate release,
            long budget,
            long revenue,
            String[] languages,
            String originalLanguage,
            double runtime,
            String homepage,
            boolean adult,
            boolean video,
            String poster) {

        Movie movie = new Movie(title, originalTitle, overview, tagline, status, genres, release, budget, revenue,
                languages, originalLanguage, runtime, homepage, adult, video, poster);

        if (movies.containsKey(id)) {
            return false;
        }

        movies.put(id, movie);
        if (release != null) {
            dateIndex.put(release, id);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(title);
        stringBuilder.append(" ");
        stringBuilder.append(overview);
        stringBuilder.append(" ");
        stringBuilder.append(originalTitle);
        searchIndex.add(id, stringBuilder.toString());

        this.size++;
        return true;
    }

    /**
     * Removes a film from the data structure, and any data
     * added through this class related to the film
     *
     * @param id The film ID
     * @return TRUE if the film has been removed successfully, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        // TODO Implement this function
        if (!movies.containsKey(id)) {
            return false;
        }

        var movie = movies.remove(id);
        dateIndex.remove(movie.getRelease(), id);
        searchIndex.remove(movie.getId());
        size--;
        return true;
    }

    /**
     * Gets all the IDs for all films
     *
     * @return An array of all film IDs stored
     */
    @Override
    public int[] getAllIDs() {
        var keys = movies.keySet();
        int[] ids = new int[keys.size()];
        int i = 0;
        for (var key: keys) {
            ids[i++] = key;
        }

        return ids;
    }

    /**
     * Finds the film IDs of all films released within a given range. If a film is
     * released either on the start or end dates, then that film should not be
     * included
     *
     * @param start The start point of the range of dates
     * @param end   The end point of the range of dates
     * @return An array of film IDs that were released between start and end
     */
    @Override
    public int[] getAllIDsReleasedInRange(LocalDate start, LocalDate end) {
        List<Integer> list = dateIndex.getRange(start, end);
        int[] ids = new int[list.size()];

        int idx = 0;
        for (Integer i: list) {
            ids[idx++] = i;
        }

        return ids;
    }

    /**
     * Gets the title of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The title of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTitle(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getTitle();
    }

    /**
     * Gets the original title of a particular film, given the ID number of that
     * film
     *
     * @param id The movie ID
     * @return The original title of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalTitle(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getOriginalTitle();
    }

    /**
     * Gets the overview of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The overview of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getOverview(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getOverview();
    }

    /**
     * Gets the tagline of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The tagline of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTagline(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getTagline();
    }

    /**
     * Gets the status of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The status of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getStatus(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getStatus();
    }

    /**
     * Gets the genres of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The genres of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public Genre[] getGenres(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getGenres();
    }

    /**
     * Gets the release date of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The release date of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public LocalDate getRelease(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getRelease();
    }

    /**
     * Gets the budget of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The budget of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getBudget(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return -1;
        }
        return movie.getBudget();
    }

    /**
     * Gets the revenue of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The revenue of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getRevenue(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return -1;
        }
        return movie.getRevenue();
    }

    /**
     * Gets the languages of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The languages of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String[] getLanguages(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getLanguages();
    }

    /**
     * Gets the original language of a particular film, given the ID number of that
     * film
     *
     * @param id The movie ID
     * @return The original language of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalLanguage(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getOriginalLanguage();
    }

    /**
     * Gets the runtime of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The runtime of the requested film. If the film cannot be found, then
     *         return -1.0d
     */
    @Override
    public double getRuntime(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return -1.0d;
        }
        return movie.getRuntime();
    }

    /**
     * Gets the homepage of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The homepage of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getHomepage(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getHomepage();
    }

    /**
     * Gets weather a particular film is classed as "adult", given the ID number of
     * that film
     *
     * @param id The movie ID
     * @return The "adult" status of the requested film. If the film cannot be
     *         found, then return false
     */
    @Override
    public boolean getAdult(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return false;
        }
        return movie.isAdult();
    }

    /**
     * Gets weather a particular film is classed as "direct-to-video", given the ID
     * number of that film
     *
     * @param id The movie ID
     * @return The "direct-to-video" status of the requested film. If the film
     *         cannot be found, then return false
     */
    @Override
    public boolean getVideo(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return false;
        }
        return movie.isVideo();
    }

    /**
     * Gets the poster URL of a particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The poster URL of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String getPoster(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return null;
        }
        return movie.getPoster();
    }

    /**
     * Sets the average IMDb score and the number of reviews used to generate this
     * score, for a particular film
     *
     * @param id          The movie ID
     * @param voteAverage The average score on IMDb for the film
     * @param voteCount   The number of reviews on IMDb that were used to generate
     *                    the average score for the film
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean setVote(int id, double voteAverage, int voteCount) {
        Movie movie = movies.get(id);
        if (movie == null) return false;
        return movie.setVote(voteAverage, voteCount);
    }

    /**
     * Gets the average score for IMDb reviews of a particular film, given the ID
     * number of that film
     *
     * @param id The movie ID
     * @return The average score for IMDb reviews of the requested film. If the film
     *         cannot be found, then return -1.0d
     */
    @Override
    public double getVoteAverage(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return -1.0d;
        }
        return movie.getVoteAverage();
    }

    /**
     * Gets the amount of IMDb reviews used to generate the average score of a
     * particular film, given the ID number of that film
     *
     * @param id The movie ID
     * @return The amount of IMDb reviews used to generate the average score of the
     *         requested film. If the film cannot be found, then return -1
     */
    @Override
    public int getVoteCount(int id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            return -1;
        }
        return movie.getVoteCount();
    }

    /**
     * Adds a given film to a collection. The collection is required to have an ID
     * number, a name, and a URL to a poster for the collection
     *
     * @param filmID                 The movie ID
     * @param collectionID           The collection ID
     * @param collectionName         The name of the collection
     * @param collectionPosterPath   The URL where the poster can
     *                               be found
     * @param collectionBackdropPath The URL where the backdrop can
     *                               be found
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addToCollection(int filmID, int collectionID, String collectionName, String collectionPosterPath,
            String collectionBackdropPath) {
        if (movies.get(filmID) == null) {
            return false;
        }

        MovieCollection collection = collections.get(collectionID);

        if (collection == null) {
            MovieCollection newCollection = new MovieCollection(collectionName, collectionPosterPath,
                    collectionBackdropPath);
            newCollection.addFilm(filmID);
            collections.put(collectionID, newCollection);
        } else {
            collection.addFilm(filmID);
        }
        filmToCollection.put(filmID, collectionID);
        return true;
    }

    /**
     * Get all films that belong to a given collection
     *
     * @param collectionID The collection ID to be searched for
     * @return An array of film IDs that correspond to the given collection ID. If
     *         there are no films in the collection ID, or if the collection ID is
     *         not valid, return an empty array.
     */
    @Override
    public int[] getFilmsInCollection(int collectionID) {
        MovieCollection collection = collections.get(collectionID);
        if (collection == null) {
            return new int[0];
        }
        return collection.getFilms();
    }

    /**
     * Gets the name of a given collection
     *
     * @param collectionID The collection ID
     * @return The name of the collection. If the collection cannot be found, then
     *         return null
     */
    @Override
    public String getCollectionName(int collectionID) {
        MovieCollection collection = collections.get(collectionID);
        if (collection == null) {
            return null;
        }
        return collection.getName();
    }

    /**
     * Gets the poster URL for a given collection
     *
     * @param collectionID The collection ID
     * @return The poster URL of the collection. If the collection cannot be found,
     *         then return null
     */
    @Override
    public String getCollectionPoster(int collectionID) {
        MovieCollection collection = collections.get(collectionID);
        if (collection == null) {
            return null;
        }
        return collection.getPosterPath();
    }

    /**
     * Gets the backdrop URL for a given collection
     *
     * @param collectionID The collection ID
     * @return The backdrop URL of the collection. If the collection cannot be
     *         found, then return null
     */
    @Override
    public String getCollectionBackdrop(int collectionID) {
        MovieCollection collection = collections.get(collectionID);
        if (collection == null) {
            return null;
        }
        return collection.getBackdropPath();
    }

    /**
     * Gets the collection ID of a given film
     *
     * @param filmID The movie ID
     * @return The collection ID for the requested film. If the film cannot be
     *         found, then return -1
     */
    @Override
    public int getCollectionID(int filmID) {
        return filmToCollection.getOrDefault(filmID, -1);
    }

    /**
     * Sets the IMDb ID for a given film
     *
     * @param filmID The movie ID
     * @param imdbID The IMDb ID
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setIMDB(int filmID, String imdbID) {
        Movie movie = movies.get(filmID);
        if (movie == null) {
            return false;
        }
        return movie.setIMDB(imdbID);
    }

    /**
     * Gets the IMDb ID for a given film
     *
     * @param filmID The movie ID
     * @return The IMDb ID for the requested film. If the film cannot be found,
     *         return null
     */
    @Override
    public String getIMDB(int filmID) {
        Movie movie = movies.get(filmID);
        if (movie == null) {
            return null;
        }
        return movie.getImdbID();
    }

    /**
     * Sets the popularity of a given film. If the popularity for a film already
     * exists, replace it with the new value
     *
     * @param id         The movie ID
     * @param popularity The popularity of the film
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setPopularity(int id, double popularity) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return false;
        }
        return movie.setPopularity(popularity);
    }

    /**
     * Gets the popularity of a given film
     *
     * @param id The movie ID
     * @return The popularity value of the requested film. If the film cannot be
     *         found, then return -1.0d. If the popularity has not been set, return
     *         0.0
     */
    @Override
    public double getPopularity(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return -1.0d;
        }
        return movie.getPopularity();
    }

    /**
     * Adds a production company to a given film
     *
     * @param id      The movie ID
     * @param company A Company object that represents the details on a production
     *                company
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCompany(int id, Company company) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return false;
        }
        return movie.addProductionCompany(company);
    }

    /**
     * Adds a production country to a given film
     *
     * @param id      The movie ID
     * @param country A ISO 3166 string containing the 2-character country code
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCountry(int id, String country) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return false;
        }
        return movie.addProductionCountry(country);
    }

    /**
     * Gets all the production companies for a given film
     *
     * @param id The movie ID
     * @return An array of Company objects that represent all the production
     *         companies that worked on the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public Company[] getProductionCompanies(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.getProductionCompanies().toArray(new Company[0]);
    }

    /**
     * Gets all the production companies for a given film
     *
     * @param id The movie ID
     * @return An array of Strings that represent all the production countries (in
     *         ISO 3166 format) that worked on the requested film. If the film
     *         cannot be found, then return null
     */
    @Override
    public String[] getProductionCountries(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.getProductionCountries().toArray(new String[0]);
    }

    /**
     * States the number of movies stored in the data structure
     *
     * @return The number of movies stored in the data structure
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Produces a list of movie IDs that have the search term in their title,
     * original title or their overview
     *
     * @param searchTerm The term that needs to be checked
     * @return An array of movie IDs that have the search term in their title,
     *         original title or their overview. If no movies have this search term,
     *         then an empty array should be returned
     */
    @Override
    public int[] findFilms(String searchTerm) {
        Integer[] items = Stream.of(searchIndex.search(searchTerm).toArray()).toArray(Integer[]::new);
        int[] result = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            result[i] = items[i].intValue();
        }
        return result;
    }
}
