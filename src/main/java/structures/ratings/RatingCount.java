package structures.ratings;

/**
 * Immutable pair of an entity id (either a user id or a movie id) and
 * the number of ratings associated with it. Used as the element type when
 * {@code Ratings.getMostRated*} sorts by rating count.
 *
 * @param entity the id whose ratings are being counted
 * @param ratingCount the number of ratings associated with {@code entity}
 */
public record RatingCount(int entity, int ratingCount) {}
