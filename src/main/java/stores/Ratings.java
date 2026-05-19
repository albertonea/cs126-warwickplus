package stores;

import java.time.LocalDateTime;
import java.util.Comparator;

import interfaces.IRatings;
import structures.data.*;
import structures.data.interfaces.Map;
import structures.ratings.MovieRating;
import structures.ratings.Rating;
import structures.ratings.RatingCount;
import structures.ratings.RatingWrapper;

public class Ratings implements IRatings {
    Stores stores;

    // Capacity chosen from the test rating counts so the hash
    // maps don't resize too many times while loading entries
    private final int EXPECTED_RATINGS = 8192;
    // Total ratings stored
    private int size = 0;
    // user id -> wrapper keyed by movie id. Optimised for per-user queries
    private final Map<Integer, RatingWrapper> userIndex = new HashMap<>(EXPECTED_RATINGS);

    // movie id -> wrapper keyed by user id. Optimised for per-movie queries
    private final Map<Integer, RatingWrapper> movieIndex = new HashMap<>(EXPECTED_RATINGS);

    /**
     * The constructor for the Ratings data store. This is where you should
     * initialise your data structures.
     * @param stores An object storing all the different key stores,
     *               including itself
     */
    public Ratings(Stores stores) {
        this.stores = stores;
    }

    /**
     * Adds a rating to the data structure. The rating is made unique by its user ID
     * and its movie ID
     * 
     * @param userID    The user ID
     * @param movieID   The movie ID
     * @param rating    The rating gave to the film by this user (between 0 and 5
     *                  inclusive)
     * @param timestamp The time at which the rating was made
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(int userid, int movieid, float rating, LocalDateTime timestamp) {
        RatingWrapper userRatings = userIndex.get(userid);
        RatingWrapper movieRatings = movieIndex.get(movieid);
        // Check if entry exists
        if (userRatings != null && userRatings.get(movieid) != null) {
            return false;
        }

        // Instantiate shared Rating reference
        Rating ratingObj = new Rating(rating, timestamp);

        // Update each index: create a fresh wrapper on the
        // first sighting of the key, otherwise extend the existing one
        if (userRatings != null) {
            userRatings.put(movieid, ratingObj);
        } else {
            userIndex.put(userid, new RatingWrapper(movieid, ratingObj));
        }

        if (movieRatings != null) {
            movieRatings.put(userid, ratingObj);
        } else {
            movieIndex.put(movieid, new RatingWrapper(userid, ratingObj));
        }

        // Increment number of ratings stored
        size++;

        return true;
    }

    /**
     * Removes a given rating, using the user ID and the movie ID as the unique
     * identifier
     * 
     * @param userID  The user ID
     * @param movieID The movie ID
     * @return TRUE if the data was removed successfully, FALSE otherwise
     */
    @Override
    public boolean remove(int userid, int movieid) {
        RatingWrapper userRatings = userIndex.get(userid);
        RatingWrapper movieRatings = movieIndex.get(movieid);

        // Check if entry exists
        if (movieRatings != null && movieRatings.get(userid) != null) {
            // Remove the entry from the user index, and remove the wrapper
            // entirely if the user no longer has any ratings.
            userRatings.remove(movieid);
            if (userRatings.size() == 0) {
                userIndex.remove(userid);
            }

            // Remove the entry from the user index, and remove the wrapper
            // entirely if the user no longer has any ratings.
            movieRatings.remove(userid);
            if (movieRatings.size() == 0) {
                movieIndex.remove(movieid);
            }

            // Decrement number of ratings stored
            size--;
            return true;
        }

        return false;
    }

    /**
     * Sets a rating for a given user ID and movie ID. Therefore, should the given
     * user have already rated the given movie, the new data should overwrite the
     * existing rating. However, if the given user has not already rated the given
     * movie, then this rating should be added to the data structure
     * 
     * @param userID    The user ID
     * @param movieID   The movie ID
     * @param rating    The new rating to be given to the film by this user (between
     *                  0 and 5 inclusive)
     * @param timestamp The time at which the new rating was made
     * @return TRUE if the data able to be added/updated, FALSE otherwise
     */
    @Override
    public boolean set(int userid, int movieid, float rating, LocalDateTime timestamp) {
        // Instantiate shared Rating reference
        Rating ratingObj = new Rating(rating, timestamp);

        // Existing ratings are overwritten, missing ones are inserted.
        // Size is incremented when new rating is added
        RatingWrapper userRatings = userIndex.get(userid);
        if (userRatings != null) {
            if (userRatings.put(movieid, ratingObj) == null) {
                // Null return means it was a new rating so increment size
                size++;
            }
        } else {
            userIndex.put(userid, new RatingWrapper(movieid, ratingObj));
            size++;
        }

        // The movie-side update mirrors the user-side one. No size
        // increment because it has already been accounted for above
        RatingWrapper movieRatings = movieIndex.get(movieid);
        if (movieRatings != null) {
            movieRatings.put(userid, ratingObj);
        } else {
            movieIndex.put(movieid, new RatingWrapper(userid, ratingObj));
        }

        return true;
    }

    /**
     * Get all the ratings for a given film
     * 
     * @param movieID The movie ID
     * @return An array of ratings. If there are no ratings or the film cannot be
     *         found in Ratings, then return an empty array
     */
    @Override
    public float[] getMovieRatings(int movieid) {
        RatingWrapper movie = movieIndex.get(movieid);
        // Check if movie exists
        if (movie == null) {
            return new float[0];
        }

        // Unbox each Rating into a primitive float
        float[] ratings = new float[movie.size()];
        int i = 0;
        for (var rating : movie.ratings()) {
            ratings[i++] = rating.getRating();
        }

        return ratings;
    }

    /**
     * Get all the ratings for a given user
     * 
     * @param userID The user ID
     * @return An array of ratings. If there are no ratings or the user cannot be
     *         found in Ratings, then return an empty array
     */
    @Override
    public float[] getUserRatings(int userid) {
        var user = userIndex.get(userid);
        if (user == null) {
            return new float[0];
        }

        // Same unboxing pattern as getMovieRatings, but from the user side.
        float[] ratings = new float[user.size()];
        int i = 0;
        for (var rating : user.ratings()) {
            ratings[i++] = rating.getRating();
        }

        return ratings;
    }

    /**
     * Get the average rating for a given film
     * 
     * @param movieID The movie ID
     * @return Produces the average rating for a given film. 
     *         If the film cannot be found in Ratings, but does exist in the Movies store, return 0.0f. 
     *         If the film cannot be found in Ratings or Movies stores, return -1.0f.
     */
    @Override
    public float getMovieAverageRating(int movieid) {
        var movie = movieIndex.get(movieid);
        // Check if movie exists
        if (movie == null) {
            // If film doesn't exist in movie store return -1
            if (stores.getMovies().getOverview(movieid) == null) {
                return -1.0f;
            }
            return 0.0f;
        }

        return movie.averageRating();
    }

    /**
     * Get the average rating for a given user
     * 
     * @param userID The user ID
     * @return Produces the average rating for a given user. If the user cannot be
     *         found in Ratings, or there are no rating, return -1.0f
     */
    @Override
    public float getUserAverageRating(int userid) {
        var user = userIndex.get(userid);
        // Check if user exists
        if (user == null || user.size() == 0) {
            return -1.0f;
        }

        return user.averageRating();
    }

    /**
     * Gets the top N movies with the most ratings, in order from most to least
     * 
     * @param num The number of movies that should be returned
     * @return A sorted array of movie IDs with the most ratings. The array should be
     *         no larger than num. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies in Ratings
     */
    @Override
    public int[] getMostRatedMovies(int num) {
        // Build a (movie id, rating count) snapshot so TopK can rank without
        // needing to know about RatingWrapper internals
        RatingCount[] movieRatingCount = new RatingCount[movieIndex.size()];
        int i = 0;

        for (var ratings : movieIndex.entryList()) {
            movieRatingCount[i++] = new RatingCount(ratings.getKey(), ratings.getValue().size());
        }

        // Initialise topK with reversed comparator for highest count first
        TopK<RatingCount> topK = new
                TopK<>(Comparator.comparingInt(RatingCount::ratingCount).reversed());

        // Retrieve topK and map the ranked records to movie ids
        return topK.topKInt(movieRatingCount, num, RatingCount::entity);
    }

    /**
     * Gets the top N users with the most ratings, in order from most to least
     * 
     * @param num The number of users that should be returned
     * @return A sorted array of user IDs with the most ratings. The array should be
     *         no larger than num. If there are less than num users in the store,
     *         then the array should be the same length as the number of users in Ratings
     */
    @Override
    public int[] getMostRatedUsers(int num) {
        // Build a (user id, rating count) snapshot so TopK can rank without
        // needing to know about RatingWrapper internals
        RatingCount[] userRatingCount = new RatingCount[userIndex.size()];
        int i = 0;

        for (var ratings : userIndex.entryList()) {
            userRatingCount[i++] = new RatingCount(ratings.getKey(), ratings.getValue().size());
        }

        // Initialise topK with reversed comparator for highest count first
        TopK<RatingCount> topK = new
                TopK<>(Comparator.comparingInt(RatingCount::ratingCount).reversed());

        // Retrieve topK and map the ranked records to user ids
        return topK.topKInt(userRatingCount, num, RatingCount::entity);
    }

    /**
     * Get the number of ratings that a movie has
     * 
     * @param movieid The movie id to be found
     * @return The number of ratings the specified movie has. 
     *         If the movie exists in the Movies store, but there are no ratings for it, then return 0. 
     *         If the movie does not exist in the Ratings or Movies store, then return -1.
     */
    @Override
    public int getNumRatings(int movieid) {
        var movie = movieIndex.get(movieid);
        // Check if movie exists
        if (movie == null) {
            // Check if movie exists in movie store
            if (stores.getMovies().getOverview(movieid) == null) {
                return -1;
            }
            return 0;
        }
        return movie.size();
    }

    /**
     * Get the highest average rated film IDs, in order of there average rating
     * (hightst first).
     * 
     * @param numResults The maximum number of results to be returned
     * @return An array of the film IDs with the highest average ratings, highest
     *         first. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies in Ratings
     */
    @Override
    public int[] getTopAverageRatedMovies(int numResults) {
        // Check if move index is not 0
        if (numResults <= 0 || movieIndex.size() == 0) {
            return new int[0];
        }

        // Map to (movieId, avgRating) record for every film to facilitate topK
        MovieRating[] movieRatings = new MovieRating[movieIndex.size()];
        int count = 0;

        for (Map.Entry<Integer, RatingWrapper> ratingMap : movieIndex.entryList()) {
            movieRatings[count++] = new MovieRating(ratingMap.getKey(), ratingMap.getValue().averageRating());
        }

        // Initialise topK with reversed comparator for highest average first
        TopK<MovieRating> topK = new
                TopK<>(Comparator.comparingDouble(MovieRating::avgRating).reversed());

        // Retrieve topK and map the ranked records to movie ids
        return topK.topKInt(movieRatings, numResults, MovieRating::movieId);
    }

    /**
     * Gets the number of ratings in the data structure
     * 
     * @return The number of ratings in the data structure
     */
    @Override
    public int size() {
        return size;
    }
}
