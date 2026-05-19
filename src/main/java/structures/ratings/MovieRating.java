package structures.ratings;

/**
 * Lightweight immutable pair of a film id and its average rating. Used as
 * the comparable element type when {@code Ratings.getTopAverageRatedMovies}
 * runs a top-k partial sort over the per-film average ratings.
 *
 * @param movieId the movie id
 * @param avgRating the average rating across every user who rated the film
 */
public record MovieRating(int movieId, double avgRating) {}
